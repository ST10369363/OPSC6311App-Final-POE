package com.example.opsc6311app

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class MonthlyLimit(
    @PrimaryKey val month: String,
    val limit: Double
)
