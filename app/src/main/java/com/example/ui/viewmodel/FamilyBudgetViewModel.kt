package com.example.ui.viewmodel

import android.app.Application
import android.content.Context
import android.graphics.Bitmap
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.local.AppDatabase
import com.example.data.local.NotificationHelper
import com.example.data.model.AppLanguage
import com.example.data.model.CategoryMonthlyStat
import com.example.data.model.CategoryWeeklyStat
import com.example.data.model.DailySpendingPoint
import com.example.data.model.ExpenseCategoryTag
import com.example.data.model.ExpenseReceipt
import com.example.data.model.FamilyMember
import com.example.data.model.FixedExpense
import com.example.data.model.IncomeSource
import com.example.data.model.MarketProductMapping
import com.example.data.model.MarketProductOption
import com.example.data.model.MarketProductTemplate
import com.example.data.model.ParsedReceipt
import com.example.data.model.ParsedReceiptItem
import com.example.data.model.ProductPricePoint
import com.example.data.model.ProductTrendSummary
import com.example.data.model.ReceiptItem
import com.example.data.model.ShoppingItem
import com.example.data.model.ThemeMode
import com.example.data.model.UpcomingScheduleItem
import com.example.data.model.WeeklyComparisonStat
import com.example.data.ocr.ReceiptOcrService
import com.example.data.repository.FamilyBudgetRepository
import com.example.ui.i18n.StringsProvider
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

data class BudgetUiState(
    val currentLanguage: AppLanguage = AppLanguage.TR,
    val themeMode: ThemeMode = ThemeMode.SYSTEM,
    val notificationsEnabled: Boolean = true,
    val activeMonthKey: String = "2026-08",
    val members: List<FamilyMember> = emptyList(),
    val currentUser: FamilyMember? = null,
    val receipts: List<ExpenseReceipt> = emptyList(),
    val allReceiptItems: List<ReceiptItem> = emptyList(),
    val incomeSources: List<IncomeSource> = emptyList(),
    val fixedExpenses: List<FixedExpense> = emptyList(),
    val totalSalaryIncome: Double = 0.0,
    val totalRentalIncome: Double = 0.0,
    val totalExtraIncome: Double = 0.0,
    val totalIncome: Double = 0.0,
    val totalRentExpense: Double = 0.0,
    val totalCreditCardExpenses: Double = 0.0,
    val totalOtherFixedExpenses: Double = 0.0,
    val totalFixedExpenses: Double = 0.0,
    val netDisposableIncome: Double = 0.0,
    val totalSpentThisMonth: Double = 0.0,
    val totalOutflow: Double = 0.0,
    val remainingFreeBudget: Double = 0.0,
    val budgetLimit: Double = 50000.0,
    val customBudgetLimit: Double? = null,
    val warningThresholdPercent: Int = 80,
    val remainingBudget: Double = 0.0,
    val usagePercentage: Float = 0f,
    val fixedExpensesRatio: Float = 0f,
    val receiptsExpensesRatio: Float = 0f,
    val savingsRatio: Float = 0f,
    val daysRemainingInMonth: Int = 1,
    val dailyAvailableBudget: Double = 0.0,
    val alertType: AlertType = AlertType.NORMAL,
    val alertMessage: String = "",
    val categoryMonthlyStats: List<CategoryMonthlyStat> = emptyList(),
    val categoryWeeklyStats: List<CategoryWeeklyStat> = emptyList(),
    val totalVatPaidThisMonth: Double = 0.0,
    val netExpenseWithoutVat: Double = 0.0,
    val vatBreakdownByRate: Map<Double, Double> = emptyMap(),
    val effectiveVatRatePercentage: Float = 0f,
    val topSpentProducts: List<Pair<String, Double>> = emptyList(),
    val productNames: List<String> = emptyList(),
    val selectedTrendProduct: String = "Süt 1L (Tam Yağlı)",
    val productTrend: ProductTrendSummary? = null,
    val upcomingSchedule: List<UpcomingScheduleItem> = emptyList(),
    val isScanningOcr: Boolean = false,
    val scannedReceiptForReview: ParsedReceipt? = null,
    val ocrErrorMessage: String? = null,
    val shoppingItems: List<ShoppingItem> = emptyList(),
    val allCategories: List<ExpenseCategoryTag> = emptyList(),
    val marketMappings: List<MarketProductMapping> = emptyList(),
    val last7DaysComparison: WeeklyComparisonStat = WeeklyComparisonStat()
)

enum class AlertType {
    NORMAL,
    WARNING,
    EXCEEDED
}

private data class AppUiPreferences(
    val language: AppLanguage,
    val themeMode: ThemeMode,
    val notificationsEnabled: Boolean,
    val activeMonth: String,
    val trendProduct: String,
    val customLimit: Double?,
    val warningThreshold: Int,
    val isScanning: Boolean,
    val reviewReceipt: ParsedReceipt?,
    val ocrError: String?
)

private data class RepoDataState(
    val members: List<FamilyMember>,
    val receipts: List<ExpenseReceipt>,
    val items: List<ReceiptItem>,
    val productNames: List<String>,
    val incomeSources: List<IncomeSource>,
    val fixedExpenses: List<FixedExpense>,
    val shoppingItems: List<ShoppingItem>,
    val categories: List<ExpenseCategoryTag>,
    val marketMappings: List<MarketProductMapping>
)

class FamilyBudgetViewModel(application: Application) : AndroidViewModel(application) {

    private val db = AppDatabase.getInstance(application)
    private val repository = FamilyBudgetRepository(
        db.familyDao(),
        db.receiptDao(),
        db.fixedBudgetDao(),
        db.shoppingDao(),
        db.categoryDao(),
        db.marketProductDao()
    )
    private val ocrService = ReceiptOcrService()

