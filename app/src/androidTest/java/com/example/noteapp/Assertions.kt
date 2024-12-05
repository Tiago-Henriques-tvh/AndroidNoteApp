package com.example.noteapp

import android.util.Log
import androidx.navigation.NavController
import org.junit.Assert

fun NavController.assertCurrentRouteName(expectedRouteName: String) {
    Log.d("NavigationTest", "Expected route name: $expectedRouteName")
    Log.d("NavigationTest", "Current route name: ${currentBackStackEntry?.destination?.route}")
    Assert.assertEquals(expectedRouteName, currentBackStackEntry?.destination?.route)
}

annotation class TestCompactWidth
annotation class TestExpandedWidth