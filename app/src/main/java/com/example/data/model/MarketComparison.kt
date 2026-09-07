package com.example.data.model

import java.util.Locale

data class MarketInfo(
    val id: String,
    val name: String,
    val shortName: String,
    val icon: String,
    val brandColorHex: Long,
    val badgeColorHex: Long = 0xFFFFFFFF,
    val description: String,
    val tagLine: String
)

data class MarketItemPrice(
    val item: ShoppingItem,
    val market: MarketInfo,
    val unitPrice: Double, // Normalized price per unit (per 1 Kg, 1 Lt, 1 Adet, 1 Paket etc.)
    val unitPriceDisplay: String, // e.g. "₺28.00 / Kg", "₺31.50 / Lt", "₺4.50 / Adet"
    val totalPrice: Double, // Effective cost for specified quantity & unit
    val isCheapestInList: Boolean = false,
    val brandNote: String? = null
)

data class MarketBasketSummary(
    val market: MarketInfo,
    val itemPrices: List<MarketItemPrice>,
    val totalBasketPrice: Double,
    val rank: Int, // 1 = En Ucuz (🥇), 2, 3...
    val isCheapest: Boolean,
    val savingsComparedToHighest: Double,
    val savingsPercentageComparedToHighest: Double,
    val cheapestItemCount: Int
)

data class SmartSplitPick(
    val item: ShoppingItem,
    val cheapestMarket: MarketInfo,
    val unitPrice: Double,
    val unitPriceDisplay: String,
    val totalPrice: Double,
    val singleMarketWorstPrice: Double,
    val savingsForItem: Double
)

data class SmartSplitBasket(
    val picks: List<SmartSplitPick>,
    val optimalTotalPrice: Double,
    val extraSavingsComparedToBestSingleMarket: Double,
    val totalSavingsComparedToWorst: Double
)

data class MarketComparisonResult(
    val sortedMarkets: List<MarketBasketSummary>, // Ucuzdan pahalıya sıralı market listesi
    val cheapestMarket: MarketBasketSummary?,
    val mostExpensiveMarket: MarketBasketSummary?,
    val smartSplit: SmartSplitBasket,
    val itemCount: Int
)

object MarketPriceEngine {

    val MARKETS = listOf(
        MarketInfo(
            id = "bim",
            name = "BİM",
            shortName = "BİM",
            icon = "🏬",
            brandColorHex = 0xFF0072CE,
            badgeColorHex = 0xFFFFFFFF,
            description = "BİM Birleşik Mağazalar A.Ş.",
            tagLine = "Toptan Fiyatına Perakende Satış"
        ),
        MarketInfo(
            id = "a101",
            name = "A101",
            shortName = "A101",
            icon = "🛒",
            brandColorHex = 0xFF00A19C,
            badgeColorHex = 0xFFFFFFFF,
            description = "A101 Yeni Mağazacılık A.Ş.",
            tagLine = "Harca Harca Bitmez"
        ),
        MarketInfo(
            id = "sok",
            name = "ŞOK Market",
            shortName = "ŞOK",
            icon = "⚡",
            brandColorHex = 0xFFF5B300,
            badgeColorHex = 0xFF0A2342,
            description = "ŞOK Marketler Ticaret A.Ş.",
            tagLine = "Yeter de Artar"
        ),
        MarketInfo(
            id = "tarimkredi",
            name = "Tarım Kredi Koop.",
            shortName = "Tarım Kredi",
            icon = "🌾",
            brandColorHex = 0xFF1B8A44,
            badgeColorHex = 0xFFFFFFFF,
            description = "Tarım Kredi Kooperatif Market",
            tagLine = "Doğrudan Çiftçiden Sofraya"
        ),
        MarketInfo(
            id = "migros",
            name = "Migros",
            shortName = "Migros",
            icon = "🛍️",
            brandColorHex = 0xFFF26822,
            badgeColorHex = 0xFFFFFFFF,
            description = "Migros Ticaret A.Ş.",
            tagLine = "Kalite ve Çeşitlilik"
        ),
        MarketInfo(
            id = "carrefoursa",
            name = "CarrefourSA",
            shortName = "CarrefourSA",
            icon = "🏪",
            brandColorHex = 0xFF004990,
            badgeColorHex = 0xFFFFFFFF,
            description = "CarrefourSA Hipermarket",
            tagLine = "Ne Lazımsa CarrefourSA'da"
        )
    )

