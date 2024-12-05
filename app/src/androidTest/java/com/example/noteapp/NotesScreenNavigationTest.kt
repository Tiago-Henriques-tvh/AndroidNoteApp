package com.example.noteapp

import androidx.activity.ComponentActivity
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.ComposeNavigator
import androidx.navigation.testing.TestNavHostController
import com.example.noteapp.ui.AppViewModelProvider
import com.example.noteapp.ui.screens.NoteAppHomeScreen
import com.example.noteapp.ui.screens.NoteAppScreens
import org.junit.Before
import org.junit.Rule
import org.junit.Test

class NotesScreenNavigationTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    private lateinit var navController: TestNavHostController

    @Before
    fun setupNotesNavHost() {
        composeTestRule.setContent {
            navController = TestNavHostController(LocalContext.current).apply {
                navigatorProvider.addNavigator(ComposeNavigator())
            }
            NoteAppHomeScreen(
                windowSize = WindowWidthSizeClass.Compact,
                preferencesViewModel = viewModel(factory = AppViewModelProvider.Factory)
            )
        }

        composeTestRule.onNodeWithText("Add Note").performClick()
        composeTestRule.onNodeWithText("Title").performTextInput("Test Title")
        composeTestRule.onNodeWithText("Confirm").performClick()
    }

    @Test
    fun notesNavHost_verifyStartDestination() {
        navController.assertCurrentRouteName(NoteAppScreens.Notes.name)
    }

    @Test
    fun notesNavHost_verifyBackNavigationNotShownOnNotesScreen() {
        val backText = composeTestRule.activity.getString(R.string.back_button)
        composeTestRule.onNodeWithContentDescription(backText).assertDoesNotExist()
    }

    @Test
    fun notesNavHost_navigateToDetailsAndBack() {
        composeTestRule.onNodeWithStringId(R.string.notecard).performClick()
        navController.assertCurrentRouteName(NoteAppScreens.NoteDetail.name)
        performNavigateUp()
    }

    @Test
    fun notesNavHost_navigateToDeleteAndBack() {
        composeTestRule.onNodeWithStringId(R.string.to_trash_screen).performClick()
        navController.assertCurrentRouteName(NoteAppScreens.NotesToDelete.name)
        performNavigateUp()
    }

    private fun performNavigateUp() {
        val backText = composeTestRule.activity.getString(R.string.back_button)
        composeTestRule.onNodeWithContentDescription(backText).performClick()
        navController.assertCurrentRouteName(NoteAppScreens.Notes.name)
    }
}