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
import com.abhinav.notesapplication.model.Note.Companion.emptyNote
import com.abhinav.notesapplication.util.PREF_LOGIN
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
        binding.tvTitle.requestFocus()

        val activity = requireActivity() as MainActivity
        viewModel = activity.viewModel
        val sharedPreferences = activity.sharedPreferences

        //Id from Navigation SafeArgs
        val id = arguments?.getInt("arg",-1) ?: -1

        var oldNote: Note? = null

        viewLifecycleOwner.lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                viewModel.getNoteById(id,sharedPreferences.getId(PREF_LOGIN)).collectLatest { note ->
                    if (oldNote == null && note != emptyNote())
                        oldNote = note.copy()
                    //emptyNote() has id = (-1)
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

            val userId = sharedPreferences.getId(PREF_LOGIN)
            val newNote = Note(id,userId,title,content,"$date $monthYear")

            if (newNote == oldNote){
                Toast.makeText(requireContext(),"Current note is same as previous note", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.updateNote(newNote)
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