package com.example.noteapp.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.noteapp.R
import com.example.noteapp.data.models.Note
import com.example.noteapp.ui.components.AddNoteDialog
import com.example.noteapp.ui.components.DisplayListNotes
import com.example.noteapp.ui.theme.NoteAppTheme
import com.example.noteapp.utils.PreDefinedColors
import com.example.noteapp.utils.WindowStateUtils

@Composable
fun NotesHomeScreen(
    notesList: List<Note> = emptyList(),
    contentPadding: WindowStateUtils = WindowStateUtils.COMPACT,
    hasNotesToDelete: Boolean,
    onDeletedNotesClicked: () -> Unit = {},
    viewModel: NotesViewModel,
    onSelectedNote: () -> Unit,
    select: (Note) -> Unit = {}
) {
    var showDialog by rememberSaveable { mutableStateOf(false) }

    Column(
        modifier = Modifier.fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally
    ) {
        NotesSearchBar(
            modifier = Modifier.padding(16.dp),
            onSearchQueryChanged = { query: String -> viewModel.searchNotes(query) },
        )

        DisplayListNotes(
            notes = notesList,
            onDelete = { note: Note -> viewModel.setNoteDone(note) },
            modifier = Modifier.weight(1f),
            onSelectedNote = onSelectedNote,
            select = select
        )

        ButtonGroup(
            showDialog = showDialog,
            onShowDialog = { showDialog = true },
            onDismissDialog = { showDialog = false },
            onAddNote = { title: String, description: String, colorName: PreDefinedColors ->
                viewModel.addNote(
                    title, description, colorName
                )
                showDialog = false
            },
            hasNotesToDelete = hasNotesToDelete,
            onDeletedNotesClicked = onDeletedNotesClicked,
            isCompact = contentPadding == WindowStateUtils.COMPACT
        )
    }
}

@Composable
fun NotesSearchBar(
    modifier: Modifier = Modifier,
    onSearchQueryChanged: (String) -> Unit,
) {
    var textState by remember { mutableStateOf(TextFieldValue("")) }

    Box(
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxWidth()
                .background(
                    MaterialTheme.colorScheme.secondaryContainer,
                    shape = MaterialTheme.shapes.medium
                )
                .padding(horizontal = 8.dp, vertical = 8.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = "Search Icon",
                    modifier = Modifier.padding(end = 8.dp),
                    tint = MaterialTheme.colorScheme.onSecondaryContainer
                )

                BasicTextField(value = textState,
                    onValueChange = {
                        textState = it
                        onSearchQueryChanged(it.text)
                    },
                    keyboardOptions = KeyboardOptions.Default.copy(
                        imeAction = ImeAction.Done
                    ),
                    singleLine = true,
                    textStyle = MaterialTheme.typography.bodySmall,
                    decorationBox = { innerTextField ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(
                                    Color.White, MaterialTheme.shapes.medium
                                )
                                .padding(8.dp), verticalAlignment = Alignment.CenterVertically
                        ) {
                            AnimatedVisibility(visible = textState.text.isEmpty()) {
                                Text(
                                    text = textState.text,
                                    style = MaterialTheme.typography.bodySmall,
                                    color = Color.Gray
                                )
                            }
                            innerTextField()
                        }
                    })
            }
        }
    }
}

@Composable
fun ButtonGroup(
    showDialog: Boolean,
    onShowDialog: () -> Unit,
    onDismissDialog: () -> Unit,
    onAddNote: (String, String, PreDefinedColors) -> Unit,
    hasNotesToDelete: Boolean,
    onDeletedNotesClicked: () -> Unit,
    isCompact: Boolean
) {
    val layoutModifier = Modifier.padding(16.dp)

    if (isCompact) {
        Column(modifier = layoutModifier, horizontalAlignment = Alignment.CenterHorizontally) {
            NoteButtons(
                showDialog,
                onShowDialog,
                onDismissDialog,
                onAddNote,
                hasNotesToDelete,
                onDeletedNotesClicked
            )
        }
    } else {
        Row(modifier = layoutModifier, verticalAlignment = Alignment.CenterVertically) {
            NoteButtons(
                showDialog,
                onShowDialog,
                onDismissDialog,
                onAddNote,
                hasNotesToDelete,
                onDeletedNotesClicked
            )
        }
    }
}

@Composable
fun NoteButtons(
    showDialog: Boolean,
    onShowDialog: () -> Unit,
    onDismissDialog: () -> Unit,
    onAddNote: (String, String, PreDefinedColors) -> Unit,
    hasNotesToDelete: Boolean,
    onDeletedNotesClicked: () -> Unit
) {
    AddNoteButton(
        onClick = onShowDialog,
        showDialog = showDialog,
        onDismiss = onDismissDialog,
        onConfirm = onAddNote,
    )
    if (hasNotesToDelete) {
        Spacer(modifier = Modifier.size(16.dp))
        DeletedNotesButton(onClick = onDeletedNotesClicked)
    }
}

@Composable
fun AddNoteButton(
    showDialog: Boolean,
    onClick: () -> Unit = {},
    onDismiss: () -> Unit = {},
    onConfirm: (title: String, description: String, color: PreDefinedColors) -> Unit = { _, _, _ -> },
) {
    Box(
        contentAlignment = Alignment.Center
    ) {
        Button(
            onClick = onClick,
            modifier = Modifier.size(300.dp, 48.dp),
            colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.tertiaryContainer)
        ) {
            Text(
                text = stringResource(R.string.add_note_button_text),
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onTertiaryContainer
            )
        }

        if (showDialog) {
            AddNoteDialog(
                onDismiss = onDismiss,
                onConfirm = onConfirm,
            )
        }
    }
}

@Composable
fun DeletedNotesButton(
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = Modifier
            .size(300.dp, 48.dp)
            .testTag(stringResource(R.string.to_trash_screen)),
        colors = ButtonDefaults.buttonColors(MaterialTheme.colorScheme.tertiaryContainer)
    ) {
        Text(
            text = stringResource(R.string.deleted_notes),
            style = MaterialTheme.typography.titleLarge,
            color = MaterialTheme.colorScheme.onTertiaryContainer
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NotesHomeScreenPreview() {
    NoteAppTheme {
        NotesSearchBar(
            onSearchQueryChanged = { },
        )
    }
}