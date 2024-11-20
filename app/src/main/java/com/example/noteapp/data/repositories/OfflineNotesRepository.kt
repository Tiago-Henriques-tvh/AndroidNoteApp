package com.example.noteapp.data.repositories

import com.example.noteapp.data.dao.NotesDao
import com.example.noteapp.data.models.Note
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.withContext

class OfflineNotesRepository(private val notesDao: NotesDao) : NotesRepository {
    override suspend fun insertNote(note: Note) =
        withContext(Dispatchers.IO) {
            notesDao.insertNote(note)
        }

    override suspend fun deleteNote(note: Note) =
        withContext(Dispatchers.IO) {
            notesDao.deleteNote(note)
        }

    override suspend fun updateNote(note: Note) =
        withContext(Dispatchers.IO) {
            notesDao.updateNote(note)
        }

    override fun getAllNotesStream(): Flow<List<Note>> = notesDao.getAllNotesStream()

    override fun getNoteStream(title: String): Flow<List<Note?>> = notesDao.getNoteStream(title)
}