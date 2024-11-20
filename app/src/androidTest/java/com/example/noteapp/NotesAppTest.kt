package com.example.noteapp

import androidx.test.ext.junit.runners.AndroidJUnit4
import androidx.test.platform.app.InstrumentationRegistry
import org.junit.Assert.assertNotNull
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class NotesAppTest {
    @Test
    fun testNotesApplicationContext() {
        val appContext = InstrumentationRegistry.getInstrumentation().targetContext
        val notesApplication = appContext.applicationContext as NotesApplication

        assertNotNull(notesApplication.userPreferencesRepository)
        assertNotNull(notesApplication.container)
    }
}