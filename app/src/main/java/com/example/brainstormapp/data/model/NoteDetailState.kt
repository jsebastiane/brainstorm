package com.example.brainstormapp.data.model

import androidx.compose.ui.graphics.Color
import com.example.brainstormapp.ui.theme.DedRed

data class NoteDetailState(
    val noteTitle: String = "",
    val isNoteTitleHintVisible: Boolean = false,
    val noteContent: String = "",
    val isNoteContentHintVisible: Boolean = false,
    val noteColor: Long = (0xFFFFFF).toLong(),
    val bottomSheetFocused: Boolean = false

)
