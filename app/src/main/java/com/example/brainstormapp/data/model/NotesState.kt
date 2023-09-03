package com.example.brainstormapp.data.model

import com.example.brainstormapp.data.database.Note

data class NotesState(
    val notesList: List<Note> = emptyList(),
    val errorMessage: String = "",
    val searchQuery: String = "",
    val searchMode: Boolean = false,
    val selectionMode: Boolean = false
    )
