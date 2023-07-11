package com.example.streamingservice.presentation

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.net.toUri
import androidx.lifecycle.LiveData
import com.example.streamingservice.R
import com.example.streamingservice.databinding.FragmentSignUpBinding
import com.example.streamingservice.databinding.FragmentUserProfileBinding
import com.example.streamingservice.domain.entities.SignInUpResponse
import com.example.streamingservice.domain.entities.User
import com.example.streamingservice.presentation.imageloader.DefaultImageLoader
import com.example.streamingservice.presentation.imageloader.ImageLoader
import com.example.streamingservice.presentation.viewmodel.SignInViewModel
import com.example.streamingservice.presentation.viewmodel.UserProfileViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class UserProfileFragment : Fragment() {

    private var _binding: FragmentUserProfileBinding? = null
    private val binding: FragmentUserProfileBinding
        get() = _binding ?: throw RuntimeException("FragmentUserProfileBinding == null")

    private val userProfileViewModel: UserProfileViewModel by viewModel()

    private lateinit var changedUserResponse: LiveData<SignInUpResponse>
    private lateinit var currentUser: User

    private val imageLoader: ImageLoader = DefaultImageLoader()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentUserProfileBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        onBind()
        setupListeners()

        binding.buttonFragmentUserProfileChange.setOnClickListener {
            sendChangedUser()
            Toast.makeText(context, "Button clicked", Toast.LENGTH_SHORT).show()
        }

        binding.textViewFragmentUserProfileBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.imageViewFragmentUserProfileImageChange.setOnClickListener {
            //ACTION_OPEN_DOCUMENT || ACTION_GET_CONTENT
            val intent = Intent(Intent.ACTION_GET_CONTENT)
            intent.type = "image/*"
            intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true)
            startActivityForResult(intent, REQUEST_CODE)
        }
    }

    private fun onBind() {
        currentUser = userProfileViewModel.currentUser

//        binding.imageViewFragmentUserProfileImage.drawable = R.drawable.symba_user_pic
        binding.editTextUsername.setText(currentUser.username)
        binding.editTextEmail.setText(currentUser.email)
        binding.textViewFragmentUserProfileUsername.text = currentUser.username
        binding.textViewFragmentUserProfilePlaylistCount.text = currentUser.userPlaylistsCount.toString()
        binding.textViewFragmentUserProfileFavoriteCount.text = currentUser.userFavoriteMusicsCount.toString()

        imageLoader.loadRecommendMusicPosterImg(
            context = requireContext(),
            url = currentUser.imgLink,
            target = binding.imageViewFragmentUserProfileImage
        )

        checkUserData()
    }

    private fun checkUserData() {
        val clickable = !(binding.editTextUsername.text.toString() == currentUser.username && binding.editTextEmail.text.toString() == currentUser.email)

        binding.buttonFragmentUserProfileChange.isClickable = clickable
        if(!clickable) {
            binding.buttonFragmentUserProfileChange.setBackgroundColor(resources.getColor(R.color.unclickable_button_color))
        }
    }

    private fun setupListeners() {
        binding.editTextUsername.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                checkUserData()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })

        binding.editTextEmail.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
            }

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                checkUserData()
            }

            override fun afterTextChanged(p0: Editable?) {
            }
        })
    }

    private fun sendChangedUser() {
        val username = binding.editTextUsername.text.toString()
        val email = binding.editTextEmail.text.toString()

        val changedUser = User(
            id = currentUser.id,
            username = username,
            email = email,
            imgLink = currentUser.imgLink,
            accessToken = currentUser.accessToken,
            imgLinkUri = currentUser.imgLinkUri
        )
        changedUserResponse = userProfileViewModel.changeUserData(changedUser)

        changedUserResponse.observe(viewLifecycleOwner) {
            if(it.status == 200) onBind()

            Toast.makeText(context, it.message, Toast.LENGTH_LONG).show()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == REQUEST_CODE && resultCode == Activity.RESULT_OK) {
            if (data?.clipData != null) {
                // Обработка выбранных нескольких фотографий
                val count = data.clipData!!.itemCount
                for (i in 0 until count) {
                    val imageUri: Uri = data.clipData!!.getItemAt(i).uri
                    // Обработка каждой выбранной фотографии
                    currentUser.imgLinkUri = imageUri
                    currentUser.imgLink = imageUri.toString()
                }
                displayUserImage(currentUser.imgLink.toUri())
            } else if (data?.data != null) {
                // Обработка выбранной одной фотографии
                val imageUri: Uri = data.data!!
                // Обработка выбранной фотографии
                displayUserImage(imageUri)
                currentUser.imgLinkUri = imageUri
                currentUser.imgLink = imageUri.toString()
            }
        }
    }

    private fun displayUserImage(imageUri: Uri) {
        binding.imageViewFragmentUserProfileImage.setImageURI(imageUri)
    }


    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    companion object {
        const val REQUEST_CODE = 1
    }
}