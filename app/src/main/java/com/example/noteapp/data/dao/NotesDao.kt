package com.example.noteapp.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.noteapp.data.models.Note
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDao {
    @Insert
    fun insertNote(note: Note)

    @Update
    fun updateNote(note: Note)

    @Delete
    fun deleteNote(note: Note)

    @Query("SELECT * FROM note ORDER BY id ASC")
    fun getAllNotesStream(): Flow<List<Note>>

    @Query("SELECT * FROM note WHERE title LIKE '%'||:title||'%' ORDER BY title ASC")
    fun getNoteStream(title: String): Flow<List<Note?>>
}
