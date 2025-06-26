package com.example.opsc6311app

import android.content.Intent
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class Limits : AppCompatActivity() {
    private lateinit var db: AppDatabase
    private lateinit var categorySpinner: Spinner
    private lateinit var monthSpinner: Spinner
    private lateinit var categoryBudgetInput: EditText
    private lateinit var monthlyBudgetInput: EditText
    private lateinit var btnSetCategory: Button
    private lateinit var btnSetMonthly: Button
    private lateinit var menuButton: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_limits)

        db = AppDatabase.getInstance(this)

        categorySpinner = findViewById(R.id.categorySpinner)
        monthSpinner = findViewById(R.id.monthSpinner)
        categoryBudgetInput = findViewById(R.id.budgetLimitInput)
        monthlyBudgetInput = findViewById(R.id.monthlyBudgetInput)
        btnSetCategory = findViewById(R.id.btnSetCategoryBudget)
        btnSetMonthly = findViewById(R.id.btnSetMonthlyBudget)
        menuButton = findViewById(R.id.btnMenu)

        menuButton.setOnClickListener {
            startActivity(Intent(this, Menu::class.java))
        }

        setupCategorySpinner()
        setupMonthSpinner()

        btnSetCategory.setOnClickListener {
            val cat = categorySpinner.selectedItem?.toString() ?: return@setOnClickListener
            categoryBudgetInput.text.toString().toDoubleOrNull()?.let { limit ->
                lifecycleScope.launch {
                    db.expenseDao().insertCategoryLimit(CategoryLimit(cat, limit))
                    Toast.makeText(this@Limits, "Category limit saved", Toast.LENGTH_SHORT).show()
                    categoryBudgetInput.text.clear()
                }
            } ?: Toast.makeText(this, "Enter valid limit", Toast.LENGTH_SHORT).show()
        }

        btnSetMonthly.setOnClickListener {
            val month = monthSpinner.selectedItem?.toString() ?: return@setOnClickListener
            monthlyBudgetInput.text.toString().toDoubleOrNull()?.let { limit ->
                lifecycleScope.launch {
                    db.expenseDao().insertMonthlyLimit(MonthlyLimit(month, limit))
                    Toast.makeText(this@Limits, "Monthly limit saved", Toast.LENGTH_SHORT).show()
                    monthlyBudgetInput.text.clear()
                }
            } ?: Toast.makeText(this, "Enter valid limit", Toast.LENGTH_SHORT).show()
        }
    }

    private fun setupCategorySpinner() {
        val cats = listOf("Food","Transport","Utilities","Other")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, cats)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = adapter
    }

    private fun setupMonthSpinner() {
        val months = listOf("January","February","March","April","May","June","July","August","September","October","November","December")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, months)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        monthSpinner.adapter = adapter
    }
}
