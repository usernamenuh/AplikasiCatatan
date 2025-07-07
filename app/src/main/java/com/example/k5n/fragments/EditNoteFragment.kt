package com.example.k5n.fragments

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.appcompat.app.AlertDialog
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.findNavController
import androidx.navigation.fragment.navArgs
import com.example.k5n.MainActivity
import com.example.k5n.R
import com.example.k5n.databinding.FragmentEditNoteBinding
import com.example.k5n.model.Note
import com.example.k5n.viewmodel.NoteViewModel

class EditNoteFragment : Fragment(R.layout.fragment_edit_note), MenuProvider {

    private var editNoteBinding: FragmentEditNoteBinding? = null
    private val binding get() = editNoteBinding!!

    private lateinit var notesViewModel: NoteViewModel
    private var currentNote: Note? = null

    private val args: EditNoteFragmentArgs by navArgs()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        editNoteBinding = FragmentEditNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        notesViewModel = (activity as MainActivity).noteViewModel

        // ✅ Cek apakah sedang dalam mode edit atau tambah baru
        currentNote = args.note
        currentNote?.let { note ->
            binding.editNoteTitle.setText(note.noteTitle)
            binding.editNoteDesc.setText(note.noteDesc)
        }

        binding.editNoteFab.setOnClickListener {
            val noteTitle = binding.editNoteTitle.text.toString().trim()
            val noteDesc = binding.editNoteDesc.text.toString().trim()

            if (noteTitle.isNotEmpty()) {
                if (currentNote != null) {
                    // Mode edit
                    val updatedNote = Note(currentNote!!.id, noteTitle, noteDesc)
                    notesViewModel.updateNote(updatedNote)
                    Toast.makeText(context, "Note updated", Toast.LENGTH_SHORT).show()
                } else {
                    // Mode tambah baru
                    val newNote = Note(0, noteTitle, noteDesc)
                    notesViewModel.addNote(newNote)
                    Toast.makeText(context, "Note added", Toast.LENGTH_SHORT).show()
                }

                view.findNavController().popBackStack(R.id.homeFragment, false)
            } else {
                Toast.makeText(context, "Please enter note title", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun deleteNote() {
        currentNote?.let { note ->
            AlertDialog.Builder(requireContext()).apply {
                setTitle("Delete Note")
                setMessage("Do you want to delete this note?")
                setPositiveButton("Delete") { _, _ ->
                    notesViewModel.deleteNote(note)
                    Toast.makeText(context, "Note deleted", Toast.LENGTH_SHORT).show()
                    view?.findNavController()?.popBackStack(R.id.homeFragment, false)
                }
                setNegativeButton("Cancel", null)
            }.create().show()
        }
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.menu_edit_note, menu)

        // Sembunyikan menu delete kalau ini catatan baru
        if (currentNote == null) {
            menu.findItem(R.id.deleteMenu)?.isVisible = false
        }
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
        return when (menuItem.itemId) {
            R.id.deleteMenu -> {
                deleteNote()
                true
            }
            else -> false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        editNoteBinding = null
    }
}
