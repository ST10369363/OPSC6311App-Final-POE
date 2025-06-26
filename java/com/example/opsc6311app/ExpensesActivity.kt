package com.example.opsc6311app

import android.net.Uri
import android.os.Bundle
import android.widget.*
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class ExpensesActivity : AppCompatActivity() {

    private var selectedImageUri: Uri? = null
    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_expenses)

        db = AppDatabase.getInstance(this)

        val spinner: Spinner = findViewById(R.id.spinnerOptions)
        val titleEt: EditText = findViewById(R.id.editTextTitle)
        val amountEt: EditText = findViewById(R.id.editTextAmount)
        val dateEt: EditText = findViewById(R.id.editTextDate)
        val descEt: EditText = findViewById(R.id.editTextDescription)
        val imagePreview: ImageView = findViewById(R.id.imagePreview)
        val uploadBtn: Button = findViewById(R.id.buttonUploadImage)
        val enterBtn: Button = findViewById(R.id.buttonEnter)
        val clearBtn: Button = findViewById(R.id.buttonClear)

        // Populate spinner with sample categories
        val categories = arrayOf("Food", "Transport", "Utilities", "Other")
        spinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, categories)

        val imagePicker = registerForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            selectedImageUri = uri
            imagePreview.setImageURI(uri)
        }

        uploadBtn.setOnClickListener {
            imagePicker.launch("image/*")
        }

        enterBtn.setOnClickListener {
            val category = spinner.selectedItem.toString()
            val title = titleEt.text.toString()
            val amount = amountEt.text.toString().toDoubleOrNull() ?: 0.0
            val date = dateEt.text.toString()
            val description = descEt.text.toString()
            val imageUri = selectedImageUri?.toString() ?: ""

            val expense = Expense(
                category = category,
                title = title,
                amount = amount,
                date = date,
                description = description,
                imageUri = imageUri
            )

            lifecycleScope.launch {
                db.expenseDao().insert(expense)
                Toast.makeText(this@ExpensesActivity, "Expense Saved", Toast.LENGTH_SHORT).show()
            }
        }

        clearBtn.setOnClickListener {
            titleEt.text.clear()
            amountEt.text.clear()
            dateEt.text.clear()
            descEt.text.clear()
            imagePreview.setImageResource(0)
            selectedImageUri = null
        }
    }
}