    init {
        viewModelScope.launch {
            repository.ensureDefaultCategories()
            repository.ensureDefaultMarketCatalog()
        }
    }

    fun clearAllData() {
        viewModelScope.launch {
            repository.clearAllData()
        }
    }

    private val sharedPrefs = application.getSharedPreferences("family_budget_settings", Context.MODE_PRIVATE)

    private val initialThemeMode: ThemeMode = try {
        val savedTheme = sharedPrefs.getString("pref_theme_mode", null)
        if (savedTheme != null) ThemeMode.valueOf(savedTheme) else ThemeMode.SYSTEM
    } catch (e: Exception) {
        ThemeMode.SYSTEM
    }

    private val initialLanguage: AppLanguage = try {
        val savedLang = sharedPrefs.getString("pref_app_language", null)
        if (savedLang != null) AppLanguage.valueOf(savedLang) else AppLanguage.TR
    } catch (e: Exception) {
        AppLanguage.TR
    }

    private val initialNotifications: Boolean = sharedPrefs.getBoolean("pref_notifications_enabled", true)

    private val _currentLanguage = MutableStateFlow(initialLanguage)
    val currentLanguage: StateFlow<AppLanguage> = _currentLanguage.asStateFlow()

    private val _themeMode = MutableStateFlow(initialThemeMode)
    val themeMode: StateFlow<ThemeMode> = _themeMode.asStateFlow()

    private val _notificationsEnabled = MutableStateFlow(initialNotifications)
    val notificationsEnabled: StateFlow<Boolean> = _notificationsEnabled.asStateFlow()

    private val _activeMonthKey = MutableStateFlow("2026-08")
    val activeMonthKey: StateFlow<String> = _activeMonthKey.asStateFlow()

    private val _selectedTrendProduct = MutableStateFlow("Süt 1L (Tam Yağlı)")
    val selectedTrendProduct: StateFlow<String> = _selectedTrendProduct.asStateFlow()

    private val _customBudgetLimit = MutableStateFlow<Double?>(null)
    val customBudgetLimit: StateFlow<Double?> = _customBudgetLimit.asStateFlow()

    private val _warningThresholdPercent = MutableStateFlow(80)
    val warningThresholdPercent: StateFlow<Int> = _warningThresholdPercent.asStateFlow()

    private val _isScanningOcr = MutableStateFlow(false)
    val isScanningOcr: StateFlow<Boolean> = _isScanningOcr.asStateFlow()

    private val _scannedReceiptForReview = MutableStateFlow<ParsedReceipt?>(null)
    val scannedReceiptForReview: StateFlow<ParsedReceipt?> = _scannedReceiptForReview.asStateFlow()

    private val _ocrErrorMessage = MutableStateFlow<String?>(null)
    val ocrErrorMessage: StateFlow<String?> = _ocrErrorMessage.asStateFlow()

    private val prefsFlow = combine(
        _currentLanguage,
        _themeMode,
        _notificationsEnabled,
        _activeMonthKey,
        _selectedTrendProduct
    ) { lang, theme, notif, month, trend ->
        Tuple5(lang, theme, notif, month, trend)
    }.combine(
        combine(_customBudgetLimit, _warningThresholdPercent) { lim, thresh -> Pair(lim, thresh) }
    ) { p1, p2 ->
        Triple(p1, p2.first, p2.second)
    }.combine(
        combine(_isScanningOcr, _scannedReceiptForReview, _ocrErrorMessage) { scan, r, e -> Triple(scan, r, e) }
    ) { p1, p2 ->
        AppUiPreferences(
            language = p1.first.a,
            themeMode = p1.first.b,
            notificationsEnabled = p1.first.c,
            activeMonth = p1.first.d,
            trendProduct = p1.first.e,
            customLimit = p1.second,
            warningThreshold = p1.third,
            isScanning = p2.first,
            reviewReceipt = p2.second,
            ocrError = p2.third
        )
    }

    private val repoFlow = combine(
        combine(repository.allMembers, repository.allReceipts, repository.allReceiptItems) { m, r, i -> Triple(m, r, i) },
        combine(repository.allProductNames, repository.allIncomeSources, repository.allFixedExpenses) { p, inc, fix -> Triple(p, inc, fix) },
        combine(repository.allShoppingItems, repository.allCategories, repository.allMarketMappings) { shop, cats, maps -> Triple(shop, cats, maps) }
    ) { (m, r, i), (p, inc, fix), (shop, cats, maps) ->
        RepoDataState(m, r, i, p, inc, fix, shop, cats, maps)
    }

