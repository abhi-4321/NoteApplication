package com.abhinav.notesapplication.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import com.abhinav.notesapplication.MainActivity
import com.abhinav.notesapplication.R
import com.abhinav.notesapplication.databinding.FragmentLoginBinding
import com.abhinav.notesapplication.model.auth.AuthRequest
import com.abhinav.notesapplication.model.auth.AuthResult
import com.abhinav.notesapplication.repository.auth.AuthRepository
import com.abhinav.notesapplication.util.Constants.IS_LOGGED_IN
import com.abhinav.notesapplication.util.Constants.JWT_TOKEN
import com.abhinav.notesapplication.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)

        viewModel = (requireActivity() as MainActivity).viewModel

        viewModel.loginFeedback.observe(viewLifecycleOwner) {
            binding.progressBar.visibility = View.GONE
            if (it != null) {
                when (it) {
                    is AuthResult.Authorized -> {
                        sharedPreferences.edit().putBoolean(IS_LOGGED_IN, true).apply()
                        Toast.makeText(requireContext(), "Logged in", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
                    }

                    is AuthResult.Unauthorized -> {
                        Toast.makeText(
                            requireContext(),
                            "You are not authorized",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is AuthResult.UnknownError -> {
                        Toast.makeText(
                            requireContext(),
                            "Unknown error occurred",
                            Toast.LENGTH_SHORT
                        ).show()
                    }

                    is AuthResult.Loading -> {
                        binding.progressBar.visibility = View.VISIBLE
                    }

                    else -> {
                        Unit
                    }
                }
            }
        }

        binding.redirect.setOnClickListener {
            findNavController().navigate(
                LoginFragmentDirections.actionLoginFragmentToSignUpFragment(),
                NavOptions.Builder().setEnterAnim(R.anim.slide_in_right).setExitAnim(R.anim.slide_out_left).setPopExitAnim(R.anim.slide_in_right).build()
            )
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

            val authRequest = AuthRequest(username, password)
            viewModel.signIn(authRequest)
        }


//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestId()
//            .build()
//        val googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
//
//        binding.loginBtn.setOnClickListener {
//            if (!isNetworkAvailable(requireContext())){
//                Snackbar.make(requireContext(),binding.root,"No Internet Connection",Snackbar.ANIMATION_MODE_SLIDE).show()
//                return@setOnClickListener
//            }
//            val signInIntent = googleSignInClient.signInIntent
//            activityResultLauncher.launch(signInIntent)
//        }
        return binding.root
    }

//    private val activityResultLauncher =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
//            val resultCode = it.resultCode
//            val resultData = it.data
//
//            if (resultCode == Activity.RESULT_OK) {
//                val result = Auth.GoogleSignInApi.getSignInResultFromIntent(
//                    resultData!!
//                )
//                handleSignInResult(result!!)
//            }
//        }

//    private fun handleSignInResult(result: GoogleSignInResult) {
//        if (result.isSuccess) {
//            val account = result.signInAccount!!
//            val name = account.displayName ?: "New user"
//            val id = account.id!!
//
//            viewModel.upsertUser(User(id, name))
//            sharedPreferences.saveBoolean(PREF_IS_LOGGED_IN, true)
//            sharedPreferences.saveToken(PREF_JWT_TOKEN,id)
//            findNavController().navigate(
//                LoginFragmentDirections.actionLoginFragmentToHomeFragment(),
//                NavOptions.Builder().setEnterAnim(R.anim.slide_in_right).setExitAnim(R.anim.slide_out_left).build()
//            )
//            Toast.makeText(requireContext(), "Welcome $name !!", Toast.LENGTH_SHORT).show()
//        } else {
//            Toast.makeText(
//                requireContext(),
//                "Sign-in failed. Please try again.",
//                Toast.LENGTH_SHORT
//            ).show()
//        }
//    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}