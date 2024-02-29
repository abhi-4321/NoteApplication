package com.abhinav.notesapplication.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.navigation.fragment.findNavController
import com.abhinav.notesapplication.databinding.FragmentEditNoteBinding
import com.abhinav.notesapplication.model.note.Note
import com.abhinav.notesapplication.viewmodel.MainViewModel
import kotlinx.coroutines.launch

class EditNoteFragment : Fragment() {

    private var _binding: FragmentEditNoteBinding? = null
    private val binding get() = _binding!!
//    private val viewModel: MainViewModel by viewModels()
//    @Inject lateinit var sharedPreferences: SharedPreferences

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentEditNoteBinding.inflate(layoutInflater,container,false)
//        binding.tvTitle.requestFocus()
//
//        //Id from Navigation SafeArgs
//        val id = arguments?.getInt("arg",-1) ?: -1
//
//        var oldNote: Note? = null
//
//        viewLifecycleOwner.lifecycleScope.launch {
//            repeatOnLifecycle(Lifecycle.State.STARTED) {
////                viewModel.getNoteById(id,sharedPreferences.getId(PREF_LOGIN)).collectLatest { note ->
////                    if (oldNote == null && note != emptyNote())
////                        oldNote = note.copy()
////                    //emptyNote() has id = (-1)
////                    if (note.id != -1){
////                        binding.date.text = note.date.substring(0,2)
////                        binding.monthYear.text = note.date.substring(3)
////                        binding.tvTitle.setText(note.title)
////                        binding.tvContent.setText(note.content)
////                    }
////                }
//            }
//        }
//
//        binding.close.setOnClickListener { findNavController().navigateUp() }
//        binding.done.setOnClickListener {
//            val title = binding.tvTitle.text.toString()
//            val content = binding.tvContent.text.toString()
//            val date = binding.date.text.toString()
//            val monthYear = binding.monthYear.text.toString()
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
////            val userId = sharedPreferences.getToken(PREF_JWT_TOKEN)
////            val newNote = Note(id,userId,title,content,"$date $monthYear")
//
////            if (newNote == oldNote){
////                Toast.makeText(requireContext(),"Current note is same as previous note", Toast.LENGTH_SHORT).show()
////                return@setOnClickListener
////            }
////
////            viewModel.updateNote(newNote)
////            Toast.makeText(requireContext(),"Note updated successfully", Toast.LENGTH_SHORT).show()
////            findNavController().navigateUp()
//        }
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