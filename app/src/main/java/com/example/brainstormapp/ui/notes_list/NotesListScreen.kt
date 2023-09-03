package com.example.brainstormapp.ui.notes_list

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.hapticfeedback.HapticFeedbackType
import androidx.compose.ui.platform.LocalHapticFeedback
import androidx.compose.ui.text.ParagraphStyle
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.brainstormapp.data.model.NotesState
import com.example.brainstormapp.ui.theme.LightPeach
import com.example.brainstormapp.ui.theme.PalePurple
import com.example.brainstormapp.ui.theme.Typography
import com.example.brainstormapp.ui.theme.archivo
import com.example.brainstormapp.util.NotesEvent
import org.koin.androidx.compose.getViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NotesListScreen(
    state: NotesState,
    onEvent: (NotesEvent) -> Unit,
    onNavigateToDetails: (String) -> Unit,
){


    val haptic = LocalHapticFeedback.current


    BackHandler(state.selectionMode || state.searchMode) {
        if (state.selectionMode){
            onEvent(NotesEvent.ToggleSelectionMode)
        }else if (state.searchMode){
            onEvent(NotesEvent.ToggleSearch)
        }
    }

    Scaffold(
        Modifier.background(PalePurple),
        containerColor = PalePurple,
        floatingActionButton = {
            FloatingActionButton(onClick = { onNavigateToDetails("-1") }) {
                Icon(Icons.Filled.Add, contentDescription = "Add note")
            }
        },
    ) { paddingValues ->

        BoxWithConstraints(modifier = Modifier
            .fillMaxSize()
            .background(Color.Transparent)
            .padding(paddingValues)
            ){

            val aspectRatio = 3.2F
            val columMarginTop = (this.maxWidth.value/aspectRatio).dp

            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 0.dp, start = 16.dp, end = 16.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                item{
                    Spacer(modifier = Modifier.height(columMarginTop + 8.dp))
                }

                itemsIndexed(state.notesList){index, item ->
                    Log.d("NOTECOLOR", "${item.tagColor}")
                    NotesItem(
                        note = item,
                        modifier = Modifier.fillMaxWidth(),
                        onClick = {
                            if(state.selectionMode){
                                onEvent(NotesEvent.SelectNote(index))
                            }else{

                                onNavigateToDetails(item.uid.toString())
                            }
                             },
                        onLongClick = {
                            onEvent(NotesEvent.OnLongClick(index))
                            haptic.performHapticFeedback(HapticFeedbackType.LongPress)
                        }
                    )
                }

                item{
                    Spacer(modifier = Modifier.height(columMarginTop))
                }
            }

            if(!state.searchMode && state.notesList.isEmpty()){
                Text(text = "Create your first note and start brainstorming", style = Typography.bodyMedium,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 32.dp)
                    .align(Alignment.Center),
                textAlign = TextAlign.Center)
            }

            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(aspectRatio)
                    .align(Alignment.TopCenter),
                shape = RoundedCornerShape(bottomEnd = 10.dp, bottomStart = 10.dp),
                elevation = CardDefaults.cardElevation(10.dp),
                colors = CardDefaults.cardColors(LightPeach),
            ) {

                AnimatedVisibility(visible = !state.selectionMode && !state.searchMode ,

                ) {
                    Row(modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(
                            buildAnnotatedString {
                                withStyle(style = ParagraphStyle(lineHeight = 25.sp)){
                                    withStyle(style = SpanStyle(
                                        fontFamily = archivo,
                                        fontWeight = FontWeight.Black,
                                        fontSize = 35.sp,
                                        color = Color.Black
                                    )){
                                        append("Sebastian,\n")
                                    }
                                    withStyle(style = SpanStyle(
                                        fontFamily = FontFamily.SansSerif,
                                        fontWeight = FontWeight.Normal,
                                        fontSize = 18.sp,
                                        color = Color.Black
                                    )
                                    ){
                                        append("Here are your notes")
                                    }
                                }
                            }
                        )

                        IconButton(onClick = {
                                             onEvent(NotesEvent.ToggleSearch)
                        },
                            modifier = Modifier.background(Color.White, CircleShape)) {
                            Icon(Icons.Default.Search, contentDescription = "search",
                            modifier = Modifier.size(30.dp))
                        }
                    }
                }

                AnimatedVisibility(visible = state.selectionMode,
                    enter = fadeIn(),
                    exit = fadeOut()
                ) {
                    Row(modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween) {

                        IconButton(onClick = {
                                             onEvent(NotesEvent.ToggleSelectionMode)
                        },
                            modifier = Modifier.background(Color.White, CircleShape)) {
                            Icon(Icons.Default.Close, contentDescription = "search",
                                modifier = Modifier.size(30.dp))
                        }

                        IconButton(onClick = {
                                             onEvent(NotesEvent.DeleteSelected)
                        },
                            modifier = Modifier.background(Color.White, CircleShape)) {
                            Icon(Icons.Default.Delete, contentDescription = "search",
                                modifier = Modifier.size(30.dp))
                        }
                    }
                }

                AnimatedVisibility(visible = state.searchMode,

                ) {

                    Row (
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween
                    ){

                        IconButton(onClick = {
                                             onEvent(NotesEvent.ToggleSearch)
                        },
                            modifier = Modifier.background(Color.White, CircleShape)) {
                            Icon(Icons.Default.Close, contentDescription = "search",
                                modifier = Modifier.size(30.dp))
                        }

                        TextField(
                            value = state.searchQuery,
                            onValueChange = {
                                onEvent(NotesEvent.SearchByTitle(it))
                            },
                            shape = RoundedCornerShape(30.dp),
                            colors = TextFieldDefaults.colors(
                                focusedIndicatorColor = Color.Transparent,
                                unfocusedIndicatorColor = Color.Transparent,
                                focusedContainerColor = Color.White,
                                unfocusedContainerColor = Color.White
                            )

                        )
                    }

                }



            }
        }



    }

}

@Preview
@Composable
fun PreviewListScreen(){
    val viewModel = getViewModel<NotesListViewModel>()
    val state = viewModel.notes
    NotesListScreen(state = state, onEvent = viewModel::onEvent, onNavigateToDetails = {})

}