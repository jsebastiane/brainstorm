package com.example.brainstormapp.ui.note_details

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.selection.toggleable
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.brainstormapp.ui.theme.DarkBlue
import com.example.brainstormapp.ui.theme.DedOrange
import com.example.brainstormapp.ui.theme.DedRed
import com.example.brainstormapp.ui.theme.LightBlue
import com.example.brainstormapp.ui.theme.LightGreen
import com.example.brainstormapp.ui.theme.LightOrange
import com.example.brainstormapp.ui.theme.Typography

private val tagColors = listOf<Long>(
    0xFFFF6363, 0xFFFB8435, 0xFFF7B500, 0xFF9DCC6C, 0xFF70B2E4, 0xFF0B2F6B)


@Composable
fun ColorSelector(modifier: Modifier, color: Long, colorEvent: (Long) -> Unit){

    Column(modifier = modifier) {

        Spacer(modifier = Modifier.height(20.dp))
        Text(modifier = Modifier.padding(bottom = 16.dp),
            text = "Color tag", style = Typography.bodyMedium, color = Color.Black)
        LazyRow(modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(
            space = 20.dp,
            alignment = Alignment.CenterHorizontally
        )) {
            items(tagColors){tagColor ->
                Box(modifier = Modifier
                    .background(Color(tagColor), CircleShape)
                    .border(3.dp, color = if (tagColor == color) Color.Black else Color.Transparent, CircleShape)
                    .size(40.dp)
                    .clickable { colorEvent(tagColor.toLong()) }
                )
            }
        }
        Spacer(modifier = Modifier.height(60.dp))

    }

}


@Preview
@Composable
fun PreviewAddNote(){
    ColorSelector(Modifier.fillMaxWidth(), 0xFFF7B500, colorEvent = {})
}