    // Accurate 2026 Turkish Market Price Benchmarks for Specific Packaged Products
    // Key: Normalized Product Name or Canonical Keyword
    private val SPECIFIC_PRODUCT_PRICES = mapOf(
        // Süt & Kahvaltılık
        "tam yağlı süt (1l)" to mapOf("bim" to 31.50, "a101" to 31.50, "sok" to 32.00, "tarimkredi" to 33.50, "migros" to 39.50, "carrefoursa" to 41.00),
        "yarım yağlı süt (1l)" to mapOf("bim" to 28.50, "a101" to 28.50, "sok" to 29.00, "tarimkredi" to 30.00, "migros" to 34.50, "carrefoursa" to 35.50),
        "laktozsuz süt (1l)" to mapOf("bim" to 36.50, "a101" to 36.50, "sok" to 37.00, "tarimkredi" to 38.00, "migros" to 44.50, "carrefoursa" to 45.00),
        "tam yağlı beyaz peynir" to mapOf("bim" to 89.00, "a101" to 89.00, "sok" to 92.00, "tarimkredi" to 95.00, "migros" to 125.00, "carrefoursa" to 129.00),
        "taze kaşar peyniri (400g)" to mapOf("bim" to 115.00, "a101" to 115.00, "sok" to 118.00, "tarimkredi" to 120.00, "migros" to 155.00, "carrefoursa" to 159.00),
        "süzme peynir (500g)" to mapOf("bim" to 72.50, "a101" to 72.50, "sok" to 74.00, "tarimkredi" to 76.00, "migros" to 94.00, "carrefoursa" to 96.00),
        "labne peynir (400g)" to mapOf("bim" to 49.50, "a101" to 49.50, "sok" to 52.00, "tarimkredi" to 54.00, "migros" to 68.00, "carrefoursa" to 70.00),
        "krem peynir (200g)" to mapOf("bim" to 39.00, "a101" to 39.00, "sok" to 41.00, "tarimkredi" to 42.00, "migros" to 52.00, "carrefoursa" to 54.00),
        "ezine klasik beyaz peynir" to mapOf("bim" to 139.00, "a101" to 139.00, "sok" to 145.00, "tarimkredi" to 142.00, "migros" to 179.00, "carrefoursa" to 185.00),
        "taze lor peyniri (500g)" to mapOf("bim" to 36.00, "a101" to 36.00, "sok" to 38.00, "tarimkredi" to 39.00, "migros" to 49.00, "carrefoursa" to 52.00),
        "yumurta (30'lu l boy)" to mapOf("bim" to 119.00, "a101" to 119.00, "sok" to 122.00, "tarimkredi" to 120.00, "migros" to 145.00, "carrefoursa" to 149.00),
        "yumurta (15'li m boy)" to mapOf("bim" to 62.00, "a101" to 62.00, "sok" to 64.00, "tarimkredi" to 63.00, "migros" to 76.00, "carrefoursa" to 78.00),
        "gezen tavuk yumurtası (10'lu)" to mapOf("bim" to 65.00, "a101" to 65.00, "sok" to 67.00, "tarimkredi" to 66.00, "migros" to 82.00, "carrefoursa" to 85.00),
        "geleneksel tereyağı (250g)" to mapOf("bim" to 85.00, "a101" to 85.00, "sok" to 88.00, "tarimkredi" to 89.00, "migros" to 115.00, "carrefoursa" to 118.00),
        "rulo kaymak (200g)" to mapOf("bim" to 59.00, "a101" to 59.00, "sok" to 62.00, "tarimkredi" to 64.00, "migros" to 79.00, "carrefoursa" to 82.00),
        "kaymaklı yoğurt (1.5kg)" to mapOf("bim" to 62.50, "a101" to 62.50, "sok" to 64.00, "tarimkredi" to 66.00, "migros" to 82.00, "carrefoursa" to 85.00),
        "süzme yoğurt (900g)" to mapOf("bim" to 72.00, "a101" to 72.00, "sok" to 75.00, "tarimkredi" to 76.00, "migros" to 95.00, "carrefoursa" to 98.00),
        "doğal siyah zeytin (500g)" to mapOf("bim" to 79.00, "a101" to 79.00, "sok" to 82.00, "tarimkredi" to 85.00, "migros" to 115.00, "carrefoursa" to 119.00),
        "çizik yeşil zeytin (500g)" to mapOf("bim" to 82.00, "a101" to 82.00, "sok" to 84.00, "tarimkredi" to 88.00, "migros" to 118.00, "carrefoursa" to 122.00),
        "süzme çiçek balı (850g)" to mapOf("bim" to 149.00, "a101" to 149.00, "sok" to 155.00, "tarimkredi" to 158.00, "migros" to 195.00, "carrefoursa" to 199.00),
        "süzme çam balı (850g)" to mapOf("bim" to 165.00, "a101" to 165.00, "sok" to 169.00, "tarimkredi" to 172.00, "migros" to 215.00, "carrefoursa" to 220.00),
        "reçel (çilek / vişne 380g)" to mapOf("bim" to 44.00, "a101" to 44.00, "sok" to 46.00, "tarimkredi" to 48.00, "migros" to 62.00, "carrefoursa" to 65.00),
        "kakaolu fındık kreması (400g)" to mapOf("bim" to 58.00, "a101" to 58.00, "sok" to 62.00, "tarimkredi" to 64.00, "migros" to 89.00, "carrefoursa" to 92.00),
        "tahin & pekmez ikili (700g)" to mapOf("bim" to 88.00, "a101" to 88.00, "sok" to 92.00, "tarimkredi" to 94.00, "migros" to 119.00, "carrefoursa" to 124.00),
        "dana kangal sucuk (250g)" to mapOf("bim" to 129.00, "a101" to 129.00, "sok" to 134.00, "tarimkredi" to 135.00, "migros" to 169.00, "carrefoursa" to 175.00),
        "dana macar salam (60g)" to mapOf("bim" to 29.00, "a101" to 29.00, "sok" to 31.00, "tarimkredi" to 32.00, "migros" to 42.00, "carrefoursa" to 44.00),
        "hindi füme (150g)" to mapOf("bim" to 44.00, "a101" to 44.00, "sok" to 46.00, "tarimkredi" to 47.00, "migros" to 59.00, "carrefoursa" to 62.00),

        // Meyve & Sebze (Kg bazlı veya Paket)
        "salkım domates" to mapOf("tarimkredi" to 28.00, "sok" to 28.50, "bim" to 29.00, "a101" to 29.00, "migros" to 36.90, "carrefoursa" to 38.50),
        "çeri domates (500g)" to mapOf("tarimkredi" to 22.00, "bim" to 24.00, "a101" to 24.00, "sok" to 25.00, "migros" to 32.00, "carrefoursa" to 34.00),
        "salatalık" to mapOf("tarimkredi" to 22.00, "sok" to 23.00, "bim" to 24.00, "a101" to 24.00, "migros" to 31.00, "carrefoursa" to 32.50),
        "patates (2kg)" to mapOf("tarimkredi" to 30.00, "bim" to 32.00, "a101" to 32.00, "sok" to 33.00, "migros" to 42.00, "carrefoursa" to 44.00),
        "kuru soğan (2kg)" to mapOf("tarimkredi" to 26.00, "bim" to 28.00, "a101" to 28.00, "sok" to 29.00, "migros" to 38.00, "carrefoursa" to 39.00),
        "tatlı mor soğan" to mapOf("tarimkredi" to 22.00, "bim" to 24.00, "sok" to 24.00, "a101" to 25.00, "migros" to 32.00, "carrefoursa" to 34.00),
        "taze dal soğan" to mapOf("tarimkredi" to 13.00, "sok" to 14.00, "bim" to 15.00, "a101" to 15.00, "migros" to 19.00, "carrefoursa" to 20.00),
        "sivri biber (500g)" to mapOf("sok" to 28.00, "tarimkredi" to 28.50, "bim" to 29.00, "a101" to 29.50, "migros" to 38.00, "carrefoursa" to 39.50),
        "çarliston biber (500g)" to mapOf("sok" to 25.00, "tarimkredi" to 26.00, "bim" to 27.00, "a101" to 27.00, "migros" to 34.00, "carrefoursa" to 36.00),
        "kırmızı kapya biber" to mapOf("tarimkredi" to 42.00, "sok" to 43.00, "bim" to 45.00, "a101" to 45.00, "migros" to 58.00, "carrefoursa" to 60.00),
        "dolmalık biber (500g)" to mapOf("sok" to 28.00, "tarimkredi" to 28.50, "bim" to 29.50, "a101" to 30.00, "migros" to 38.00, "carrefoursa" to 40.00),
        "limon" to mapOf("tarimkredi" to 32.00, "bim" to 34.00, "a101" to 34.00, "sok" to 35.00, "migros" to 44.00, "carrefoursa" to 46.00),
        "yerli muz" to mapOf("tarimkredi" to 65.00, "bim" to 68.00, "a101" to 68.00, "sok" to 70.00, "migros" to 84.00, "carrefoursa" to 86.00),
        "kırmızı elma" to mapOf("tarimkredi" to 28.00, "sok" to 29.00, "bim" to 30.00, "a101" to 30.00, "migros" to 39.00, "carrefoursa" to 41.00),
        "yeşil elma" to mapOf("tarimkredi" to 34.00, "sok" to 36.00, "bim" to 37.00, "a101" to 37.00, "migros" to 48.00, "carrefoursa" to 50.00),
        "sıkmalık portakal (2kg)" to mapOf("tarimkredi" to 36.00, "bim" to 38.00, "sok" to 39.00, "a101" to 40.00, "migros" to 52.00, "carrefoursa" to 55.00),
        "mandalina (1.5kg)" to mapOf("tarimkredi" to 32.00, "bim" to 34.00, "sok" to 35.00, "a101" to 35.00, "migros" to 46.00, "carrefoursa" to 48.00),
        "taze havuç" to mapOf("tarimkredi" to 21.00, "sok" to 22.00, "bim" to 23.00, "a101" to 23.00, "migros" to 29.00, "carrefoursa" to 31.00),
        "kıvırcık marul" to mapOf("tarimkredi" to 18.00, "sok" to 19.00, "bim" to 20.00, "a101" to 20.00, "migros" to 26.00, "carrefoursa" to 28.00),
        "taze yeşillik demeti" to mapOf("tarimkredi" to 12.00, "sok" to 13.00, "bim" to 14.00, "a101" to 14.00, "migros" to 18.00, "carrefoursa" to 19.00),
        "yaprak ıspanak" to mapOf("tarimkredi" to 29.00, "sok" to 30.00, "bim" to 32.00, "a101" to 32.00, "migros" to 42.00, "carrefoursa" to 44.00),
        "ince pırasa" to mapOf("tarimkredi" to 25.00, "sok" to 26.00, "bim" to 28.00, "a101" to 28.00, "migros" to 36.00, "carrefoursa" to 38.00),
        "brokoli / karnabahar" to mapOf("tarimkredi" to 38.00, "sok" to 40.00, "bim" to 42.00, "a101" to 42.00, "migros" to 54.00, "carrefoursa" to 56.00),
        "sakız kabak" to mapOf("tarimkredi" to 24.00, "sok" to 25.00, "bim" to 26.00, "a101" to 26.00, "migros" to 34.00, "carrefoursa" to 36.00),
        "kemer patlıcan" to mapOf("tarimkredi" to 29.00, "sok" to 30.00, "bim" to 32.00, "a101" to 32.00, "migros" to 42.00, "carrefoursa" to 44.00),
        "kültür mantarı (400g)" to mapOf("tarimkredi" to 34.00, "bim" to 36.00, "a101" to 36.00, "sok" to 38.00, "migros" to 48.00, "carrefoursa" to 50.00),
        "çilek (500g)" to mapOf("tarimkredi" to 40.00, "bim" to 42.00, "a101" to 42.00, "sok" to 44.00, "migros" to 56.00, "carrefoursa" to 59.00),
        "karpuz / kavun" to mapOf("tarimkredi" to 38.00, "bim" to 42.00, "a101" to 42.00, "sok" to 44.00, "migros" to 54.00, "carrefoursa" to 56.00),

        // Et, Tavuk & Balık
        "dana kıyma (%15-%20 yağlı)" to mapOf("tarimkredi" to 195.00, "bim" to 198.00, "a101" to 198.00, "sok" to 205.00, "migros" to 235.00, "carrefoursa" to 240.00),
        "dana kuşbaşı" to mapOf("tarimkredi" to 215.00, "bim" to 218.00, "a101" to 218.00, "sok" to 225.00, "migros" to 255.00, "carrefoursa" to 260.00),
        "dana antrikot / bonfile (400g)" to mapOf("tarimkredi" to 245.00, "bim" to 255.00, "a101" to 255.00, "sok" to 260.00, "migros" to 310.00, "carrefoursa" to 315.00),
        "dana biftek / kontrfile (500g)" to mapOf("tarimkredi" to 230.00, "bim" to 238.00, "a101" to 238.00, "sok" to 242.00, "migros" to 290.00, "carrefoursa" to 295.00),
        "kasap / inegöl köfte (400g)" to mapOf("tarimkredi" to 148.00, "bim" to 155.00, "a101" to 155.00, "sok" to 158.00, "migros" to 189.00, "carrefoursa" to 194.00),
        "piliç göğüs fileto" to mapOf("bim" to 155.00, "a101" to 155.00, "sok" to 158.00, "tarimkredi" to 160.00, "migros" to 185.00, "carrefoursa" to 190.00),
        "piliç but / baget" to mapOf("bim" to 99.00, "a101" to 99.00, "sok" to 102.00, "tarimkredi" to 105.00, "migros" to 125.00, "carrefoursa" to 128.00),
        "piliç ızgara kanat" to mapOf("bim" to 139.00, "a101" to 139.00, "sok" to 142.00, "tarimkredi" to 145.00, "migros" to 169.00, "carrefoursa" to 174.00),
        "taze bütün piliç" to mapOf("bim" to 129.00, "a101" to 129.00, "sok" to 134.00, "tarimkredi" to 135.00, "migros" to 159.00, "carrefoursa" to 164.00),
        "dana pastırma (120g)" to mapOf("bim" to 99.00, "a101" to 99.00, "sok" to 105.00, "tarimkredi" to 108.00, "migros" to 135.00, "carrefoursa" to 139.00),
        "taze somon fileto (400g)" to mapOf("bim" to 175.00, "a101" to 175.00, "sok" to 180.00, "tarimkredi" to 185.00, "migros" to 225.00, "carrefoursa" to 230.00),
        "taze levrek / çipura" to mapOf("tarimkredi" to 195.00, "bim" to 205.00, "a101" to 205.00, "sok" to 210.00, "migros" to 245.00, "carrefoursa" to 250.00),
        "ton balığı konservesi (3x75g)" to mapOf("bim" to 82.00, "a101" to 82.00, "sok" to 86.00, "tarimkredi" to 89.00, "migros" to 119.00, "carrefoursa" to 124.00),

        // Fırın & Unlu Mamül
        "taş fırın somun ekmek" to mapOf("bim" to 18.00, "a101" to 18.00, "sok" to 18.00, "tarimkredi" to 18.00, "migros" to 22.00, "carrefoursa" to 22.00),
        "tam buğday ekmeği (500g)" to mapOf("bim" to 29.00, "a101" to 29.00, "sok" to 32.00, "tarimkredi" to 33.00, "migros" to 42.00, "carrefoursa" to 44.00),
        "çavdar / çok tahıllı ekmek" to mapOf("bim" to 32.00, "a101" to 32.00, "sok" to 34.00, "tarimkredi" to 35.00, "migros" to 45.00, "carrefoursa" to 46.00),
        "tost ekmeği (büyük dilim)" to mapOf("bim" to 36.00, "a101" to 36.00, "sok" to 38.00, "tarimkredi" to 39.00, "migros" to 49.00, "carrefoursa" to 52.00),
        "susamlı sokak simiti" to mapOf("bim" to 24.00, "a101" to 24.00, "sok" to 25.00, "tarimkredi" to 25.00, "migros" to 32.00, "carrefoursa" to 34.00),
        "günlük taze yufka (5'li)" to mapOf("bim" to 38.00, "a101" to 38.00, "sok" to 41.00, "tarimkredi" to 42.00, "migros" to 52.00, "carrefoursa" to 55.00),
        "dürüm lavaş / tortilla (12'li)" to mapOf("bim" to 32.00, "a101" to 32.00, "sok" to 34.00, "tarimkredi" to 35.00, "migros" to 45.00, "carrefoursa" to 46.00),

        // Temel Gıda & Bakliyat
        "ayçiçek yağı (5l)" to mapOf("bim" to 249.00, "a101" to 249.00, "sok" to 254.00, "tarimkredi" to 255.00, "migros" to 310.00, "carrefoursa" to 315.00),
        "ayçiçek yağı (1l / 2l)" to mapOf("bim" to 54.00, "a101" to 54.00, "sok" to 56.00, "tarimkredi" to 56.00, "migros" to 68.00, "carrefoursa" to 70.00),
        "sızma zeytinyağı (1l)" to mapOf("tarimkredi" to 290.00, "bim" to 295.00, "a101" to 295.00, "sok" to 305.00, "migros" to 375.00, "carrefoursa" to 380.00),
        "riviera zeytinyağı (1l / 2l)" to mapOf("tarimkredi" to 235.00, "bim" to 240.00, "a101" to 240.00, "sok" to 248.00, "migros" to 295.00, "carrefoursa" to 300.00),
        "baldo pirinç (1kg / 2.5kg)" to mapOf("bim" to 68.00, "a101" to 68.00, "sok" to 70.00, "tarimkredi" to 70.00, "migros" to 88.00, "carrefoursa" to 90.00),
        "osmancık pirinç (1kg)" to mapOf("bim" to 48.00, "a101" to 48.00, "sok" to 50.00, "tarimkredi" to 50.00, "migros" to 64.00, "carrefoursa" to 66.00),
        "pilavlık bulgur (1kg)" to mapOf("bim" to 31.00, "a101" to 31.00, "sok" to 32.00, "tarimkredi" to 32.00, "migros" to 44.00, "carrefoursa" to 45.00),
        "köftelik ince bulgur (1kg)" to mapOf("bim" to 29.00, "a101" to 29.00, "sok" to 30.00, "tarimkredi" to 30.00, "migros" to 42.00, "carrefoursa" to 43.00),
        "kırmızı mercimek (1kg)" to mapOf("bim" to 44.00, "a101" to 44.00, "sok" to 46.00, "tarimkredi" to 45.00, "migros" to 59.00, "carrefoursa" to 60.00),
        "yeşil mercimek (1kg)" to mapOf("bim" to 49.00, "a101" to 49.00, "sok" to 51.00, "tarimkredi" to 50.00, "migros" to 65.00, "carrefoursa" to 68.00),
        "koçbaşı nohut (1kg)" to mapOf("bim" to 54.00, "a101" to 54.00, "sok" to 56.00, "tarimkredi" to 55.00, "migros" to 72.00, "carrefoursa" to 74.00),
        "kuru fasulye (dermason 1kg)" to mapOf("bim" to 66.00, "a101" to 66.00, "sok" to 68.00, "tarimkredi" to 68.00, "migros" to 89.00, "carrefoursa" to 92.00),
        "makarna çeşitleri (500g)" to mapOf("bim" to 26.00, "a101" to 26.00, "sok" to 27.00, "tarimkredi" to 28.00, "migros" to 38.00, "carrefoursa" to 40.00),
        "şehriye (tel & arpa 500g)" to mapOf("bim" to 14.00, "a101" to 14.00, "sok" to 14.50, "tarimkredi" to 15.00, "migros" to 21.00, "carrefoursa" to 22.00),
        "buğday unu (2kg / 5kg)" to mapOf("bim" to 41.00, "a101" to 41.00, "sok" to 43.00, "tarimkredi" to 44.00, "migros" to 55.00, "carrefoursa" to 58.00),
        "toz şeker (1kg / 5kg)" to mapOf("tarimkredi" to 34.00, "bim" to 34.50, "a101" to 34.50, "sok" to 35.00, "migros" to 42.00, "carrefoursa" to 43.00),
        "küp şeker (1kg 360'lı)" to mapOf("tarimkredi" to 37.00, "bim" to 37.50, "a101" to 37.50, "sok" to 38.00, "migros" to 46.00, "carrefoursa" to 47.00),
        "domates salçası (830g)" to mapOf("bim" to 44.00, "a101" to 44.00, "sok" to 46.00, "tarimkredi" to 46.00, "migros" to 64.00, "carrefoursa" to 66.00),
        "biber salçası (700g tatlı/acı)" to mapOf("bim" to 58.00, "a101" to 58.00, "sok" to 60.00, "tarimkredi" to 60.00, "migros" to 79.00, "carrefoursa" to 82.00),
        "iyotlu sofra tuzu (750g)" to mapOf("bim" to 12.00, "a101" to 12.00, "sok" to 13.00, "tarimkredi" to 13.00, "migros" to 18.00, "carrefoursa" to 19.00),

        // İçecekler
        "siyah dökme çay (1kg)" to mapOf("bim" to 148.00, "a101" to 148.00, "sok" to 152.00, "tarimkredi" to 150.00, "migros" to 185.00, "carrefoursa" to 189.00),
        "demlik poşet çay (100'lü)" to mapOf("bim" to 105.00, "a101" to 105.00, "sok" to 110.00, "tarimkredi" to 112.00, "migros" to 139.00, "carrefoursa" to 145.00),
        "türk kahvesi (100g)" to mapOf("bim" to 35.00, "a101" to 36.00, "sok" to 37.00, "tarimkredi" to 38.00, "migros" to 49.00, "carrefoursa" to 50.00),
        "filtre kahve (250g)" to mapOf("bim" to 115.00, "a101" to 115.00, "sok" to 120.00, "tarimkredi" to 122.00, "migros" to 155.00, "carrefoursa" to 159.00),
        "granül kahve (gold 100g)" to mapOf("bim" to 75.00, "a101" to 75.00, "sok" to 79.00, "tarimkredi" to 82.00, "migros" to 109.00, "carrefoursa" to 115.00),
        "sade maden suyu (6'lı)" to mapOf("bim" to 38.00, "a101" to 38.00, "sok" to 40.00, "tarimkredi" to 41.00, "migros" to 52.00, "carrefoursa" to 54.00),
        "doğal kaynak suyu (5l)" to mapOf("bim" to 18.00, "a101" to 18.00, "sok" to 19.00, "tarimkredi" to 19.00, "migros" to 26.00, "carrefoursa" to 27.00),
        "%100 meyve suyu (1l)" to mapOf("bim" to 32.00, "a101" to 32.00, "sok" to 34.00, "tarimkredi" to 35.00, "migros" to 45.00, "carrefoursa" to 46.00),
        "ayran (1.5l)" to mapOf("bim" to 22.50, "a101" to 22.50, "sok" to 24.00, "tarimkredi" to 24.50, "migros" to 32.00, "carrefoursa" to 34.00),

        // Temizlik & Deterjan
        "toz çamaşır deterjanı (6kg)" to mapOf("bim" to 195.00, "a101" to 195.00, "sok" to 205.00, "tarimkredi" to 215.00, "migros" to 295.00, "carrefoursa" to 305.00),
        "sıvı çamaşır deterjanı (3l)" to mapOf("bim" to 155.00, "a101" to 155.00, "sok" to 165.00, "tarimkredi" to 175.00, "migros" to 245.00, "carrefoursa" to 250.00),
        "konsantre çamaşır yumuşatıcı" to mapOf("bim" to 65.00, "a101" to 65.00, "sok" to 68.00, "tarimkredi" to 70.00, "migros" to 95.00, "carrefoursa" to 98.00),
        "bulaşık makinesi tableti (50'li)" to mapOf("bim" to 145.00, "a101" to 145.00, "sok" to 155.00, "tarimkredi" to 165.00, "migros" to 275.00, "carrefoursa" to 285.00),
        "sıvı bulaşık deterjanı (1.5l)" to mapOf("bim" to 42.00, "a101" to 42.00, "sok" to 45.00, "tarimkredi" to 46.00, "migros" to 72.00, "carrefoursa" to 75.00),
        "yağ çözücü mutfak spreyi (750ml)" to mapOf("bim" to 36.00, "a101" to 36.00, "sok" to 39.00, "tarimkredi" to 42.00, "migros" to 58.00, "carrefoursa" to 62.00),
        "yüzey temizleyici (1.5l)" to mapOf("bim" to 39.00, "a101" to 39.00, "sok" to 42.00, "tarimkredi" to 44.00, "migros" to 62.00, "carrefoursa" to 65.00),
        "ultra çamaşır suyu (810g jel)" to mapOf("bim" to 29.00, "a101" to 29.00, "sok" to 32.00, "tarimkredi" to 33.00, "migros" to 46.00, "carrefoursa" to 48.00),
        "tuvalet kağıdı (3 katlı 16'lı)" to mapOf("bim" to 129.00, "a101" to 129.00, "sok" to 135.00, "tarimkredi" to 139.00, "migros" to 189.00, "carrefoursa" to 195.00),
        "kağıt havlu (3 katlı 6'lı)" to mapOf("bim" to 69.00, "a101" to 69.00, "sok" to 72.00, "tarimkredi" to 74.00, "migros" to 98.00, "carrefoursa" to 102.00),

        // Kişisel Bakım & Bebek
        "şampuan (400ml - 500ml)" to mapOf("bim" to 79.00, "a101" to 79.00, "sok" to 82.00, "tarimkredi" to 85.00, "migros" to 125.00, "carrefoursa" to 129.00),
        "sıvı el sabunu (500ml)" to mapOf("bim" to 28.00, "a101" to 29.00, "sok" to 30.00, "tarimkredi" to 32.00, "migros" to 46.00, "carrefoursa" to 48.00),
        "diş macunu (75ml)" to mapOf("bim" to 54.00, "a101" to 54.00, "sok" to 58.00, "tarimkredi" to 60.00, "migros" to 88.00, "carrefoursa" to 92.00),
        "bebek bezi (maxi / junior)" to mapOf("bim" to 245.00, "a101" to 245.00, "sok" to 250.00, "tarimkredi" to 255.00, "migros" to 340.00, "carrefoursa" to 345.00)
    )

