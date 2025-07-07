package com.example.k5n.fragments

import android.os.Bundle
import android.view.*
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.k5n.MainActivity
import com.example.k5n.R
import com.example.k5n.adapter.NoteAdapter
import com.example.k5n.databinding.FragmentHomeBinding
import com.example.k5n.model.Note
import com.example.k5n.viewmodel.NoteViewModel

class HomeFragment : Fragment(R.layout.fragment_home), SearchView.OnQueryTextListener, MenuProvider {

    private var homeBinding: FragmentHomeBinding? = null
    private val binding get() = homeBinding!!

    private lateinit var noteViewModel: NoteViewModel
    private lateinit var noteAdapter: NoteAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        homeBinding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(this, viewLifecycleOwner, Lifecycle.State.RESUMED)

        noteViewModel = (activity as MainActivity).noteViewModel
        setupHomeRecyclerView()

        // Tombol tambah note
        binding.addNoteFab.setOnClickListener {
            findNavController().navigate(R.id.action_homeFragment_to_addNoteFragment)
        }
    }

    private fun setupHomeRecyclerView() {
        noteAdapter = NoteAdapter()

        // Klik item note ➜ navigasi ke editNoteFragment
        noteAdapter.setOnItemClickListener { note ->
            val action = HomeFragmentDirections.actionHomeFragmentToEditNoteFragment(note)
            findNavController().navigate(action)
        }

        binding.homeRecyclerView.apply {
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(true)
            adapter = noteAdapter
        }

        noteViewModel.getAllNotes().observe(viewLifecycleOwner) { notes ->
            noteAdapter.differ.submitList(notes)
            updateUI(notes)
        }
    }

    private fun updateUI(note: List<Note>?) {
        if (note != null && note.isNotEmpty()) {
            binding.emptyNotesImage.visibility = View.GONE
            binding.homeRecyclerView.visibility = View.VISIBLE
        } else {
            binding.emptyNotesImage.visibility = View.VISIBLE
            binding.homeRecyclerView.visibility = View.GONE
        }
    }

    private fun searchNote(query: String?) {
        val searchQuery = "%$query%"
        noteViewModel.searchNote(searchQuery).observe(viewLifecycleOwner) { list ->
            noteAdapter.differ.submitList(list)
        }
    }

    override fun onQueryTextSubmit(query: String?): Boolean = false

    override fun onQueryTextChange(newText: String?): Boolean {
        newText?.let { searchNote(it) }
        return true
    }

    override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
        menu.clear()
        menuInflater.inflate(R.menu.home_menu, menu)

        val menuSearch = menu.findItem(R.id.searchMenu).actionView as SearchView
        menuSearch.isSubmitButtonEnabled = false
        menuSearch.setOnQueryTextListener(this)
    }

    override fun onMenuItemSelected(menuItem: MenuItem): Boolean = false

    override fun onDestroyView() {
        super.onDestroyView()
        homeBinding = null
    }
}
