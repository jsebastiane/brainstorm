package com.example.brainstormapp.data.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.brainstormapp.util.converters.Converters

@Database(entities = [Note::class], version = 1)
@TypeConverters(Converters::class)
abstract class NotesAppDatabase: RoomDatabase() {
    abstract fun notesDao(): NotesDao

//    companion object{
//        @Volatile
//        private var INSTANCE: NotesAppDatabase? = null
//
//        fun getDatabase(context: Context): NotesAppDatabase {
//            return INSTANCE ?: synchronized(this){
//                val instance = Room.databaseBuilder(
//                    context.applicationContext,
//                    NotesAppDatabase::class.java,
//                    "NotesDatabase"
//                ).build()
//                INSTANCE = instance
//                instance
//            }
//        }
//
//    }
}