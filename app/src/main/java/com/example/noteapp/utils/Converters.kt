package com.example.noteapp.utils

import androidx.compose.ui.graphics.Color

object Converters {
    fun colorToLong(color: Color): Long {
        val alpha = (color.alpha * 255).toInt()
        val red = (color.red * 255).toInt()
        val green = (color.green * 255).toInt()
        val blue = (color.blue * 255).toInt()
        return (alpha.toLong() shl 24) or (red.toLong() shl 16) or (green.toLong() shl 8) or blue.toLong()
    }

    fun longToColor(value: Long): Color {
        return Color(value)
    }

    fun predefinedColorToString(color: PreDefinedColors): String {
        return color.name
    }
}

enum class PreDefinedColors {
    Yellow,
    Red,
    Blue,
    Orange,
    Random
}