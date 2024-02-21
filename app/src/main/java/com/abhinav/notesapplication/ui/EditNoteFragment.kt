package com.abhinav.notesapplication.ui

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.abhinav.notesapplication.databinding.FragmentEditNoteBinding
import com.abhinav.notesapplication.model.Note
import com.abhinav.notesapplication.util.PREF_LOGIN
import com.abhinav.notesapplication.util.PreferenceManager
import com.abhinav.notesapplication.viewmodel.MainViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class EditNoteFragment : Fragment() {

    private var _binding: FragmentEditNoteBinding? = null
    private val binding get() = _binding!!
    private lateinit var viewModel: MainViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditNoteBinding.inflate(layoutInflater,container,false)
        viewModel = (requireActivity() as MainActivity).viewModel

        val id = arguments?.getInt("arg",-1) ?: -1
        Log.e("IdEditNote","$id")

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getNoteById(id).collectLatest { note ->
                    if (note.id != -1){
                        binding.date.text = note.date.substring(0,2)
                        binding.monthYear.text = note.date.substring(3)
                        binding.tvTitle.setText(note.title)
                        binding.tvContent.setText(note.content)
                    }
                }
            }
        }

        binding.close.setOnClickListener { findNavController().navigateUp() }
        binding.done.setOnClickListener {
            val title = binding.tvTitle.text.toString()
            val content = binding.tvContent.text.toString()
            val date = binding.date.text.toString()
            val monthYear = binding.monthYear.text.toString()

            if (title.isEmpty()) {
                binding.tvTitle.error = "Title can not be empty!!"
                return@setOnClickListener
            }
            if (content.isEmpty()) {
                binding.tvContent.error = "Content can not be empty!!"
                return@setOnClickListener
            }

            val sharedPreferences = PreferenceManager(requireContext())
            val userId = sharedPreferences.getInt(PREF_LOGIN)

            viewModel.updateNote(Note(id,userId,title,content,"$date $monthYear"))
            Toast.makeText(requireContext(),"Note updated successfully", Toast.LENGTH_SHORT).show()
            findNavController().navigateUp()
        }

        viewModel = (requireActivity() as MainActivity).viewModel

        return binding.root
    }
    companion object {
        fun newInstance(arg: Int): EditNoteFragment {
            val fragment = EditNoteFragment()
            val args = Bundle()
            args.putInt("Id", arg)
            fragment.arguments = args
            return fragment
        }
    }
}