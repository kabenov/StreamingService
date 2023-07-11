package com.example.streamingservice

import android.app.Application
import android.app.NotificationChannel
import android.app.NotificationManager
import android.os.Build
import com.example.streamingservice.di.dataModule
import com.example.streamingservice.di.domainModule
import com.example.streamingservice.di.presentationModule
import org.koin.android.ext.koin.androidContext
import org.koin.core.context.startKoin

class StreamingServiceApplication: Application() {

    override fun onCreate() {
        super.onCreate()

        startKoin {
            androidContext(this@StreamingServiceApplication)
            modules(
                presentationModule,
                domainModule,
                dataModule
            )
        }

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O){
            val notificationChannel = NotificationChannel(CHANNEL_ID, "Now Playing Song", NotificationManager.IMPORTANCE_HIGH)
            notificationChannel.description = "This is a important channel for showing song!!"
            val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(notificationChannel)
        }
    }

    companion object {
        const val CHANNEL_ID = "channel1"
        const val PLAYER_PLAY = "play"
        const val PLAYER_NEXT = "next"
        const val PLAYER_PREVIOUS = "previous"
        const val PLAYER_EXIT = "exit"
    }
}