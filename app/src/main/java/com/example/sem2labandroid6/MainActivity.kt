package com.example.sem2labandroid6

import android.os.Bundle
import android.Manifest
import android.content.ContentUris
import android.net.Uri
import android.provider.MediaStore
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private val permissionRequestCode = 100
    private val requiredPermissions = arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val recyclerView = findViewById<RecyclerView>(R.id.rView)
        recyclerView.layoutManager = GridLayoutManager(this, 2)
        ActivityCompat.requestPermissions(this, requiredPermissions,100)
        val images = getImages()
        recyclerView.adapter = ImagesAdapter(images)

    }
    private fun getImages(): List<Uri> {
        val imageUris = mutableListOf<Uri>()
        val projection = arrayOf(MediaStore.Images.Media._ID, MediaStore.Images.Media.DATE_ADDED)
        val sortOrder = "${MediaStore.Images.Media.DATE_ADDED} DESC"
        contentResolver.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, null, null, sortOrder)?.use {
            cursor -> val idColumn = cursor.getColumnIndexOrThrow(MediaStore.Images.Media._ID)
            while (cursor.moveToNext()) {
                val id = cursor.getLong(idColumn)
                val contentUri = ContentUris.withAppendedId(MediaStore.Images.Media .EXTERNAL_CONTENT_URI, id)
                imageUris.add(contentUri)
            }

        }
        return imageUris
    }
}

