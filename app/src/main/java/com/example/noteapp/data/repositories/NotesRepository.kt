package com.example.noteapp.data.repositories

import com.example.noteapp.data.models.Note
import kotlinx.coroutines.flow.Flow

interface NotesRepository {
    suspend fun insertNote(note: Note)
    suspend fun deleteNote(note: Note)
    suspend fun updateNote(note: Note)
    fun getAllNotesStream(): Flow<List<Note>>
    fun getNoteStream(title: String): Flow<List<Note?>>
}