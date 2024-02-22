package com.abhinav.notesapplication.ui

import android.app.Activity
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.abhinav.notesapplication.R
import com.abhinav.notesapplication.databinding.FragmentLoginBinding
import com.abhinav.notesapplication.model.User
import com.abhinav.notesapplication.util.PREF_IS_LOGGED_IN
import com.abhinav.notesapplication.util.PREF_LOGIN
import com.abhinav.notesapplication.util.PreferenceManager
import com.abhinav.notesapplication.viewmodel.MainViewModel
import com.google.android.gms.auth.api.Auth
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.auth.api.signin.GoogleSignInResult


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel
    private lateinit var sharedPreferences: PreferenceManager

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(layoutInflater, container, false)
        viewModel = (requireActivity() as MainActivity).viewModel

        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestId()
            .build()
        val googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)

        sharedPreferences = PreferenceManager(requireContext())

        binding.loginBtn.setOnClickListener {
            val signInIntent = googleSignInClient.signInIntent
            activityResultLauncher.launch(signInIntent)
        }
        return binding.root
    }

    private val activityResultLauncher =
        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            val resultCode = it.resultCode
            val resultData = it.data

            if (resultCode == Activity.RESULT_OK) {
                val result = Auth.GoogleSignInApi.getSignInResultFromIntent(
                    resultData!!
                )
                handleSignInResult(result!!)
            }
        }

    private fun handleSignInResult(result: GoogleSignInResult) {
        if (result.isSuccess) {
            val account = result.signInAccount!!
            val name = account.displayName ?: "New user"
            val id = account.id!!

            viewModel.upsertUser(User(id, name))
            sharedPreferences.saveBoolean(PREF_IS_LOGGED_IN, true)
            sharedPreferences.saveId(PREF_LOGIN,id)
            findNavController().navigate(R.id.action_loginFragment_to_homeFragment)
            Toast.makeText(requireContext(), "Welcome $name !!", Toast.LENGTH_SHORT).show()
        } else {
            Toast.makeText(
                requireContext(),
                "Sign-in failed. Please try again.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}