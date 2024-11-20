package com.example.noteapp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.noteapp.R
import com.example.noteapp.data.models.Note
import com.example.noteapp.ui.AppViewModelProvider
import com.example.noteapp.ui.components.NoteCard
import com.example.noteapp.ui.theme.NoteAppTheme

@Composable
fun NoteDetailScreen(
    note: Note,
    viewModel: NotesViewModel = viewModel(factory = AppViewModelProvider.Factory),
    onNavigateBack: () -> Unit = {}
) {
    var title by rememberSaveable { mutableStateOf(note.title) }
    var description by rememberSaveable { mutableStateOf(note.description) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NoteCard(
            note = note,
            modifier = Modifier.weight(0.9f),
            isEditable = true,
            onTitleChange = { title = it },
            onDescriptionChange = { description = it },
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                val updatedNote = note.copy(title = title, description = description)
                viewModel.updateNote(updatedNote)
                onNavigateBack()
            },
            content = {
                Text(
                    text = stringResource(R.string.update_note),
                    style = MaterialTheme.typography.titleLarge,
                    color = MaterialTheme.colorScheme.onTertiaryContainer
                )
            },
            modifier = Modifier.fillMaxWidth(),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.tertiaryContainer)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NoteDetailScreenPreview() {
    NoteAppTheme {
        NoteDetailScreen(
            note = Note(1, "Title", "Description", "Blue", 0xFF0000FF, false),
        )
    }
}