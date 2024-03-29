package com.example.brainstormapp.ui.notes_list

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.brainstormapp.data.database.Note
import com.example.brainstormapp.ui.theme.DedRed
import com.example.brainstormapp.ui.theme.PeachWhite
import com.example.brainstormapp.ui.theme.Typography
import java.util.Date

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun NotesItem(
    note: Note,
    modifier: Modifier,
    onClick: () -> Unit,
    onLongClick: () -> Unit){


    var primaryColour by remember{ mutableStateOf(Color.White) }
    var secondaryColour by remember{ mutableStateOf(Color.Black) }
    val interactionSource = remember { MutableInteractionSource() }

    if(note.selected){
        primaryColour = Color.Black
        secondaryColour = Color.White
    }else{
        primaryColour = Color.White
        secondaryColour = Color.Black
    }


    BoxWithConstraints(modifier = Modifier
        .fillMaxWidth()
        .height(150.dp)
        .background(Color.Transparent, RoundedCornerShape(23.dp))
        .combinedClickable(
            onClick = onClick,
            onLongClick = {onLongClick()
            },
            interactionSource = interactionSource,
            indication = null
        )

    ){
        val width = this.maxWidth
        val height = this.maxHeight
        Canvas(modifier = Modifier.fillMaxSize()){
            drawRoundRect(color = Color.Black, size = Size(width.toPx() - 6.dp.toPx(),
                height.toPx() - 6.dp.toPx()),cornerRadius = CornerRadius(12.dp.toPx(), 12.dp.toPx()),
                topLeft = Offset(x= 0.dp.toPx(), y = 6.dp.toPx())
            )
            drawRoundRect(color = primaryColour, size = Size(width.toPx() - 6.dp.toPx(),
                height.toPx() - 6.dp.toPx()), cornerRadius = CornerRadius(12.dp.toPx(), 12.dp.toPx()),
                topLeft = Offset(x= 6.dp.toPx(), y = 0.dp.toPx())
            )
            drawRoundRect(color = Color.Black, size = Size(width.toPx() - 6.dp.toPx(),
                height.toPx() - 6.dp.toPx()), cornerRadius = CornerRadius(12.dp.toPx(), 12.dp.toPx()),
                style = Stroke(width= 2.dp.toPx()),
                topLeft = Offset(x= 6.dp.toPx(), y = 0.dp.toPx())
            )

        }
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(bottom = 20.dp, end = 20.dp, start = 18.dp, top = 12.dp),
        ) {

            Box(modifier = Modifier
                .size(12.dp)
                .clip(CircleShape)
                .border(1.dp, Color.Black, CircleShape)
                .background(Color(note.tagColor!!))
            ) {
            }
            Text(modifier = Modifier.padding(top = 6.dp, start = 22.dp, end = 18.dp),
                text = note.title!!,
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = Typography.titleMedium,
                color = secondaryColour
            )
            Text(modifier = Modifier.padding(start = 22.dp, end = 18.dp, top = 12.dp),
                text = note.content!!.ifEmpty { "This note has no content" },
                maxLines = 2,
                overflow = TextOverflow.Ellipsis,
                style = Typography.bodySmall,
                color = secondaryColour)
        }
    }
//    Card(modifier = modifier
//        .height(150.dp)
//        .shadow(4.dp, RoundedCornerShape(12.dp))
//        .background(PeachWhite, RoundedCornerShape(12.dp))
//        .border(2.dp, Color.Black, RoundedCornerShape(12.dp))
//        .combinedClickable(
//            onClick = onClick,
//            onLongClick = {onLongClick()
//                          println("LONG CLICK")
//            },
//        ),
//        colors = CardDefaults.cardColors(Color.White)
//    ) {
//        Column(modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)) {
//            Box(modifier = Modifier
//                .size(12.dp)
//                .clip(CircleShape)
//                .border(1.dp, Color.Black, CircleShape)
//                .background(Color(note.tagColor!!)))
//
//            Text(modifier = Modifier.padding(top=6.dp, start = 20.dp, end = 20.dp),
//                text = "${note.title}",
//                maxLines = 2,
//                overflow = TextOverflow.Ellipsis,
//                style = Typography.titleMedium)
//            Text(modifier = Modifier.padding(top=12.dp, start = 20.dp, end = 20.dp),
//                text = "${note.content}",
//                maxLines = 2,
//                overflow = TextOverflow.Ellipsis,
//                style = Typography.bodySmall)
//        }
//
//
//    }
}


@Preview
@Composable
fun TestNotesItem(){
    val note = Note(0, "Macroeconomics Paper", 0, "Some content goes here for demo that hopefully spans over two lines otherwise it should",
        Date(System.currentTimeMillis())
    )
    Column(modifier = Modifier
        .background(Color.Cyan)
        .height(500.dp)
        .fillMaxWidth()
        .padding(16.dp)) {
        NotesItem(note = note, Modifier.fillMaxWidth(),
            onClick = {},
            onLongClick = {})
    }

}

//@Composable
//fun NotesItem2(note: Note, modifier: Modifier){
//    Box(modifier = modifier
//        .height(150.dp)
//        .background(PeachWhite, RoundedCornerShape(12.dp))
//        .border(2.dp, Color.Black, RoundedCornerShape(12.dp))
//    ) {
//        Column(modifier = Modifier
//            .fillMaxSize()
//            .padding(16.dp)) {
//            Box(modifier = Modifier
//                .size(12.dp)
//                .clip(CircleShape)
//                .border(1.dp, Color.Black, CircleShape)
//                .background(DedRed))
//
//            Text(modifier = Modifier.padding(top=6.dp, start = 20.dp, end = 20.dp),
//                text = "${note.title}",
//                maxLines = 2,
//                overflow = TextOverflow.Ellipsis,
//                style = Typography.titleMedium)
//            Text(modifier = Modifier.padding(top=12.dp, start = 20.dp, end = 20.dp),
//                text = "${note.content}",
//                maxLines = 2,
//                overflow = TextOverflow.Ellipsis,
//                style = Typography.bodySmall)
//        }
//
//
//    }
//}