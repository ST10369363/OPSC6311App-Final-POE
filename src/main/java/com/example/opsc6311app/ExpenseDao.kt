package com.example.opsc6311app

import androidx.room.*
import com.example.opsc6311app.CategoryLimit
import com.example.opsc6311app.MonthlyLimit

@Dao
interface ExpenseDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(expense: Expense)

    @Query("SELECT * FROM Expenses")//if need be remove the s
    suspend fun getAll(): List<Expense>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategoryLimit(limit: CategoryLimit)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertMonthlyLimit(limit: MonthlyLimit)

    @Query("SELECT * FROM CategoryLimit WHERE category = :category LIMIT 1")
    suspend fun getCategoryLimit(category: String): CategoryLimit?

    @Query("SELECT * FROM MonthlyLimit WHERE month = :month LIMIT 1")
    suspend fun getMonthlyLimit(month: String): MonthlyLimit?

    @Query("SELECT * FROM CategoryLimit")
    suspend fun getAllCategoryLimits(): List<CategoryLimit>

    @Query("SELECT * FROM MonthlyLimit")
    suspend fun getAllMonthlyLimits(): List<MonthlyLimit>



}
