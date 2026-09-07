package com.example.data.model

import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "market_product_mappings",
    indices = [
        Index("marketId"),
        Index("productName"),
        Index("productKey"),
        Index("brand"),
        Index("category"),
        Index("isAvailable")
    ]
)
data class MarketProductMapping(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val productKey: String, // Normalized lookup key (e.g. "mis laktozsuz sut", "dost sut", "domates")
    val productName: String, // Clean display name (e.g. "Mis Laktozsuz Süt (1L)")
    val brand: String, // Brand name (e.g. "Mis", "Dost", "Sütaş", "Pınar", "Birşah", "TK Koop")
    val category: String, // Category (e.g. "Süt & Kahvaltılık", "Temel Gıda", "Meyve & Sebze")
    val unit: String = "Lt", // "Lt", "Kg", "Adet", "Paket", "Gram"
    val standardQuantity: Double = 1.0,
    val marketId: String, // "bim", "a101", "sok", "tarimkredi", "migros", "carrefoursa"
    val marketName: String, // "BİM", "A101", "ŞOK", "Tarım Kredi", "Migros", "CarrefourSA"
    val marketProductName: String, // Retailer specific listing name (e.g. "Mis Laktozsuz Süt 1 Lt")
    val price: Double, // Validated current price in TRY
    val unitPriceDisplay: String = "", // e.g. "37,00 ₺ / Lt"
    val isAvailable: Boolean = true, // Whether this retailer officially stocks/sells this product
    val isPrivateLabel: Boolean = false, // True if store-exclusive brand (e.g. Mis -> ŞOK only)
    val lastVerifiedDate: String = "2026-08",
    val updatedAt: Long = System.currentTimeMillis()
)
