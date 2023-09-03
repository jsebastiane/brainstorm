package com.example.brainstormapp.ui.notes_list

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.brainstormapp.data.model.NotesState
import com.example.brainstormapp.domain.repo.NoteRepository
import com.example.brainstormapp.util.NotesEvent
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class NotesListViewModel(private val notesRepo: NoteRepository): ViewModel() {


    var notes by mutableStateOf<NotesState>(NotesState())
        private set


    init {
        getAllNotes()
    }

    fun onEvent(event: NotesEvent){
        when(event){

            is NotesEvent.SortByDate -> {
                getAllNotes()
                Log.d("Notes", "SortByDate called")
            }

            is NotesEvent.SortByTitle -> {
                viewModelScope.launch {
                    notesRepo.getNotesOrderedByTitle().collectLatest {sortedList ->
                        notes = notes.copy(notesList = sortedList)
                    }
                }
            }

            is NotesEvent.SearchByTitle -> {
                notes = notes.copy(searchQuery = event.query)

                viewModelScope.launch {
                   notesRepo.getNotesByTitle(notes.searchQuery).collectLatest {sortedList ->
                       notes = notes.copy(notesList = sortedList)
                   }
                }
            }

            is NotesEvent.ToggleSearch -> {

                if(notes.searchMode){
                    notes = notes.copy(searchQuery = "")
                    getAllNotes()
                }
                //the 'close' button and 'search' button will never be displayed at the same time
                //so this works
                notes = notes.copy(searchMode = !notes.searchMode)



            }

            is NotesEvent.SelectNote -> {
                addNoteToSelected(event.note)
            }

            is NotesEvent.OnLongClick -> {
                Log.d("SelectionMode", "Called")
                if(notes.selectionMode){
                    addNoteToSelected(event.note)
                }else{
                    Log.d("SelectionMode", "Activated")
                    notes = notes.copy(selectionMode = true)
                    addNoteToSelected(event.note)
                }

            }

            is NotesEvent.DeleteSelected -> {
                viewModelScope.launch {
                    for (note in notes.notesList){
                        if (note.selected){
                            notesRepo.delete(note)
                        }
                    }
                    notes = notes.copy(selectionMode = false)
//                    getAllNotes()
                }
            }

            is NotesEvent.ToggleSelectionMode -> {
                notes = notes.copy(notesList = notes.notesList.map {
                    if (it.selected){
                        it.copy(selected = false)
                    }else it
                }, selectionMode = false)
            }


            //Toggle on if long press made

        }

    }

    private fun getAllNotes(){
        viewModelScope.launch {
            notesRepo.getNotesOrderedByDate().collectLatest { newList ->
                notes = notes.copy(notesList = newList)
            }
        }
    }

    private fun addNoteToSelected(note: Int){
        val currentList = notes.notesList
        notes = notes.copy(notesList = List(currentList.size) { index ->
            if(currentList[index] == currentList[note]){
                currentList[index].copy(selected = !currentList[index].selected)
            }else currentList[index]
        })


    }

}