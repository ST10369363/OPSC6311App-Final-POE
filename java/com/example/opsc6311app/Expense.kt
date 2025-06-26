package com.example.opsc6311app

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "expenses")
data class Expense(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val category: String,
    val title: String,
    val amount: Double,
    val date: String,
    val description: String,
    val imageUri: String // URI to selected image
)
