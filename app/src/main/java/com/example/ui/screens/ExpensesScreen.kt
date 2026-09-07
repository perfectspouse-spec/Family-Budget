package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.FilterAlt
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.RestartAlt
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Sort
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CategoryClassifier
import com.example.data.model.ExpenseCategoryTag
import com.example.data.model.ExpenseReceipt
import com.example.ui.components.CategoryManagementDialog
import com.example.ui.components.ReceiptItemCard
import com.example.ui.i18n.StringsProvider
import com.example.ui.viewmodel.BudgetUiState
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

enum class ExpenseSortOption {
    NEWEST,
    OLDEST,
    HIGHEST_AMOUNT,
    LOWEST_AMOUNT
}

@Composable
fun ExpensesScreen(
    state: BudgetUiState,
    strings: StringsProvider,
    onReceiptClick: (ExpenseReceipt) -> Unit,
    onScanReceiptClick: () -> Unit = {},
    onManualExpenseClick: () -> Unit = {},
    onAddCategory: (name: String, icon: String, colorHex: Long) -> Unit = { _, _, _ -> },
    onUpdateCategory: (ExpenseCategoryTag) -> Unit = {},
    onDeleteCategory: (ExpenseCategoryTag) -> Unit = {}
) {
    var searchQuery by remember { mutableStateOf("") }
    var selectedCategoryFilter by remember { mutableStateOf("Tümü") }
    var selectedMemberFilter by remember { mutableStateOf("Tümü") }
    
    // Date Filters
    var selectedDatePreset by remember { mutableStateOf("Tümü") } // Tümü, Bugün, Bu Hafta, Son 7 Gün, Bu Ay, Son 30 Gün, Geçen Ay, Özel
    var customStartDate by remember { mutableStateOf("") }
    var customEndDate by remember { mutableStateOf("") }
    var showCustomDateFields by remember { mutableStateOf(false) }

    // Expense Amount Filters
    var selectedAmountPreset by remember { mutableStateOf("Tümü") } // Tümü, 0-100, 100-500, 500-1500, 1500-5000, 5000+, Özel
    var minAmountInput by remember { mutableStateOf("") }
    var maxAmountInput by remember { mutableStateOf("") }
    var showCustomAmountFields by remember { mutableStateOf(false) }

    // Sorting
    var selectedSortOption by remember { mutableStateOf(ExpenseSortOption.NEWEST) }

    // UI state
    var isFilterPanelExpanded by remember { mutableStateOf(false) }
    var showCategoryManageDialog by remember { mutableStateOf(false) }

    val todayDateStr = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date()) }

    // Map receiptId to item names to enable deep search inside receipt items!
    val receiptIdToItemNames = remember(state.allReceiptItems) {
        state.allReceiptItems.groupBy { it.receiptId }
            .mapValues { (_, items) -> items.joinToString(" ") { it.productName } }
    }

    // Active filters count
    val activeFiltersCount = remember(
        searchQuery, selectedCategoryFilter, selectedMemberFilter,
        selectedDatePreset, customStartDate, customEndDate,
        selectedAmountPreset, minAmountInput, maxAmountInput
    ) {
        var count = 0
        if (searchQuery.isNotBlank()) count++
        if (selectedCategoryFilter != "Tümü") count++
        if (selectedMemberFilter != "Tümü") count++
        if (selectedDatePreset != "Tümü") count++
        if (selectedDatePreset == "Özel" && (customStartDate.isNotBlank() || customEndDate.isNotBlank())) count++
        if (selectedAmountPreset != "Tümü") count++
        if (selectedAmountPreset == "Özel" && (minAmountInput.isNotBlank() || maxAmountInput.isNotBlank())) count++
        count
    }

    val minAmountVal = minAmountInput.toDoubleOrNull()
    val maxAmountVal = maxAmountInput.toDoubleOrNull()

    val filteredReceipts = state.receipts.filter { receipt ->
        // 1. Text & item search matching
        val itemsContent = receiptIdToItemNames[receipt.id] ?: ""
        val matchesSearch = searchQuery.isBlank() ||
                receipt.merchantName.contains(searchQuery, ignoreCase = true) ||
                receipt.category.contains(searchQuery, ignoreCase = true) ||
                receipt.familyMemberName.contains(searchQuery, ignoreCase = true) ||
                itemsContent.contains(searchQuery, ignoreCase = true) ||
                (receipt.note != null && receipt.note.contains(searchQuery, ignoreCase = true))

        // 2. Member matching
        val matchesMember = selectedMemberFilter == "Tümü" ||
                receipt.familyMemberName.equals(selectedMemberFilter, ignoreCase = true)

        // 3. Category matching
        val matchesCategory = selectedCategoryFilter == "Tümü" ||
                receipt.category.contains(selectedCategoryFilter, ignoreCase = true)

        // 4. Date range matching
        val matchesDate = when (selectedDatePreset) {
            "Tümü" -> true
            "Bugün" -> receipt.receiptDate == todayDateStr
            "Bu Hafta" -> isDateWithinDays(receipt.receiptDate, 7)
            "Son 7 Gün" -> isDateWithinDays(receipt.receiptDate, 7)
            "Bu Ay" -> receipt.receiptDate.startsWith(state.activeMonthKey)
            "Son 30 Gün" -> isDateWithinDays(receipt.receiptDate, 30)
            "Geçen Ay" -> isDateInPreviousMonth(receipt.receiptDate)
            "Özel" -> {
                val rDate = receipt.receiptDate
                (customStartDate.isBlank() || rDate >= customStartDate) &&
                        (customEndDate.isBlank() || rDate <= customEndDate)
            }
            else -> true
        }

        // 5. Expense Amount matching
        val matchesAmount = when (selectedAmountPreset) {
            "Tümü" -> true
            "0 - 100 ₺" -> receipt.totalAmount in 0.0..100.0
            "100 - 500 ₺" -> receipt.totalAmount in 100.0..500.0
            "500 - 1.500 ₺" -> receipt.totalAmount in 500.0..1500.0
            "1.500 - 5.000 ₺" -> receipt.totalAmount in 1500.0..5000.0
            "5.000 ₺ +" -> receipt.totalAmount >= 5000.0
            "Özel" -> {
                val minOk = minAmountVal == null || receipt.totalAmount >= minAmountVal
                val maxOk = maxAmountVal == null || receipt.totalAmount <= maxAmountVal
                minOk && maxOk
            }
            else -> true
        }

        matchesSearch && matchesMember && matchesCategory && matchesDate && matchesAmount
    }.let { list ->
        // 6. Sorting
        when (selectedSortOption) {
            ExpenseSortOption.NEWEST -> list.sortedWith(compareByDescending<ExpenseReceipt> { it.receiptDate }.thenByDescending { it.id })
            ExpenseSortOption.OLDEST -> list.sortedWith(compareBy<ExpenseReceipt> { it.receiptDate }.thenBy { it.id })
            ExpenseSortOption.HIGHEST_AMOUNT -> list.sortedByDescending { it.totalAmount }
            ExpenseSortOption.LOWEST_AMOUNT -> list.sortedBy { it.totalAmount }
        }
    }

    val totalFilteredSpent = filteredReceipts.sumOf { it.totalAmount }
    val totalFilteredVat = filteredReceipts.sumOf { it.vatAmount }
    val averageReceiptAmount = if (filteredReceipts.isNotEmpty()) totalFilteredSpent / filteredReceipts.size else 0.0

    val resetAllFilters = {
        searchQuery = ""
        selectedCategoryFilter = "Tümü"
        selectedMemberFilter = "Tümü"
        selectedDatePreset = "Tümü"
        customStartDate = ""
        customEndDate = ""
        showCustomDateFields = false
        selectedAmountPreset = "Tümü"
        minAmountInput = ""
        maxAmountInput = ""
        showCustomAmountFields = false
        selectedSortOption = ExpenseSortOption.NEWEST
    }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("expenses_screen_container")
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxWidth()
                .widthIn(max = 1100.dp)
                .align(Alignment.TopCenter),
            contentPadding = PaddingValues(bottom = 80.dp)
        ) {
            // Title & Description
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    Text(
                        text = strings.navExpenses,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "Tarih aralığı, harcama tutarı, mağaza veya aile bireyine göre akıllı filtreleme",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Quick Actions: Fiş Tarama & Manuel Fiş Girişi (Sayfa Başı)
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    horizontalArrangement = Arrangement.spacedBy(10.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Fiş Tarama (OCR) Button
                    Surface(
                        onClick = onScanReceiptClick,
                        shape = RoundedCornerShape(14.dp),
                        color = Color(0xFF00695C),
                        shadowElevation = 2.dp,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("expenses_scan_receipt_ocr_button")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CameraAlt,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = strings.scanReceipt,
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                maxLines = 1,
                                softWrap = false,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }

                    // Manuel Fiş Girişi Button
                    Surface(
                        onClick = onManualExpenseClick,
                        shape = RoundedCornerShape(14.dp),
                        color = MaterialTheme.colorScheme.secondary,
                        shadowElevation = 2.dp,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("expenses_manual_expense_entry_button")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxSize()
                                .padding(horizontal = 10.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSecondary,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = strings.manualExpense,
                                color = MaterialTheme.colorScheme.onSecondary,
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.5.sp,
                                maxLines = 1,
                                softWrap = false,
                                overflow = TextOverflow.Ellipsis
                            )
                        }
                    }
                }
            }

            // Summary KPI Banner
            item {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(18.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Filtrelenen Harcamalar",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Box(
                                        modifier = Modifier
                                            .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.15f), RoundedCornerShape(6.dp))
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = "${filteredReceipts.size} Fiş",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = String.format(Locale.getDefault(), "%,.2f ₺", totalFilteredSpent),
                                    fontSize = 22.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "Ödenen KDV",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = String.format(Locale.getDefault(), "%,.2f ₺", totalFilteredVat),
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color(0xFF1565C0)
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Ort. Fiş: " + String.format(Locale.getDefault(), "%,.0f ₺", averageReceiptAmount),
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }

            // 1. Search Bar & Filter Toggle Button
            item {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = { searchQuery = it },
                        modifier = Modifier
                            .weight(1f)
                            .testTag("expenses_search_bar"),
                        placeholder = { Text(strings.searchPlaceholder, fontSize = 12.sp, maxLines = 1) },
                        leadingIcon = { Icon(Icons.Default.Search, contentDescription = "Ara", tint = MaterialTheme.colorScheme.primary) },
                        trailingIcon = {
                            if (searchQuery.isNotEmpty()) {
                                IconButton(onClick = { searchQuery = "" }) {
                                    Icon(Icons.Default.Clear, contentDescription = "Temizle", modifier = Modifier.size(18.dp))
                                }
                            }
                        },
                        shape = RoundedCornerShape(14.dp),
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = MaterialTheme.colorScheme.primary,
                            unfocusedBorderColor = MaterialTheme.colorScheme.outlineVariant
                        )
                    )

                    Spacer(modifier = Modifier.width(8.dp))

                    // Advanced Filter Toggle Button with Badge
                    Surface(
                        modifier = Modifier
                            .size(52.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .clickable { isFilterPanelExpanded = !isFilterPanelExpanded }
                            .testTag("toggle_filters_button"),
                        color = if (isFilterPanelExpanded || activeFiltersCount > 0) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surfaceVariant,
                        shape = RoundedCornerShape(14.dp)
                    ) {
                        Box(contentAlignment = Alignment.Center) {
                            if (activeFiltersCount > 0) {
                                BadgedBox(
                                    badge = {
                                        Badge(
                                            containerColor = MaterialTheme.colorScheme.primary,
                                            contentColor = Color.White
                                        ) {
                                            Text("$activeFiltersCount", fontSize = 9.sp)
                                        }
                                    }
                                ) {
                                    Icon(
                                        imageVector = if (isFilterPanelExpanded) Icons.Default.Tune else Icons.Default.FilterList,
                                        contentDescription = "Filtreler",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            } else {
                                Icon(
                                    imageVector = if (isFilterPanelExpanded) Icons.Default.Tune else Icons.Default.FilterList,
                                    contentDescription = "Filtreler",
                                    tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
                    }
                }
            }

            // Active Filters Ribbon (if any active filter)
            if (activeFiltersCount > 0) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 2.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.FilterAlt, contentDescription = null, modifier = Modifier.size(14.dp), tint = MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "$activeFiltersCount Aktif Filtre",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }

                            TextButton(
                                onClick = resetAllFilters,
                                contentPadding = PaddingValues(horizontal = 6.dp, vertical = 0.dp),
                                modifier = Modifier.height(28.dp).testTag("clear_all_filters_button")
                            ) {
                                Icon(Icons.Default.RestartAlt, contentDescription = null, modifier = Modifier.size(13.dp))
                                Spacer(modifier = Modifier.width(3.dp))
                                Text("Tümünü Temizle", fontSize = 11.sp, fontWeight = FontWeight.SemiBold)
                            }
                        }

                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp),
                            contentPadding = PaddingValues(vertical = 2.dp)
                        ) {
                            if (selectedDatePreset != "Tümü") {
                                item {
                                    val dateLabel = if (selectedDatePreset == "Özel") "$customStartDate ~ $customEndDate" else selectedDatePreset
                                    ActiveFilterChip(
                                        label = "📅 $dateLabel",
                                        onRemove = {
                                            selectedDatePreset = "Tümü"
                                            customStartDate = ""
                                            customEndDate = ""
                                            showCustomDateFields = false
                                        }
                                    )
                                }
                            }

                            if (selectedAmountPreset != "Tümü") {
                                item {
                                    val amountLabel = if (selectedAmountPreset == "Özel") {
                                        "${minAmountInput.ifBlank { "0" }} - ${maxAmountInput.ifBlank { "∞" }} ₺"
                                    } else selectedAmountPreset
                                    ActiveFilterChip(
                                        label = "💰 $amountLabel",
                                        onRemove = {
                                            selectedAmountPreset = "Tümü"
                                            minAmountInput = ""
                                            maxAmountInput = ""
                                            showCustomAmountFields = false
                                        }
                                    )
                                }
                            }

                            if (selectedMemberFilter != "Tümü") {
                                item {
                                    ActiveFilterChip(
                                        label = "👤 $selectedMemberFilter",
                                        onRemove = { selectedMemberFilter = "Tümü" }
                                    )
                                }
                            }

                            if (selectedCategoryFilter != "Tümü") {
                                item {
                                    ActiveFilterChip(
                                        label = "🏷️ $selectedCategoryFilter",
                                        onRemove = { selectedCategoryFilter = "Tümü" }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // Quick Date Filter Ribbon (Always accessible inline)
            item {
                Column(modifier = Modifier.padding(top = 4.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 2.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.DateRange,
                                contentDescription = null,
                                modifier = Modifier.size(15.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = strings.filterByDateRange,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        Text(
                            text = if (isFilterPanelExpanded) "Daha Az Göster" else "Tüm Filtreler & Sıralama",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.clickable { isFilterPanelExpanded = !isFilterPanelExpanded }
                        )
                    }

                    val datePresets = listOf(
                        "Tümü" to strings.filterAll,
                        "Bugün" to "Bugün",
                        "Son 7 Gün" to strings.filterLast7Days,
                        "Bu Ay" to strings.filterThisMonth,
                        "Son 30 Gün" to strings.filterLast30Days,
                        "Geçen Ay" to "Geçen Ay",
                        "Özel" to strings.filterCustom
                    )

                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        items(datePresets) { (key, label) ->
                            val isSelected = selectedDatePreset == key
                            FilterChip(
                                selected = isSelected,
                                onClick = {
                                    selectedDatePreset = key
                                    showCustomDateFields = (key == "Özel")
                                    if (key == "Özel" && !isFilterPanelExpanded) {
                                        isFilterPanelExpanded = true
                                    }
                                },
                                label = { Text(label, fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                    selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                                )
                            )
                        }
                    }
                }
            }

            // Comprehensive Expandable Filter Panel
            item {
                AnimatedVisibility(
                    visible = isFilterPanelExpanded,
                    enter = expandVertically() + fadeIn(),
                    exit = shrinkVertically() + fadeOut()
                ) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                            .testTag("expanded_filter_panel"),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            // Section Header
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(Icons.Default.Tune, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(18.dp))
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = strings.advancedFilters,
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }

                                TextButton(
                                    onClick = resetAllFilters,
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 0.dp),
                                    modifier = Modifier.height(28.dp)
                                ) {
                                    Text(strings.clearFilters, fontSize = 11.sp)
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            // 1. EXPENSE AMOUNT FILTER SECTION
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Payments, contentDescription = null, modifier = Modifier.size(16.dp), tint = Color(0xFF2E7D32))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = strings.filterByAmount,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))

                            val amountPresets = listOf(
                                "Tümü",
                                "0 - 100 ₺",
                                "100 - 500 ₺",
                                "500 - 1.500 ₺",
                                "1.500 - 5.000 ₺",
                                "5.000 ₺ +",
                                "Özel"
                            )

                            LazyRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                items(amountPresets) { preset ->
                                    val isSelected = selectedAmountPreset == preset
                                    FilterChip(
                                        selected = isSelected,
                                        onClick = {
                                            selectedAmountPreset = preset
                                            showCustomAmountFields = (preset == "Özel")
                                        },
                                        label = { Text(preset, fontSize = 10.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = Color(0xFFE8F5E9),
                                            selectedLabelColor = Color(0xFF1B5E20)
                                        )
                                    )
                                }
                            }

                            // Custom Min & Max Amount Inputs
                            AnimatedVisibility(visible = showCustomAmountFields || selectedAmountPreset == "Özel") {
                                Row(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(top = 8.dp),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    OutlinedTextField(
                                        value = minAmountInput,
                                        onValueChange = { minAmountInput = it.filter { ch -> ch.isDigit() || ch == '.' } },
                                        label = { Text(strings.minAmount, fontSize = 10.sp) },
                                        placeholder = { Text("0") },
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                        singleLine = true,
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier.weight(1f).testTag("filter_min_amount_input")
                                    )

                                    Text("-", fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)

                                    OutlinedTextField(
                                        value = maxAmountInput,
                                        onValueChange = { maxAmountInput = it.filter { ch -> ch.isDigit() || ch == '.' } },
                                        label = { Text(strings.maxAmount, fontSize = 10.sp) },
                                        placeholder = { Text("10000") },
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                        singleLine = true,
                                        shape = RoundedCornerShape(10.dp),
                                        modifier = Modifier.weight(1f).testTag("filter_max_amount_input")
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            // 2. CUSTOM DATE RANGE INPUTS (When Custom Date is Active)
                            AnimatedVisibility(visible = showCustomDateFields || selectedDatePreset == "Özel") {
                                Column {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(Icons.Default.CalendarMonth, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = "Özel Tarih Aralığı Seçimi",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                    Spacer(modifier = Modifier.height(6.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        OutlinedTextField(
                                            value = customStartDate,
                                            onValueChange = { customStartDate = it },
                                            label = { Text(strings.startDate, fontSize = 10.sp) },
                                            placeholder = { Text("YYYY-MM-DD") },
                                            singleLine = true,
                                            shape = RoundedCornerShape(10.dp),
                                            modifier = Modifier.weight(1f).testTag("filter_start_date_input")
                                        )

                                        OutlinedTextField(
                                            value = customEndDate,
                                            onValueChange = { customEndDate = it },
                                            label = { Text(strings.endDate, fontSize = 10.sp) },
                                            placeholder = { Text("YYYY-MM-DD") },
                                            singleLine = true,
                                            shape = RoundedCornerShape(10.dp),
                                            modifier = Modifier.weight(1f).testTag("filter_end_date_input")
                                        )
                                    }

                                    // Quick Date Helper Buttons
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(top = 6.dp),
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        OutlinedButton(
                                            onClick = {
                                                customStartDate = getStartOfWeek()
                                                customEndDate = todayDateStr
                                            },
                                            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
                                            modifier = Modifier.height(28.dp)
                                        ) {
                                            Text("Bu Hafta", fontSize = 10.sp)
                                        }

                                        OutlinedButton(
                                            onClick = {
                                                customStartDate = getStartOfMonth()
                                                customEndDate = todayDateStr
                                            },
                                            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
                                            modifier = Modifier.height(28.dp)
                                        ) {
                                            Text("Bu Ay", fontSize = 10.sp)
                                        }

                                        OutlinedButton(
                                            onClick = {
                                                val (s, e) = getPreviousMonthRange()
                                                customStartDate = s
                                                customEndDate = e
                                            },
                                            contentPadding = PaddingValues(horizontal = 6.dp, vertical = 2.dp),
                                            modifier = Modifier.height(28.dp)
                                        ) {
                                            Text("Geçen Ay", fontSize = 10.sp)
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(14.dp))
                                }
                            }

                            // 3. SORTING OPTIONS
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(Icons.Default.Sort, contentDescription = null, modifier = Modifier.size(16.dp), tint = MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = strings.sortBy,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                            Spacer(modifier = Modifier.height(6.dp))

                            val sortOptions = listOf(
                                ExpenseSortOption.NEWEST to strings.sortNewestFirst,
                                ExpenseSortOption.OLDEST to strings.sortOldestFirst,
                                ExpenseSortOption.HIGHEST_AMOUNT to strings.sortHighestAmount,
                                ExpenseSortOption.LOWEST_AMOUNT to strings.sortLowestAmount
                            )

                            LazyRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                items(sortOptions) { (option, label) ->
                                    val isSelected = selectedSortOption == option
                                    FilterChip(
                                        selected = isSelected,
                                        onClick = { selectedSortOption = option },
                                        label = { Text(label, fontSize = 10.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                                        leadingIcon = {
                                            when (option) {
                                                ExpenseSortOption.NEWEST -> Icon(Icons.Default.DateRange, contentDescription = null, modifier = Modifier.size(12.dp))
                                                ExpenseSortOption.OLDEST -> Icon(Icons.Default.CalendarToday, contentDescription = null, modifier = Modifier.size(12.dp))
                                                ExpenseSortOption.HIGHEST_AMOUNT -> Icon(Icons.Default.ArrowDownward, contentDescription = null, modifier = Modifier.size(12.dp))
                                                ExpenseSortOption.LOWEST_AMOUNT -> Icon(Icons.Default.ArrowUpward, contentDescription = null, modifier = Modifier.size(12.dp))
                                            }
                                        }
                                    )
                                }
                            }
                        }
                    }
                }
            }

            // 2. Family Member Filter Row
            if (state.members.isNotEmpty()) {
                item {
                    Column(modifier = Modifier.padding(top = 2.dp)) {
                        Row(
                            modifier = Modifier.padding(horizontal = 16.dp, vertical = 2.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.Group,
                                contentDescription = null,
                                modifier = Modifier.size(15.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Aile Bireyine Göre Filtrele",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        LazyRow(
                            modifier = Modifier.fillMaxWidth(),
                            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // "Tümü" chip
                            item {
                                val isAllSelected = selectedMemberFilter == "Tümü"
                                FilterChip(
                                    selected = isAllSelected,
                                    onClick = { selectedMemberFilter = "Tümü" },
                                    label = { Text("Tüm Aile", fontSize = 11.sp, fontWeight = if (isAllSelected) FontWeight.Bold else FontWeight.Normal) },
                                    leadingIcon = {
                                        Icon(Icons.Default.Group, contentDescription = null, modifier = Modifier.size(14.dp))
                                    },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                        selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                )
                            }

                            // Each member chip
                            items(state.members) { member ->
                                val isSelected = selectedMemberFilter.equals(member.name, ignoreCase = true)
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { selectedMemberFilter = member.name },
                                    label = { Text(member.name, fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                                    leadingIcon = {
                                        Box(
                                            modifier = Modifier
                                                .size(16.dp)
                                                .background(Color(member.avatarColorHex), CircleShape),
                                            contentAlignment = Alignment.Center
                                        ) {
                                            Text(
                                                text = member.name.take(1).uppercase(),
                                                color = Color.White,
                                                fontSize = 9.sp,
                                                fontWeight = FontWeight.Bold
                                            )
                                        }
                                    },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = Color(member.avatarColorHex).copy(alpha = 0.2f),
                                        selectedLabelColor = MaterialTheme.colorScheme.onSurface
                                    )
                                )
                            }
                        }
                    }
                }
            }

            // 3. Category Filter Chips
            item {
                Column(modifier = Modifier.padding(top = 2.dp)) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 2.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                Icons.Default.FilterAlt,
                                contentDescription = null,
                                modifier = Modifier.size(15.dp),
                                tint = MaterialTheme.colorScheme.secondary
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = strings.category,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }

                        TextButton(
                            onClick = { showCategoryManageDialog = true },
                            modifier = Modifier.testTag("manage_categories_button")
                        ) {
                            Icon(
                                Icons.Default.Tune,
                                contentDescription = null,
                                modifier = Modifier.size(13.dp),
                                tint = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Kategorileri Düzenle",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    LazyRow(
                        modifier = Modifier.fillMaxWidth(),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 4.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        // "Tümü" chip
                        item {
                            val isAllSelected = selectedCategoryFilter == "Tümü"
                            FilterChip(
                                selected = isAllSelected,
                                onClick = { selectedCategoryFilter = "Tümü" },
                                label = { Text("Tümü", fontSize = 11.sp, fontWeight = if (isAllSelected) FontWeight.Bold else FontWeight.Normal) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                                    selectedLabelColor = MaterialTheme.colorScheme.onSecondaryContainer
                                )
                            )
                        }

                        // Available categories from tag list or strings
                        if (state.allCategories.isNotEmpty()) {
                            items(state.allCategories, key = { it.id }) { catTag ->
                                val isSelected = selectedCategoryFilter.equals(catTag.name, ignoreCase = true)
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { selectedCategoryFilter = catTag.name },
                                    label = {
                                        Row(verticalAlignment = Alignment.CenterVertically) {
                                            Text(text = catTag.icon, fontSize = 12.sp)
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Text(
                                                text = catTag.name,
                                                fontSize = 11.sp,
                                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                            )
                                        }
                                    },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = Color(catTag.colorHex).copy(alpha = 0.25f),
                                        selectedLabelColor = MaterialTheme.colorScheme.onSurface
                                    )
                                )
                            }
                        } else {
                            val defaultCategories = CategoryClassifier.ALL_CATEGORIES
                            items(defaultCategories) { cat ->
                                val isSelected = selectedCategoryFilter == cat
                                FilterChip(
                                    selected = isSelected,
                                    onClick = { selectedCategoryFilter = cat },
                                    label = { Text(cat, fontSize = 11.sp, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal) },
                                    colors = FilterChipDefaults.filterChipColors(
                                        selectedContainerColor = MaterialTheme.colorScheme.secondaryContainer,
                                        selectedLabelColor = MaterialTheme.colorScheme.onSecondaryContainer
                                    )
                                )
                            }
                        }
                    }
                }
            }

            item {
                Spacer(modifier = Modifier.height(6.dp))
            }

            // Receipts list
            if (filteredReceipts.isEmpty()) {
                item {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f))
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(32.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.ReceiptLong,
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                                modifier = Modifier.size(48.dp)
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            Text(
                                text = "Seçilen filtrelere uygun harcama kaydı bulunamadı.",
                                color = MaterialTheme.colorScheme.onSurface,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Arama kriterlerinizi, tarih aralığını veya tutar filtresini değiştirmeyi deneyebilirsiniz.",
                                color = MaterialTheme.colorScheme.onSurfaceVariant,
                                fontSize = 12.sp,
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                            Spacer(modifier = Modifier.height(16.dp))
                            OutlinedButton(
                                onClick = resetAllFilters,
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(Icons.Default.RestartAlt, contentDescription = null, modifier = Modifier.size(16.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text("Filtreleri Sıfırla")
                            }
                        }
                    }
                }
            } else {
                items(filteredReceipts, key = { it.id }) { receipt ->
                    ReceiptItemCard(
                        receipt = receipt,
                        onClick = { onReceiptClick(receipt) }
                    )
                }
            }
        }

        if (showCategoryManageDialog) {
            CategoryManagementDialog(
                categories = state.allCategories,
                strings = strings,
                onDismiss = { showCategoryManageDialog = false },
                onAddCategory = onAddCategory,
                onUpdateCategory = onUpdateCategory,
                onDeleteCategory = onDeleteCategory
            )
        }
    }
}

@Composable
private fun ActiveFilterChip(
    label: String,
    onRemove: () -> Unit
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = MaterialTheme.colorScheme.primaryContainer,
        modifier = Modifier.padding(vertical = 2.dp)
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = label,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.width(4.dp))
            Icon(
                imageVector = Icons.Default.Close,
                contentDescription = "Kaldır",
                tint = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier
                    .size(14.dp)
                    .clickable { onRemove() }
            )
        }
    }
}

