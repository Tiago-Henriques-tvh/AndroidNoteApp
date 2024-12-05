package com.example.noteapp.ui.screens

import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.noteapp.R
import com.example.noteapp.ui.AppViewModelProvider
import com.example.noteapp.ui.navigation.NoteNavHost
import com.example.noteapp.ui.screens.viewModels.NotesViewModel
import com.example.noteapp.ui.screens.viewModels.UserPreferencesViewModel
import com.example.noteapp.ui.theme.NoteAppTheme
import com.example.noteapp.utils.WindowStateUtils

enum class NoteAppScreens(@StringRes val title: Int) {
    Notes(title = R.string.my_notes), NotesToDelete(title = R.string.notes_to_delete), NoteDetail(
        title = R.string.note_detail
    )
}

@Composable
fun NoteAppHomeScreen(
    modifier: Modifier = Modifier,
    windowSize: WindowWidthSizeClass = WindowWidthSizeClass.Compact,
    preferencesViewModel: UserPreferencesViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val navController = rememberNavController()
    val backStackEntry by navController.currentBackStackEntryAsState()
    val currentScreen = NoteAppScreens.valueOf(
        backStackEntry?.destination?.route ?: NoteAppScreens.Notes.name
    )
    val contentPadding = determineContentType(windowSize)
    val viewModel: NotesViewModel = viewModel(factory = AppViewModelProvider.Factory)
    val uiState by viewModel.uiState.collectAsState()

    Scaffold(topBar = {
        TopAppBar(
            currentScreen = currentScreen,
            canNavigateBack = navController.previousBackStackEntry != null,
            navigateUp = { navController.navigateUp() },
            changeTheme = { isChecked ->
                preferencesViewModel.changeTheme(isBlackTheme = isChecked)
            },
            isBlackTheme = preferencesViewModel.isDarkThemeFlow.collectAsState(initial = preferencesViewModel.getInitialTheme()).value
        )
    }) { innerPadding ->
        NoteNavHost(
            navController = navController,
            innerPadding = innerPadding,
            contentPadding = contentPadding,
            viewModel = viewModel,
            uiState = uiState
        )
    }
}

private fun determineContentType(windowSize: WindowWidthSizeClass): WindowStateUtils {
    return when (windowSize) {
        WindowWidthSizeClass.Expanded -> WindowStateUtils.EXPANDED
        else -> WindowStateUtils.COMPACT
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TopAppBar(
    currentScreen: NoteAppScreens,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    changeTheme: (Boolean) -> Unit,
    isBlackTheme: Boolean = false,
    modifier: Modifier = Modifier
) {
    CenterAlignedTopAppBar(title = {
        Row(
            verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(8.dp)
        ) {
            Text(
                text = stringResource(currentScreen.title),
                style = MaterialTheme.typography.displayMedium,
                modifier = Modifier.weight(0.8f)
            )
            ThemeSwitch(
                isBlackTheme = isBlackTheme,
                onToggle = changeTheme,
                modifier = Modifier.weight(0.2f)
            )
        }
    }, modifier = modifier, colors = TopAppBarDefaults.topAppBarColors(
        containerColor = MaterialTheme.colorScheme.primaryContainer
    ), navigationIcon = {
        if (canNavigateBack) {
            IconButton(onClick = navigateUp) {
                Icon(
                    imageVector = Icons.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back_button)
                )
            }
        }
    })
}

@Composable
fun ThemeSwitch(
    isBlackTheme: Boolean, onToggle: (Boolean) -> Unit, modifier: Modifier = Modifier
) {
    Switch(
        checked = isBlackTheme, onCheckedChange = onToggle, colors = SwitchDefaults.colors(
            checkedThumbColor = MaterialTheme.colorScheme.secondary,
            uncheckedThumbColor = MaterialTheme.colorScheme.onSecondary
        ), modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun NoteScreenPreview() {
    NoteAppTheme(darkTheme = true) {
        NoteAppHomeScreen()
    }
}

@Preview(showBackground = true, widthDp = 1000)
@Composable
fun NoteScreenPreviewDark() {
    NoteAppTheme(darkTheme = true) {
        Surface {
            NoteAppHomeScreen(windowSize = WindowWidthSizeClass.Expanded)
        }
    }
}
