package com.example.noteapp.data.models

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "note")
data class Note(
    @PrimaryKey(autoGenerate = true)
    val id: Int,
    val title: String,
    val description: String,
    @ColumnInfo(name = "color_name")
    val colorName: String,
    @ColumnInfo(name = "color")
    val color: Long,
    val done: Boolean
)