package com.example.sem2labandroid6

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ImageDescription::class], version = 2, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun imageDescriptionDao(): ImageDescriptionDao
}
