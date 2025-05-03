package com.example.sem2labandroid6

import android.app.Application
import android.content.Context
import androidx.room.Room
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.HiltAndroidApp
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class GalleryDatabaseModule {
    @Provides
    @Singleton
    fun provideGalleryDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, "app_database").fallbackToDestructiveMigration().build()

    }
    @Provides
    fun provideDescriptionDAO(database: AppDatabase): ImageDescriptionDao{
        return database.imageDescriptionDao()
    }
}
@HiltAndroidApp
class MyApplication : Application()