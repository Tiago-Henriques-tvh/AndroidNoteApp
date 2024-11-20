package com.example.noteapp

import com.example.noteapp.data.UserPreferencesRepository
import com.example.noteapp.data.models.Note
import com.example.noteapp.data.repositories.NotesRepository
import com.example.noteapp.ui.screens.NotesViewModel
import com.example.noteapp.utils.PreDefinedColors
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.verify

@ExperimentalCoroutinesApi
class NotesViewModelTest {
    private lateinit var viewModel: NotesViewModel

    @Mock
    private lateinit var notesRepository: NotesRepository

    @Mock
    private lateinit var userPreferencesRepository: UserPreferencesRepository

    @Before
    fun setUp() {
        MockitoAnnotations.openMocks(this)
        viewModel = NotesViewModel(notesRepository, userPreferencesRepository)
    }

    @Test
    fun `test changeSelectedNote updates the selectedNote state`() = runBlockingTest {
        val note = Note(1, "Sample Title", "Sample Description", "Red", 0xFFFF0000, false)

        viewModel.changeSelectedNote(note)

        val selectedNote = viewModel.uiState.value.selectedNote
        assert(selectedNote == note)
    }

    @Test
    fun `test changeTheme updates the theme and saves it to preferences`() = runBlockingTest {
        val isBlackTheme = true

        viewModel.changeTheme(isBlackTheme)

        verify(userPreferencesRepository).saveThemePreference(isBlackTheme)
        assert(viewModel.uiState.value.isBlackTheme.value == isBlackTheme)
    }

    @Test
    fun `test addNote inserts a note and refreshes notes`() = runBlockingTest {
        val title = "New Note"
        val description = "New Note Description"
        val colorName = PreDefinedColors.Red

        viewModel.addNote(title, description, colorName)
        verify(notesRepository).insertNote(Mockito.any())
    }

    @Test
    fun `test searchNotes filters and updates notes`() = runBlockingTest {
        val note1 = Note(1, "Sample Title", "Description 1", "Red", 0xFFFF0000, false)
        val note2 = Note(2, "Other Title", "Description 2", "Blue", 0xFF0000FF, false)
        val titleSearch = "Sample"

        val mockNotesFlow = MutableStateFlow(listOf(note1, note2))
        `when`(notesRepository.getAllNotesStream()).thenReturn(mockNotesFlow)


        viewModel.searchNotes(titleSearch)
        assertEquals(viewModel.uiState.value.notes.contains(note1), true)
        assertEquals(viewModel.uiState.value.notes.contains(note2), false)
    }

    @Test
    fun `test deleteNote removes the note and refreshes the notes`() = runBlockingTest {
        val note = Note(1, "Sample Title", "Sample Description", "Red", 0xFFFF0000, false)
        viewModel.deleteNote(note)
        verify(notesRepository).deleteNote(note)

    }
}
