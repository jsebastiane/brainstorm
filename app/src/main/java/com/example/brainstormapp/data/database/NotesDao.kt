package com.example.brainstormapp.data.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Query
import androidx.room.Upsert
import kotlinx.coroutines.flow.Flow

@Dao
interface NotesDao{

    //Might be a long
    @Query("SELECT * FROM note WHERE uid LIKE :noteId")
    suspend fun getNoteById(noteId: Int): Note

    //Update or Insert
    @Upsert
    suspend fun insertNote(note: Note) : Long

    @Delete
    suspend fun delete(note: Note)

    @Query("SELECT * FROM note ORDER BY dateCreated DESC")
    fun getNotesOrderedByDate(): Flow<List<Note>>

    @Query("SELECT * FROM note ORDER BY title DESC")
    fun getNotesOrderedByTitle(): Flow<List<Note>>

    @Query("SELECT * FROM note WHERE title LIKE '%' || :searchQuery || '%' ORDER BY dateCreated DESC")
    fun getNotesByTitle(searchQuery: String): Flow<List<Note>>
}
