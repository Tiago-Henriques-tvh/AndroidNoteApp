package com.example.noteapp

import com.example.noteapp.data.models.Note
import com.example.noteapp.data.repositories.NotesRepository
import com.example.noteapp.ui.screens.viewModels.NotesUIState
import com.example.noteapp.ui.screens.viewModels.NotesViewModel
import com.example.noteapp.utils.PreDefinedColors
import io.mockk.MockKAnnotations
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import junit.framework.TestCase.assertTrue
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.toList
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Test

@ExperimentalCoroutinesApi
class NotesViewModelTest {

    private lateinit var viewModel: NotesViewModel

    @MockK(relaxed = true)
    private lateinit var notesRepository: NotesRepository

    @Before
    fun setUp() {
        Dispatchers.setMain(StandardTestDispatcher())
        MockKAnnotations.init(this, relaxUnitFun = true)

        // Mock repository behavior
        notesRepository = mockk(relaxed = true)
        coEvery { notesRepository.getAllNotesStream() } returns flowOf(emptyList())

        // Initialize ViewModel
        viewModel = NotesViewModel(notesRepository)
    }

    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `test addNote adds a note to the repository`() = runTest {
        val note = Note(
            id = 0,
            title = "Test Note",
            description = "Test Description",
            colorName = "RandomColor",
            color = 0xFFFFFF,
            done = false
        )

        coEvery { notesRepository.insertNote(any()) } returns Unit
        val expectedNotes = listOf(note)
        coEvery { notesRepository.getAllNotesStream() } returns flowOf(expectedNotes)

        viewModel.addNote(note.title, note.description, PreDefinedColors.Random)

        coVerify { notesRepository.insertNote(note) }

        val currentNotes = mutableListOf<NotesUIState>()
        viewModel.uiState.toList(currentNotes)
        assertTrue(currentNotes.any { it.notes.contains(note) })
    }
}