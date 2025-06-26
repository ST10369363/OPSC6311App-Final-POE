package com.example.opsc6311app

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class CategoryLimit(
    @PrimaryKey val category: String,
    val limit: Double
)
