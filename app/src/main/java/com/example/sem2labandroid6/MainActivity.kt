package com.example.sem2labandroid6

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private val viewModel: ImagesViewModel by lazy {
        ViewModelProvider(this)[ImagesViewModel::class.java]
    }
    private val permissionRequestCode = 100
    private lateinit var recyclerView: RecyclerView
    private var hasPermission = false
    private val requiredPermission
        get() = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            Manifest.permission.READ_MEDIA_IMAGES
        } else {
            Manifest.permission.READ_EXTERNAL_STORAGE
        }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        recyclerView = findViewById(R.id.rView)
        recyclerView.layoutManager = GridLayoutManager(this, 3)



        viewModel.images.observe(this) { images ->
            recyclerView.adapter = ImagesAdapter(images)
        }

        checkPermissions()
        if (hasPermission) {
            viewModel.loadImages(this)
        }
    }

    private fun checkPermissions() {
        hasPermission = ContextCompat.checkSelfPermission(
                this,
                requiredPermission
            ) == PackageManager.PERMISSION_GRANTED
        if (!hasPermission) {
            ActivityCompat.requestPermissions(
                this,
                arrayOf(requiredPermission),
                permissionRequestCode
            )
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == permissionRequestCode) {
            hasPermission = grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED
            if (hasPermission) {
                viewModel.loadImages(this)
            } else {
                Toast.makeText(this, "Разрешение нужно для работы", Toast.LENGTH_SHORT).show()
                finish()
            }
        }

    }

}

