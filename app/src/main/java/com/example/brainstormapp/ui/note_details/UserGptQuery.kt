package com.example.brainstormapp.ui.note_details

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.brainstormapp.data.model.chat.Message
import com.example.brainstormapp.ui.theme.Typography

@Composable
fun UserGptQuery(modifier: Modifier = Modifier, message: Message){
    Column(
        modifier = modifier
    ) {
        Text(text = "You", style = Typography.labelSmall, color = Color.Gray)
        Text(text = message.content, style = Typography.bodySmall)
    }
}