package com.example.opsc6311app

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class Expenses : AppCompatActivity() {

    private var selectedImageUri: Uri? = null
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expenses)

        db = AppDatabase.getInstance(this)

        val spinner = findViewById<Spinner>(R.id.spinnerOptions)
        val titleEt = findViewById<EditText>(R.id.editTextTitle)
        val amountEt = findViewById<EditText>(R.id.editTextAmount)
        val dateEt = findViewById<EditText>(R.id.editTextDate)
        val descEt = findViewById<EditText>(R.id.editTextDescription)
        val imagePreview = findViewById<ImageView>(R.id.imagePreview)
        val uploadBtn = findViewById<Button>(R.id.buttonUploadImage)
        val enterBtn = findViewById<Button>(R.id.buttonEnter)
        val clearBtn = findViewById<Button>(R.id.buttonClear)
        val menuBtn = findViewById<Button>(R.id.Emenu) // ← Menu button

        spinner.adapter = ArrayAdapter(
            this,
            android.R.layout.simple_spinner_dropdown_item,
            arrayOf("Food", "Transport", "Utilities", "Other")
        )

        val imagePicker = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            selectedImageUri = uri
            imagePreview.setImageURI(uri)
        }

        uploadBtn.setOnClickListener {
            imagePicker.launch("image/*")
        }

        enterBtn.setOnClickListener {
            val expense = Expense(
                category = spinner.selectedItem.toString(),
                title = titleEt.text.toString(),
                amount = amountEt.text.toString().toDoubleOrNull() ?: 0.0,
                date = dateEt.text.toString(),
                description = descEt.text.toString(),
                imageUri = selectedImageUri?.toString() ?: ""
            )

            lifecycleScope.launch {
                db.expenseDao().insert(expense)
                Toast.makeText(this@Expenses, "Saved!", Toast.LENGTH_SHORT).show()
            }
        }

        clearBtn.setOnClickListener {
            titleEt.text.clear()
            amountEt.text.clear()
            dateEt.text.clear()
            descEt.text.clear()
            imagePreview.setImageResource(0)
        }

        // ✅ Menu button click listener
        menuBtn.setOnClickListener {
            startActivity(Intent(this, Menu::class.java))
        }
    }
}
