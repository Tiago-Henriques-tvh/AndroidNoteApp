package com.example.noteapp.ui.screens

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.noteapp.data.models.Note
import com.example.noteapp.ui.AppViewModelProvider
import com.example.noteapp.ui.components.DisplayListNotes

@Composable
fun NotesToDeleteScreen(
    notesToDelete: List<Note> = emptyList(),
    navigateUp: () -> Boolean,
    viewModel: NotesViewModel = viewModel(factory = AppViewModelProvider.Factory),
) {
    val onDeleteNote = { note: Note -> viewModel.deleteNote(note) }

    DisplayListNotes(
        notes = notesToDelete,
        onDelete = onDeleteNote,
    )

    if (notesToDelete.isEmpty()) {
        navigateUp()
    }
}