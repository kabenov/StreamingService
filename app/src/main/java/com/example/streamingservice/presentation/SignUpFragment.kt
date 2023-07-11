package com.example.streamingservice.presentation

import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.streamingservice.databinding.FragmentSignUpBinding
import com.example.streamingservice.domain.entities.SignUpRequest
import com.example.streamingservice.presentation.common.AbstractTextWatcher
import com.example.streamingservice.presentation.viewmodel.SignUpViewModel
import org.koin.androidx.viewmodel.ext.android.viewModel

class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding: FragmentSignUpBinding
        get() = _binding ?: throw RuntimeException("FragmentSignUpBinding == null")

    private val signUpViewModel: SignUpViewModel by viewModel()

    private lateinit var signUpRequest: SignUpRequest

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        listenerEditTexts()

        binding.buttonFragmentSignUpButton.setOnClickListener {
            val username = binding.editTextFragmentSignUpUsername.text.trim().toString()
            val email = binding.editTextFragmentSignUpEmail.text.trim().toString()
            val password = binding.editTextFragmentSignUpPassword.text.trim().toString()
            val rePassword = binding.editTextFragmentSignUpRePassword.text.trim().toString()

            if (password != rePassword) {
                binding.textViewFragmentSignUpErrorMessage.text = "Passwords are not equal"
            }
            else {
                signUpRequest = SignUpRequest(username, email, password)
                signUpViewModel.registerUser(signUpRequest)
            }
        }

        signUpViewModel.registerUserLiveData.observe(viewLifecycleOwner) {
            if(it.status == 400) {
                binding.textViewFragmentSignUpErrorMessage.text = it.message
            }
            else {
                binding.textViewFragmentSignUpErrorMessage.text = ""
                toSecondActivity(signUpRequest.username, signUpRequest.email)
            }
        }
    }

    private fun listenerEditTexts() {
        var isUsernameEditTextEmpty = true
        var isEmailEditTextEmpty = true
        var isPasswordEditTextEmpty = true
        var isRePasswordEditTextEmpty = true

        binding.editTextFragmentSignUpUsername.addTextChangedListener(object: AbstractTextWatcher() {
            override fun afterTextChanged(p0: Editable?) {
                isUsernameEditTextEmpty = p0.toString().isBlank() // isBlank() как isEmpty() но еще и проверяет на наличие пробелов

                if (!isUsernameEditTextEmpty && !isEmailEditTextEmpty && !isPasswordEditTextEmpty && !isRePasswordEditTextEmpty) {
                    binding.buttonFragmentSignUpButton.isEnabled = !isUsernameEditTextEmpty
                }
            }
        })

        binding.editTextFragmentSignUpEmail.addTextChangedListener(object: AbstractTextWatcher() {
            override fun afterTextChanged(p0: Editable?) {
                isEmailEditTextEmpty = p0.toString().isBlank() // isBlank() как isEmpty() но еще и проверяет на наличие пробелов

                if (!isUsernameEditTextEmpty && !isEmailEditTextEmpty && !isPasswordEditTextEmpty && !isRePasswordEditTextEmpty) {
                    binding.buttonFragmentSignUpButton.isEnabled = !isEmailEditTextEmpty
                }
            }
        })

        binding.editTextFragmentSignUpPassword.addTextChangedListener(object: AbstractTextWatcher() {
            override fun afterTextChanged(p0: Editable?) {
                isPasswordEditTextEmpty = p0.toString().isBlank() // isBlank() как isEmpty() но еще и проверяет на наличие пробелов

                if (!isUsernameEditTextEmpty && !isEmailEditTextEmpty && !isPasswordEditTextEmpty && !isRePasswordEditTextEmpty) {
                    binding.buttonFragmentSignUpButton.isEnabled = !isPasswordEditTextEmpty
                }
            }
        })

        binding.editTextFragmentSignUpRePassword.addTextChangedListener(object: AbstractTextWatcher() {
            override fun afterTextChanged(p0: Editable?) {
                isRePasswordEditTextEmpty = p0.toString().isBlank() // isBlank() как isEmpty() но еще и проверяет на наличие пробелов

                if (!isUsernameEditTextEmpty && !isEmailEditTextEmpty && !isPasswordEditTextEmpty && !isRePasswordEditTextEmpty) {
                    binding.buttonFragmentSignUpButton.isEnabled = !isRePasswordEditTextEmpty
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