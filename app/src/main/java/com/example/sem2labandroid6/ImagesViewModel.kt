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
    private val _images = MutableLiveData<List<ImageItem>>()
    val images: LiveData<List<ImageItem>> = _images

    fun loadImages() {
        viewModelScope.launch(Dispatchers.IO) {
            val uris = queryImages()
            _images.postValue(uris.map { ImageItem(it) })
        }
    }

    private fun queryImages(): List<Uri> {
        val collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(MediaStore.Images.Media._ID, MediaStore.Images.Media.MIME_TYPE, MediaStore.Images.Media.DATA)

        return getApplication<Application>().contentResolver.query(
            collection,
            projection,
            null,
            null,
            "${MediaStore.Images.Media.DATE_ADDED} DESC"
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            val mimeTypeColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.MIME_TYPE)
            val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)
            mutableListOf<Uri>().apply {
                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val mimeType = cursor.getString(mimeTypeColumn)
                    val path = cursor.getString(dataColumn)
                    Log.d("IMAGE_DEBUG", "Found: $mimeType at $path")
                    add(ContentUris.withAppendedId(collection, id))
                }
                Log.d("IMAGE_DEBUG", "Found ${size} images")
            }
        } ?: emptyList()
    }
    fun updateDescription(position: Int, newDescription: String) {
        val currentList = _images.value?.toMutableList() ?: return
        currentList[position] = currentList[position].copy(description = newDescription)
        _images.postValue(currentList)
    }
}