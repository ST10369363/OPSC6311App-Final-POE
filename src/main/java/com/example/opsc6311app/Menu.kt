package com.example.opsc6311app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class Menu : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_menu)

        val btnHome = findViewById<Button>(R.id.btnHome)
        val btnExpenses = findViewById<Button>(R.id.btnExpenses)
        val btnLimits = findViewById<Button>(R.id.btnLimits)
        val btnUser = findViewById<Button>(R.id.btnUser)

        btnHome.setOnClickListener {
            startActivity(Intent(this, Home::class.java))
        }

        btnExpenses.setOnClickListener {
            startActivity(Intent(this, Expenses::class.java))
        }

        btnLimits.setOnClickListener {
            startActivity(Intent(this, Limits::class.java))
        }

        btnUser.setOnClickListener {
            startActivity(Intent(this, User::class.java))
        }
    }
}
