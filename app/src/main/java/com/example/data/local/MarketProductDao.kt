package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.data.model.MarketProductMapping
import kotlinx.coroutines.flow.Flow

@Dao
interface MarketProductDao {

    @Query("SELECT * FROM market_product_mappings WHERE isAvailable = 1 ORDER BY category ASC, productName ASC")
    fun getAllAvailableMappings(): Flow<List<MarketProductMapping>>

    @Query("SELECT * FROM market_product_mappings WHERE isAvailable = 1 ORDER BY category ASC, productName ASC")
    suspend fun getAllAvailableMappingsSync(): List<MarketProductMapping>

    @Query("SELECT * FROM market_product_mappings WHERE marketId = :marketId AND isAvailable = 1 ORDER BY category ASC, productName ASC")
    fun getProductsByMarket(marketId: String): Flow<List<MarketProductMapping>>

    @Query("SELECT * FROM market_product_mappings WHERE marketId = :marketId AND isAvailable = 1 ORDER BY category ASC, productName ASC")
    suspend fun getProductsByMarketSync(marketId: String): List<MarketProductMapping>

    @Query("SELECT * FROM market_product_mappings WHERE category = :category AND isAvailable = 1 ORDER BY price ASC")
    fun getProductsByCategory(category: String): Flow<List<MarketProductMapping>>

    @Query("SELECT * FROM market_product_mappings WHERE (LOWER(productName) LIKE '%' || LOWER(:query) || '%' OR LOWER(productKey) LIKE '%' || LOWER(:query) || '%' OR LOWER(brand) LIKE '%' || LOWER(:query) || '%') AND isAvailable = 1 ORDER BY price ASC")
    fun searchAvailableProducts(query: String): Flow<List<MarketProductMapping>>

    @Query("SELECT * FROM market_product_mappings WHERE (LOWER(productName) LIKE '%' || LOWER(:query) || '%' OR LOWER(productKey) LIKE '%' || LOWER(:query) || '%' OR LOWER(brand) LIKE '%' || LOWER(:query) || '%') AND isAvailable = 1 ORDER BY price ASC")
    suspend fun searchAvailableProductsSync(query: String): List<MarketProductMapping>

    @Query("SELECT * FROM market_product_mappings WHERE marketId = :marketId AND (productKey = :productKey OR LOWER(productName) LIKE '%' || LOWER(:productName) || '%') AND isAvailable = 1 LIMIT 1")
    suspend fun findProductPriceInMarket(marketId: String, productKey: String, productName: String): MarketProductMapping?

    @Query("SELECT * FROM market_product_mappings WHERE (productKey = :productKey OR LOWER(productName) LIKE '%' || LOWER(:productName) || '%') AND isAvailable = 1 ORDER BY price ASC")
    suspend fun findMappingsForProduct(productKey: String, productName: String): List<MarketProductMapping>

    @Query("SELECT * FROM market_product_mappings WHERE LOWER(brand) = LOWER(:brand) AND isAvailable = 1 ORDER BY price ASC")
    suspend fun findMappingsForBrand(brand: String): List<MarketProductMapping>

    @Query("SELECT DISTINCT marketId FROM market_product_mappings WHERE (productKey = :productKey OR LOWER(productName) LIKE '%' || LOWER(:productName) || '%') AND isAvailable = 1")
    suspend fun getValidMarketIdsForProduct(productKey: String, productName: String): List<String>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(mappings: List<MarketProductMapping>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(mapping: MarketProductMapping): Long

    @Update
    suspend fun update(mapping: MarketProductMapping)

    @Query("DELETE FROM market_product_mappings WHERE id = :id")
    suspend fun deleteById(id: Long)

    @Query("DELETE FROM market_product_mappings")
    suspend fun clearAll()

    @Query("SELECT COUNT(*) FROM market_product_mappings")
    suspend fun getCount(): Int
}
