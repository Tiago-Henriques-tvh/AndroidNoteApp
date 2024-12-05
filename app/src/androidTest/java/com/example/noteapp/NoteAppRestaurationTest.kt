package com.example.noteapp

import androidx.activity.ComponentActivity
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createAndroidComposeRule
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.noteapp.ui.AppViewModelProvider
import com.example.noteapp.ui.screens.NoteAppHomeScreen
import org.junit.Rule
import org.junit.Test

class NoteAppRestaurationTest {
    @get:Rule
    val composeTestRule = createAndroidComposeRule<ComponentActivity>()

    @Test
    @TestCompactWidth
    fun compactDevice_addNote_afterConfigChange() {
        val stateRestorationTester = StateRestorationTester(composeTestRule)
        stateRestorationTester.setContent {
            NoteAppHomeScreen(
                windowSize = WindowWidthSizeClass.Compact,
                preferencesViewModel = viewModel(factory = AppViewModelProvider.Factory)
            )
        }

        composeTestRule.onNodeWithText("Add Note").performClick()
        composeTestRule.onNodeWithText("Title").performTextInput("Test Title")
        composeTestRule.onNodeWithText("Confirm").performClick()

        stateRestorationTester.emulateSavedInstanceStateRestore()
    }

    @Test
    @TestExpandedWidth
    fun expandedDevice_addNote_afterConfigChange() {
        val stateRestorationTester = StateRestorationTester(composeTestRule)
        stateRestorationTester.setContent {
            NoteAppHomeScreen(
                windowSize = WindowWidthSizeClass.Expanded,
                preferencesViewModel = viewModel(factory = AppViewModelProvider.Factory)
            )
        }

        composeTestRule.onNodeWithText("Add Note").performClick()
        composeTestRule.onNodeWithText("Title").performTextInput("Test Title")
        composeTestRule.onNodeWithText("Confirm").performClick()

        stateRestorationTester.emulateSavedInstanceStateRestore()
    }
}
