package com.example.noteapp.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.example.noteapp.R
import com.example.noteapp.data.models.Note

@Composable
fun DisplayListNotes(
    notes: List<Note>,
    onDelete: (Note) -> Unit,
    modifier: Modifier = Modifier,
    onSelectedNote: () -> Unit = {},
    select: (Note) -> Unit = {}
) {
    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background),
        verticalArrangement = Arrangement.Top,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        item {
            if (notes.isEmpty()) {
                Text(
                    text = stringResource(R.string.no_notes_yet),
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(32.dp),
                    color = MaterialTheme.colorScheme.onBackground
                )
            }
        }
        items(notes, key = { it.id }) { note ->
            NoteCard(
                note = note,
                onDelete = { onDelete(note) },
                modifier = Modifier.padding(8.dp),
                onSelectedNote = onSelectedNote,
                select = select,
            )
        }
    }
}
