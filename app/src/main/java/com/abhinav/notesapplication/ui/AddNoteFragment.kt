package com.abhinav.notesapplication.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.abhinav.notesapplication.databinding.FragmentAddNoteBinding
import com.abhinav.notesapplication.viewmodel.MainViewModel
import java.text.SimpleDateFormat
import java.util.Locale

class AddNoteFragment : Fragment() {

    private var _binding: FragmentAddNoteBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddNoteBinding.inflate(layoutInflater,container,false)
//        binding.tvTitle.requestFocus()
//
//        val date = SimpleDateFormat("dd MMM yyyy", Locale.getDefault()).format(System.currentTimeMillis()).toString()
//        binding.date.text = date.substring(0,2)
//        binding.monthYear.text = date.substring(3)
//
//        binding.done.setOnClickListener {
//            val title = binding.tvTitle.text.toString()
//            val content = binding.tvContent.text.toString()
//
//            if (title.isEmpty()) {
//                binding.tvTitle.error = "Title can not be empty!!"
//                return@setOnClickListener
//            }
//            if (content.isEmpty()) {
//                binding.tvContent.error = "Content can not be empty!!"
//                return@setOnClickListener
//            }
//
////            viewModel.insertNote(Note(0,userId,title,content,date))
//            Toast.makeText(requireContext(),"Note saved successfully",Toast.LENGTH_SHORT).show()
//            findNavController().navigateUp()
//        }
//        binding.close.setOnClickListener { findNavController().navigateUp() }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

}