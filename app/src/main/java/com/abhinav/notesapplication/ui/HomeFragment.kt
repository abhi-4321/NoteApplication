package com.abhinav.notesapplication.ui

import android.content.Context
import android.content.SharedPreferences
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.NavOptions
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.abhinav.notesapplication.MainActivity
import com.abhinav.notesapplication.R
import com.abhinav.notesapplication.databinding.FragmentHomeBinding
import com.abhinav.notesapplication.util.AdapterClickListener
import com.abhinav.notesapplication.util.Constants.JWT_TOKEN
import com.abhinav.notesapplication.util.NotesListAdapter
import com.abhinav.notesapplication.viewmodel.MainViewModel
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val LOGOUT = "Log out"
private const val DELETE = "Delete"

@AndroidEntryPoint
class HomeFragment : Fragment(), AdapterClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel

    @Inject
    lateinit var sharedPreferences: SharedPreferences

    private var googleSignInClient: GoogleSignInClient? = null
//    @Inject lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)

        viewModel = (requireActivity() as MainActivity).viewModel

        //Google Sign-in Client
//        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
//            .requestEmail()
//            .build()
//        googleSignInClient = GoogleSignIn.getClient(requireContext(), gso)
//
//        binding.btn.setOnClickListener { findNavController().navigate(R.id.action_homeFragment_to_addNoteFragment) }
//        binding.addNote.setOnClickListener { findNavController().navigate(R.id.action_homeFragment_to_addNoteFragment) }
//        binding.logout.setOnClickListener { showDialog(LOGOUT, "Do you want to logout?", -1) }
//
//        //Notes List Recycler View
//        val listAdapter = NotesListAdapter(this)
//
//        binding.recycler.apply {
//            layoutManager = LinearLayoutManager(requireContext())
//            adapter = listAdapter
//        }
//
//        viewLifecycleOwner.lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.STARTED) {
////                viewModel.getNotes(sharedPreferences.getId(PREF_LOGIN))
////                    .collectLatest { notesList ->
////                        listAdapter.submitList(notesList)
////                        delay(500)
////                        setLayout(notesList.isEmpty())
////                    }
//            }
//        }

        binding.title.text = sharedPreferences.getString(JWT_TOKEN,"Notes")

        return binding.root
    }

    private fun setLayout(flag: Boolean) {
        if (flag) {
            binding.linearLayout.visibility = View.VISIBLE
            binding.recycler.visibility = View.GONE
        } else {
            binding.recycler.visibility = View.VISIBLE
            binding.linearLayout.visibility = View.GONE
        }
    }

    private fun showDialog(title: String, message: String, id: Int) {
        val builder = AlertDialog.Builder(requireContext())
        builder.setTitle(title)
        builder.setMessage(message)
        builder.setPositiveButton("Yes") { dialog, _ ->
            if (title.equals(LOGOUT, false))
                logout()
            else if (title.equals(DELETE, false))
//                viewModel.deleteNote(id, sharedPreferences.getId(PREF_LOGIN))
            dialog.dismiss()
        }

        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }

        val dialog: AlertDialog = builder.create()
        dialog.show()
    }

    private fun logout() {
//        if (!isNetworkAvailable(requireContext())){
//            Snackbar.make(requireContext(),binding.root,"No Internet Connection", Snackbar.ANIMATION_MODE_SLIDE).show()
//            return
//        }
//        try {
//            googleSignInClient?.let { client ->
//                client.signOut().addOnCompleteListener {
//                    if (it.isSuccessful) {
//                        Toast.makeText(
//                            requireContext(),
//                            "Logged out successfully!!",
//                            Toast.LENGTH_SHORT
//                        ).show()
////                        sharedPreferences.saveToken(PREF_JWT_TOKEN, "")
////                        sharedPreferences.saveBoolean(PREF_IS_LOGGED_IN, false)
//                        findNavController().navigate(
//                            HomeFragmentDirections.actionHomeFragmentToLoginFragment(),
//                            NavOptions.Builder().setPopUpTo(R.id.homeFragment,true).build()
//                        )
//                    }
//                }
//            }
//        } catch (e: Exception) {
//            Toast.makeText(requireContext(), "Error: $e", Toast.LENGTH_SHORT).show()
//        }
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDeleteClick(id: Int) {
        showDialog(DELETE, "Do you want to delete the selected note?", id)
    }

    override fun onEditClick(id: Int) {
        val action = HomeFragmentDirections.actionHomeFragmentToEditNoteFragment(id)
        findNavController().navigate(action)
    }
}