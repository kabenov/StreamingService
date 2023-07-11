package com.example.streamingservice.presentation

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.example.streamingservice.R
import com.example.streamingservice.databinding.ActivityAuthorizationBinding
import com.example.streamingservice.databinding.ActivityMainBinding

class AuthorizationActivity : AppCompatActivity() {

    private var _binding: ActivityAuthorizationBinding? = null
    private val binding: ActivityAuthorizationBinding
        get() = _binding ?: throw RuntimeException("ActivityAuthorizationBinding == null")

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _binding = ActivityAuthorizationBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setFragment()
    }

    private fun setFragment() {
        val fragmentManager = supportFragmentManager
        val fragmentTransaction = fragmentManager.beginTransaction()
        fragmentTransaction.replace(R.id.authorization_container, AuthorizationFragment())
        fragmentTransaction.addToBackStack(null)
        fragmentTransaction.commit()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}