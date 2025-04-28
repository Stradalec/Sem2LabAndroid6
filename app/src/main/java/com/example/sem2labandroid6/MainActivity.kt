package com.example.sem2labandroid6

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.PackageManager.PERMISSION_GRANTED
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView

class MainActivity : AppCompatActivity() {
    private val viewModel: ImagesViewModel by viewModels()
    private lateinit var recyclerView: RecyclerView
    private val permissionRequestCode = 100

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        setupRecyclerView()
        checkPermissions()
        observeViewModel()


    }

    private fun setupRecyclerView() {
        recyclerView = findViewById(R.id.rView)
        recyclerView.layoutManager = GridLayoutManager(this, 3)
        recyclerView.adapter = ImagesAdapter { position ->
            showDescriptionEditor(position)
        }
        recyclerView.setHasFixedSize(true)
    }

    private fun observeViewModel() {
        viewModel.images.observe(this) { items ->
            (recyclerView.adapter as? ImagesAdapter)?.apply {
                updateItems(items)
                recyclerView.post {
                    recyclerView.requestLayout()
                }
            }
        }
    }

    private fun checkPermissions() {
        val permission = when {
            Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU ->
                Manifest.permission.READ_MEDIA_IMAGES

            else ->
                Manifest.permission.READ_EXTERNAL_STORAGE
        }

        if (checkSelfPermission(permission) == PERMISSION_GRANTED) {
            viewModel.loadImages()
        } else {
            requestPermissions(arrayOf(permission), permissionRequestCode)
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == permissionRequestCode && grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            viewModel.loadImages()
        }
    }
    private fun showDescriptionEditor(position: Int) {
        val currentItem = viewModel.images.value?.get(position) ?: return
        val intent = Intent(this, EditDescriptionActivity::class.java).apply {
            putExtra(EditDescriptionActivity.EXTRA_POSITION, position)
            putExtra(EditDescriptionActivity.EXTRA_DESCRIPTION, currentItem.description)
        }
        startActivityForResult(intent, EditDescriptionActivity.RESULT_UPDATE)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == EditDescriptionActivity.RESULT_UPDATE
            && resultCode == Activity.RESULT_OK) {

            val position = data?.getIntExtra(EditDescriptionActivity.EXTRA_POSITION, -1) ?: -1
            val newDescription = data?.getStringExtra(EditDescriptionActivity.EXTRA_DESCRIPTION) ?: ""

            if (position != -1) {
                viewModel.updateDescription(position, newDescription)
            }
        }
    }
}

