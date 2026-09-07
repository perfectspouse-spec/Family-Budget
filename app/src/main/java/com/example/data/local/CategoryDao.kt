package com.example.data.local
 
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.ExpenseCategoryTag
import kotlinx.coroutines.flow.Flow
 
@Dao
interface CategoryDao {
    @Query("SELECT * FROM expense_categories ORDER BY isDefault DESC, name ASC")
    fun getAllCategories(): Flow<List<ExpenseCategoryTag>>
 
    @Query("SELECT * FROM expense_categories ORDER BY isDefault DESC, name ASC")
    suspend fun getAllCategoriesSync(): List<ExpenseCategoryTag>
 
    @Query("SELECT * FROM expense_categories WHERE name = :name LIMIT 1")
    suspend fun getCategoryByName(name: String): ExpenseCategoryTag?
 
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategory(category: ExpenseCategoryTag): Long
 
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(categories: List<ExpenseCategoryTag>)
 
    @Update
    suspend fun updateCategory(category: ExpenseCategoryTag)
 
    @Delete
    suspend fun deleteCategory(category: ExpenseCategoryTag)
 
    @Query("DELETE FROM expense_categories WHERE id = :id AND isDefault = 0")
    suspend fun deleteCategoryById(id: Long)
 
    @Query("SELECT COUNT(*) FROM expense_categories")
    suspend fun getCategoryCount(): Int
}
