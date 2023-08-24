package com.example.brainstormapp.util

import com.example.brainstormapp.data.database.Note

sealed interface NoteDetailEvent{

    object ShowBottomSheet: NoteDetailEvent
    object UpsertNote: NoteDetailEvent
    data class SendMessage(val message: String): NoteDetailEvent
    object DeleteNote: NoteDetailEvent
    data class SetTitle(val title: String): NoteDetailEvent
    data class SetNoteContent(val content: String): NoteDetailEvent
    data class SetColour(val colour: Long): NoteDetailEvent
    data class SetBottomSheetView(val view: BottomSheetView): NoteDetailEvent
    data class AddGptResponse(val text: String, val index: Int): NoteDetailEvent
}