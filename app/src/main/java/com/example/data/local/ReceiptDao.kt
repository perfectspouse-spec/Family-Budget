package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.example.data.model.BudgetLimit
import com.example.data.model.ExpenseReceipt
import com.example.data.model.ReceiptItem
import kotlinx.coroutines.flow.Flow

@Dao
interface ReceiptDao {
    @Query("SELECT * FROM expense_receipts ORDER BY receiptDate DESC, timestamp DESC")
    fun getAllReceipts(): Flow<List<ExpenseReceipt>>

    @Query("SELECT * FROM expense_receipts WHERE receiptDate LIKE :monthPrefix || '%' ORDER BY receiptDate DESC")
    fun getReceiptsByMonth(monthPrefix: String): Flow<List<ExpenseReceipt>>

    @Query("SELECT * FROM expense_receipts WHERE id = :id LIMIT 1")
    suspend fun getReceiptById(id: Long): ExpenseReceipt?

    @Query("SELECT * FROM receipt_items WHERE receiptId = :receiptId")
    fun getItemsForReceipt(receiptId: Long): Flow<List<ReceiptItem>>

    @Query("SELECT * FROM receipt_items WHERE receiptId = :receiptId")
    suspend fun getItemsForReceiptSync(receiptId: Long): List<ReceiptItem>

    @Query("SELECT * FROM receipt_items ORDER BY date DESC")
    fun getAllReceiptItems(): Flow<List<ReceiptItem>>

    @Query("SELECT DISTINCT productName FROM receipt_items ORDER BY productName ASC")
    fun getAllProductNames(): Flow<List<String>>

    @Query("SELECT * FROM receipt_items WHERE productName = :productName ORDER BY date ASC")
    fun getProductHistory(productName: String): Flow<List<ReceiptItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReceipt(receipt: ExpenseReceipt): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReceipts(receipts: List<ExpenseReceipt>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReceiptItems(items: List<ReceiptItem>)

    @Update
    suspend fun updateReceipt(receipt: ExpenseReceipt)

    @Delete
    suspend fun deleteReceipt(receipt: ExpenseReceipt)

    @Query("DELETE FROM receipt_items WHERE receiptId = :receiptId")
    suspend fun deleteItemsForReceipt(receiptId: Long)

    @Transaction
    suspend fun insertReceiptWithItems(receipt: ExpenseReceipt, items: List<ReceiptItem>): Long {
        val receiptId = insertReceipt(receipt)
        val itemsWithId = items.map { it.copy(receiptId = receiptId, date = receipt.receiptDate) }
        insertReceiptItems(itemsWithId)
        return receiptId
    }

    @Transaction
    suspend fun deleteReceiptWithItems(receipt: ExpenseReceipt) {
        deleteItemsForReceipt(receipt.id)
        deleteReceipt(receipt)
    }

    // Budget Limits
    @Query("SELECT * FROM budget_limits WHERE monthKey = :monthKey LIMIT 1")
    fun getBudgetLimit(monthKey: String): Flow<BudgetLimit?>

    @Query("SELECT * FROM budget_limits WHERE monthKey = :monthKey LIMIT 1")
    suspend fun getBudgetLimitSync(monthKey: String): BudgetLimit?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun setBudgetLimit(budgetLimit: BudgetLimit)

    @Query("SELECT COUNT(*) FROM expense_receipts")
    suspend fun getReceiptCount(): Int

    @Query("DELETE FROM expense_receipts")
    suspend fun deleteAllReceipts()

    @Query("DELETE FROM receipt_items")
    suspend fun deleteAllReceiptItems()

    @Query("DELETE FROM budget_limits")
    suspend fun deleteAllBudgetLimits()
}
