package com.example.streamingservice.domain.entities

import android.media.MediaMetadataRetriever
import java.io.Serializable

data class Music (
    val id: String,
    val uri: String = "https://audio-ssl.itunes.apple.com/itunes-assets/AudioPreview125/v4/c6/ef/30/c6ef3043-c03c-caf7-caa3-a3431a086c65/mzaf_11631466525512100935.plus.aac.ep.m4a",
    val title: String,
    val author: String,
    val duration: Long = 90000,
    val genre: Genre = Genre(0, "Pop"),
    val posterUri: String
): Serializable

//fun getImageArt(path: String): ByteArray? {
//    val retriever = MediaMetadataRetriever()
//    retriever.setDataSource(path)
//    return retriever.embeddedPicture
//}

fun milliSecondsToTimer(millis: Long): String {
    var timerString = ""
    val secondsString: String

    val hours = (millis / (1000 * 60 * 60)).toInt()
    val minutes = ((millis % (1000 * 60 * 60)) / (1000 * 60)).toInt()
    val seconds = ((millis % (1000 * 60 * 60)) % (1000 * 60) / 1000).toInt()

    if (hours > 0) {
        timerString = "$hours:"
    }

    if (seconds < 10) {
        secondsString = "0$seconds"
    }
    else {
        secondsString = "$seconds"
    }

    timerString = "$timerString$minutes:$secondsString"
    return timerString
}