    // Fallback category average multipliers relative to standard benchmark
    private val MARKET_CATEGORY_FACTORS = mapOf(
        "bim" to 0.94,
        "a101" to 0.94,
        "sok" to 0.96,
        "tarimkredi" to 0.96,
        "migros" to 1.12,
        "carrefoursa" to 1.15
    )

    /**
     * Finds the closest match for a product name in specific benchmarks or template catalog.
     */
    fun findBenchmarkForProductName(name: String): Map<String, Double>? {
        val clean = name.trim().lowercase(Locale.forLanguageTag("tr"))
        // 1. Direct exact match
        SPECIFIC_PRODUCT_PRICES[clean]?.let { return it }

        // 2. Exact match on catalog template names
        for ((key, prices) in SPECIFIC_PRODUCT_PRICES) {
            if (clean.contains(key) || key.contains(clean)) {
                return prices
            }
        }

        // 3. Keyword matching (prioritizing longer key matches)
        val sortedKeys = SPECIFIC_PRODUCT_PRICES.keys.sortedByDescending { it.length }
        for (key in sortedKeys) {
            val keyWords = key.split(" ").filter { it.length > 2 }
            if (keyWords.isNotEmpty() && keyWords.all { clean.contains(it) }) {
                return SPECIFIC_PRODUCT_PRICES[key]
            }
        }

        return null
    }

