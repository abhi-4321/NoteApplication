package com.abhinav.notesapplication.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.navigation.ui.NavigationUI
import com.abhinav.notesapplication.R
import com.abhinav.notesapplication.databinding.FragmentLoginBinding
import com.abhinav.notesapplication.model.User
import com.abhinav.notesapplication.util.PREF_LOGGED_IN
import com.abhinav.notesapplication.util.PREF_LOGIN
import com.abhinav.notesapplication.util.PreferenceManager
import com.abhinav.notesapplication.viewmodel.MainViewModel


class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentLoginBinding.inflate(layoutInflater,container,false)
        viewModel = (requireActivity() as MainActivity).viewModel

        val sharedPreferences = PreferenceManager(requireContext())

        binding.loginBtn.setOnClickListener {
            viewModel.insertUser(User(0,"abhinav4321"))
            sharedPreferences.saveBoolean(PREF_LOGGED_IN,true)
            sharedPreferences.saveInt(PREF_LOGIN,0)
            findNavController().apply {
                navigate(R.id.action_loginFragment_to_homeFragment)
            }
        }
        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}