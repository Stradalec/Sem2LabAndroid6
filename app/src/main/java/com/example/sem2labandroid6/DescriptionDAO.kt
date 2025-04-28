package com.example.sem2labandroid6

import android.util.Log
import androidx.room.Dao
import androidx.room.Query
import androidx.room.Upsert
@Dao
interface ImageDescriptionDao {
    @Upsert
    suspend fun upsert(description: ImageDescription) {
        Log.d("ROOM", "Сохранение: mediaId=${description.mediaId}, описание='${description.description}'")
    }

    @Query("SELECT description FROM image_descriptions WHERE mediaId = :mediaId")
    suspend fun getDescription(mediaId: Long): String? {
        return getDescriptionInternal(mediaId).also {
            Log.d("ROOM", "Загрузка: mediaId=$mediaId, описание='$it'")
        }
    }

    @Query("SELECT description FROM image_descriptions WHERE mediaId = :mediaId")
     abstract suspend fun getDescriptionInternal(mediaId: Long): String?
}