package com.example.sem2labandroid6

import android.net.Uri

data class ImageItem(
    val mediaId: Long,
    val uri: Uri,
    val description: String = ""
)
