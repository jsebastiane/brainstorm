package com.example.brainstormapp.util

import androidx.room.Room
import com.example.brainstormapp.data.repo.NoteRepositoryImpl
import com.example.brainstormapp.data.database.NotesAppDatabase
import com.example.brainstormapp.domain.repo.NoteRepository
import com.example.brainstormapp.ui.notes_list.NotesListViewModel
import com.example.brainstormapp.ui.note_details.NoteDetailsViewModel
import com.example.brainstormapp.util.converters.Converters
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.bind
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

//get() is used to get the Koin instance of that constructor param?

val appModule = module {
    single {
        Room.databaseBuilder(
            androidContext(),
            NotesAppDatabase::class.java,
            "notes_database"
        ).addTypeConverter(Converters())
            .build()
    }

    single { get<NotesAppDatabase>().notesDao() }
    singleOf(::NoteRepositoryImpl) {
        bind<NoteRepository>()
    }
    viewModelOf(::NotesListViewModel)
    viewModelOf(::NoteDetailsViewModel)




}