    val uiState: StateFlow<BudgetUiState> = combine(
        prefsFlow,
        repoFlow
    ) { prefs, repo ->
        val strings = StringsProvider(prefs.language)
        val currentUser = repo.members.firstOrNull { it.isCurrentUser } ?: repo.members.firstOrNull()

        // 1. Total monthly income calculated from income sources (Maaş, Kira Geliri, Ek Gelir)
        val totalSalary = repo.incomeSources.filter { it.type == "MAAS" }.sumOf { it.amount }
        val totalRental = repo.incomeSources.filter { it.type == "KIRA_GELIRI" }.sumOf { it.amount }
        val totalExtra = repo.incomeSources.filter { it.type == "EK_GELIR" }.sumOf { it.amount }
        val totalIncome = if (repo.incomeSources.isNotEmpty()) {
            totalSalary + totalRental + totalExtra
        } else {
            repo.members.sumOf { it.monthlySalary + it.additionalIncome }
        }

        // 2. Fixed recurring expenses (Kira, 3 Adet Kredi Kartı, Aidat, Fatura)
        val totalRentExpense = repo.fixedExpenses.filter { it.type == "KIRA" }.sumOf { it.amount }
        val totalCreditCards = repo.fixedExpenses.filter { it.type.startsWith("KREDI_KARTI") }.sumOf { it.amount }
        val totalOtherFixed = repo.fixedExpenses.filter { it.type != "KIRA" && !it.type.startsWith("KREDI_KARTI") }.sumOf { it.amount }
        val totalFixedExpenses = repo.fixedExpenses.sumOf { it.amount }

        // Net Disposable Budget = Total Monthly Income - Total Fixed Expenses
        val netDisposable = (totalIncome - totalFixedExpenses).coerceAtLeast(0.0)

        // 3. Filter receipts for current active month
        val currentMonthReceipts = repo.receipts.filter {
            if (it.receiptDate.length >= 7) it.receiptDate.startsWith(prefs.activeMonth)
            else true
        }

        val totalSpent = currentMonthReceipts.sumOf { it.totalAmount }
        
        // Automatic Calculation: Remaining Monthly Budget = Total Monthly Income - Total Fixed Expenses - Tracked Receipts
        val totalOutflow = totalFixedExpenses + totalSpent
        val calculatedIncomeLimit = if (totalIncome > 0) totalIncome else 50000.0
        val limit = prefs.customLimit ?: calculatedIncomeLimit
        val usagePct = if (limit > 0) ((totalOutflow / limit) * 100).toFloat() else 0f
        val warningPct = prefs.warningThreshold
        val remainingMonthlyBudget = limit - totalOutflow

        // Calculate Allocation Ratios
        val fixedRatio = if (limit > 0) ((totalFixedExpenses / limit) * 100).toFloat() else 0f
        val receiptsRatio = if (limit > 0) ((totalSpent / limit) * 100).toFloat() else 0f
        val savingsRatio = if (limit > 0) ((remainingMonthlyBudget.coerceAtLeast(0.0) / limit) * 100).toFloat() else 0f

        // Calculate Days Remaining in Current Month and Daily Spendable Budget
        val calendar = Calendar.getInstance()
        val maxDays = calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
        val currentDay = calendar.get(Calendar.DAY_OF_MONTH)
        val daysRemainingInMonth = (maxDays - currentDay + 1).coerceAtLeast(1)
        val dailySpendable = if (remainingMonthlyBudget > 0) remainingMonthlyBudget / daysRemainingInMonth else 0.0

        val (alertType, alertMsg) = when {
            totalOutflow > limit -> {
                val over = totalOutflow - limit
                AlertType.EXCEEDED to strings.alertExceeded.format(String.format(Locale.getDefault(), "%,.0f ₺", over))
            }
            usagePct >= warningPct -> {
                AlertType.WARNING to strings.alertWarning.format("%${usagePct.toInt()}")
            }
            else -> {
                AlertType.NORMAL to strings.alertNormal
            }
        }

        // 4. Category Monthly Stats
        val categoryGroups = currentMonthReceipts.groupBy { it.category }
        val catStats = categoryGroups.map { (cat, catReceipts) ->
            val spent = catReceipts.sumOf { it.totalAmount }
            CategoryMonthlyStat(
                category = cat,
                totalSpent = spent,
                percentage = if (totalSpent > 0) (spent / totalSpent).toFloat() else 0f,
                transactionCount = catReceipts.size
            )
        }.sortedByDescending { it.totalSpent }

        // 5. Category Weekly Stats for current month
        val weeklyStats = calculateWeeklyCategoryStats(currentMonthReceipts)

        // 6. Monthly VAT (KDV) Calculations for Active Month
        val currentMonthItems = repo.items.filter { it.date.startsWith(prefs.activeMonth) }
        val totalVatFromReceipts = currentMonthReceipts.sumOf { r ->
            if (r.vatAmount > 0) r.vatAmount
            else if (r.vatRate > 0) r.totalAmount * (r.vatRate / (100.0 + r.vatRate))
            else 0.0
        }
        val totalVatFromItems = currentMonthItems.filter { !it.isCancelled && it.totalPrice > 0 }.sumOf { item ->
            if (item.vatRate > 0) item.totalPrice * (item.vatRate / (100.0 + item.vatRate))
            else 0.0
        }
        val totalVatPaid = if (totalVatFromItems > 0) totalVatFromItems else totalVatFromReceipts
        val netWithoutVat = (totalSpent - totalVatPaid).coerceAtLeast(0.0)
        val effectiveVatPct = if (netWithoutVat > 0) ((totalVatPaid / netWithoutVat) * 100).toFloat() else 0f

        val vatBreakdownMap = mutableMapOf<Double, Double>()
        if (currentMonthItems.isNotEmpty()) {
            currentMonthItems.filter { !it.isCancelled && it.vatRate > 0 && it.totalPrice > 0 }.forEach { item ->
                val v = item.totalPrice * (item.vatRate / (100.0 + item.vatRate))
                vatBreakdownMap[item.vatRate] = (vatBreakdownMap[item.vatRate] ?: 0.0) + v
            }
        }
        if (vatBreakdownMap.isEmpty() && currentMonthReceipts.isNotEmpty()) {
            currentMonthReceipts.forEach { r ->
                val rateKey = if (r.vatRate > 0) r.vatRate else 10.0
                val v = if (r.vatAmount > 0) r.vatAmount else r.totalAmount * (rateKey / (100.0 + rateKey))
                vatBreakdownMap[rateKey] = (vatBreakdownMap[rateKey] ?: 0.0) + v
            }
        }

        // 7. Top Spent Products
        val topProducts = currentMonthItems
            .groupBy { it.productName }
            .mapValues { (_, pItems) -> pItems.sumOf { it.totalPrice } }
            .toList()
            .sortedByDescending { it.second }
            .take(10)

        // 7. Selected Product Trend
        val trendSummary = calculateProductTrend(prefs.trendProduct, repo.items)

        // 8. Upcoming Payments and Income Timeline
        val currentCalendarDay = Calendar.getInstance().get(Calendar.DAY_OF_MONTH)
        val scheduleList = mutableListOf<UpcomingScheduleItem>()

        repo.incomeSources.forEach { inc ->
            val diff = inc.depositDay - currentCalendarDay
            scheduleList.add(
                UpcomingScheduleItem(
                    id = inc.id,
                    title = inc.title,
                    amount = inc.amount,
                    dayOfMonth = inc.depositDay,
                    isIncome = true,
                    type = inc.type,
                    isCompleted = inc.isReceived,
                    memberName = inc.familyMemberName,
                    daysRemaining = diff,
                    note = inc.note
                )
            )
        }

        repo.fixedExpenses.forEach { exp ->
            val diff = exp.dueDay - currentCalendarDay
            scheduleList.add(
                UpcomingScheduleItem(
                    id = exp.id,
                    title = exp.title,
                    amount = exp.amount,
                    dayOfMonth = exp.dueDay,
                    isIncome = false,
                    type = exp.type,
                    isCompleted = exp.isPaidThisMonth,
                    memberName = exp.familyMemberName,
                    daysRemaining = diff,
                    note = exp.note
                )
            )
        }
        val sortedSchedule = scheduleList.sortedBy { it.dayOfMonth }

        val weeklyComparison = calculateWeeklyComparison(repo.receipts)

        BudgetUiState(
            currentLanguage = prefs.language,
            themeMode = prefs.themeMode,
            notificationsEnabled = prefs.notificationsEnabled,
            activeMonthKey = prefs.activeMonth,
            members = repo.members,
            currentUser = currentUser,
            receipts = repo.receipts,
            allReceiptItems = repo.items,
            incomeSources = repo.incomeSources,
            fixedExpenses = repo.fixedExpenses,
            totalSalaryIncome = totalSalary,
            totalRentalIncome = totalRental,
            totalExtraIncome = totalExtra,
            totalIncome = totalIncome,
            totalRentExpense = totalRentExpense,
            totalCreditCardExpenses = totalCreditCards,
            totalOtherFixedExpenses = totalOtherFixed,
            totalFixedExpenses = totalFixedExpenses,
            netDisposableIncome = netDisposable,
            totalSpentThisMonth = totalSpent,
            totalOutflow = totalOutflow,
            remainingFreeBudget = remainingMonthlyBudget,
            budgetLimit = limit,
            warningThresholdPercent = warningPct,
            remainingBudget = remainingMonthlyBudget,
            usagePercentage = usagePct,
            fixedExpensesRatio = fixedRatio,
            receiptsExpensesRatio = receiptsRatio,
            savingsRatio = savingsRatio,
            daysRemainingInMonth = daysRemainingInMonth,
            dailyAvailableBudget = dailySpendable,
            alertType = alertType,
            alertMessage = alertMsg,
            categoryMonthlyStats = catStats,
            categoryWeeklyStats = weeklyStats,
            totalVatPaidThisMonth = totalVatPaid,
            netExpenseWithoutVat = netWithoutVat,
            vatBreakdownByRate = vatBreakdownMap,
            effectiveVatRatePercentage = effectiveVatPct,
            topSpentProducts = topProducts,
            productNames = repo.productNames,
            selectedTrendProduct = prefs.trendProduct,
            productTrend = trendSummary,
            upcomingSchedule = sortedSchedule,
            isScanningOcr = prefs.isScanning,
            scannedReceiptForReview = prefs.reviewReceipt,
            ocrErrorMessage = prefs.ocrError,
            shoppingItems = repo.shoppingItems,
            allCategories = repo.categories,
            marketMappings = repo.marketMappings,
            last7DaysComparison = weeklyComparison
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = BudgetUiState()
    )

    fun setLanguage(language: AppLanguage) {
        _currentLanguage.value = language
        sharedPrefs.edit().putString("pref_app_language", language.name).apply()
    }

    fun setThemeMode(mode: ThemeMode) {
        _themeMode.value = mode
        sharedPrefs.edit().putString("pref_theme_mode", mode.name).apply()
    }

    fun setNotificationsEnabled(enabled: Boolean) {
        _notificationsEnabled.value = enabled
        sharedPrefs.edit().putBoolean("pref_notifications_enabled", enabled).apply()
    }

    fun setCustomBudgetLimit(limit: Double?) {
        _customBudgetLimit.value = limit
        if (limit != null && limit > 0) {
            checkAndTriggerBudgetAlert(0.0)
        }
    }

    fun setWarningThresholdPercent(percent: Int) {
        _warningThresholdPercent.value = percent.coerceIn(50, 95)
        checkAndTriggerBudgetAlert(0.0)
    }

    fun checkAndTriggerBudgetAlert(additionalOutflow: Double = 0.0) {
        if (!_notificationsEnabled.value) return
        val state = uiState.value
        val newOutflow = state.totalOutflow + additionalOutflow
        val limit = state.budgetLimit
        val warningPct = state.warningThresholdPercent
        if (limit <= 0) return

        val percent = ((newOutflow / limit) * 100).toInt()
        val context = getApplication<Application>()
        val remaining = (limit - newOutflow).coerceAtLeast(0.0)
        val daily = if (remaining > 0) remaining / state.daysRemainingInMonth else 0.0

        if (newOutflow > limit) {
            NotificationHelper.showExceededBudgetNotification(
                context = context,
                currentSpent = newOutflow,
                limit = limit,
                overspentAmount = newOutflow - limit
            )
        } else if (percent >= warningPct) {
            NotificationHelper.showApproachingBudgetNotification(
                context = context,
                currentSpent = newOutflow,
                limit = limit,
                percent = percent,
                remainingAmount = remaining,
                dailySpendable = daily,
                daysLeft = state.daysRemainingInMonth
            )
        }
    }

    fun sendTestNotification() {
        val app = getApplication<Application>()
        val state = uiState.value
        val simulatedOutflow = if (state.totalOutflow > 0) state.totalOutflow else state.budgetLimit * 0.85
        val limit = state.budgetLimit
        val percent = if (limit > 0) ((simulatedOutflow / limit) * 100).toInt() else 85
        val remaining = (limit - simulatedOutflow).coerceAtLeast(0.0)
        val daily = if (remaining > 0) remaining / state.daysRemainingInMonth.coerceAtLeast(1) else 0.0

        NotificationHelper.showApproachingBudgetNotification(
            context = app,
            currentSpent = simulatedOutflow,
            limit = limit,
            percent = percent,
            remainingAmount = remaining,
            dailySpendable = daily,
            daysLeft = state.daysRemainingInMonth
        )
    }

    fun setActiveMonth(monthKey: String) {
        _activeMonthKey.value = monthKey
    }

    fun setSelectedTrendProduct(productName: String) {
        _selectedTrendProduct.value = productName
    }

    fun setCurrentFamilyMember(memberId: Long) {
        viewModelScope.launch {
            repository.setCurrentUser(memberId)
        }
    }

    fun approveFamilyMember(memberId: Long) {
        viewModelScope.launch {
            repository.setMemberApproved(memberId, true)
        }
    }

    fun addFamilyMember(
        name: String,
        countryCode: String,
        phone: String,
        role: String,
        salary: Double,
        additionalIncome: Double,
        autoApprove: Boolean = false
    ) {
        viewModelScope.launch {
            val colors = listOf(0xFF00695C, 0xFF8E24AA, 0xFFFB8C00, 0xFF0288D1, 0xFFE91E63, 0xFF43A047)
            val member = FamilyMember(
                name = name,
                countryCode = countryCode,
                phoneNumber = phone,
                role = role,
                avatarColorHex = colors.random(),
                isApproved = autoApprove,
                monthlySalary = salary,
                additionalIncome = additionalIncome,
                isCurrentUser = false
            )
            val memberId = repository.insertFamilyMember(member)
            // If salary > 0, also create matching income source
            if (salary > 0) {
                repository.insertIncomeSource(
                    IncomeSource(
                        title = "$name - Maaş",
                        type = "MAAS",
                        amount = salary,
                        depositDay = 1,
                        familyMemberId = memberId,
                        familyMemberName = name,
                        isReceived = true
                    )
                )
            }
        }
    }

    fun updateFamilyMember(member: FamilyMember) {
        viewModelScope.launch {
            repository.updateFamilyMember(member)
        }
    }

    fun deleteFamilyMember(member: FamilyMember) {
        viewModelScope.launch {
            repository.deleteFamilyMember(member)
        }
    }

    // --- Income Sources Management (Maaş, Kira Geliri, Ek Gelir) ---
    fun addIncomeSource(
        title: String,
        type: String,
        amount: Double,
        depositDay: Int,
        familyMemberId: Long,
        familyMemberName: String,
        note: String? = null
    ) {
        viewModelScope.launch {
            val income = IncomeSource(
                title = title,
                type = type,
                amount = amount,
                depositDay = depositDay.coerceIn(1, 31),
                familyMemberId = familyMemberId,
                familyMemberName = familyMemberName,
                isReceived = false,
                note = note
            )
            repository.insertIncomeSource(income)
        }
    }

    fun updateIncomeSource(income: IncomeSource) {
        viewModelScope.launch {
            repository.updateIncomeSource(income)
        }
    }

    fun deleteIncomeSource(income: IncomeSource) {
        viewModelScope.launch {
            repository.deleteIncomeSource(income)
        }
    }

    fun toggleIncomeReceived(id: Long, isReceived: Boolean) {
        viewModelScope.launch {
            repository.setIncomeReceived(id, isReceived)
        }
    }

    // --- Fixed Expenses Management (Kira, 3 Adet Kredi Kartı, Aidat, Fatura) ---
    fun addFixedExpense(
        title: String,
        type: String,
        amount: Double,
        dueDay: Int,
        familyMemberId: Long,
        familyMemberName: String,
        note: String? = null
    ) {
        viewModelScope.launch {
            val fixedExpense = FixedExpense(
                title = title,
                type = type,
                amount = amount,
                dueDay = dueDay.coerceIn(1, 31),
                familyMemberId = familyMemberId,
                familyMemberName = familyMemberName,
                isPaidThisMonth = false,
                note = note
            )
            repository.insertFixedExpense(fixedExpense)
            checkAndTriggerBudgetAlert(additionalOutflow = amount)
        }
    }

    fun updateFixedExpense(expense: FixedExpense) {
        viewModelScope.launch {
            repository.updateFixedExpense(expense)
        }
    }

    fun deleteFixedExpense(expense: FixedExpense) {
        viewModelScope.launch {
            repository.deleteFixedExpense(expense)
        }
    }

    fun toggleFixedExpensePaid(id: Long, isPaid: Boolean) {
        viewModelScope.launch {
            repository.setFixedExpensePaid(id, isPaid)
        }
    }

    fun saveReceipt(
        merchantName: String,
        receiptDate: String,
        category: String,
        vatRate: Double,
        vatAmount: Double,
        totalAmount: Double,
        familyMemberId: Long,
        familyMemberName: String,
        paymentMethod: String,
        items: List<ParsedReceiptItem>,
        note: String? = null
    ) {
        viewModelScope.launch {
            val receipt = ExpenseReceipt(
                merchantName = merchantName,
                receiptDate = receiptDate,
                timestamp = System.currentTimeMillis(),
                category = category,
                vatRate = vatRate,
                vatAmount = vatAmount,
                totalAmount = totalAmount,
                familyMemberId = familyMemberId,
                familyMemberName = familyMemberName,
                paymentMethod = paymentMethod,
                note = note
            )

            val receiptItems = if (items.isNotEmpty()) {
                items.map {
                    ReceiptItem(
                        receiptId = 0,
                        productName = it.productName.ifBlank { merchantName },
                        category = it.category.ifBlank { category },
                        quantity = it.quantity,
                        unitPrice = it.unitPrice,
                        totalPrice = it.totalPrice,
                        vatRate = if (it.vatRate > 0) it.vatRate else vatRate,
                        date = receiptDate,
                        isCancelled = it.isCancelled
                    )
                }
            } else {
                listOf(
                    ReceiptItem(
                        receiptId = 0,
                        productName = merchantName,
                        category = category,
                        quantity = 1.0,
                        unitPrice = totalAmount,
                        totalPrice = totalAmount,
                        vatRate = vatRate,
                        date = receiptDate,
                        isCancelled = false
                    )
                )
            }

            repository.insertReceiptWithItems(receipt, receiptItems)
            _scannedReceiptForReview.value = null

            // Check if notification is triggered for approaching / exceeded budget
            checkAndTriggerBudgetAlert(additionalOutflow = totalAmount)
        }
    }

    fun deleteReceipt(receipt: ExpenseReceipt) {
        viewModelScope.launch {
            repository.deleteReceipt(receipt)
        }
    }

    fun loadReceiptItems(receiptId: Long, onLoaded: (List<ReceiptItem>) -> Unit) {
        viewModelScope.launch {
            val items = repository.getItemsForReceiptSync(receiptId)
            onLoaded(items)
        }
    }

    fun scanReceiptBitmap(bitmap: Bitmap) {
        viewModelScope.launch {
            _isScanningOcr.value = true
            _ocrErrorMessage.value = null
            try {
                val result = ocrService.parseReceiptImage(bitmap)
                result.onSuccess { parsed ->
                    _scannedReceiptForReview.value = parsed
                }.onFailure { e ->
                    _ocrErrorMessage.value = e.message ?: "OCR error"
                }
            } catch (e: Exception) {
                _ocrErrorMessage.value = e.message
            } finally {
                _isScanningOcr.value = false
            }
        }
    }

    fun selectPresetReceipt(preset: ParsedReceipt) {
        _scannedReceiptForReview.value = preset
    }

    fun dismissReviewDialog() {
        _scannedReceiptForReview.value = null
    }

    fun clearOcrError() {
        _ocrErrorMessage.value = null
    }

    // Shopping List Operations
    fun addShoppingItem(
        name: String,
        category: String = "Market & Gıda",
        quantity: Double = 1.0,
        unit: String = "Adet",
        estimatedPrice: Double? = null,
        note: String? = null,
        familyMemberId: Long = 0,
        familyMemberName: String = "",
        avatarColor: Long = 0xFF00897B
    ) {
        viewModelScope.launch {
            val finalMemberId = if (familyMemberId > 0) familyMemberId else uiState.value.currentUser?.id ?: 0L
            val finalMemberName = if (familyMemberName.isNotBlank()) familyMemberName else uiState.value.currentUser?.name ?: "Aile Üyesi"
            val finalColor = if (avatarColor != 0L) avatarColor else uiState.value.currentUser?.avatarColorHex ?: 0xFF00897B
            val item = ShoppingItem(
                name = name.trim(),
                category = category,
                quantity = if (quantity > 0) quantity else 1.0,
                unit = unit,
                estimatedPrice = estimatedPrice,
                note = note?.trim()?.ifBlank { null },
                isPurchased = false,
                familyMemberId = finalMemberId,
                familyMemberName = finalMemberName,
                addedByAvatarColor = finalColor
            )
            repository.insertShoppingItem(item)
        }
    }

    fun addQuickShoppingItem(
        template: MarketProductTemplate,
        categoryName: String,
        member: FamilyMember? = null
    ) {
        val targetMember = member ?: uiState.value.currentUser
        addShoppingItem(
            name = template.name,
            category = categoryName,
            quantity = template.defaultQuantity,
            unit = template.defaultUnit,
            estimatedPrice = template.estimatedPrice,
            familyMemberId = targetMember?.id ?: 0L,
            familyMemberName = targetMember?.name ?: "",
            avatarColor = targetMember?.avatarColorHex ?: 0xFF00897B
        )
    }

    fun addMarketSelectedShoppingItems(
        marketOptions: List<MarketProductOption>,
        categoryName: String,
        member: FamilyMember? = null
    ) {
        val targetMember = member ?: uiState.value.currentUser
        viewModelScope.launch {
            marketOptions.forEach { opt ->
                val item = ShoppingItem(
                    name = "${opt.marketName} • ${opt.displayName}",
                    category = categoryName,
                    quantity = opt.quantity,
                    unit = opt.unit,
                    estimatedPrice = opt.price,
                    note = "${opt.marketName} (${opt.unitPriceDisplay})",
                    isPurchased = false,
                    familyMemberId = targetMember?.id ?: 0L,
                    familyMemberName = targetMember?.name ?: "Aile Üyesi",
                    addedByAvatarColor = targetMember?.avatarColorHex ?: 0xFF00897B
                )
                repository.insertShoppingItem(item)
            }
        }
    }

    fun addMarketMappingToShopping(
        mapping: MarketProductMapping,
        quantity: Double = 1.0,
        member: FamilyMember? = null
    ) {
        val targetMember = member ?: uiState.value.currentUser
        viewModelScope.launch {
            val finalPrice = mapping.price * quantity
            val item = ShoppingItem(
                name = "${mapping.marketName} • ${mapping.productName}",
                category = mapping.category,
                quantity = quantity,
                unit = mapping.unit,
                estimatedPrice = finalPrice,
                note = "${mapping.marketName} (${mapping.unitPriceDisplay})",
                isPurchased = false,
                familyMemberId = targetMember?.id ?: 0L,
                familyMemberName = targetMember?.name ?: "Aile Üyesi",
                addedByAvatarColor = targetMember?.avatarColorHex ?: 0xFF00897B
            )
            repository.insertShoppingItem(item)
        }
    }

    fun addMultipleVoiceShoppingItems(
        items: List<com.example.data.voice.VoiceShoppingItemDraft>,
        member: FamilyMember? = null
    ) {
        val targetMember = member ?: uiState.value.currentUser
        viewModelScope.launch {
            items.forEach { draft ->
                if (draft.name.isNotBlank()) {
                    val item = ShoppingItem(
                        name = draft.name.trim(),
                        category = draft.category,
                        quantity = if (draft.quantity > 0) draft.quantity else 1.0,
                        unit = draft.unit,
                        estimatedPrice = draft.estimatedPrice,
                        note = "🎙️ Sesli Eklendi",
                        isPurchased = false,
                        familyMemberId = targetMember?.id ?: 0L,
                        familyMemberName = targetMember?.name ?: "Aile Üyesi",
                        addedByAvatarColor = targetMember?.avatarColorHex ?: 0xFF00897B
                    )
                    repository.insertShoppingItem(item)
                }
            }
        }
    }

    fun resetMarketCatalogToDefaults() {
        viewModelScope.launch {
            repository.refreshMarketCatalog()
        }
    }

    fun toggleShoppingItemPurchased(id: Long, isPurchased: Boolean) {
        viewModelScope.launch {
            repository.toggleShoppingItemPurchased(id, isPurchased)
        }
    }

    fun updateShoppingItem(item: ShoppingItem) {
        viewModelScope.launch {
            repository.updateShoppingItem(item)
        }
    }

    fun deleteShoppingItem(item: ShoppingItem) {
        viewModelScope.launch {
            repository.deleteShoppingItem(item)
        }
    }

    fun clearPurchasedShoppingItems() {
        viewModelScope.launch {
            repository.clearPurchasedShoppingItems()
        }
    }

    fun convertPurchasedShoppingItemsToExpense(
        merchantName: String = "Süpermarket",
        category: String = "Market & Gıda",
        paymentMethod: String = "Kredi Kartı",
        memberId: Long = 0,
        memberName: String = "",
        onCompleted: (() -> Unit)? = null
    ) {
        viewModelScope.launch {
            val purchased = uiState.value.shoppingItems.filter { it.isPurchased }
            if (purchased.isEmpty()) return@launch

            val now = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())
            val targetMemberId = if (memberId > 0) memberId else uiState.value.currentUser?.id ?: 0L
            val targetMemberName = if (memberName.isNotBlank()) memberName else uiState.value.currentUser?.name ?: "Aile Üyesi"

            val total = purchased.sumOf { (it.estimatedPrice ?: 0.0) }
            val receipt = ExpenseReceipt(
                merchantName = merchantName,
                receiptDate = now,
                category = category,
                vatRate = 10.0,
                vatAmount = total * 0.10,
                totalAmount = if (total > 0) total else 1.0,
                familyMemberId = targetMemberId,
                familyMemberName = targetMemberName,
                paymentMethod = paymentMethod,
                note = "Alışveriş listesinden aktarıldı (${purchased.size} ürün)"
            )

            val receiptItems = purchased.map { item ->
                val unitPrice = item.estimatedPrice?.let { if (item.quantity > 0) it / item.quantity else it } ?: 0.0
                ReceiptItem(
                    receiptId = 0,
                    productName = item.name,
                    category = item.category,
                    quantity = item.quantity,
                    unitPrice = unitPrice,
                    totalPrice = item.estimatedPrice ?: 0.0,
                    vatRate = 10.0,
                    date = now
                )
            }

            repository.insertReceiptWithItems(receipt, receiptItems)
            repository.clearPurchasedShoppingItems()
            onCompleted?.invoke()
        }
    }

