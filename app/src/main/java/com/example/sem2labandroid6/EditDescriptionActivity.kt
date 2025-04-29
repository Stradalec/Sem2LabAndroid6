package com.example.sem2labandroid6

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.view.inputmethod.InputMethodManager
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
        etDescription.requestFocus()
        Handler(Looper.getMainLooper()).postDelayed({
            val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            imm.showSoftInput(etDescription, InputMethodManager.SHOW_IMPLICIT)
        }, 200)

        btnSave.setOnClickListener {
            handleSaveAction(etDescription)
            finish()
        }

        btnCancel.setOnClickListener {
            handleCancelAction(etDescription)
            finish()
        }
    }
    private fun handleSaveAction(etDescription: EditText) {

        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(etDescription.windowToken, 0)

        etDescription.clearFocus()


            val resultIntent = Intent().apply {
                putExtra(EXTRA_POSITION, intent.getIntExtra(EXTRA_POSITION, -1))
                putExtra(EXTRA_DESCRIPTION, etDescription.text.toString())
            }
            setResult(RESULT_UPDATE, resultIntent)
            finish()

    }
    private fun handleCancelAction(etDescription: EditText) {
        val imm = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(etDescription.windowToken, 0)
        etDescription.clearFocus()
        finish()
    }
}