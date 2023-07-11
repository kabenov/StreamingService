package com.example.streamingservice.data

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.example.streamingservice.data.api_clients.ApiClientBackend
import com.example.streamingservice.data.api_clients.ApiClientShazam
import com.example.streamingservice.data.dbmodels.UserUpdateDbModel
import com.example.streamingservice.data.user.CurrentUser
import com.example.streamingservice.domain.StreamingServiceRepository
import com.example.streamingservice.domain.entities.*
import com.fasterxml.jackson.databind.ObjectMapper
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.ResponseBody
import okio.buffer
import okio.source
import org.json.JSONArray
import org.json.JSONObject
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.io.File

class StreamingServiceRepositoryImpl(
    private val apiClientBackend: ApiClientBackend,
    private val apiClientShazam: ApiClientShazam,
    private val context: Context
    ): StreamingServiceRepository {

    private val registerMessage = MutableLiveData<SignInUpResponse>()
    private val signInMessage = MutableLiveData<SignInUpResponse>()

    override fun registerUser(signUpRequest: SignUpRequest) {
        apiClientBackend.userSignUp(signUpRequest).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (!response.isSuccessful) {
                    registerMessage.value = SignInUpResponse(null, "Error: Username is already taken!", 400)
                    return
                }

                val responseBody = response.body()!!
                val responseJsonObject = JSONObject(responseBody.string())

                val signInUpResponse = parseJsonObjectToSignInUpResponse(responseJsonObject)
                saveUserData(signInUpResponse.user)

                registerMessage.value = signInUpResponse
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d(PARSING_TAG, t.localizedMessage ?: "Failure Occurred")
            }
        })
    }

    override fun getRegisterUserLiveData(): LiveData<SignInUpResponse> = registerMessage

    override fun signInUser(signInRequest: SignInRequest) {
        apiClientBackend.userSignIn(signInRequest).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (!response.isSuccessful) {
                    signInMessage.value = SignInUpResponse(null, "Bad credentials", 401)
                    return
                }

                val responseBody = response.body()!!
                val responseJsonObject = JSONObject(responseBody.string())

                val signInUpResponse = parseJsonObjectToSignInUpResponse(responseJsonObject)
                saveUserData(signInUpResponse.user)

                signInMessage.value =signInUpResponse
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d(PARSING_TAG, t.localizedMessage ?: "Failure Occurred")
            }
        })
    }

    override fun getSignInUserLiveData(): LiveData<SignInUpResponse> = signInMessage

    override fun getCurrentUser(): User {
        return CurrentUser.user ?: throw RuntimeException("User data doesn't exist")
    }

    override fun changeUserData(user: User): LiveData<SignInUpResponse> {
        val changedUserResponse = MutableLiveData<SignInUpResponse>()

        val newUser = UserUpdateDbModel(
            id = user.id,
            username = user.username,
            email = user.email,
            img_link = user.imgLink
        )

        val objectMapper = ObjectMapper()
        val newUserJson = objectMapper.writeValueAsString(newUser)
        val tempFile = File.createTempFile("user", ".json")
        tempFile.writeText(newUserJson)

        val fileRequestBody = tempFile.asRequestBody("application/json".toMediaType())


        val filePart = user.imgLinkUri?.let { uri ->
            val inputStream = context.contentResolver.openInputStream(uri)
            val bufferedSource = inputStream?.source()?.buffer()
            val requestBody = bufferedSource?.let { source ->
                RequestBody.create("image/png".toMediaTypeOrNull(), source.readByteArray())
            }
            requestBody?.let {
                MultipartBody.Part.createFormData("file", File(uri.path).name, it)
            }
        }

        val accessToken = getAccessToken(user.accessToken)

        apiClientBackend.changeUserData(fileRequestBody, filePart, accessToken).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (!response.isSuccessful) {
                    changedUserResponse.value = SignInUpResponse(null, response.message(), response.code())
                    return
                }

                val responseBody = response.body()!!
                val responseJsonObject = JSONObject(responseBody.string())

                val signInUpResponse = parseJsonObjectToSignInUpResponse(responseJsonObject)
                saveUserData(signInUpResponse.user)

                changedUserResponse.value = signInUpResponse
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d(PARSING_TAG, t.localizedMessage ?: "Failure Occurred")
            }
        })

        return changedUserResponse
    }

    private fun parseJsonObjectToSignInUpResponse(responseJsonObject: JSONObject): SignInUpResponse {
        val userObject = responseJsonObject.getJSONObject("data")
        val userId = userObject.getLong("id")
        val userName = userObject.getString("username")
        val userEmail = userObject.getString("email")

        val userImgLink = userObject.getString("img_link")
        //API_BACKEND_BASE_URL
        //http://localhost:8080/api/image/90caf3b1-c2da-4ad5-bd16-bed37218b58d
        var resUserImgLink = "http://192.168.104.197:8080/api/image/90caf3b1-c2da-4ad5-bd16-bed37218b58d"
        if(userImgLink.length > 21) {
            resUserImgLink = API_BACKEND_BASE_URL + userImgLink.substring(21)
        }

        val userAccessToken = userObject.getString("accessToken")

        val message = responseJsonObject.getString("message")
        val status = responseJsonObject.getInt("status")

        return SignInUpResponse(
            user = User(userId, userName, userEmail, resUserImgLink, userAccessToken),
            message = message,
            status = status
        )
    }


    override fun getRecommendedMusics(): LiveData<List<Music>> {
        val recommendedMusicList = MutableLiveData<List<Music>>()

        apiClientShazam.getChartByCountry("KZ").enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (!response.isSuccessful) throw RuntimeException("Response getChartByCountry isn't successful")

                val responseBody = response.body()!!
                val responseJsonArray = JSONArray(responseBody.string())

                recommendedMusicList.value = parseJsonArrayToMusicListForRecommendMusic(responseJsonArray)
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d(PARSING_TAG, t.localizedMessage ?: "Failure Occurred")
            }
        })

        return recommendedMusicList
    }

    private fun parseJsonArrayToMusicListForRecommendMusic(responseJsonArray: JSONArray): List<Music> {
        val musicList = mutableListOf<Music>()

        for(index in 0 until responseJsonArray.length()){
            val jsonObject = (responseJsonArray[index] as? JSONObject) ?: continue
            val country = parseJsonObjectToMusic(jsonObject)

            musicList.add(country)
        }

        return musicList
    }

    private fun parseJsonObjectToMusic(jsonObject: JSONObject): Music {
        val musicId = jsonObject.getString("key")
        val title = jsonObject.getString("title")
        val author = jsonObject.getString("subtitle")

        println("MusicId: $musicId")

        var posterUrl = "https://is2-ssl.mzstatic.com/image/thumb/Features125/v4/5b/b3/20/5bb32012-5b39-9e0f-b6de-668b2e5ed27c/pr_source.png/800x800cc.jpg"
        try {
            val images = jsonObject.getJSONObject("images")
            posterUrl = images.getString("background")
        } catch (e: Exception) {
            e.printStackTrace()
        }

        var url = "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview125/v4/c6/ef/30/c6ef3043-c03c-caf7-caa3-a3431a086c65/mzaf_11631466525512100935.plus.aac.ep.m4a"
        try {
            val hub = jsonObject.getJSONObject("hub")
            val actions = hub.getJSONArray("actions")
            url = (actions[1] as JSONObject).getString("uri")
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return Music(
            id = musicId,
            uri = url,
            title = title,
            author = author,
            posterUri = posterUrl
        )
    }



    override fun getMusicById(musicId: Int) {

    }

    override fun getRecommendedAudiobooks() {

    }

    override fun getAudiobookById() {

    }

    override fun getMusicGenres() {

    }

    override fun getAudiobookGenres() {

    }

    override fun getMusicsByGenre(genre: Genre) {

    }

    override fun getNewAlbums() {

    }

    override fun getMusicsByName(name: String) {

    }

    override fun getMusicsByAuthor(author: String) {

    }

    override fun getAlbumsByName(name: String) {

    }

    override fun getAlbumsByAuthor(author: String) {

    }

    override fun getAudiobooksByName(name: String) {

    }

    override fun getAudiobooksByAuthor(author: String) {

    }

    override fun multiSearch(query: String): LiveData<List<Music>> {
        val searchedMusicList = MutableLiveData<List<Music>>()

        apiClientShazam.multiSearch(query).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (!response.isSuccessful) throw RuntimeException("Response multiSearch isn't successful")

                val responseBody = response.body()!!
                val responseJsonObject = JSONObject(responseBody.string())

                searchedMusicList.value = parseJsonObjectToMusicListForMultiSearch(responseJsonObject)
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d(PARSING_TAG, t.localizedMessage ?: "Failure Occurred")
            }
        })

        return searchedMusicList
    }

    private fun parseJsonObjectToMusicListForMultiSearch(responseJsonObject: JSONObject): List<Music> {
        val searchResultList = mutableListOf<Music>()

        val tracksJsonObject = responseJsonObject.getJSONObject("tracks")
        val hitsJsonArray = tracksJsonObject.getJSONArray("hits")

        for(index in 0 until hitsJsonArray.length()){
            val hitJsonObject = (hitsJsonArray[index] as? JSONObject) ?: continue
            val track = hitJsonObject.getJSONObject("track")
            val music = parseJsonObjectToMusic(track)

            searchResultList.add(music)
        }

        return searchResultList
    }

    override fun getUserFavorites(): LiveData<List<Music>> {
        val userFavoritesLiveData = MutableLiveData<List<Music>>()

        return userFavoritesLiveData
    }

    override fun getUserPlaylist(): LiveData<List<Playlist>> {
        val userPlaylistLiveData = MutableLiveData<List<Playlist>>()

        val currentUser = getCurrentUser()
        val accessToken = getAccessToken(currentUser.accessToken)

        apiClientBackend.getUserPlaylist(accessToken).enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (!response.isSuccessful) throw RuntimeException("Response getUserPlaylist isn't successful")

                val responseBody = response.body()!!
                val responseJsonObject = JSONObject(responseBody.string())

                userPlaylistLiveData.value = parseJsonObjectToPlaylistList(responseJsonObject)
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d(PARSING_TAG, t.localizedMessage ?: "Failure Occurred")
            }
        })

        return userPlaylistLiveData
    }

    private fun parseJsonObjectToPlaylistList(jsonObject: JSONObject): List<Playlist> {
        val playlistList = mutableListOf<Playlist>()

        val dataJsonArray = jsonObject.getJSONArray("data")

        for(index in 0 until dataJsonArray.length()){
            val jsonObject = (dataJsonArray[index] as? JSONObject) ?: continue
            val playlist = parseJsonObjectToPlaylist(jsonObject)

            playlistList.add(playlist)
        }

        return playlistList
    }

    private fun parseJsonObjectToPlaylistListHome(jsonObject: JSONObject): List<Playlist> {
        val playlistList = mutableListOf<Playlist>()

        val dataJsonObject = jsonObject.getJSONObject("data")
        val dataJsonArray = dataJsonObject.getJSONArray("content")

        for(index in 0 until dataJsonArray.length()){
            val jsonObject = (dataJsonArray[index] as? JSONObject) ?: continue
            val playlist = parseJsonObjectToPlaylist(jsonObject)

            playlistList.add(playlist)
        }

        return playlistList
    }

    private fun parseJsonObjectToPlaylist(jsonObject: JSONObject): Playlist {
        val playlistId = jsonObject.getString("id")
        val playlistName = jsonObject.getString("name")
        val playlistImgLink = jsonObject.getString("img_link")

        val musicJsonArray = jsonObject.getJSONArray("musics")
        val playlistMusicList = mutableListOf<Music>()

        for(index in 0 until musicJsonArray.length()){
            val jsonObject = (musicJsonArray[index] as? JSONObject) ?: continue
            val music = parseJsonObjectToMusicBackend(jsonObject)

            playlistMusicList.add(music)
        }

        return Playlist(
            id = playlistId,
            playlistName = playlistName,
            imageLink = playlistImgLink,
            musics = playlistMusicList
        )
    }

    private fun parseJsonObjectToMusicBackend(jsonObject: JSONObject): Music {
        var musicId = jsonObject.getString("id")

        var musicName = "Music"
        var musicDuration = 90000L
        var musicImgLink = "https://dailymix-images.scdn.co/v2/img/ab6761610000e5ebb8db53f5ad80ee41f244428e/2/en/default"
        var musicUrl = "http://localhost:8080/api/track/1.mp3"
        try {
            musicName = jsonObject.getString("name")
            musicDuration = jsonObject.getLong("duration")
            musicImgLink = jsonObject.getString("img_link")
            musicUrl = jsonObject.getString("public_path")
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val resMusicImgLink = API_BACKEND_BASE_URL + musicUrl.substring(21)



        var author = "Author"


        return Music(
            id = musicId,
            uri = resMusicImgLink,
            title = musicName,
            author = author,
            posterUri = musicImgLink
        )
    }

    private fun saveUserData(user: User?) {
        CurrentUser.user = user
    }

    private fun getAccessToken(accessToken: String) : String = "Bearer $accessToken"

    override fun getAllAudioBook(): LiveData<List<Audiobook>> {
        val userPlaylistLiveData = MutableLiveData<List<Audiobook>>()



        return userPlaylistLiveData
    }

    override fun getAllPlaylist(): LiveData<List<Playlist>> {
        val allPlaylistLiveData = MutableLiveData<List<Playlist>>()

        apiClientBackend.getAllPlaylist().enqueue(object : Callback<ResponseBody> {
            override fun onResponse(call: Call<ResponseBody>, response: Response<ResponseBody>) {
                if (!response.isSuccessful) throw RuntimeException("Response getUserPlaylist isn't successful")

                val responseBody = response.body()!!
                val responseJsonObject = JSONObject(responseBody.string())

                allPlaylistLiveData.value = parseJsonObjectToPlaylistListHome(responseJsonObject)
            }

            override fun onFailure(call: Call<ResponseBody>, t: Throwable) {
                Log.d(PARSING_TAG, t.localizedMessage ?: "Failure Occurred")
            }
        })
//        apiClientBackend.getAllAudioBook()
        return allPlaylistLiveData
    }


}