    /**
     * Calculates the price for a given shopping item at a specific market.
     * Returns: (unitPricePerBaseUnit, unitDisplay, totalPriceForSpecifiedQuantity)
     */
    fun calculateItemPriceForMarket(
        item: ShoppingItem,
        market: MarketInfo,
        brandPreference: String? = null
    ): Triple<Double, String, Double> {
        val lowerName = item.name.lowercase(Locale.forLanguageTag("tr"))
        val unit = item.unit.trim()
        val quantity = if (item.quantity > 0) item.quantity else 1.0

        val benchmarkPrices = findBenchmarkForProductName(item.name)

        val isNationalBrand = !brandPreference.isNullOrBlank() &&
            !brandPreference.contains("Tümü", ignoreCase = true) &&
            !brandPreference.contains("Farketmez", ignoreCase = true)

        val factor = MARKET_CATEGORY_FACTORS[market.id] ?: 1.0

        val (calculatedUnitBasePrice, totalCalculatedPrice) = if (benchmarkPrices != null && benchmarkPrices.containsKey(market.id)) {
            val marketBench = benchmarkPrices[market.id]!!
            // If user specified brand preference that is national (e.g. Sütaş, Ariel, Yudum), adjust price
            val effectiveBench = if (isNationalBrand) {
                when (market.id) {
                    "migros", "carrefoursa" -> marketBench
                    else -> (marketBench * 1.15) // National brand sold at discounters has standard market price
                }
            } else {
                marketBench
            }

            val total = effectiveBench * quantity
            Pair(effectiveBench, total)
        } else if (item.estimatedPrice != null && item.estimatedPrice > 0) {
            val baseUnitEst = item.estimatedPrice
            val marketUnit = (baseUnitEst * factor)
            val total = marketUnit * quantity
            Pair(marketUnit, total)
        } else {
            // General fallback unit price by category
            val defaultBase = when {
                item.category.contains("Et", ignoreCase = true) -> 240.0
                item.category.contains("Süt", ignoreCase = true) -> 65.0
                item.category.contains("Meyve", ignoreCase = true) -> 35.0
                item.category.contains("Temizlik", ignoreCase = true) -> 80.0
                item.category.contains("Temel", ignoreCase = true) -> 45.0
                item.category.contains("İçecek", ignoreCase = true) -> 38.0
                else -> 40.0
            }
            val marketUnit = (defaultBase * factor)
            val total = marketUnit * quantity
            Pair(marketUnit, total)
        }

        val unitDisplay = when {
            unit.equals("Kg", ignoreCase = true) -> String.format(Locale.getDefault(), "₺%.2f / Kg", calculatedUnitBasePrice)
            unit.equals("Gram", ignoreCase = true) -> String.format(Locale.getDefault(), "₺%.2f / Pkt", calculatedUnitBasePrice)
            unit.equals("Lt", ignoreCase = true) -> String.format(Locale.getDefault(), "₺%.2f / Lt", calculatedUnitBasePrice)
            unit.equals("Paket", ignoreCase = true) -> String.format(Locale.getDefault(), "₺%.2f / Pkt", calculatedUnitBasePrice)
            unit.equals("Koli", ignoreCase = true) -> String.format(Locale.getDefault(), "₺%.2f / Koli", calculatedUnitBasePrice)
            unit.equals("Demet", ignoreCase = true) -> String.format(Locale.getDefault(), "₺%.2f / Demet", calculatedUnitBasePrice)
            unit.equals("Kutu", ignoreCase = true) -> String.format(Locale.getDefault(), "₺%.2f / Kutu", calculatedUnitBasePrice)
            unit.equals("Rulo", ignoreCase = true) -> String.format(Locale.getDefault(), "₺%.2f / Rulo", calculatedUnitBasePrice)
            else -> String.format(Locale.getDefault(), "₺%.2f / Adet", calculatedUnitBasePrice)
        }

        return Triple(calculatedUnitBasePrice, unitDisplay, totalCalculatedPrice)
    }

