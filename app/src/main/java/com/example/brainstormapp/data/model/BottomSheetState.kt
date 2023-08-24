package com.example.brainstormapp.data.model

import com.example.brainstormapp.data.model.chat.Message
import com.example.brainstormapp.util.BottomSheetView

data class BottomSheetState(
    val visible: Boolean = false,
    val view: BottomSheetView = BottomSheetView.OPTIONS,
    val chatHistory: List<Message> = emptyList()

)
