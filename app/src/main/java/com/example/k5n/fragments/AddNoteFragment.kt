package com.example.k5n.fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import com.example.k5n.MainActivity
import com.example.k5n.R
import com.example.k5n.databinding.FragmentAddNoteBinding
import com.example.k5n.model.Note
import com.example.k5n.viewmodel.NoteViewModel

class AddNoteFragment : Fragment(R.layout.fragment_add_note), MenuProvider {

    private var _binding: FragmentAddNoteBinding? = null
    private val binding get() = _binding!!

    private lateinit var noteViewModel: NoteViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentAddNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        noteViewModel = (activity as MainActivity).noteViewModel

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        binding.addNoteFab.setOnClickListener {
            saveNote(view)
        }
    }

    private fun saveNote(view: View) {
        val noteTitle = binding.addNoteTitle.text.toString().trim()
        val noteDesc = binding.addNoteDesc.text.toString().trim()

        if (noteTitle.isNotEmpty()) {
            val note = Note(0, noteTitle, noteDesc)
            noteViewModel.addNote(note)
            Toast.makeText(requireContext(), "Note saved", Toast.LENGTH_SHORT).show()
            view.findNavController().popBackStack(R.id.homeFragment, false)
        } else {
            Toast.makeText(requireContext(), "Please enter a title", Toast.LENGTH_SHORT).show()
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
       
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean = false

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
