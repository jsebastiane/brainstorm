package com.example.brainstormapp.data.model

import com.example.brainstormapp.data.database.Note
import com.example.brainstormapp.util.SortType

data class NotesState(
    val notesList: List<Note> = emptyList(),
    val isAddingNote: Boolean = false,
    val noteTitle: String = "",
    val sortType: SortType = SortType.DATE,
    val colour: Long = 0x000000
    )
