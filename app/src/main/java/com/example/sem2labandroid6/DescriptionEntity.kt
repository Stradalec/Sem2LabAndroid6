package com.example.sem2labandroid6

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "image_descriptions")
data class ImageDescription(
    @PrimaryKey
    val mediaId: Long,
    val description: String
)