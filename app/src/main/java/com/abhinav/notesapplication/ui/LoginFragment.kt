package com.abhinav.notesapplication.ui

import android.app.Activity
import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.navigation.NavOptions
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
import com.google.android.material.snackbar.Snackbar


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
            if (!isNetworkAvailable(requireContext())){
                Snackbar.make(requireContext(),binding.root,"No Internet Connection",Snackbar.ANIMATION_MODE_SLIDE).show()
                return@setOnClickListener
            }
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

    private fun isNetworkAvailable(context: Context): Boolean {
        val connectivityManager =
            context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        val network = connectivityManager.activeNetwork
        val networkCapabilities = connectivityManager.getNetworkCapabilities(network)

        return networkCapabilities != null &&
                (networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                        networkCapabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR))
    }

    private fun handleSignInResult(result: GoogleSignInResult) {
        if (result.isSuccess) {
            val account = result.signInAccount!!
            val name = account.displayName ?: "New user"
            val id = account.id!!

            viewModel.upsertUser(User(id, name))
            sharedPreferences.saveBoolean(PREF_IS_LOGGED_IN, true)
            sharedPreferences.saveId(PREF_LOGIN,id)
            findNavController().navigate(
                LoginFragmentDirections.actionLoginFragmentToHomeFragment(),
                NavOptions.Builder().setEnterAnim(R.anim.slide_in_right).setExitAnim(R.anim.slide_out_left).build()
            )
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