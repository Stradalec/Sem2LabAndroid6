package com.example.sem2labandroid6

import android.app.Application
import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class ImagesViewModel(application: Application) : AndroidViewModel(application) {
    private val _images = MutableLiveData<List<Uri>>()
    val images: LiveData<List<Uri>> = _images

    fun loadImages() {
        viewModelScope.launch(Dispatchers.IO) {
            _images.postValue(queryImages())
        }
    }

    private fun queryImages(): List<Uri> {
        val collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Images.Media._ID)

        return getApplication<Application>().contentResolver.query(
            collection,
            projection,
            null,
            null,
            "${MediaStore.Images.Media.DATE_ADDED} DESC"
        )?.use { cursor ->
            mutableListOf<Uri>().apply {
                val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
                while (cursor.moveToNext()) {
                    add(ContentUris.withAppendedId(collection, cursor.getLong(idColumn)))
                }
                Log.d("IMAGE_DEBUG", "Found ${size} images")
            }
        } ?: emptyList()
    }
}