package com.example.noteapp.utils

import androidx.compose.ui.graphics.Color
import kotlin.random.Random

fun getColor(colorName: PreDefinedColors): Color {
    return when (colorName) {
        PreDefinedColors.Red -> Color(0xFFFF5252)
        PreDefinedColors.Blue -> Color(0xFF40C4FF)
        PreDefinedColors.Orange -> Color(0xFFFFAB40)
        PreDefinedColors.Yellow -> Color(0xFFFFD740)
        else -> Color.Gray
    }
}

fun getRandomColor(): Color {
    val red = Random.nextInt(100, 256)
    val green = Random.nextInt(100, 256)
    val blue = Random.nextInt(100, 256)
    return Color(red, green, blue)
}