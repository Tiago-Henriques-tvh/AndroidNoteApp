package com.example.noteapp.ui.screens.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteapp.data.models.Note
import com.example.noteapp.data.repositories.NotesRepository
import com.example.noteapp.utils.Converters
import com.example.noteapp.utils.PreDefinedColors
import com.example.noteapp.utils.getColor
import com.example.noteapp.utils.getRandomColor
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class NotesUIState(
    val notes: List<Note> = emptyList(), // For home screen
    val toDeleteList: List<Note> = emptyList(), // For delete screen
    val selectedNote: Note? = null // For detail screen
)

class NotesViewModel(
    private val notesRepository: NotesRepository,
) : ViewModel() {
    private val _uiState = MutableStateFlow(NotesUIState())
    val uiState: StateFlow<NotesUIState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            refreshNotes()
        }
    }

    fun changeSelectedNote(note: Note) {
        _uiState.update { currentState ->
            currentState.copy(selectedNote = note)
        }
    }

    fun addNote(title: String, description: String, colorName: PreDefinedColors) {
        viewModelScope.launch {
            val color = if (colorName == PreDefinedColors.Random) {
                getRandomColor()
            } else {
                getColor(colorName)
            }
            val newNote = Note(
                title = title,
                description = description,
                colorName = Converters.predefinedColorToString(colorName),
                color = Converters.colorToLong(color),
                done = false,
                id = 0
            )
            notesRepository.insertNote(newNote)
            refreshNotes()
        }
    }

    fun setNoteDone(note: Note) {
        viewModelScope.launch {
            notesRepository.updateNote(note.copy(done = !note.done))
            refreshNotes()
        }
    }

    fun deleteNote(note: Note) {
        viewModelScope.launch {
            notesRepository.deleteNote(note)
            refreshNotes()
        }
    }

    fun searchNotes(title: String) {
        viewModelScope.launch {
            notesRepository.getNoteStream(title)
                .filterNotNull()
                .collect { notes ->
                    _uiState.update { currentState ->
                        currentState.copy(notes = notes.filterNotNull().filter { !it.done })
                    }
                }
        }
    }

    fun updateNote(note: Note) {
        viewModelScope.launch {
            notesRepository.updateNote(note)
            refreshNotes()
        }
    }

    private suspend fun refreshNotes() {
        notesRepository.getAllNotesStream()
            .filterNotNull()
            .collect { notes ->
                _uiState.update { currentState ->
                    currentState.copy(
                        notes = notes.filter { !it.done },
                        toDeleteList = notes.filter { it.done })
                }
            }
    }
}