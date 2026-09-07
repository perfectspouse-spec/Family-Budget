package com.example.data.local

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.ShoppingItem
import kotlinx.coroutines.flow.Flow

@Dao
interface ShoppingDao {
    @Query("SELECT * FROM shopping_items ORDER BY isPurchased ASC, id DESC")
    fun getAllShoppingItems(): Flow<List<ShoppingItem>>

    @Query("SELECT * FROM shopping_items WHERE isPurchased = 0 ORDER BY id DESC")
    fun getPendingShoppingItems(): Flow<List<ShoppingItem>>

    @Query("SELECT * FROM shopping_items WHERE isPurchased = 1 ORDER BY purchasedAt DESC, id DESC")
    fun getPurchasedShoppingItems(): Flow<List<ShoppingItem>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShoppingItem(item: ShoppingItem): Long

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertShoppingItems(items: List<ShoppingItem>)

    @Update
    suspend fun updateShoppingItem(item: ShoppingItem)

    @Query("UPDATE shopping_items SET isPurchased = :isPurchased, purchasedAt = :purchasedAt WHERE id = :id")
    suspend fun toggleShoppingItemPurchased(id: Long, isPurchased: Boolean, purchasedAt: Long?)

    @Delete
    suspend fun deleteShoppingItem(item: ShoppingItem)

    @Query("DELETE FROM shopping_items WHERE id = :id")
    suspend fun deleteShoppingItemById(id: Long)

    @Query("DELETE FROM shopping_items WHERE isPurchased = 1")
    suspend fun clearPurchasedShoppingItems()

    @Query("DELETE FROM shopping_items")
    suspend fun clearAllShoppingItems()
}
