package com.example.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(tableName = "family_members")
data class FamilyMember(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val countryCode: String = "+90",
    val phoneNumber: String,
    val role: String = "Admin", // Admin, Spouse, Child, Member
    val avatarColorHex: Long = 0xFF00897B,
    val isApproved: Boolean = true,
    val monthlySalary: Double = 0.0,
    val additionalIncome: Double = 0.0,
    val isCurrentUser: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "expense_categories",
    indices = [Index("name", unique = true)]
)
data class ExpenseCategoryTag(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val icon: String = "🏷️",
    val colorHex: Long = 0xFF4CAF50,
    val isDefault: Boolean = false,
    val createdAt: Long = System.currentTimeMillis()
)

@Entity(
    tableName = "expense_receipts",
    indices = [Index("receiptDate"), Index("familyMemberId")]
)
data class ExpenseReceipt(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val merchantName: String,
    val receiptDate: String, // YYYY-MM-DD
    val timestamp: Long = System.currentTimeMillis(),
    val category: String, // Market, Akaryakıt, Gıda & Restoran, Giyim, Fatura, Sağlık, Ev & Yaşam, Diğer
    val vatRate: Double = 10.0, // e.g. 1%, 10%, 20%
    val vatAmount: Double = 0.0,
    val totalAmount: Double,
    val familyMemberId: Long,
    val familyMemberName: String,
    val paymentMethod: String = "Kredi Kartı", // Kredi Kartı, Nakit, Banka Kartı
    val rawOcrText: String? = null,
    val note: String? = null
)

@Entity(
    tableName = "receipt_items",
    indices = [Index("receiptId"), Index("productName"), Index("date")]
)
data class ReceiptItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val receiptId: Long,
    val productName: String,
    val category: String = "Genel",
    val quantity: Double = 1.0,
    val unitPrice: Double = 0.0,
    val totalPrice: Double = 0.0,
    val vatRate: Double = 10.0,
    val date: String, // YYYY-MM-DD
    val timestamp: Long = System.currentTimeMillis(),
    val isCancelled: Boolean = false
)

@Entity(tableName = "budget_limits")
data class BudgetLimit(
    @PrimaryKey
    val monthKey: String, // e.g. "2026-08"
    val overallLimit: Double = 50000.0,
    val warningThresholdPercent: Int = 80, // Notify when spent >= 80%
    val updatedAt: Long = System.currentTimeMillis()
)

@Entity(tableName = "income_sources")
data class IncomeSource(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String, // e.g. "Ahmet - Maaş", "Kadıköy Daire Kira Geliri", "Freelance Gelir"
    val type: String = "MAAS", // "MAAS", "KIRA_GELIRI", "EK_GELIR"
    val amount: Double = 0.0,
    val depositDay: Int = 1, // 1..31 (Hesaba geçeceği gün)
    val familyMemberId: Long = 0,
    val familyMemberName: String = "",
    val isReceived: Boolean = false,
    val note: String? = null
)

@Entity(tableName = "fixed_expenses")
data class FixedExpense(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val title: String, // e.g. "Ev Kirası", "Kredi Kartı 1 (Garanti Bonus)", "Kredi Kartı 2 (İş Maximum)", "Kredi Kartı 3 (Yapı Kredi)", "Site Aidatı", "İnternet & Faturalar"
    val type: String = "KREDI_KARTI_1", // "KIRA", "KREDI_KARTI_1", "KREDI_KARTI_2", "KREDI_KARTI_3", "AIDAT", "FATURA", "SIGORTA", "DIGER"
    val amount: Double = 0.0,
    val dueDay: Int = 15, // 1..31 (Son ödeme günü)
    val familyMemberId: Long = 0,
    val familyMemberName: String = "",
    val isPaidThisMonth: Boolean = false,
    val note: String? = null
)

@Entity(
    tableName = "shopping_items",
    indices = [Index("category"), Index("isPurchased"), Index("familyMemberId")]
)
data class ShoppingItem(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val name: String,
    val category: String = "Market & Gıda", // e.g. "Süt & Kahvaltılık", "Meyve & Sebze", etc.
    val quantity: Double = 1.0,
    val unit: String = "Adet", // "Adet", "Kg", "Lt", "Paket", "Gram", "Koli", "Demet"
    val estimatedPrice: Double? = null,
    val note: String? = null,
    val isPurchased: Boolean = false,
    val familyMemberId: Long = 0,
    val familyMemberName: String = "",
    val addedByAvatarColor: Long = 0xFF00897B,
    val createdAt: Long = System.currentTimeMillis(),
    val purchasedAt: Long? = null
)

