package com.example.opsc6311app

import android.content.Intent
import android.os.Bundle
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import kotlinx.coroutines.launch

class Home : AppCompatActivity() {
    private lateinit var db: AppDatabase
    private lateinit var spinnerExpense: Spinner
    private lateinit var recyclerViewExpenses: androidx.recyclerview.widget.RecyclerView
    private lateinit var spinnerExpenseCB: Spinner
    private lateinit var spinnerMonth: Spinner
    private lateinit var textCategoryLimit: TextView
    private lateinit var textMonthlyLimit: TextView
    private lateinit var buttonMenu: Button
    private lateinit var expenseAdapter: ExpenseAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        db = AppDatabase.getInstance(this)

        spinnerExpense = findViewById(R.id.spinnerExpense)
        recyclerViewExpenses = findViewById(R.id.recyclerViewExpenses)
        spinnerExpenseCB = findViewById(R.id.spinnerExpenseCB)
        spinnerMonth = findViewById(R.id.spinnerMonth)
        textCategoryLimit = findViewById(R.id.textCategoryLimit)
        textMonthlyLimit = findViewById(R.id.textMonthlyLimit)
        buttonMenu = findViewById(R.id.buttonMenu)

        // RecyclerView setup
        expenseAdapter = ExpenseAdapter(emptyList())
        recyclerViewExpenses.layoutManager = LinearLayoutManager(this)
        recyclerViewExpenses.adapter = expenseAdapter

        buttonMenu.setOnClickListener {
            startActivity(Intent(this, Menu::class.java))
        }

        loadCategorySpinners()
        setupMonthSpinner()

        spinnerExpense.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View?, pos: Int, id: Long) {
                val category = parent.getItemAtPosition(pos).toString()
                lifecycleScope.launch {
                    expenseAdapter.updateData(db.expenseDao().getAll().filter { it.category == category })
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        spinnerExpenseCB.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View?, pos: Int, id: Long) {
                val cat = parent.getItemAtPosition(pos).toString()
                lifecycleScope.launch {
                    val limit = db.expenseDao().getCategoryLimit(cat)
                    textCategoryLimit.text = "Category Budget Limit: ${limit?.limit ?: "Not Set"}"
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }

        spinnerMonth.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>, view: android.view.View?, pos: Int, id: Long) {
                val month = parent.getItemAtPosition(pos).toString()
                lifecycleScope.launch {
                    val limit = db.expenseDao().getMonthlyLimit(month)
                    textMonthlyLimit.text = "Monthly Budget Limit: ${limit?.limit ?: "Not Set"}"
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>) {}
        }
    }

    private fun loadCategorySpinners() {
        lifecycleScope.launch {
            val categories = db.expenseDao().getAll().map { it.category }.distinct()
            val catAdapter = ArrayAdapter(this@Home, android.R.layout.simple_spinner_item, categories)
            catAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
            spinnerExpense.adapter = catAdapter
            spinnerExpenseCB.adapter = catAdapter
        }
    }

    private fun setupMonthSpinner() {
        val months = listOf("January","February","March","April","May","June","July","August","September","October","November","December")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_item, months)
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        spinnerMonth.adapter = adapter
    }
}
