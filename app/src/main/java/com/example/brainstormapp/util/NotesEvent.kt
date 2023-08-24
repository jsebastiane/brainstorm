package com.example.brainstormapp.util

import com.example.brainstormapp.data.database.Note

sealed interface NotesEvent{

//    object SaveNote : NotesEvent
//    object Loading : NotesEvent
//    object Error : NotesEvent

    data class SortNotes(val sortType: SortType): NotesEvent
    data class DeleteNote(val note: Note): NotesEvent
    data class SetColour(val colour: Int): NotesEvent
}