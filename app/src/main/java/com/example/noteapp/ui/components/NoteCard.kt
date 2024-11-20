package com.example.noteapp.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.noteapp.data.models.Note
import com.example.noteapp.ui.theme.NoteAppTheme
import com.example.noteapp.utils.Converters
import com.example.noteapp.utils.PreDefinedColors

@Composable
fun NoteCard(
    note: Note,
    modifier: Modifier = Modifier,
    onDelete: () -> Unit = {},
    onSelectedNote: () -> Unit = {},
    select: (Note) -> Unit = {},
    isEditable: Boolean = false,
    onTitleChange: (String) -> Unit = {},
    onDescriptionChange: (String) -> Unit = {}
) {
    var title by remember(note.id) { mutableStateOf(note.title) }
    var description by remember(note.id) { mutableStateOf(note.description) }
    val color = Converters.longToColor(note.color)

    ElevatedCard(
        modifier = modifier,
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(color),
        onClick = {
            if (!isEditable) {
                onSelectedNote()
                select(note)
            }
        }
    ) {
        Column(
            modifier = Modifier
                .padding(16.dp)
                .width(360.dp),
            verticalArrangement = Arrangement.Center,
        ) {
            Row(
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                if (isEditable) {
                    TextField(
                        value = title,
                        onValueChange = {
                            title = it
                            onTitleChange(it)
                        },
                        textStyle = MaterialTheme.typography.titleLarge,
                        modifier = Modifier
                            .width(270.dp)
                            .weight(0.9f),
                        maxLines = 4,
                        colors = TextFieldDefaults.colors(
                            disabledContainerColor = color,
                            unfocusedContainerColor = color,
                            focusedContainerColor = color,
                            disabledTextColor = Color.Black,
                            unfocusedTextColor = Color.Black,
                            focusedTextColor = Color.Black
                        ),
                    )
                } else {
                    Text(
                        text = title,
                        modifier = Modifier
                            .width(270.dp)
                            .weight(0.9f),
                        style = MaterialTheme.typography.titleLarge,
                        color = Color.Black
                    )
                }
                if (!isEditable) DeleteNoteButton(
                    onClick = onDelete,
                    modifier = Modifier.weight(0.1f)
                )
            }

            if (isEditable) {
                TextField(
                    value = description,
                    onValueChange = {
                        description = it
                        onDescriptionChange(it)
                    },
                    textStyle = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.fillMaxWidth(),
                    colors = TextFieldDefaults.colors(
                        disabledContainerColor = color,
                        unfocusedContainerColor = color,
                        focusedContainerColor = color,
                        disabledTextColor = Color.Black,
                        unfocusedTextColor = Color.Black,
                        focusedTextColor = Color.Black
                    ),
                )
            } else {
                Text(
                    text = description,
                    style = MaterialTheme.typography.bodyMedium,
                    color = Color.Black,
                )
            }
        }
    }
}

@Composable
private fun DeleteNoteButton(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    IconButton(
        onClick = onClick, modifier = modifier
    ) {
        Icon(
            imageVector = Icons.Filled.Close,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.error
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NoteCardPreview() {
    NoteAppTheme(darkTheme = true) {
        NoteCard(
            Note(
                title = "Note title 1",
                description = "Some random text as a description of the note. This needs to fill up some space to be tested, so Im putting some random text here.",
                color = Converters.colorToLong(Color(0xDDF163D2)),
                done = false,
                id = 0,
                colorName = Converters.predefinedColorToString(PreDefinedColors.Random)
            ),
            onSelectedNote = {}
        )
    }
}
