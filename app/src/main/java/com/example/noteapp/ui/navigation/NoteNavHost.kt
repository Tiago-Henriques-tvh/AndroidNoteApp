package com.example.noteapp.ui.navigation

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.example.noteapp.ui.screens.NoteAppScreens
import com.example.noteapp.ui.screens.NoteDetailScreen
import com.example.noteapp.ui.screens.NotesHomeScreen
import com.example.noteapp.ui.screens.NotesToDeleteScreen
import com.example.noteapp.ui.screens.viewModels.NotesUIState
import com.example.noteapp.ui.screens.viewModels.NotesViewModel
import com.example.noteapp.utils.WindowStateUtils

@Composable
fun NoteNavHost(
    navController: NavHostController,
    innerPadding: PaddingValues,
    contentPadding: WindowStateUtils,
    viewModel: NotesViewModel,
    uiState: NotesUIState
) {
    NavHost(
        navController = navController,
        startDestination = NoteAppScreens.Notes.name,
        modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)
    ) {
        composable(NoteAppScreens.Notes.name) {
            NotesHomeScreen(
                notesList = uiState.notes,
                contentPadding = contentPadding,
                hasNotesToDelete = uiState.toDeleteList.isNotEmpty(),
                onDeletedNotesClicked = { navController.navigate(NoteAppScreens.NotesToDelete.name) },
                onSelectedNote = { navController.navigate(NoteAppScreens.NoteDetail.name) },
                select = viewModel::changeSelectedNote,
                viewModel = viewModel
            )
        }
        composable(NoteAppScreens.NotesToDelete.name) {
            NotesToDeleteScreen(
                notesToDelete = uiState.toDeleteList,
                navigateUp = { navController.navigateUp() },
                viewModel = viewModel
            )
        }
        composable(NoteAppScreens.NoteDetail.name) {
            uiState.selectedNote?.let { note ->
                NoteDetailScreen(
                    note = note,
                    viewModel = viewModel,
                    onNavigateBack = { navController.navigateUp() }
                )
            }
        }
    }
}
