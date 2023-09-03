package com.example.brainstormapp.data.database

import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.PrimaryKey
import java.util.Date

@Entity
data class Note(
    @PrimaryKey(autoGenerate = true)
    val uid: Int = 0,
    val title: String?,
    val tagColor: Long?,
    val content: String?,
    val dateCreated: Date?,
    @Ignore
    var selected: Boolean = false
){
    constructor(uid: Int, title: String, tagColor: Long, content: String, dateCreated: Date) : this(
        uid,
        title,
        tagColor,
        content,
        dateCreated,
        false
    )
}