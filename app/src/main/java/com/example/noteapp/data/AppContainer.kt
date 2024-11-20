package com.example.noteapp.data

import android.content.Context
import com.example.noteapp.data.database.NotesDatabase
import com.example.noteapp.data.repositories.NotesRepository
import com.example.noteapp.data.repositories.OfflineNotesRepository

interface AppContainer {
    val notesRepository: NotesRepository
}

class AppDataContainer(private val context: Context) : AppContainer {
    override val notesRepository: NotesRepository by lazy {
        OfflineNotesRepository(NotesDatabase.getDatabase(context).notesDao())
    }
}