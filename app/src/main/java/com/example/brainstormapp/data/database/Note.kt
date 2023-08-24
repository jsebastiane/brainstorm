package com.example.brainstormapp.data.database

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class Note(
    @PrimaryKey(autoGenerate = true)
    val uid: Int = 0,
    val title: String?,
    val tagColor: Long?,
    val content: String?,
    val dateCreated: Date?



)