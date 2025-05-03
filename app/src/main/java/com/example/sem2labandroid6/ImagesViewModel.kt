package com.example.sem2labandroid6

import android.app.Application
import android.content.ContentUris
import android.provider.MediaStore
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class ImagesViewModel @Inject constructor(application: Application, private val dao: ImageDescriptionDao) : AndroidViewModel(application) {
    private val _images = MutableLiveData<List<ImageItem>>()
    val images: LiveData<List<ImageItem>> = _images

    fun loadImages() {
        viewModelScope.launch(Dispatchers.IO) {
            val dbData = dao.getAllDescriptions().associateBy { it.mediaId }
            val newItems = queryImages().map { item ->
                item.copy(description = dbData[item.mediaId]?.description ?: "")
            }.toList()

            _images.postValue(newItems)
        }
    }

    private fun queryImages(): List<ImageItem> {
        val collection = MediaStore.Images.Media.EXTERNAL_CONTENT_URI
        val projection = arrayOf(
            MediaStore.Images.Media._ID,
            MediaStore.Images.Media.MIME_TYPE,
            MediaStore.Images.Media.DATA
        )

        return getApplication<Application>().contentResolver.query(
            collection,
            projection,
            null,
            null,
            "${MediaStore.Images.Media.DATE_ADDED} DESC"
        )?.use { cursor ->
            val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)

            mutableListOf<ImageItem>().apply {
                while (cursor.moveToNext()) {
                    val id = cursor.getLong(idColumn)
                    val uri = ContentUris.withAppendedId(collection, id)

                    add(
                        ImageItem(
                            mediaId = id,
                            uri = uri,
                            description = ""
                        )
                    )
                }
            }
        } ?: emptyList()
    }

    fun updateDescription(mediaId: Long, newDescription: String) {
        viewModelScope.launch(Dispatchers.IO) {
            dao.upsert(ImageDescription(mediaId, newDescription))
            val newList = _images.value?.map { item ->
                if (item.mediaId == mediaId) {
                    item.copy(description = newDescription)
                } else {
                    item
                }
            }?.toList() ?: emptyList()
            withContext(Dispatchers.Main) {
                _images.postValue(newList)
            }

        }
    }

}