    private fun calculateWeeklyCategoryStats(receipts: List<ExpenseReceipt>): List<CategoryWeeklyStat> {
        val result = mutableListOf<CategoryWeeklyStat>()
        val weeks = (1..4)
        weeks.forEach { weekNum ->
            val weekReceipts = receipts.filter { receipt ->
                val day = receipt.receiptDate.takeLast(2).toIntOrNull() ?: 1
                when (weekNum) {
                    1 -> day in 1..7
                    2 -> day in 8..14
                    3 -> day in 15..21
                    else -> day >= 22
                }
            }
            val weekLabel = "Hafta $weekNum"
            val groupedByCat = weekReceipts.groupBy { it.category }
            groupedByCat.forEach { (cat, catRecs) ->
                result.add(
                    CategoryWeeklyStat(
                        weekNumber = weekNum,
                        weekLabel = weekLabel,
                        category = cat,
                        totalSpent = catRecs.sumOf { it.totalAmount }
                    )
                )
            }
        }
        return result
    }

    private fun calculateProductTrend(productName: String, allItems: List<ReceiptItem>): ProductTrendSummary? {
        val history = allItems.filter { it.productName.equals(productName, ignoreCase = true) && !it.isCancelled }
        if (history.isEmpty()) return null

        val dateFormat = SimpleDateFormat("yyyy-MM", Locale.getDefault())
        val displayMonthFormat = SimpleDateFormat("MMM yyyy", Locale("tr"))

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

        if (points.isEmpty()) return null

        val currentPrice = points.last().averageUnitPrice
        val previousPrice = if (points.size > 1) points.first().averageUnitPrice else currentPrice
        val changePercent = if (previousPrice > 0) ((currentPrice - previousPrice) / previousPrice) * 100.0 else 0.0
        val cat = history.firstOrNull()?.category ?: "Market & Gıda"

        return ProductTrendSummary(
            productName = productName,
            category = cat,
            currentPrice = currentPrice,
            previousPrice = previousPrice,
            changePercent = changePercent,
            points = points
        )
    }

