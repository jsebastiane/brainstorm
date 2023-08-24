package com.example.brainstormapp

import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.example.brainstormapp.data.database.Note
import com.example.brainstormapp.data.database.NotesAppDatabase
import com.example.brainstormapp.data.database.NotesDao
import com.example.brainstormapp.ui.theme.DedRed
import com.example.brainstormapp.ui.theme.LightBlue
import com.example.brainstormapp.util.converters.Converters
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import java.util.Date

@RunWith(AndroidJUnit4::class)
class NotesDaoTest {

    private lateinit var db: NotesAppDatabase
    private lateinit var notesDao: NotesDao

    @Before
    fun setUp(){
        db = Room.inMemoryDatabaseBuilder(
            ApplicationProvider.getApplicationContext(),
            NotesAppDatabase::class.java
        ).addTypeConverter(Converters())
            .build()

        notesDao = db.notesDao()
    }

    @After
    fun tearDown(){
        db.close()
    }

    @Test
    fun testInsertAndGetNoteById() = runBlocking{
        val note = Note(
            uid = 1,
            title = "Test Note",
            content = "This is a test note",
            tagColor = 0xffffff,
            dateCreated = Date(System.currentTimeMillis())
        )

        notesDao.insertNote(note)

        val result = notesDao.getNoteById(note.uid)


        assertEquals(note, result)
    }

    @Test
    fun testDeleteNote() = runBlocking {

        val note = Note(
            uid = 1,
            title = "Test Note",
            content = "This is a test note",
            tagColor = 0xffffff,
            dateCreated = Date(System.currentTimeMillis())
        )

        notesDao.insertNote(note)
        notesDao.delete(note)

        val result = notesDao.getNoteById(note.uid)
        assertNull(result)
    }

    @Test
    fun testGetNotesOrderedByDate() = runBlocking {
        // Create some test notes
        val note1 = Note(
            uid = 1,
            title = "Note 1",
            content = "This is note 1",
            tagColor = 0xffffff,
            dateCreated = Date(System.currentTimeMillis()))

        val note2 = Note(
            uid = 2,
            title = "Note 2",
            content = "This is note 2",
            tagColor = 0xffffff,
            dateCreated = Date(System.currentTimeMillis() + 1000L)
        )

        // Insert the notes into the database
        notesDao.insertNote(note1)
        notesDao.insertNote(note2)

        // Get the notes ordered by date
        val result = notesDao.getNotesOrderedByDate().first()

        // Verify that the result is in the correct order
        assertEquals(listOf(note2, note1), result)
    }
}