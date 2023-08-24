package com.example.brainstormapp.ui.note_details

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.room.util.copy
import com.example.brainstormapp.data.database.Note
import com.example.brainstormapp.data.model.BottomSheetState
import com.example.brainstormapp.data.model.NoteDetailState
import com.example.brainstormapp.data.model.chat.Message
import com.example.brainstormapp.domain.repo.NoteRepository
import com.example.brainstormapp.ui.theme.DedRed
import com.example.brainstormapp.util.BottomSheetView
import com.example.brainstormapp.util.NoteDetailEvent
import com.google.android.gms.tasks.Task
import com.google.firebase.functions.FirebaseFunctions
import com.google.firebase.functions.FirebaseFunctionsException
import com.google.firebase.functions.ktx.functions
import com.google.firebase.ktx.Firebase
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date

class NoteDetailsViewModel(
    private val notesRepo: NoteRepository,
    private val savedStateHandle: SavedStateHandle
) : ViewModel() {

    private val noteContent = savedStateHandle.getStateFlow("noteContent", "")
    private val noteTitle = savedStateHandle.getStateFlow("noteTitle", "Untitled")
    private val noteColour = savedStateHandle.getStateFlow("noteColour", 0xFFF7B500)
    private val isNoteTitleFocused = savedStateHandle.getStateFlow("isNoteTitleFocused", false)
    private val isNoteContentFocused = savedStateHandle.getStateFlow("isNoteTitleFocused", false)
    private val isBottomSheetVisible = savedStateHandle.getStateFlow("isBottomSheetVisible", false)
    private val bottomSheetView = savedStateHandle.getStateFlow("bottomSheetView", BottomSheetView.OPTIONS)
    private val gptChat = savedStateHandle.getStateFlow("chatHistory", listOf<Message>())




    private var existingNote: Note? = null

    private var functions: FirebaseFunctions = Firebase.functions
    // Should I store date of creation?


    // Don't think we need this we can just use the existingNote variable above
    private var existingNoteId: Long? = null

    // Collect state from composable
    val state = combine(
        noteContent,
        noteTitle,
        noteColour,
        isNoteTitleFocused,
        isNoteContentFocused
    ) { content, title, colour, isTitleFocused, isContentFocused ->
        NoteDetailState(
            noteContent = content,
            noteTitle = title,
            isNoteTitleHintVisible = title.isEmpty() && !isTitleFocused,
            isNoteContentHintVisible = content.isEmpty() && !isContentFocused,
            noteColor = colour,
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NoteDetailState())



    val bottomSheetState = combine(
        isBottomSheetVisible,
        bottomSheetView,
        gptChat
    ) { sheetVisible, sheetView, chat ->

        BottomSheetState(
            visible = sheetVisible,
            view = sheetView,
            chatHistory = chat
        )

    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), BottomSheetState())


    init {


        savedStateHandle.get<Long>("noteId")?.let { noteId ->

            if (noteId == -1L) {
                return@let
            }

            this.existingNoteId = noteId
            viewModelScope.launch {
                notesRepo.getNoteById(noteId.toInt()).let { note ->
                    existingNote = note
                    savedStateHandle["noteTitle"] = note.title
                    savedStateHandle["noteContent"] = note.content
                    savedStateHandle["noteColour"] = note.tagColor

                }
            }

        }
    }




    fun onEvent(event: NoteDetailEvent) {
        when (event) {

            // Save on navigate back and when title changes
            is NoteDetailEvent.UpsertNote -> {
                //Will only be called after navigating back
                // If the user made edits to the note it won't be null
                existingNote?.let { currentNote ->
                    val newNote = currentNote.copy(
                        title = noteTitle.value,
                        content = noteContent.value,
                        tagColor = noteColour.value
                    )
                    viewModelScope.launch {
                        notesRepo.insertNote(newNote)
                    }
                }
            }

            is NoteDetailEvent.SendMessage ->{

                //Add message to arrayList
                sendMessage(event.message).addOnCompleteListener {task ->

                    if(!task.isSuccessful){
                        val e = task.exception
                        Log.d("GPT", e?.message.toString())
                        if(e is FirebaseFunctionsException){
                            //INTERNAL if issue was related to function call
                            val code = e.code
                            Log.d("GPT", code.toString())

                            //Error details
                            val details = e.details
                            Log.d("GPT", details.toString())
                        }else{
                            Log.d("GPT", "OTHER ISSUE")
                        }
                    } else {


                        //Checking if the state observed by ui changes

                        val messageList = arrayListOf<Message>()
                        messageList.addAll(gptChat.value)
                        messageList.add(task.result)
                        savedStateHandle["chatHistory"] = messageList
//                        Log.d("GPT", "DRIVING ME CRAZY --> ${bottomSheetState.value.chatHistory}")

                    }

//                    processJsonResponse(response.result)
                }
            }


            is NoteDetailEvent.SetBottomSheetView -> {
                savedStateHandle["bottomSheetView"] = event.view
            }

            is NoteDetailEvent.SetNoteContent -> {
                savedStateHandle["noteContent"] = event.content
                //Considering whether to add live DB updates when note content is changed
//                viewModelScope.launch {
//                    notesRepo.insertNote()
//                }
            }


            //called from delete button in dropdown
            is NoteDetailEvent.DeleteNote -> {
                existingNote?.let {noteToDelete ->
                    viewModelScope.launch {
                        notesRepo.delete(noteToDelete)
                    }
                } ?: kotlin.run {
                    Log.d("NOTE DELETE", "Not note to delete")
                }

            }

            is NoteDetailEvent.SetTitle -> {
                savedStateHandle["noteTitle"] = event.title

                //If the note did not exist make not and set current note to the ID returned by room
                if(existingNoteId == null){
                    val note = Note(
                        title = state.value.noteTitle,
                        tagColor = state.value.noteColor,
                        content = "",
                        dateCreated = Date(System.currentTimeMillis())
                    )
                    viewModelScope.launch {
                        val noteId = notesRepo.insertNote(note)
                        notesRepo.getNoteById(noteId.toInt()).let { updatedNote ->
                            existingNote = updatedNote
                            existingNoteId = noteId
                        }
                    }
                }else{
                    //Existing note won't be null
                    val newNote = existingNote!!.copy(title = noteTitle.value)
                    viewModelScope.launch {
                        notesRepo.insertNote(newNote)
                    }
                }

            }

            //UpsertNote should also be called from here
            is NoteDetailEvent.SetColour -> {
                savedStateHandle["noteColour"] = event.colour
            }


            is NoteDetailEvent.ShowBottomSheet -> {
                savedStateHandle["isBottomSheetVisible"] = !isBottomSheetVisible.value
            }

            is NoteDetailEvent.AddGptResponse -> {
                savedStateHandle["noteContent"] = "${noteContent.value}\n----Brain----\n${event.text}"
                updateChat(event.index)

            }


        }
    }

    //Messages will be list [Message, Message]
    private fun sendMessage(text: String): Task<Message> {
        //turn list of Message to Json String
        val gson = GsonBuilder().excludeFieldsWithoutExposeAnnotation().create()
        val messageList = arrayListOf<Message>()
        messageList.addAll(gptChat.value)
        messageList.add(Message("user", text))
        savedStateHandle["chatHistory"] = messageList

        val jsonString = gson.toJson(messageList)
        Log.d("GPT", jsonString)
        val data = hashMapOf(
            "text" to jsonString
        )


        return functions
            .getHttpsCallable("callChat")
            .call(data)
            .continueWith { task ->
                Log.d("GPT", "${task.result?.data}")
                val json = Gson().toJson(task.result.data)
                val result = Gson().fromJson<Message>(json, Message::class.java)
                result
            }
    }

    private fun updateChat(j: Int){
        savedStateHandle["chatHistory"] = gptChat.value.mapIndexed{i, message ->
            if(i == j){
                Log.d("GPT_RESPONSE", "FOUND ONE")
                message.copy(added = true)
            }else message
        }
    }

//    private fun processJsonResponse(response: String){
//        val json = Gson().toJson(response)
//        val something = Gson().fromJson<Message>(json, Message::class.java)
////        val processedMessage = Gson().fromJson(response, Message::class.java)
//        Log.d("GPT", "FINAL: $something")
//        //Backend will return List<Message> it should not return the entire response from API
//        //json to Message data class
//        //update savedStateHandle["gptChat"]
//        //
//
//
//    }

}