private fun isDateWithinDays(dateStr: String, days: Int): Boolean {
    return try {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val receiptDate = sdf.parse(dateStr) ?: return false
        val cal = Calendar.getInstance()
        cal.add(Calendar.DAY_OF_YEAR, -days)
        val cutoff = cal.time
        receiptDate.after(cutoff) || receiptDate == cutoff
    } catch (e: Exception) {
        true
    }
}

private fun isDateInPreviousMonth(dateStr: String): Boolean {
    return try {
        val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
        val receiptDate = sdf.parse(dateStr) ?: return false
        val cal = Calendar.getInstance()
        cal.add(Calendar.MONTH, -1)
        val targetMonth = cal.get(Calendar.MONTH)
        val targetYear = cal.get(Calendar.YEAR)

        val rCal = Calendar.getInstance()
        rCal.time = receiptDate
        rCal.get(Calendar.MONTH) == targetMonth && rCal.get(Calendar.YEAR) == targetYear
    } catch (e: Exception) {
        false
    }
}

private fun getStartOfWeek(): String {
    val cal = Calendar.getInstance()
    cal.firstDayOfWeek = Calendar.MONDAY
    cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY)
    return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.time)
}

private fun getStartOfMonth(): String {
    val cal = Calendar.getInstance()
    cal.set(Calendar.DAY_OF_MONTH, 1)
    return SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.time)
}

private fun getPreviousMonthRange(): Pair<String, String> {
    val cal = Calendar.getInstance()
    cal.add(Calendar.MONTH, -1)
    cal.set(Calendar.DAY_OF_MONTH, 1)
    val start = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.time)

    cal.set(Calendar.DAY_OF_MONTH, cal.getActualMaximum(Calendar.DAY_OF_MONTH))
    val end = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(cal.time)
    return Pair(start, end)
}

