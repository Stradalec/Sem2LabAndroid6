package com.example.sem2labandroid6

import android.app.Application
import android.content.ContentUris
import android.content.Context
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
    private val database = AppDatabase.getInstance(application)
    private val dao = database.imageDescriptionDao()
    private val _images = MutableLiveData<List<ImageItem>>()
    val images: LiveData<List<ImageItem>> = _images

    fun loadImages() {
        viewModelScope.launch(Dispatchers.IO) {
            val items = queryImages().map { item ->
                item.copy(description = dao.getDescription(item.mediaId) ?: "")
            }
            _images.postValue(items)
        }
    }

    private fun queryImages(): List<ImageItem> {
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
            val dataColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA)

            mutableListOf<ImageItem>().apply {
                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    Log.d("MEDIA", "Найден mediaId: $id")
                    val path = cursor.getString(dataColumn)
                    val uri = ContentUris.withAppendedId(collection, id)

                    add(ImageItem(
                        mediaId = id,
                        uri = uri,
                        description = ""
                    ))
                }
            }
        } ?: emptyList()
    }
    fun updateDescription(position: Int, newDescription: String) {
        viewModelScope.launch(Dispatchers.IO) {
            val item = _images.value?.get(position) ?: return@launch
            dao.upsert(ImageDescription(item.mediaId, newDescription))
            val newList = _images.value?.toMutableList()?.apply {
                this[position] = item.copy(description = newDescription)
            }
            _images.postValue(newList!!)
        }
        loadImages()
    }

}