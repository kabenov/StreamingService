package com.example.streamingservice.presentation

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.streamingservice.databinding.FragmentSignInBinding
import com.example.streamingservice.domain.entities.SignInRequest
import com.example.streamingservice.presentation.common.AbstractTextWatcher
import com.example.streamingservice.presentation.viewmodel.SignInViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignInFragment : Fragment() {
    private var _binding: FragmentSignInBinding? = null
    private val binding: FragmentSignInBinding
        get() = _binding ?: throw RuntimeException("FragmentSignInBinding == null")

    private val signInViewModel: SignInViewModel by viewModel()

    private lateinit var signInRequest: SignInRequest

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignInBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listenerEditTexts()

        binding.buttonFragmentSignInButton.setOnClickListener {
            val username = binding.editTextFragmentSignInUsername.text.trim().toString()
            val password = binding.editTextFragmentSignInPassword.text.trim().toString()

            signInRequest = SignInRequest(username, password)
            signInViewModel.signInUser(signInRequest)
        }

        signInViewModel.signInUserLiveData.observe(viewLifecycleOwner) {
            if(it.status == 401) {
                binding.textViewFragmentSignInErrorMessage.text = it.message
            }
            else {
                binding.textViewFragmentSignInErrorMessage.text = ""
                toSecondActivity(signInRequest.username, signInRequest.password)
            }
        }

    }

    private fun listenerEditTexts() {
        var isUsernameEditTextEmpty = true
        var isPasswordEditTextEmpty = true

        binding.editTextFragmentSignInUsername.addTextChangedListener(object: AbstractTextWatcher() {
            override fun afterTextChanged(p0: Editable?) {
                isUsernameEditTextEmpty = p0.toString().isBlank() // isBlank() как isEmpty() но еще и проверяет на наличие пробелов

                if (!isUsernameEditTextEmpty && !isPasswordEditTextEmpty) {
                    binding.buttonFragmentSignInButton.isEnabled = !isUsernameEditTextEmpty
                }
            }
        })

        binding.editTextFragmentSignInPassword.addTextChangedListener(object: AbstractTextWatcher() {
            override fun afterTextChanged(p0: Editable?) {
                isPasswordEditTextEmpty = p0.toString().isBlank() // isBlank() как isEmpty() но еще и проверяет на наличие пробелов

                if (!isUsernameEditTextEmpty && !isPasswordEditTextEmpty) {
                    binding.buttonFragmentSignInButton.isEnabled = !isPasswordEditTextEmpty
                }
            }
        })
    }

    private fun toSecondActivity(user: String, token: String){
        val intent = Intent(activity, MainActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        intent.putExtra("user_name", user)
        intent.putExtra("token", token)
        startActivity(intent)
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }
}