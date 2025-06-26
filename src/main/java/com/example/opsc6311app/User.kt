package com.example.opsc6311app

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.github.mikephil.charting.charts.BarChart
import com.github.mikephil.charting.data.*
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter
import kotlinx.coroutines.launch

class User : AppCompatActivity() {

    private lateinit var db: AppDatabase
    private lateinit var chartCategory: BarChart
    private lateinit var chartMonthly: BarChart
    private lateinit var chartExpenseVsLimit: BarChart
    private lateinit var buttonMenu: Button
    private lateinit var buttonLogout: Button

    private lateinit var textUsername: TextView
    private lateinit var textPassword: TextView
    private lateinit var textLoginStreak: TextView
    private lateinit var textBadge: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_user)

        db = AppDatabase.getInstance(this)

        // Bind views
        chartCategory = findViewById(R.id.chartCategoryLimits)
        chartMonthly = findViewById(R.id.chartMonthlyLimits)
        chartExpenseVsLimit = findViewById(R.id.chartExpenseVsLimit)
        buttonMenu = findViewById(R.id.buttonMenu)
        buttonLogout = findViewById(R.id.buttonLogout)
        textUsername = findViewById(R.id.textUsername)
        textPassword = findViewById(R.id.textPassword)
        textLoginStreak = findViewById(R.id.textLoginStreak)
        textBadge = findViewById(R.id.textBadge)

        // Get user info from intent
        val username = intent.getStringExtra("username")
        val password = intent.getStringExtra("password")

        // Debug log
        Log.d("UserScreen", "Received username=$username, password=$password")

        // Display user info
        textUsername.text = username ?: "Username not found"
        textPassword.text = password ?: "Password not found"

        buttonMenu.setOnClickListener {
            startActivity(Intent(this, Menu::class.java))
        }

        buttonLogout.setOnClickListener {
            val logoutIntent = Intent(this, MainActivity::class.java)
            logoutIntent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            startActivity(logoutIntent)
            finish()
        }

        loadCategoryChart()
        loadMonthlyChart()
        loadExpenseVsLimitChart()
        showLoginStreak()
        checkBudgetMasterBadge()
    }

    private fun loadCategoryChart() {
        lifecycleScope.launch {
            val limits = db.expenseDao().getAllCategoryLimits()
            val entries = limits.mapIndexed { i, it -> BarEntry(i.toFloat(), it.limit.toFloat()) }
            val labels = limits.map { it.category }
            val set = BarDataSet(entries, "Category Limits")
            val data = BarData(set).apply { barWidth = 0.9f }

            chartCategory.data = data
            chartCategory.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
            chartCategory.xAxis.granularity = 1f
            chartCategory.setFitBars(true)
            chartCategory.invalidate()
        }
    }

    private fun loadMonthlyChart() {
        lifecycleScope.launch {
            val limits = db.expenseDao().getAllMonthlyLimits()
            val entries = limits.mapIndexed { i, it -> BarEntry(i.toFloat(), it.limit.toFloat()) }
            val labels = limits.map { it.month }
            val set = BarDataSet(entries, "Monthly Limits")
            val data = BarData(set).apply { barWidth = 0.9f }

            chartMonthly.data = data
            chartMonthly.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
            chartMonthly.xAxis.granularity = 1f
            chartMonthly.setFitBars(true)
            chartMonthly.invalidate()
        }
    }

    private fun loadExpenseVsLimitChart() {
        lifecycleScope.launch {
            val expenses = db.expenseDao().getAll()
            val categoryLimits = db.expenseDao().getAllCategoryLimits()

            val expenseTotalsByCategory = expenses.groupBy { it.category }
                .mapValues { entry -> entry.value.sumOf { it.amount } }

            val labels = categoryLimits.map { it.category }
            val expenseEntries = mutableListOf<BarEntry>()
            val limitEntries = mutableListOf<BarEntry>()

            labels.forEachIndexed { index, category ->
                val totalExpense = expenseTotalsByCategory[category] ?: 0.0
                val limit = categoryLimits.find { it.category == category }?.limit ?: 0.0
                expenseEntries.add(BarEntry(index.toFloat(), totalExpense.toFloat()))
                limitEntries.add(BarEntry(index.toFloat(), limit.toFloat()))
            }

            val expenseSet = BarDataSet(expenseEntries, "Expenses").apply {
                color = getColor(R.color.purple_200)
            }
            val limitSet = BarDataSet(limitEntries, "Budget Limit").apply {
                color = getColor(R.color.teal_700)
            }

            val data = BarData(expenseSet, limitSet)
            data.barWidth = 0.3f
            chartExpenseVsLimit.data = data
            chartExpenseVsLimit.groupBars(0f, 0.2f, 0.05f)
            chartExpenseVsLimit.xAxis.valueFormatter = IndexAxisValueFormatter(labels)
            chartExpenseVsLimit.xAxis.granularity = 1f
            chartExpenseVsLimit.xAxis.isGranularityEnabled = true
            chartExpenseVsLimit.setFitBars(true)
            chartExpenseVsLimit.invalidate()
        }
    }

    private fun showLoginStreak() {
        val prefs = getSharedPreferences("user_prefs", MODE_PRIVATE)
        val lastLogin = prefs.getLong("last_login", 0)
        val streak = prefs.getInt("login_streak", 0)

        val today = System.currentTimeMillis()
        val oneDay = 24 * 60 * 60 * 1000

        val newStreak = when {
            lastLogin == 0L || today - lastLogin > 2 * oneDay -> 1
            today - lastLogin <= oneDay -> streak + 1
            else -> streak
        }

        prefs.edit()
            .putLong("last_login", today)
            .putInt("login_streak", newStreak)
            .apply()

        textLoginStreak.text = "🔥 Login Streak: $newStreak days"
    }

    private fun checkBudgetMasterBadge() {
        lifecycleScope.launch {
            val expenses = db.expenseDao().getAll()
            val limits = db.expenseDao().getAllCategoryLimits()

            val totalByCategory = expenses.groupBy { it.category }
                .mapValues { it.value.sumOf { e -> e.amount } }

            val underBudget = limits.all { limit ->
                val total = totalByCategory[limit.category] ?: 0.0
                total <= limit.limit
            }

            if (underBudget) {
                textBadge.text = "🏆 Budget Master Badge Earned!"
            } else {
                textBadge.text = "💰 Keep budgeting to earn badges!"
            }
        }
    }
}
