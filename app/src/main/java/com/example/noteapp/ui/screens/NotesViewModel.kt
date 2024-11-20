package com.example.noteapp.ui.screens


import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.noteapp.data.UserPreferencesRepository
import com.example.noteapp.data.models.Note
import com.example.noteapp.data.repositories.NotesRepository
import com.example.noteapp.utils.Converters
import com.example.noteapp.utils.PreDefinedColors
import com.example.noteapp.utils.getColor
import com.example.noteapp.utils.getRandomColor
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

data class NotesUIState(
    val notes: List<Note> = emptyList(),
    val toDeleteList: List<Note> = emptyList(),
    val isBlackTheme: MutableState<Boolean> = mutableStateOf(false),
    val selectedNote: Note? = null
)

class NotesViewModel(
    private val notesRepository: NotesRepository,
    private val userPreferencesRepository: UserPreferencesRepository
) : ViewModel() {
    private val _uiState = MutableStateFlow(NotesUIState())
    val uiState: StateFlow<NotesUIState> = _uiState.asStateFlow()

    init {
        viewModelScope.launch {
            initializeViewModel()
        }
    }

    fun changeSelectedNote(note: Note) {
        _uiState.update { currentState ->
            currentState.copy(selectedNote = note)
        }
    }

    fun changeTheme(isBlackTheme: Boolean) {
        viewModelScope.launch {
            userPreferencesRepository.saveThemePreference(isBlackTheme)
            _uiState.update { currentState ->
                currentState.copy(isBlackTheme = mutableStateOf(isBlackTheme))
            }
        }
    }

    fun isDarkThemeFlow(): Flow<Boolean> {
        return userPreferencesRepository.isBlackTheme
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

    private suspend fun initializeViewModel() {
        observeUserPreferences()
        refreshNotes()
    }

    private fun observeUserPreferences() {
        viewModelScope.launch {
            userPreferencesRepository.isBlackTheme
                .collect { isBlackTheme ->
                    _uiState.update { currentState ->
                        currentState.copy(isBlackTheme = mutableStateOf(isBlackTheme))
                    }
                }
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