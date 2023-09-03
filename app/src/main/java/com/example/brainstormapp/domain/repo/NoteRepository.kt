package com.example.brainstormapp.domain.repo

import com.example.brainstormapp.data.database.Note
import kotlinx.coroutines.flow.Flow

interface NoteRepository {
    suspend fun getNoteById(noteId: Int): Note

    suspend fun insertNote(note: Note): Long

    suspend fun delete(note: Note)

    fun getNotesOrderedByDate(): Flow<List<Note>>

    fun getNotesOrderedByTitle(): Flow<List<Note>>

    fun getNotesByTitle(searchQuery: String): Flow<List<Note>>
}