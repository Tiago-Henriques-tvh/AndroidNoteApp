package com.example.noteapp

import android.content.Context
import androidx.room.Room.inMemoryDatabaseBuilder
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.noteapp.data.dao.NotesDao
import com.example.noteapp.data.database.NotesDatabase
import com.example.noteapp.data.models.Note
import junit.framework.TestCase.assertEquals
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class NotesDaoTest {
    private lateinit var noteDao: NotesDao
    private lateinit var notesDatabase: NotesDatabase

    private val note1 = Note(1, "Title1", "Description1", "Blue", 0xFF0000FF, false)
    private val note2 = Note(2, "Title2", "Description2", "Blue", 0xFF0000FF, false)
    private val note3 = Note(3, "Title3", "Description3", "Blue", 0xFF0000FF, false)

    @Before
    fun createDb() {
        val context: Context = ApplicationProvider.getApplicationContext()
        notesDatabase =
            inMemoryDatabaseBuilder(context, NotesDatabase::class.java).allowMainThreadQueries()
                .build()
        noteDao = notesDatabase.notesDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        notesDatabase.close()
    }

    @Test
    @Throws(Exception::class)
    fun insertAndGetNote() = runBlocking {
        insertOneNoteToDB()
        val retrievedNotes = noteDao.getNoteStream(note1.title).first()
        assertEquals(listOf(note1), retrievedNotes)
    }

    @Test
    @Throws(Exception::class)
    fun getAllNotes() = runBlocking {
        insertThreeNotesToDB()
        val retrievedNotes = noteDao.getAllNotesStream().first()
        assertEquals(listOf(note1, note2, note3), retrievedNotes)
    }

    @Test
    @Throws(Exception::class)
    fun updateNoteAndGetNote() = runBlocking {
        insertOneNoteToDB()

        val updatedNote = note1.copy(title = "Updated Title", description = "Updated Description")
        noteDao.updateNote(updatedNote)

        val retrievedNotes = noteDao.getNoteStream(updatedNote.title).first()
        assertEquals(listOf(updatedNote), retrievedNotes)
    }

    @Test
    @Throws(Exception::class)
    fun deleteNoteAndGetNote() = runBlocking {
        insertOneNoteToDB()
        noteDao.deleteNote(note1)
        val retrievedNotes = noteDao.getNoteStream(note1.title).first()
        assertEquals(emptyList<Note>(), retrievedNotes)
    }

    @Test
    @Throws(Exception::class)
    fun searchByNotesTitle() = runBlocking {
        insertThreeNotesToDB()
        val retrievedNotes = noteDao.getNoteStream("1").first()
        assertEquals(listOf(note1), retrievedNotes)
    }

    private fun insertOneNoteToDB() {
        noteDao.insertNote(note1)
    }

    private fun insertThreeNotesToDB() {
        noteDao.insertNote(note1)
        noteDao.insertNote(note2)
        noteDao.insertNote(note3)

    }
}

