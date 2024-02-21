package com.abhinav.notesapplication.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.abhinav.notesapplication.R
import com.abhinav.notesapplication.databinding.FragmentHomeBinding
import com.abhinav.notesapplication.util.NotesListAdapter
import com.abhinav.notesapplication.util.AdapterClickListener
import com.abhinav.notesapplication.util.PREF_LOGGED_IN
import com.abhinav.notesapplication.util.PREF_LOGIN
import com.abhinav.notesapplication.util.PreferenceManager
import com.abhinav.notesapplication.viewmodel.MainViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class HomeFragment : Fragment(), AdapterClickListener {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater,container,false)

        binding.btn.setOnClickListener { findNavController().navigate(R.id.action_homeFragment_to_addNoteFragment) }
        binding.addNote.setOnClickListener { findNavController().navigate(R.id.action_homeFragment_to_addNoteFragment) }

        viewModel = (requireActivity() as MainActivity).viewModel

        val listAdapter = NotesListAdapter(this)

        binding.recycler.apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = listAdapter
        }

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getNotes().collectLatest { notesList ->
                    setLayout(notesList.isEmpty())
                    listAdapter.submitList(notesList)
                }
            }
        }

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost : MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider{
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.home_menu,menu)
            }

            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                Toast.makeText(requireContext(),"Huihui",Toast.LENGTH_SHORT).show()
                return when(menuItem.itemId) {
                    R.id.logout -> {
                        Toast.makeText(requireContext(),"Hui",Toast.LENGTH_SHORT).show()
                        true
                    }
                    else -> false
                }
            }
        })
    }

    private fun logout() {
        val sharedPreferences = PreferenceManager(requireContext())
        sharedPreferences.saveInt(PREF_LOGIN,-1)
        sharedPreferences.saveBoolean(PREF_LOGGED_IN,false)
        findNavController().navigate(R.id.action_homeFragment_to_addNoteFragment)
    }

    private fun setLayout(flag: Boolean) {
        if (flag) {
            binding.linearLayout.visibility = View.VISIBLE
            binding.recycler.visibility = View.GONE
        }
        else {
            binding.recycler.visibility = View.VISIBLE
            binding.linearLayout.visibility = View.GONE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onDeleteClick(id: Int) {
        viewModel.deleteNote(id)
    }

    override fun onEditClick(id: Int) {
        val action = HomeFragmentDirections.actionHomeFragmentToEditNoteFragment(id)
        findNavController().navigate(action)
    }
}