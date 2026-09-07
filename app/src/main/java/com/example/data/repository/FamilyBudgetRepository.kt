package com.example.data.repository

import com.example.data.local.CategoryDao
import com.example.data.local.FamilyDao
import com.example.data.local.FixedBudgetDao
import com.example.data.local.MarketCatalogSeeder
import com.example.data.local.MarketProductDao
import com.example.data.local.ReceiptDao
import com.example.data.local.ShoppingDao
import com.example.data.model.BudgetLimit
import com.example.data.model.CategoryMonthlyStat
import com.example.data.model.CategoryWeeklyStat
import com.example.data.model.ExpenseCategoryTag
import com.example.data.model.ExpenseReceipt
import com.example.data.model.FamilyMember
import com.example.data.model.FixedExpense
import com.example.data.model.IncomeSource
import com.example.data.model.MarketProductMapping
import com.example.data.model.ProductPricePoint
import com.example.data.model.ProductTrendSummary
import com.example.data.model.ReceiptItem
import com.example.data.model.ShoppingItem
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class FamilyBudgetRepository(
    private val familyDao: FamilyDao,
    private val receiptDao: ReceiptDao,
    private val fixedBudgetDao: FixedBudgetDao,
    private val shoppingDao: ShoppingDao,
    private val categoryDao: CategoryDao,
    private val marketProductDao: MarketProductDao
) {
    val allMembers: Flow<List<FamilyMember>> = familyDao.getAllMembers()
    val allReceipts: Flow<List<ExpenseReceipt>> = receiptDao.getAllReceipts()
    val allReceiptItems: Flow<List<ReceiptItem>> = receiptDao.getAllReceiptItems()
    val allProductNames: Flow<List<String>> = receiptDao.getAllProductNames()
    val allIncomeSources: Flow<List<IncomeSource>> = fixedBudgetDao.getAllIncomeSources()
    val allFixedExpenses: Flow<List<FixedExpense>> = fixedBudgetDao.getAllFixedExpenses()
    val allShoppingItems: Flow<List<ShoppingItem>> = shoppingDao.getAllShoppingItems()
    val pendingShoppingItems: Flow<List<ShoppingItem>> = shoppingDao.getPendingShoppingItems()
    val purchasedShoppingItems: Flow<List<ShoppingItem>> = shoppingDao.getPurchasedShoppingItems()
    val allCategories: Flow<List<ExpenseCategoryTag>> = categoryDao.getAllCategories()
    val allMarketMappings: Flow<List<MarketProductMapping>> = marketProductDao.getAllAvailableMappings()

    // Category / Tag actions
    suspend fun insertCategory(category: ExpenseCategoryTag): Long =
        categoryDao.insertCategory(category)

    suspend fun updateCategory(category: ExpenseCategoryTag) =
        categoryDao.updateCategory(category)

    suspend fun deleteCategory(category: ExpenseCategoryTag) =
        categoryDao.deleteCategory(category)

    suspend fun deleteCategoryById(id: Long) =
        categoryDao.deleteCategoryById(id)

    suspend fun ensureDefaultCategories() {
        if (categoryDao.getCategoryCount() == 0) {
            val defaults = listOf(
                ExpenseCategoryTag(name = "Gıda", icon = "🍎", colorHex = 0xFF4CAF50, isDefault = true),
                ExpenseCategoryTag(name = "Kira", icon = "🏠", colorHex = 0xFF3F51B5, isDefault = true),
                ExpenseCategoryTag(name = "Eğitim", icon = "🎓", colorHex = 0xFF9C27B0, isDefault = true),
                ExpenseCategoryTag(name = "Eğlence", icon = "🎭", colorHex = 0xFFFF9800, isDefault = true),
                ExpenseCategoryTag(name = "Market", icon = "🛒", colorHex = 0xFF2196F3, isDefault = true),
                ExpenseCategoryTag(name = "Ulaşım & Akaryakıt", icon = "⛽", colorHex = 0xFF009688, isDefault = true),
                ExpenseCategoryTag(name = "Fatura & Aidat", icon = "⚡", colorHex = 0xFFE91E63, isDefault = true),
                ExpenseCategoryTag(name = "Sağlık & Eczane", icon = "💊", colorHex = 0xFFE53935, isDefault = true),
                ExpenseCategoryTag(name = "Giyim & Moda", icon = "👗", colorHex = 0xFF795548, isDefault = true),
                ExpenseCategoryTag(name = "Diğer", icon = "📌", colorHex = 0xFF607D8B, isDefault = true)
            )
            categoryDao.insertAll(defaults)
        }
    }

    // Shopping List actions
    suspend fun insertShoppingItem(item: ShoppingItem): Long =
        shoppingDao.insertShoppingItem(item)

    suspend fun insertShoppingItems(items: List<ShoppingItem>) =
        shoppingDao.insertShoppingItems(items)

    suspend fun updateShoppingItem(item: ShoppingItem) =
        shoppingDao.updateShoppingItem(item)

    suspend fun toggleShoppingItemPurchased(id: Long, isPurchased: Boolean) =
        shoppingDao.toggleShoppingItemPurchased(id, isPurchased, if (isPurchased) System.currentTimeMillis() else null)

    suspend fun deleteShoppingItem(item: ShoppingItem) =
        shoppingDao.deleteShoppingItem(item)

    suspend fun deleteShoppingItemById(id: Long) =
        shoppingDao.deleteShoppingItemById(id)

    suspend fun clearPurchasedShoppingItems() =
        shoppingDao.clearPurchasedShoppingItems()

    suspend fun clearAllShoppingItems() =
        shoppingDao.clearAllShoppingItems()

    fun getReceiptsByMonth(monthPrefix: String): Flow<List<ExpenseReceipt>> =
        receiptDao.getReceiptsByMonth(monthPrefix)

    fun getItemsForReceipt(receiptId: Long): Flow<List<ReceiptItem>> =
        receiptDao.getItemsForReceipt(receiptId)

    suspend fun getItemsForReceiptSync(receiptId: Long): List<ReceiptItem> =
        receiptDao.getItemsForReceiptSync(receiptId)

    fun getBudgetLimit(monthKey: String): Flow<BudgetLimit?> =
        receiptDao.getBudgetLimit(monthKey)

    suspend fun saveBudgetLimit(limit: BudgetLimit) =
        receiptDao.setBudgetLimit(limit)

    suspend fun insertFamilyMember(member: FamilyMember): Long =
        familyDao.insertMember(member)

    suspend fun updateFamilyMember(member: FamilyMember) =
        familyDao.updateMember(member)

    suspend fun deleteFamilyMember(member: FamilyMember) =
        familyDao.deleteMember(member)

    suspend fun setCurrentUser(memberId: Long) {
        familyDao.clearCurrentUser()
        familyDao.setCurrentUser(memberId)
    }

    suspend fun setMemberApproved(memberId: Long, approved: Boolean) =
        familyDao.setMemberApproved(memberId, approved)

    // Income Source actions
    suspend fun insertIncomeSource(income: IncomeSource): Long =
        fixedBudgetDao.insertIncomeSource(income)

    suspend fun updateIncomeSource(income: IncomeSource) =
        fixedBudgetDao.updateIncomeSource(income)

    suspend fun deleteIncomeSource(income: IncomeSource) =
        fixedBudgetDao.deleteIncomeSource(income)

    suspend fun setIncomeReceived(id: Long, isReceived: Boolean) =
        fixedBudgetDao.setIncomeReceived(id, isReceived)

    // Fixed Expense actions
    suspend fun insertFixedExpense(expense: FixedExpense): Long =
        fixedBudgetDao.insertFixedExpense(expense)

    suspend fun updateFixedExpense(expense: FixedExpense) =
        fixedBudgetDao.updateFixedExpense(expense)

    suspend fun deleteFixedExpense(expense: FixedExpense) =
        fixedBudgetDao.deleteFixedExpense(expense)

    suspend fun setFixedExpensePaid(id: Long, isPaid: Boolean) =
        fixedBudgetDao.setFixedExpensePaid(id, isPaid)

    suspend fun insertReceiptWithItems(receipt: ExpenseReceipt, items: List<ReceiptItem>): Long =
        receiptDao.insertReceiptWithItems(receipt, items)

    suspend fun deleteReceipt(receipt: ExpenseReceipt) =
        receiptDao.deleteReceiptWithItems(receipt)

    suspend fun clearAllData() {
        receiptDao.deleteAllReceiptItems()
        receiptDao.deleteAllReceipts()
        receiptDao.deleteAllBudgetLimits()
        familyDao.deleteAllMembers()
        fixedBudgetDao.deleteAllIncomeSources()
        fixedBudgetDao.deleteAllFixedExpenses()
        shoppingDao.clearAllShoppingItems()
    }

    // Calculate aggregated product price trend over months
    fun getProductPriceTrend(productName: String): Flow<ProductTrendSummary?> {
        return receiptDao.getProductHistory(productName).combine(allReceiptItems) { history, _ ->
            if (history.isEmpty()) {
                return@combine null
            }
            val dateFormat = SimpleDateFormat("yyyy-MM", Locale.getDefault())
            val displayMonthFormat = SimpleDateFormat("MMM yyyy", Locale("tr"))

            // Group items by year-month
            val groupedByMonth = history.groupBy { item ->
                if (item.date.length >= 7) item.date.substring(0, 7)
                else dateFormat.format(Date(item.timestamp))
            }.toSortedMap()

            val points = mutableListOf<ProductPricePoint>()
            groupedByMonth.forEach { (monthKey, itemsInMonth) ->
                val validPrices = itemsInMonth.map { if (it.unitPrice > 0) it.unitPrice else it.totalPrice / Math.max(it.quantity, 1.0) }
                if (validPrices.isNotEmpty()) {
                    val avg = validPrices.average()
                    val min = validPrices.minOrNull() ?: avg
                    val max = validPrices.maxOrNull() ?: avg
                    val label = try {
                        val parsed = dateFormat.parse(monthKey)
                        if (parsed != null) displayMonthFormat.format(parsed).replaceFirstChar { it.uppercase() } else monthKey
                    } catch (e: Exception) {
                        monthKey
                    }
                    points.add(ProductPricePoint(label, avg, min, max, itemsInMonth.size))
                }
            }

            if (points.isEmpty()) return@combine null

            val currentPrice = points.last().averageUnitPrice
            val previousPrice = if (points.size > 1) points.first().averageUnitPrice else currentPrice
            val changePercent = if (previousPrice > 0) ((currentPrice - previousPrice) / previousPrice) * 100.0 else 0.0
            val cat = history.firstOrNull()?.category ?: "Genel"

            ProductTrendSummary(
                productName = productName,
                category = cat,
                currentPrice = currentPrice,
                previousPrice = previousPrice,
                changePercent = changePercent,
                points = points
            )
        }
    }

    // Market Product Mapping & Retailer Verification DB operations
    suspend fun ensureDefaultMarketCatalog() {
        if (marketProductDao.getCount() == 0) {
            marketProductDao.insertAll(MarketCatalogSeeder.getDefaultMappings())
        }
    }

    suspend fun refreshMarketCatalog() {
        marketProductDao.clearAll()
        marketProductDao.insertAll(MarketCatalogSeeder.getDefaultMappings())
    }

    fun getProductsByMarket(marketId: String): Flow<List<MarketProductMapping>> =
        marketProductDao.getProductsByMarket(marketId)

    suspend fun getProductsByMarketSync(marketId: String): List<MarketProductMapping> =
        marketProductDao.getProductsByMarketSync(marketId)

    fun searchMarketProducts(query: String): Flow<List<MarketProductMapping>> =
        marketProductDao.searchAvailableProducts(query)

    suspend fun findProductPriceInMarket(marketId: String, productKey: String, productName: String): MarketProductMapping? =
        marketProductDao.findProductPriceInMarket(marketId, productKey, productName)

    suspend fun findMappingsForProduct(productKey: String, productName: String): List<MarketProductMapping> =
        marketProductDao.findMappingsForProduct(productKey, productName)

    suspend fun getValidMarketIdsForProduct(productKey: String, productName: String): List<String> =
        marketProductDao.getValidMarketIdsForProduct(productKey, productName)

    suspend fun insertMarketProduct(mapping: MarketProductMapping): Long =
        marketProductDao.insert(mapping)

    suspend fun updateMarketProduct(mapping: MarketProductMapping) =
        marketProductDao.update(mapping)

    suspend fun deleteMarketProductById(id: Long) =
        marketProductDao.deleteById(id)
}
