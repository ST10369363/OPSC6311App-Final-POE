package com.example.opsc6311app

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import kotlinx.coroutines.launch

class SignUp : AppCompatActivity() {

    private lateinit var db: AppDatabase

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        db = AppDatabase.getInstance(this)

        val usernameField = findViewById<EditText>(R.id.signupUsername)
        val passwordField = findViewById<EditText>(R.id.signupPassword)
        val signupButton = findViewById<Button>(R.id.signupButton)
        val loginRedirect = findViewById<Button>(R.id.loginRedirect)

        signupButton.setOnClickListener {
            val username = usernameField.text.toString().trim()
            val password = passwordField.text.toString().trim()

            if (username.isNotEmpty() && password.isNotEmpty()) {
                lifecycleScope.launch {
                    val existingUser = db.userDao().getUser(username)
                    if (existingUser == null) {
                        db.userDao().insertUser(UserEntity(username, password))
                        runOnUiThread {
                            Toast.makeText(this@SignUp, "User Registered!", Toast.LENGTH_SHORT).show()
                            val intent = Intent(this@SignUp, MainActivity::class.java)
                            startActivity(intent)
                            finish()
                        }
                    } else {
                        runOnUiThread {
                            Toast.makeText(this@SignUp, "Username already exists", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            } else {
                Toast.makeText(this, "Please enter username and password", Toast.LENGTH_SHORT).show()
            }
        }

        loginRedirect.setOnClickListener {
            startActivity(Intent(this, MainActivity::class.java))
            finish()
        }
    }
}
