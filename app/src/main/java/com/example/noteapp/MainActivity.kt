package com.example.noteapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.asPaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.material3.Scaffold
import androidx.compose.material3.windowsizeclass.ExperimentalMaterial3WindowSizeClassApi
import androidx.compose.material3.windowsizeclass.calculateWindowSizeClass
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.noteapp.ui.AppViewModelProvider
import com.example.noteapp.ui.screens.NoteAppHomeScreen
import com.example.noteapp.ui.screens.NotesViewModel
import com.example.noteapp.ui.theme.NoteAppTheme

class MainActivity : ComponentActivity() {
    @OptIn(ExperimentalMaterial3WindowSizeClassApi::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            val viewModel: NotesViewModel = viewModel(factory = AppViewModelProvider.Factory)
            val isDarkTheme by viewModel.isDarkThemeFlow().collectAsState(initial = false)

            NoteAppTheme(darkTheme = isDarkTheme) {
                val layoutDirection = LocalLayoutDirection.current
                Scaffold(
                    modifier = Modifier.padding(
                        start = WindowInsets.safeDrawing.asPaddingValues()
                            .calculateLeftPadding(layoutDirection),
                        end = WindowInsets.safeDrawing.asPaddingValues()
                            .calculateRightPadding(layoutDirection),
                    )
                ) { innerPadding ->
                    val windowSize = calculateWindowSizeClass(this)
                    NoteAppHomeScreen(
                        modifier = Modifier.padding(innerPadding),
                        windowSize = windowSize.widthSizeClass,
                        viewModel
                    )
                }
            }
        }
    }
}