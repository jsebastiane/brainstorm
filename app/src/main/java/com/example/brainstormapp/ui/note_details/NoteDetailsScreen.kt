package com.example.brainstormapp.ui.note_details

import android.util.Log
import androidx.activity.OnBackPressedDispatcher
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.windowInsetsBottomHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Send
import androidx.compose.material.icons.filled.Share
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.example.brainstormapp.data.model.BottomSheetState
import com.example.brainstormapp.data.model.NoteDetailState
import com.example.brainstormapp.ui.theme.LightPeach
import com.example.brainstormapp.ui.theme.PalePurple
import com.example.brainstormapp.ui.theme.PeachWhite
import com.example.brainstormapp.ui.theme.Purple40
import com.example.brainstormapp.ui.theme.Typography
import com.example.brainstormapp.util.BottomSheetView
import com.example.brainstormapp.util.NoteDetailEvent
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalComposeUiApi::class,
    ExperimentalMaterialApi::class
)
@Composable
fun NoteDetailsScreen(
    state: State<NoteDetailState>,
    bottomSheetView: State<BottomSheetState>,
    onNavigateUp: () -> Unit,
    onEvent: (NoteDetailEvent) -> Unit,
){

    //Override on back press to
    var skipHalfExpanded by remember { mutableStateOf(true) }

    val someState = androidx.compose.material.rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        skipHalfExpanded = skipHalfExpanded)

    val scrollState = rememberLazyListState()

    var openBottomSheet by rememberSaveable { mutableStateOf(false) }

    val scope = rememberCoroutineScope()


    BackHandler {
        if(someState.isVisible){
            scope.launch {
                someState.hide()
            }
        }else{
            onNavigateUp()
        }
    }

    ModalBottomSheetLayout(
        sheetState = someState,
        sheetShape = RoundedCornerShape(topEnd = 16.dp, topStart = 16.dp),
        sheetContent = {

            Spacer(modifier = Modifier.height(24.dp))

            bottomSheetView.value.let { bottomSheetViewState ->
                when(bottomSheetViewState.view){

                    BottomSheetView.OPTIONS -> {
                        Column(modifier = Modifier.fillMaxWidth()) {
                            ListItem(
                                headlineContent = { Text(text = "Share") },
                                leadingContent = { Icon(Icons.Filled.Share, contentDescription = "Share")},
                                modifier = Modifier.clickable {  }
                            )
                            ListItem(
                                headlineContent = { Text(text = "Delete") },
                                leadingContent = { Icon(Icons.Filled.Delete, contentDescription = "Delete note")},
                                modifier = Modifier.clickable { onEvent(NoteDetailEvent.DeleteNote) }
                            )

                            Spacer(modifier = Modifier.height(30.dp))
                        }
                    }

                    BottomSheetView.COLOUR_TAG -> {

                        ColorSelector(modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                            color = state.value.noteColor,
                            colorEvent = {selectedColor ->
                                onEvent(NoteDetailEvent.SetColour(selectedColor))
                            })
//                    Spacer(modifier = Modifier.height(40.dp))


                    }

                    BottomSheetView.GPT_QUERY -> {
                        var gptQuery by remember { mutableStateOf("") }

                        Column(modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(0.99f)
                            ) {

                            Text(
                                modifier = Modifier.fillMaxWidth(),
                                text = "Brain",
                                style = Typography.titleMedium,
                                textAlign = TextAlign.Center
                            )

                            LazyColumn(modifier = Modifier
                                .fillMaxWidth()
                                .weight(1F),
                                state = scrollState){

                                scope.launch {
                                    scrollState.animateScrollToItem(bottomSheetViewState.chatHistory.size)
                                }

                                itemsIndexed(bottomSheetViewState.chatHistory){i, message ->
                                    Log.d("INDEX", "$i")
                                    if(message.role == "user"){
                                        UserGptQuery(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .padding(16.dp),
                                            message = message)
                                    }else {
                                        GptResponse(
                                            modifier = Modifier
                                                .fillMaxWidth()
                                                .background(LightPeach)
                                                .padding(16.dp),
                                            message = message,
                                            onAddResponse = {
                                                Log.d("GPT_RESPONSE", "ADD PRESSED")
                                                onEvent(NoteDetailEvent.AddGptResponse(message.content, i))
                                            }
                                        )


                                    }
                                }
                            }

                            Row(modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                                OutlinedTextField(
                                    modifier = Modifier.weight(1F),
                                    shape = RoundedCornerShape(30.dp),
                                    value = gptQuery,
                                    onValueChange = { gptQuery = it },
                                    maxLines = 2)
                                IconButton(onClick = { onEvent(NoteDetailEvent.SendMessage(gptQuery),)
                                    Log.d("GPT", "CLICKED GPT")},
                                    modifier = Modifier.background(PalePurple, CircleShape)) {
                                    Icon(Icons.Default.Send, contentDescription = "send message")
                                }
                            }
                            Spacer(Modifier.windowInsetsBottomHeight(WindowInsets.navigationBars))
                            Spacer(modifier = Modifier.height(30.dp))


                        }

                    }
                }
            }




        }) {

        Scaffold(
//            contentWindowInsets = WindowInsets(0,0,0,0),
            topBar = {
                CenterAlignedTopAppBar(
                    colors = TopAppBarDefaults.centerAlignedTopAppBarColors(LightPeach),
                    title = { Text(text = "Created 10-06-23 11:43", style = Typography.labelSmall,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis)},
                    navigationIcon = {
                        IconButton(onClick = {onNavigateUp()}) {
                            Icon(imageVector = Icons.Filled.ArrowBack, contentDescription = "")
                        }
                    },
                    actions = {

                        IconButton(onClick = {
                            onEvent(NoteDetailEvent.SetBottomSheetView(BottomSheetView.GPT_QUERY))
                            scope.launch { someState.show() }
                        }) {
                            Icon(Icons.Filled.Face, contentDescription = "")
                        }

                        Box(modifier = Modifier
                            .background(shape = CircleShape, color = Color(state.value.noteColor))
                            .size(20.dp)
                            .clickable {
                                onEvent(NoteDetailEvent.SetBottomSheetView(BottomSheetView.COLOUR_TAG))
                                scope.launch { someState.show() }
                            })

                        IconButton(onClick = {
                            onEvent(NoteDetailEvent.SetBottomSheetView(BottomSheetView.OPTIONS))
                            scope.launch { someState.show() }
                        }) {
                            Icon(Icons.Filled.MoreVert, contentDescription = "")
                        }





                    },

                )
            },
            content = {paddingValues ->
                Column(
                    modifier = Modifier
                        .padding(paddingValues)
                        .background(color = PeachWhite)
                        .fillMaxWidth()
                ) {

                    Spacer(modifier = Modifier.height(20.dp))

                    BasicTextField(modifier = Modifier
                        .padding(16.dp)
                        .fillMaxWidth(),
                        value = state.value.noteTitle,
                        onValueChange = {
                            onEvent(NoteDetailEvent.SetTitle(it)) },
                        textStyle = Typography.titleLarge)
                    BasicTextField(modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .fillMaxWidth()
                        .weight(1F),
                        value = state.value.noteContent,
                        onValueChange = {
                            onEvent(NoteDetailEvent.SetNoteContent(it))},
                        textStyle = Typography.bodyMedium)


                }
            })

    }

}

//@Preview
//@Composable
//fun PreviewNoteDetailsScreen(){
//    NoteDetailsScreen(onNavigateUp = {}, onEvent = {})
//}