data class ParsedReceiptItem(
    val productName: String,
    val quantity: Double = 1.0,
    val unitPrice: Double = 0.0,
    val totalPrice: Double = 0.0,
    val vatRate: Double = 1.0,
    val category: String = "Market & Gıda",
    val isCancelled: Boolean = false
)

data class ParsedReceipt(
    val merchantName: String,
    val receiptDate: String,
    val category: String,
    val vatRate: Double,
    val vatAmount: Double,
    val totalAmount: Double,
    val items: List<ParsedReceiptItem>,
    val rawText: String? = null
)

enum class AppLanguage(val code: String, val displayName: String, val flag: String) {
    TR("tr", "Türkçe", "🇹🇷"),
    EN("en", "English", "🇬🇧"),
    DE("de", "Deutsch", "🇩🇪"),
    ES("es", "Español", "🇪🇸"),
    FR("fr", "Français", "🇫🇷"),
    AR("ar", "العربية", "🇸🇦"),
    RU("ru", "Русский", "🇷🇺"),
    IT("it", "Italiano", "🇮🇹")
}

enum class ThemeMode(val titleTr: String, val titleEn: String) {
    SYSTEM("Sistem Varsayılanı", "System Default"),
    LIGHT("Açık Tema", "Light Theme"),
    DARK("Karanlık Tema", "Dark Theme")
}

data class ProductPricePoint(
    val monthLabel: String,
    val averageUnitPrice: Double,
    val minPrice: Double,
    val maxPrice: Double,
    val sampleCount: Int
)

data class ProductTrendSummary(
    val productName: String,
    val category: String,
    val currentPrice: Double,
    val previousPrice: Double,
    val changePercent: Double,
    val points: List<ProductPricePoint>
)

data class CategoryMonthlyStat(
    val category: String,
    val totalSpent: Double,
    val percentage: Float,
    val transactionCount: Int
)

data class CategoryWeeklyStat(
    val weekNumber: Int,
    val weekLabel: String,
    val category: String,
    val totalSpent: Double
)

data class UpcomingScheduleItem(
    val id: Long,
    val title: String,
    val amount: Double,
    val dayOfMonth: Int,
    val isIncome: Boolean,
    val type: String,
    val isCompleted: Boolean,
    val memberName: String,
    val daysRemaining: Int,
    val note: String? = null,
    val icon: String = when {
        isIncome -> when (type) {
            "MAAS" -> "💼"
            "KIRA_GELIRI" -> "🏠"
            else -> "📈"
        }
        type == "KIRA" -> "🏡"
        type.startsWith("KREDI_KARTI") -> "💳"
        type == "AIDAT" -> "🏢"
        type == "FATURA" -> "⚡"
        type == "SIGORTA" -> "🛡️"
        else -> "📌"
    },
    val statusDescription: String = if (isIncome) {
        if (isCompleted) "Hesaba Geçti" else (if (daysRemaining == 0) "Bugün hesaba geçiyor" else if (daysRemaining > 0) "$daysRemaining gün kaldı" else "${-daysRemaining} gün önce")
    } else {
        if (isCompleted) "Ödendi ✓" else (if (daysRemaining == 0) "Bugün son gün!" else if (daysRemaining > 0) "$daysRemaining gün kaldı" else "${-daysRemaining} gün gecikti")
    }
)

data class DailySpendingPoint(
    val dayLabel: String,
    val dateStr: String,
    val amount: Double
)

data class WeeklyComparisonStat(
    val currentWeekSpent: Double = 0.0,
    val previousWeekSpent: Double = 0.0,
    val differenceAmount: Double = 0.0,
    val changePercentage: Double = 0.0,
    val isIncrease: Boolean = false,
    val currentWeekReceiptCount: Int = 0,
    val previousWeekReceiptCount: Int = 0,
    val dailyBreakdown: List<DailySpendingPoint> = emptyList(),
    val currentWeekAverageDaily: Double = 0.0,
    val topCategoryThisWeek: String = "",
    val topCategorySpentThisWeek: Double = 0.0
)



