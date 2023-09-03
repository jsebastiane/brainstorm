package com.example.brainstormapp.util

import com.example.brainstormapp.data.database.Note

sealed interface NotesEvent{

//    object SaveNote : NotesEvent
//    object Loading : NotesEvent
//    object Error : NotesEvent

    object SortByDate: NotesEvent
    object SortByTitle: NotesEvent
    object ToggleSearch: NotesEvent
    object DeleteSelected: NotesEvent

    object ToggleSelectionMode: NotesEvent

    data class SelectNote(val note: Int): NotesEvent

    data class OnLongClick(val note: Int): NotesEvent
    data class SearchByTitle(val query: String): NotesEvent

}