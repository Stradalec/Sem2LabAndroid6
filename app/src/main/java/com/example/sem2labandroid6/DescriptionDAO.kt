package com.example.sem2labandroid6

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert

@Dao
interface ImageDescriptionDao {
    @Upsert
    suspend fun upsert(description: ImageDescription)

    @Query("SELECT description FROM image_descriptions WHERE mediaId = :mediaId")
    suspend fun getDescription(mediaId: Long): String?

    @Query("SELECT * FROM image_descriptions")
    suspend fun getAllDescriptions(): List<ImageDescription>
}