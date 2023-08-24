package com.example.brainstormapp.ui.notes_list

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brainstormapp.data.database.Note
import com.example.brainstormapp.data.model.NotesState
import com.example.brainstormapp.domain.repo.NoteRepository
import com.example.brainstormapp.util.NotesEvent
import com.example.brainstormapp.util.SortType
import com.google.firebase.functions.FirebaseFunctions
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.util.Date

class NotesListViewModel(private val notesRepo: NoteRepository): ViewModel() {


    private val _sortType = MutableStateFlow(SortType.DATE)




    // When _sortType is changed above, this reacts and retrieves data from RoomDB in
    // new order
    @OptIn(ExperimentalCoroutinesApi::class)
    private val _notes = _sortType
        .flatMapLatest { sortType ->
            when(sortType){
                SortType.DATE -> notesRepo.getNotesOrderedByDate()
                SortType.TITLE -> notesRepo.getNotesOrderedByTitle()
            }
        }



    private val _state = MutableStateFlow(NotesState())

    //Flow state of all flows represented in a single NotesState object
    val state = combine(_state, _sortType, _notes){ state, sortType, notes ->
        state.copy(
            notesList = notes,
            sortType = sortType
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), NotesState())


    fun onEvent(event: NotesEvent){
        when(event){
            is NotesEvent.DeleteNote -> {
                viewModelScope.launch {
                    notesRepo.delete(event.note)
                }
            }

            /* Change to even.colour after*/
            is NotesEvent.SetColour -> {
                _state.update {it.copy(
                    colour = event.colour.toLong()
                )}
            }

            is NotesEvent.SortNotes -> {
                _sortType.value = event.sortType
            }

        }
    }

}