package com.example.brainstormapp.ui.note_details

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.brainstormapp.data.model.chat.Message
import com.example.brainstormapp.ui.theme.PalePurple
import com.example.brainstormapp.ui.theme.PeachWhite
import com.example.brainstormapp.ui.theme.Purple40
import com.example.brainstormapp.ui.theme.Typography

@Composable
fun GptResponse(modifier: Modifier = Modifier, message: Message, onAddResponse: () -> Unit){
    Column(
        modifier = modifier
    ) {
        Row(modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically) {
            Text(text = "Brain", style = Typography.labelSmall, color = Color.Gray)

            if(message.role == "loading"){
                Box(modifier = Modifier.size(48.dp), contentAlignment = Alignment.Center){
                    CircularProgressIndicator(
                        modifier = Modifier.size(18.dp),
                        strokeWidth = 2.dp,
                        color = PalePurple,
                    )
                }

            }else{
                IconButton(onClick = { onAddResponse() }) {
                    if (message.added){
                        Icon(Icons.Default.CheckCircle, contentDescription = "add_response",
                            tint = Purple40)
                    }else{
                        Icon(Icons.Default.AddCircle, contentDescription = "add_response",
                            tint = PalePurple)
                    }

                }
            }


        }
        Text(text = message.content, style = Typography.bodySmall)
    }
}


@Preview
@Composable
fun PreviewGptResponse(){
    GptResponse(message = Message("user", "hello")) {
        
    }
}