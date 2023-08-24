package com.example.brainstormapp.ui.note_details

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.brainstormapp.ui.theme.Typography

@Composable
fun NoteOptions(modifier: Modifier){
    Column(modifier = modifier) {
        Spacer(modifier = Modifier.height(20.dp))
        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Filled.Share, contentDescription = "share note")
            }
            
            Text(text = "Share", style = Typography.titleSmall)
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            IconButton(onClick = { /*TODO*/ }) {
                Icon(Icons.Filled.Delete, contentDescription = "share note")
            }

            Text(text = "Delete", style = Typography.titleSmall)
        }
    }
}

@Preview
@Composable
fun NoteOptionsPreview(){
    NoteOptions(modifier = Modifier.fillMaxWidth().padding(horizontal = 24.dp))
}