    private fun calculateWeeklyComparison(allReceipts: List<ExpenseReceipt>): WeeklyComparisonStat {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val dayFormat = SimpleDateFormat("EEE", Locale("tr"))
        val calendar = Calendar.getInstance()

        calendar.set(Calendar.HOUR_OF_DAY, 0)
        calendar.set(Calendar.MINUTE, 0)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        val todayMs = calendar.timeInMillis
        val oneDayMs = 24L * 60 * 60 * 1000

        val current7DaysDates = (6 downTo 0).map { offset ->
            val cal = Calendar.getInstance().apply {
                timeInMillis = todayMs - (offset * oneDayMs)
            }
            val dateStr = sdf.format(cal.time)
            val dayLabel = try {
                dayFormat.format(cal.time).replace(".", "").take(3).replaceFirstChar { it.uppercase() }
            } catch (e: Exception) {
                dateStr.takeLast(2)
            }
            dateStr to dayLabel
        }
        val current7DaysDateSet = current7DaysDates.map { it.first }.toSet()

        val prev7DaysDates = (13 downTo 7).map { offset ->
            val cal = Calendar.getInstance().apply {
                timeInMillis = todayMs - (offset * oneDayMs)
            }
            sdf.format(cal.time)
        }.toSet()

        val currentWeekReceipts = allReceipts.filter { r ->
            if (r.receiptDate.isNotBlank()) {
                r.receiptDate in current7DaysDateSet
            } else {
                val diffDays = (todayMs - r.timestamp) / oneDayMs
                diffDays in 0..6
            }
        }

        val previousWeekReceipts = allReceipts.filter { r ->
            if (r.receiptDate.isNotBlank()) {
                r.receiptDate in prev7DaysDates
            } else {
                val diffDays = (todayMs - r.timestamp) / oneDayMs
                diffDays in 7..13
            }
        }

        val currentSpent = currentWeekReceipts.sumOf { it.totalAmount }
        val previousSpent = previousWeekReceipts.sumOf { it.totalAmount }
        val diffAmount = currentSpent - previousSpent
        val changePercent = if (previousSpent > 0) {
            ((currentSpent - previousSpent) / previousSpent) * 100.0
        } else if (currentSpent > 0) {
            100.0
        } else {
            0.0
        }

        val dailyBreakdown = current7DaysDates.map { (dateStr, dayLabel) ->
            val daySpent = currentWeekReceipts.filter { it.receiptDate == dateStr }.sumOf { it.totalAmount }
            DailySpendingPoint(dayLabel = dayLabel, dateStr = dateStr, amount = daySpent)
        }

        val topCategoryEntry = currentWeekReceipts.groupBy { it.category }
            .maxByOrNull { entry -> entry.value.sumOf { it.totalAmount } }
        val topCategoryName = topCategoryEntry?.key ?: ""
        val topCategorySpent = topCategoryEntry?.value?.sumOf { it.totalAmount } ?: 0.0

        return WeeklyComparisonStat(
            currentWeekSpent = currentSpent,
            previousWeekSpent = previousSpent,
            differenceAmount = diffAmount,
            changePercentage = changePercent,
            isIncrease = diffAmount > 0,
            currentWeekReceiptCount = currentWeekReceipts.size,
            previousWeekReceiptCount = previousWeekReceipts.size,
            dailyBreakdown = dailyBreakdown,
            currentWeekAverageDaily = currentSpent / 7.0,
            topCategoryThisWeek = topCategoryName,
            topCategorySpentThisWeek = topCategorySpent
        )
    }

    // Category / Tag Customization Actions
    fun addCustomCategory(name: String, icon: String = "🏷️", colorHex: Long = 0xFF4CAF50) {
        if (name.isBlank()) return
        viewModelScope.launch {
            val cat = ExpenseCategoryTag(
                name = name.trim(),
                icon = icon.ifBlank { "🏷️" },
                colorHex = colorHex,
                isDefault = false
            )
            repository.insertCategory(cat)
        }
    }

    fun updateCustomCategory(category: ExpenseCategoryTag) {
        viewModelScope.launch {
            repository.updateCategory(category)
        }
    }

    fun deleteCustomCategory(category: ExpenseCategoryTag) {
        viewModelScope.launch {
            repository.deleteCategory(category)
        }
    }

    fun deleteCustomCategoryById(id: Long) {
        viewModelScope.launch {
            repository.deleteCategoryById(id)
        }
    }
}

private data class Tuple5<A, B, C, D, E>(val a: A, val b: B, val c: C, val d: D, val e: E)
