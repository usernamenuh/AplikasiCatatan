package com.example.k5n.viewmodel

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.k5n.repository.NoteRepository

class NoteViewModelFactory(
    private val application: Application,
    private val noteRepository: NoteRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NoteViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return NoteViewModel(application, noteRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
