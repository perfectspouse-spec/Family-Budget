package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteDatabase
import com.example.data.model.BudgetLimit
import com.example.data.model.ExpenseCategoryTag
import com.example.data.model.ExpenseReceipt
import com.example.data.model.FamilyMember
import com.example.data.model.FixedExpense
import com.example.data.model.IncomeSource
import com.example.data.model.MarketProductMapping
import com.example.data.model.ReceiptItem
import com.example.data.model.ShoppingItem
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@Database(
    entities = [
        FamilyMember::class,
        ExpenseReceipt::class,
        ReceiptItem::class,
        BudgetLimit::class,
        IncomeSource::class,
        FixedExpense::class,
        ShoppingItem::class,
        ExpenseCategoryTag::class,
        MarketProductMapping::class
    ],
    version = 7,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun familyDao(): FamilyDao
    abstract fun receiptDao(): ReceiptDao
    abstract fun fixedBudgetDao(): FixedBudgetDao
    abstract fun shoppingDao(): ShoppingDao
    abstract fun categoryDao(): CategoryDao
    abstract fun marketProductDao(): MarketProductDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "family_budget.db"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
