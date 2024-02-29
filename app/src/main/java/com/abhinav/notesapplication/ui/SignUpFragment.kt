package com.abhinav.notesapplication.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
import androidx.navigation.NavOptionsBuilder
import androidx.navigation.fragment.findNavController
import com.abhinav.notesapplication.MainActivity
import com.abhinav.notesapplication.R
import com.abhinav.notesapplication.databinding.FragmentSignUpBinding
import com.abhinav.notesapplication.model.auth.AuthRequest
import com.abhinav.notesapplication.model.auth.AuthResult
import com.abhinav.notesapplication.repository.auth.AuthRepository
import com.abhinav.notesapplication.util.Constants
import com.abhinav.notesapplication.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SignUpFragment : Fragment() {

    private var _binding: FragmentSignUpBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private var authRequest = AuthRequest("","")

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSignUpBinding.inflate(layoutInflater, container, false)
        viewModel = (requireActivity() as MainActivity).viewModel

        viewModel.signUpFeedback.observe(viewLifecycleOwner) {
            if (it != null) {
                when (it) {
                    is AuthResult.Authorized -> {
                        binding.progressBar.visibility = View.GONE
                        sharedPreferences.edit().putBoolean(Constants.IS_LOGGED_IN, true).apply()
                        Toast.makeText(requireContext(), "Logged in", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(SignUpFragmentDirections.actionSignUpFragmentToHomeFragment())
                    }

                    is AuthResult.Unauthorized -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(
                            requireContext(),
                            "You are not authorized",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is AuthResult.UnknownError -> {
                        binding.progressBar.visibility = View.GONE
                        Toast.makeText(
                            requireContext(),
                            "Unknown error occurred",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is AuthResult.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    is AuthResult.SignedUp -> {
                        viewModel.signIn(authRequest)
                    }
                }
            }
        }

        binding.redirect.setOnClickListener {
            findNavController().navigateUp()
        }

        binding.googleLogin.setOnClickListener {
            Toast.makeText(
                requireContext(),
                "Under development",
                Toast.LENGTH_SHORT
            ).show()
        }

        binding.loginBtn.setOnClickListener {
            val username = binding.username.text.toString()
            val password = binding.password.text.toString()

            if (username.isEmpty()) {
                binding.username.error = "Username can't be empty"
                return@setOnClickListener
            } else if (password.isEmpty()) {
                binding.password.error = "Password can't be empty"
                return@setOnClickListener
            } else {
                if (password.length < 8) {
                    binding.password.error = "Password is too short"
                    return@setOnClickListener
                }
            }

            authRequest = AuthRequest(username, password)
            viewModel.signUp(authRequest)
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}