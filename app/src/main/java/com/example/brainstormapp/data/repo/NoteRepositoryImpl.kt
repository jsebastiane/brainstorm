package com.example.brainstormapp.data.repo

import com.example.brainstormapp.data.database.Note
import com.example.brainstormapp.data.database.NotesDao
import com.example.brainstormapp.domain.repo.NoteRepository
import kotlinx.coroutines.flow.Flow
import java.util.Date

class NoteRepositoryImpl(private val dao: NotesDao): NoteRepository {

    override suspend fun getNoteById(noteId: Int): Note {
        return dao.getNoteById(noteId)
    }

    override suspend fun insertNote(note: Note): Long {
        return dao.insertNote(note)
    }

    override suspend fun delete(note: Note) {
        dao.delete(note)
    }

    override fun getNotesOrderedByDate(): Flow<List<Note>> {
        return dao.getNotesOrderedByDate()
    }

    override fun getNotesOrderedByTitle(): Flow<List<Note>> {
        return dao.getNotesOrderedByTitle()
    }
}