package com.example.noteapp.ui

import androidx.lifecycle.ViewModelProvider.AndroidViewModelFactory.Companion.APPLICATION_KEY
import androidx.lifecycle.viewmodel.CreationExtras
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.noteapp.NotesApplication
import com.example.noteapp.ui.screens.viewModels.NotesViewModel
import com.example.noteapp.ui.screens.viewModels.UserPreferencesViewModel

object AppViewModelProvider {
    val Factory = viewModelFactory {
        initializer {
            NotesViewModel(
                notesApplication().container.notesRepository,
            )
        }

        initializer {
            val application = (this[APPLICATION_KEY] as NotesApplication)
            UserPreferencesViewModel(application.userPreferencesRepository)
        }
    }
}

fun CreationExtras.notesApplication(): NotesApplication =
    (this[APPLICATION_KEY] as NotesApplication)