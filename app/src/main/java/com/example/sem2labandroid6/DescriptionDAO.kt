package com.example.sem2labandroid6

import android.util.Log
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
@Dao
interface ImageDescriptionDao {
    @Upsert
    suspend fun upsert(description: ImageDescription)

    @Query("SELECT description FROM image_descriptions WHERE mediaId = :mediaId")
    suspend fun getDescription(mediaId: Long): String?

    @Query("SELECT description FROM image_descriptions WHERE mediaId = :mediaId")
    suspend fun getDescriptionInternal(mediaId: Long): String?
}