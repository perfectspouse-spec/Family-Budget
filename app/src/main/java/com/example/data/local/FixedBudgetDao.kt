package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.FixedExpense
import com.example.data.model.IncomeSource
import kotlinx.coroutines.flow.Flow

@Dao
interface FixedBudgetDao {
    // --- Income Sources (Maaş, Kira Geliri, Ek Gelir) ---
    @Query("SELECT * FROM income_sources ORDER BY depositDay ASC, id ASC")
    fun getAllIncomeSources(): Flow<List<IncomeSource>>

    @Query("SELECT * FROM income_sources WHERE id = :id LIMIT 1")
    suspend fun getIncomeSourceById(id: Long): IncomeSource?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIncomeSource(incomeSource: IncomeSource): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertIncomeSources(incomeSources: List<IncomeSource>)

    @Update
    suspend fun updateIncomeSource(incomeSource: IncomeSource)

    @Delete
    suspend fun deleteIncomeSource(incomeSource: IncomeSource)

    @Query("UPDATE income_sources SET isReceived = :isReceived WHERE id = :id")
    suspend fun setIncomeReceived(id: Long, isReceived: Boolean)

    @Query("SELECT COUNT(*) FROM income_sources")
    suspend fun getIncomeSourceCount(): Int

    @Query("DELETE FROM income_sources")
    suspend fun deleteAllIncomeSources()

    // --- Fixed Expenses (Kira, Kredi Kartı 1-2-3, Aidat, Fatura) ---
    @Query("SELECT * FROM fixed_expenses ORDER BY dueDay ASC, id ASC")
    fun getAllFixedExpenses(): Flow<List<FixedExpense>>

    @Query("SELECT * FROM fixed_expenses WHERE id = :id LIMIT 1")
    suspend fun getFixedExpenseById(id: Long): FixedExpense?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFixedExpense(fixedExpense: FixedExpense): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFixedExpenses(fixedExpenses: List<FixedExpense>)

    @Update
    suspend fun updateFixedExpense(fixedExpense: FixedExpense)

    @Delete
    suspend fun deleteFixedExpense(fixedExpense: FixedExpense)

    @Query("UPDATE fixed_expenses SET isPaidThisMonth = :isPaid WHERE id = :id")
    suspend fun setFixedExpensePaid(id: Long, isPaid: Boolean)

    @Query("SELECT COUNT(*) FROM fixed_expenses")
    suspend fun getFixedExpenseCount(): Int

    @Query("DELETE FROM fixed_expenses")
    suspend fun deleteAllFixedExpenses()
}
