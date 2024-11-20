package com.example.noteapp.ui.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
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
import com.example.noteapp.R
import com.example.noteapp.ui.theme.NoteAppTheme
import com.example.noteapp.utils.PreDefinedColors

@Composable
fun AddNoteDialog(
    onDismiss: () -> Unit,
    onConfirm: (title: String, description: String, color: PreDefinedColors) -> Unit,
) {
    var title by rememberSaveable { mutableStateOf("") }
    var description by rememberSaveable { mutableStateOf("") }
    var selectedColor by rememberSaveable { mutableStateOf(PreDefinedColors.Random) }
    val colors = listOf(
        PreDefinedColors.Red,
        PreDefinedColors.Orange,
        PreDefinedColors.Blue,
        PreDefinedColors.Yellow,
        PreDefinedColors.Random
    )
    val scrollState = rememberScrollState()

    AlertDialog(
        onDismissRequest = onDismiss,
        confirmButton = {
            TextButton(onClick = { onConfirm(title, description, selectedColor) }) {
                Text(
                    text = stringResource(R.string.add_note_confirm),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text(
                    text = stringResource(R.string.add_note_cancel),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        },
        title = {
            Text(
                text = stringResource(R.string.add_note_button),
                style = MaterialTheme.typography.displayLarge
            )
        },
        text = {
            Column(
                modifier = Modifier.verticalScroll(scrollState)
            ) {
                NoteInputField(label = stringResource(R.string.title_dialog_box),
                    value = title,
                    onValueChange = { title = it })
                Spacer(modifier = Modifier.height(16.dp))
                NoteInputField(label = stringResource(R.string.description_dialog_box),
                    value = description,
                    onValueChange = { description = it })
                Spacer(modifier = Modifier.height(16.dp))
                ColorSelectionRow(colors = colors,
                    selectedColor = selectedColor,
                    onColorSelected = { selectedColor = it })
            }
        },
        containerColor = MaterialTheme.colorScheme.secondaryContainer,
        shape = MaterialTheme.shapes.medium
    )
}

@Composable
fun NoteInputField(label: String, value: String, onValueChange: (String) -> Unit) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, style = MaterialTheme.typography.bodyMedium) },
        maxLines = 5,
        textStyle = MaterialTheme.typography.bodySmall,
        colors = TextFieldDefaults.colors(MaterialTheme.colorScheme.onTertiaryContainer),
        shape = MaterialTheme.shapes.small
    )
}

@Composable
fun ColorSelectionRow(
    colors: List<PreDefinedColors>,
    selectedColor: PreDefinedColors,
    onColorSelected: (PreDefinedColors) -> Unit
) {
    Text(text = "Select a color tag:")
    Column {
        for (color in colors) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                RadioButton(
                    selected = color == selectedColor,
                    onClick = { onColorSelected(color) },
                    colors = RadioButtonDefaults.colors(
                        selectedColor = MaterialTheme.colorScheme.tertiary,
                        unselectedColor = MaterialTheme.colorScheme.onTertiaryContainer
                    )
                )
                Text(text = color.name)
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun AddNoteDialogPreview() {
    NoteAppTheme(darkTheme = true) {
        AddNoteDialog(onDismiss = {}, onConfirm = { _, _, _ -> })
    }
}