package com.example.sem2labandroid6

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import androidx.appcompat.app.AppCompatActivity

class EditDescriptionActivity : AppCompatActivity() {
    companion object {
        const val EXTRA_POSITION = "position"
        const val EXTRA_DESCRIPTION = "description"
        const val RESULT_UPDATE = 100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_edit_description)

        val etDescription = findViewById<EditText>(R.id.etDescription)
        val btnSave = findViewById<Button>(R.id.btnSave)
        val btnCancel = findViewById<Button>(R.id.btnCancel)

        etDescription.setText(intent.getStringExtra(EXTRA_DESCRIPTION))

        btnSave.setOnClickListener {
            val resultIntent = Intent().apply {
                putExtra(EXTRA_POSITION, intent.getIntExtra(EXTRA_POSITION, -1))
                putExtra(EXTRA_DESCRIPTION, etDescription.text.toString())
            }
            setResult(RESULT_UPDATE, resultIntent)
            finish()
        }

        btnCancel.setOnClickListener {
            finish()
        }
    }
}