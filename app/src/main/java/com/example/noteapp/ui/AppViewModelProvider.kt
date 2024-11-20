package com.example.noteapp.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.noteapp.NotesApplication
import com.example.noteapp.ui.screens.NotesViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            val application = (this[APPLICATION_KEY] as NotesApplication)

            NotesViewModel(
                notesApplication().container.notesRepository,
                application.userPreferencesRepository
            )
        }
    }
}

fun CreationExtras.notesApplication(): NotesApplication =
    (this[APPLICATION_KEY] as NotesApplication)