    /**
     * Computes the complete market basket comparison for all items in the shopping list.
     * Orders markets from cheapest to most expensive (Ucuzdan Pahalıya).
     */
    fun compareShoppingList(items: List<ShoppingItem>): MarketComparisonResult {
        if (items.isEmpty()) {
            return MarketComparisonResult(
                sortedMarkets = emptyList(),
                cheapestMarket = null,
                mostExpensiveMarket = null,
                smartSplit = SmartSplitBasket(emptyList(), 0.0, 0.0, 0.0),
                itemCount = 0
            )
        }

        val marketItemPricesMap = mutableMapOf<String, MutableList<MarketItemPrice>>()
        MARKETS.forEach { market ->
            marketItemPricesMap[market.id] = mutableListOf()
        }

        val smartSplitPicks = mutableListOf<SmartSplitPick>()

        items.forEach { item ->
            var minItemCost = Double.MAX_VALUE
            var maxItemCost = 0.0
            var cheapestMarketForThisItem = MARKETS.first()
            var cheapestUnitPrice = 0.0
            var cheapestUnitPriceDisplay = ""

            val tempPrices = mutableListOf<Pair<MarketInfo, Triple<Double, String, Double>>>()

            MARKETS.forEach { market ->
                val (unitPrice, unitDisplay, totalCost) = calculateItemPriceForMarket(item, market)
                tempPrices.add(market to Triple(unitPrice, unitDisplay, totalCost))

                if (totalCost < minItemCost) {
                    minItemCost = totalCost
                    cheapestMarketForThisItem = market
                    cheapestUnitPrice = unitPrice
                    cheapestUnitPriceDisplay = unitDisplay
                }
                if (totalCost > maxItemCost) {
                    maxItemCost = totalCost
                }
            }

            // Smart split pick
            smartSplitPicks.add(
                SmartSplitPick(
                    item = item,
                    cheapestMarket = cheapestMarketForThisItem,
                    unitPrice = cheapestUnitPrice,
                    unitPriceDisplay = cheapestUnitPriceDisplay,
                    totalPrice = minItemCost,
                    singleMarketWorstPrice = maxItemCost,
                    savingsForItem = (maxItemCost - minItemCost).coerceAtLeast(0.0)
                )
            )

            // Market specific brands
            tempPrices.forEach { (market, priceData) ->
                val (unitPrice, unitDisplay, totalCost) = priceData
                val isCheapest = (totalCost <= minItemCost + 0.01)

                val brandExample = ShoppingCatalog.getRealMarketBrandForProduct(market.id, item.name, item.category)

                marketItemPricesMap[market.id]!!.add(
                    MarketItemPrice(
                        item = item,
                        market = market,
                        unitPrice = unitPrice,
                        unitPriceDisplay = unitDisplay,
                        totalPrice = totalCost,
                        isCheapestInList = isCheapest,
                        brandNote = brandExample
                    )
                )
            }
        }

        val unsortedBaskets = MARKETS.map { market ->
            val itemPrices = marketItemPricesMap[market.id] ?: emptyList()
            val totalBasketPrice = itemPrices.sumOf { it.totalPrice }
            val cheapestCount = itemPrices.count { it.isCheapestInList }

            MarketBasketSummary(
                market = market,
                itemPrices = itemPrices,
                totalBasketPrice = totalBasketPrice,
                rank = 0,
                isCheapest = false,
                savingsComparedToHighest = 0.0,
                savingsPercentageComparedToHighest = 0.0,
                cheapestItemCount = cheapestCount
            )
        }

        val sortedBaskets = unsortedBaskets.sortedBy { it.totalBasketPrice }
        val highestPrice = sortedBaskets.lastOrNull()?.totalBasketPrice ?: 0.0
        val lowestPrice = sortedBaskets.firstOrNull()?.totalBasketPrice ?: 0.0

        val rankedBaskets = sortedBaskets.mapIndexed { index, basket ->
            val savings = (highestPrice - basket.totalBasketPrice).coerceAtLeast(0.0)
            val pct = if (highestPrice > 0) (savings / highestPrice) * 100.0 else 0.0

            basket.copy(
                rank = index + 1,
                isCheapest = (index == 0),
                savingsComparedToHighest = savings,
                savingsPercentageComparedToHighest = pct
            )
        }

        val optimalTotal = smartSplitPicks.sumOf { it.totalPrice }
        val extraSavings = (lowestPrice - optimalTotal).coerceAtLeast(0.0)
        val totalSavingsVsWorst = (highestPrice - optimalTotal).coerceAtLeast(0.0)

        val smartSplit = SmartSplitBasket(
            picks = smartSplitPicks,
            optimalTotalPrice = optimalTotal,
            extraSavingsComparedToBestSingleMarket = extraSavings,
            totalSavingsComparedToWorst = totalSavingsVsWorst
        )

        return MarketComparisonResult(
            sortedMarkets = rankedBaskets,
            cheapestMarket = rankedBaskets.firstOrNull(),
            mostExpensiveMarket = rankedBaskets.lastOrNull(),
            smartSplit = smartSplit,
            itemCount = items.size
        )
    }
}
