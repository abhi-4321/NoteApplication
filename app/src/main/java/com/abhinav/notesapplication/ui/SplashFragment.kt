package com.abhinav.notesapplication.ui

import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import com.abhinav.notesapplication.MainActivity
import com.abhinav.notesapplication.R
import com.abhinav.notesapplication.databinding.FragmentSplashBinding
import com.abhinav.notesapplication.model.auth.AuthResult
import com.abhinav.notesapplication.util.Constants
import com.abhinav.notesapplication.viewmodel.MainViewModel
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class SplashFragment : Fragment() {

    private var _binding: FragmentSplashBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel
    @Inject
    lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSplashBinding.inflate(inflater, container, false)
        viewModel = (requireActivity() as MainActivity).viewModel

        viewModel.authenticate()
        viewModel.authorizeFeedback.observe(viewLifecycleOwner){
            if (it != null) {
                when (it) {
                    is AuthResult.Authorized -> {
                        sharedPreferences.edit().putBoolean(Constants.IS_LOGGED_IN, true).apply()
                        Toast.makeText(requireContext(), "Logged in", Toast.LENGTH_SHORT).show()
                        findNavController().navigate(R.id.action_splashFragment_to_homeFragment)
                    }
                    is AuthResult.Unauthorized -> {
                        findNavController().navigate(R.id.action_splashFragment_to_loginFragment)
                    }
                    else -> {
                        Unit
                    }
                }
            }
        }

        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}