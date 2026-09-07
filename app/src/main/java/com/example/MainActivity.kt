package com.example

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.BarChart
import androidx.compose.material.icons.filled.CameraAlt
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.NavigationRailItem
import androidx.compose.material3.NavigationRailItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FamilyMember
import com.example.data.model.ExpenseReceipt
import com.example.data.model.FixedExpense
import com.example.data.model.IncomeSource
import com.example.data.model.ReceiptItem
import com.example.ui.components.AddFamilyMemberDialog
import com.example.ui.components.CameraScannerDialog
import com.example.ui.components.EditFamilyMemberDialog
import com.example.ui.components.FixedExpenseDialog
import com.example.ui.components.IncomeDialog
import com.example.ui.components.ManualExpenseDialog
import com.example.ui.components.ReceiptDetailDialog
import com.example.ui.components.ReceiptOcrReviewDialog
import com.example.ui.i18n.StringsProvider
import com.example.ui.screens.ExpensesScreen
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.ReceiptScannerScreen
import com.example.ui.screens.SettingsScreen
import com.example.ui.screens.ShoppingListScreen
import com.example.ui.screens.StatisticsScreen
import com.example.ui.theme.MyApplicationTheme
import com.example.ui.viewmodel.FamilyBudgetViewModel

sealed class AppDestination(val route: String, val titleTr: String, val icon: ImageVector) {
    object Home : AppDestination("home", "Özet", Icons.Default.Home)
    object Expenses : AppDestination("expenses", "Harcamalar", Icons.Default.ReceiptLong)
    object Shopping : AppDestination("shopping", "Alışveriş", Icons.Default.ShoppingCart)
    object Scanner : AppDestination("scanner", "Fiş Tara", Icons.Default.CameraAlt)
    object Statistics : AppDestination("statistics", "İstatistik", Icons.Default.BarChart)
    object Settings : AppDestination("settings", "Aile & Ayar", Icons.Default.Group)
}

class MainActivity : ComponentActivity() {
    private val viewModel: FamilyBudgetViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            val uiState by viewModel.uiState.collectAsState()
            MyApplicationTheme(themeMode = uiState.themeMode) {
                FamilyBudgetApp(viewModel = viewModel)
            }
        }
    }
}

@Composable
fun FamilyBudgetApp(viewModel: FamilyBudgetViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val strings = remember(uiState.currentLanguage) { StringsProvider(uiState.currentLanguage) }

    var currentScreen by remember { mutableStateOf<AppDestination>(AppDestination.Home) }

    // Dialog state holders
    var showCameraScanner by remember { mutableStateOf(false) }
    var showManualExpenseDialog by remember { mutableStateOf(false) }
    var showAddMemberDialog by remember { mutableStateOf(false) }
    var editingFamilyMember by remember { mutableStateOf<FamilyMember?>(null) }
    var showIncomeDialog by remember { mutableStateOf(false) }
    var editingIncomeSource by remember { mutableStateOf<IncomeSource?>(null) }
    var showFixedExpenseDialog by remember { mutableStateOf(false) }
    var editingFixedExpense by remember { mutableStateOf<FixedExpense?>(null) }
    var selectedReceiptForDetail by remember { mutableStateOf<ExpenseReceipt?>(null) }
    var selectedReceiptItems by remember { mutableStateOf<List<ReceiptItem>>(emptyList()) }

    val destinations = listOf(
        AppDestination.Home,
        AppDestination.Expenses,
        AppDestination.Shopping,
        AppDestination.Statistics,
        AppDestination.Settings
    )

    @Composable
    fun ScreenContent() {
        when (currentScreen) {
            AppDestination.Home -> {
                HomeScreen(
                    state = uiState,
                    strings = strings,
                    onLanguageChange = { viewModel.setLanguage(it) },
                    onMonthChange = { viewModel.setActiveMonth(it) },
                    onScanReceiptClick = { currentScreen = AppDestination.Scanner },
                    onManualExpenseClick = { showManualExpenseDialog = true },
                    onPresetSelected = { viewModel.selectPresetReceipt(it) },
                    onReceiptClick = { receipt ->
                        selectedReceiptForDetail = receipt
                        viewModel.loadReceiptItems(receipt.id) { items ->
                            selectedReceiptItems = items.ifEmpty {
                                listOf(
                                    ReceiptItem(
                                        receiptId = receipt.id,
                                        productName = receipt.merchantName,
                                        category = receipt.category,
                                        quantity = 1.0,
                                        unitPrice = receipt.totalAmount,
                                        totalPrice = receipt.totalAmount,
                                        vatRate = receipt.vatRate,
                                        date = receipt.receiptDate
                                    )
                                )
                            }
                        }
                    },
                    onNavigateToFamily = { currentScreen = AppDestination.Settings },
                    onNavigateToStatistics = { currentScreen = AppDestination.Statistics },
                    onNavigateToShopping = { currentScreen = AppDestination.Shopping }
                )
            }
            AppDestination.Shopping -> {
                ShoppingListScreen(
                    uiState = uiState,
                    viewModel = viewModel,
                    strings = strings
                )
            }
            AppDestination.Expenses -> {
                ExpensesScreen(
                    state = uiState,
                    strings = strings,
                    onScanReceiptClick = { currentScreen = AppDestination.Scanner },
                    onManualExpenseClick = { showManualExpenseDialog = true },
                    onReceiptClick = { receipt ->
                        selectedReceiptForDetail = receipt
                        viewModel.loadReceiptItems(receipt.id) { items ->
                            selectedReceiptItems = items.ifEmpty {
                                listOf(
                                    ReceiptItem(
                                        receiptId = receipt.id,
                                        productName = receipt.merchantName,
                                        category = receipt.category,
                                        quantity = 1.0,
                                        unitPrice = receipt.totalAmount,
                                        totalPrice = receipt.totalAmount,
                                        vatRate = receipt.vatRate,
                                        date = receipt.receiptDate
                                    )
                                )
                            }
                        }
                    },
                    onAddCategory = { name, icon, colorHex ->
                        viewModel.addCustomCategory(name, icon, colorHex)
                    },
                    onUpdateCategory = { cat ->
                        viewModel.updateCustomCategory(cat)
                    },
                    onDeleteCategory = { cat ->
                        viewModel.deleteCustomCategory(cat)
                    }
                )
            }
            AppDestination.Scanner -> {
                ReceiptScannerScreen(
                    state = uiState,
                    strings = strings,
                    onClose = { currentScreen = AppDestination.Home },
                    onImageCaptured = { bitmap ->
                        viewModel.scanReceiptBitmap(bitmap)
                    },
                    onPresetSelected = { preset ->
                        viewModel.selectPresetReceipt(preset)
                    },
                    onManualEntryClick = {
                        showManualExpenseDialog = true
                    }
                )
            }
            AppDestination.Statistics -> {
                StatisticsScreen(
                    state = uiState,
                    strings = strings,
                    onSelectTrendProduct = { viewModel.setSelectedTrendProduct(it) }
                )
            }
            AppDestination.Settings -> {
                SettingsScreen(
                    state = uiState,
                    strings = strings,
                    onLanguageChange = { viewModel.setLanguage(it) },
                    onThemeModeChange = { viewModel.setThemeMode(it) },
                    onNotificationsToggle = { viewModel.setNotificationsEnabled(it) },
                    onSendTestNotification = { viewModel.sendTestNotification() },
                    onAddMemberClick = { showAddMemberDialog = true },
                    onEditMemberClick = { member -> editingFamilyMember = member },
                    onSetCurrentUser = { viewModel.setCurrentFamilyMember(it) },
                    onApproveMember = { viewModel.approveFamilyMember(it) },
                    onDeleteMember = { viewModel.deleteFamilyMember(it) },
                    onReceiptClick = { receipt ->
                        selectedReceiptForDetail = receipt
                        viewModel.loadReceiptItems(receipt.id) { items ->
                            selectedReceiptItems = items.ifEmpty {
                                listOf(
                                    ReceiptItem(
                                        receiptId = receipt.id,
                                        productName = receipt.merchantName,
                                        category = receipt.category,
                                        quantity = 1.0,
                                        unitPrice = receipt.totalAmount,
                                        totalPrice = receipt.totalAmount,
                                        vatRate = receipt.vatRate,
                                        date = receipt.receiptDate
                                    )
                                )
                            }
                        }
                    },
                    onAddIncomeClick = {
                        editingIncomeSource = null
                        showIncomeDialog = true
                    },
                    onEditIncomeClick = { income ->
                        editingIncomeSource = income
                        showIncomeDialog = true
                    },
                    onToggleIncomeReceived = { id, received ->
                        viewModel.toggleIncomeReceived(id, received)
                    },
                    onDeleteIncome = { income ->
                        viewModel.deleteIncomeSource(income)
                    },
                    onAddFixedExpenseClick = {
                        editingFixedExpense = null
                        showFixedExpenseDialog = true
                    },
                    onEditFixedExpenseClick = { expense ->
                        editingFixedExpense = expense
                        showFixedExpenseDialog = true
                    },
                    onToggleFixedExpensePaid = { id, paid ->
                        viewModel.toggleFixedExpensePaid(id, paid)
                    },
                    onDeleteFixedExpense = { expense ->
                        viewModel.deleteFixedExpense(expense)
                    },
                    onUpdateWarningThreshold = { threshold ->
                        viewModel.setWarningThresholdPercent(threshold)
                    },
                    onUpdateCustomBudgetLimit = { limit ->
                        viewModel.setCustomBudgetLimit(limit)
                    },
                    onClearAllData = {
                        viewModel.clearAllData()
                    }
                )
            }
        }
    }

    BoxWithConstraints(
        modifier = Modifier
            .fillMaxSize()
            .testTag("app_root_layout")
    ) {
        val isWideScreen = maxWidth >= 720.dp

        if (isWideScreen) {
            // Tablet, Desktop, Web Responsive Row Layout with NavigationRail
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                NavigationRail(
                    containerColor = MaterialTheme.colorScheme.surface,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    header = {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier.padding(vertical = 16.dp)
                        ) {
                            Surface(
                                shape = CircleShape,
                                color = MaterialTheme.colorScheme.primaryContainer,
                                modifier = Modifier.size(44.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Icon(
                                        imageVector = Icons.Default.AccountBalanceWallet,
                                        contentDescription = "App Logo",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(24.dp)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Bütçe",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            FloatingActionButton(
                                onClick = { showCameraScanner = true },
                                containerColor = Color(0xFF00695C),
                                contentColor = Color.White,
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.size(44.dp).testTag("rail_scan_ocr")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CameraAlt,
                                    contentDescription = strings.scanReceipt,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                    },
                    modifier = Modifier
                        .fillMaxHeight()
                        .testTag("tablet_navigation_rail")
                ) {
                    Spacer(modifier = Modifier.height(8.dp))
                    destinations.forEach { dest ->
                        val isSelected = currentScreen == dest
                        val label = when (dest) {
                            AppDestination.Home -> strings.navOverview
                            AppDestination.Expenses -> strings.navExpenses
                            AppDestination.Shopping -> strings.navShopping
                            AppDestination.Scanner -> strings.scanReceipt
                            AppDestination.Statistics -> strings.navStatistics
                            AppDestination.Settings -> strings.navSettings
                        }

                        NavigationRailItem(
                            selected = isSelected,
                            onClick = { currentScreen = dest },
                            icon = {
                                Icon(
                                    imageVector = dest.icon,
                                    contentDescription = label,
                                    modifier = Modifier.size(22.dp)
                                )
                            },
                            label = {
                                Text(
                                    text = label,
                                    fontSize = 11.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = NavigationRailItemDefaults.colors(
                                selectedIconColor = MaterialTheme.colorScheme.primary,
                                selectedTextColor = MaterialTheme.colorScheme.primary,
                                indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
                            ),
                            modifier = Modifier.testTag("rail_nav_${dest.route}")
                        )
                    }
                }

                Box(
                    modifier = Modifier
                        .fillMaxHeight()
                        .weight(1f)
                        .padding(horizontal = 8.dp)
                ) {
                    ScreenContent()
                }
            }
        } else {
            // Mobile Compact Layout with Bottom NavigationBar
            Scaffold(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("app_scaffold"),
                bottomBar = {
                    NavigationBar(
                        containerColor = MaterialTheme.colorScheme.surface,
                        tonalElevation = 8.dp
                    ) {
                        destinations.forEach { dest ->
                            val isSelected = currentScreen == dest
                            val label = when (dest) {
                                AppDestination.Home -> strings.navOverview
                                AppDestination.Expenses -> strings.navExpenses
                                AppDestination.Shopping -> strings.navShopping
                                AppDestination.Scanner -> strings.scanReceipt
                                AppDestination.Statistics -> strings.navStatistics
                                AppDestination.Settings -> strings.navSettings
                            }

                            NavigationBarItem(
                                selected = isSelected,
                                onClick = { currentScreen = dest },
                                icon = {
                                    Icon(
                                        imageVector = dest.icon,
                                        contentDescription = label,
                                        modifier = Modifier.size(22.dp)
                                    )
                                },
                                label = {
                                    Text(
                                        text = label,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = MaterialTheme.colorScheme.primary,
                                    selectedTextColor = MaterialTheme.colorScheme.primary,
                                    indicatorColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.6f)
                                ),
                                modifier = Modifier.testTag("nav_${dest.route}")
                            )
                        }
                    }
                }
            ) { innerPadding ->
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(innerPadding)
                ) {
                    ScreenContent()
                }
            }
        }
    }

    // 1. Camera OCR Scanner Dialog
    if (showCameraScanner) {
        CameraScannerDialog(
            strings = strings,
            isScanning = uiState.isScanningOcr,
            onDismiss = { showCameraScanner = false },
            onImageCaptured = { bitmap ->
                showCameraScanner = false
                viewModel.scanReceiptBitmap(bitmap)
            },
            onPresetSelected = { preset ->
                showCameraScanner = false
                viewModel.selectPresetReceipt(preset)
            }
        )
    }

    // 2. OCR Scanned Review & Edit Dialog
    uiState.scannedReceiptForReview?.let { parsed ->
        ReceiptOcrReviewDialog(
            parsedReceipt = parsed,
            members = uiState.members,
            activeUser = uiState.currentUser,
            strings = strings,
            onDismiss = { viewModel.dismissReviewDialog() },
            onConfirm = { merchant, date, category, vatRate, vatAmount, total, memberId, memberName, pm, items ->
                viewModel.saveReceipt(
                    merchantName = merchant,
                    receiptDate = date,
                    category = category,
                    vatRate = vatRate,
                    vatAmount = vatAmount,
                    totalAmount = total,
                    familyMemberId = memberId,
                    familyMemberName = memberName,
                    paymentMethod = pm,
                    items = items
                )
                if (currentScreen == AppDestination.Scanner) {
                    currentScreen = AppDestination.Expenses
                }
            }
        )
    }

    // 3. Manual Expense Entry Dialog
    if (showManualExpenseDialog) {
        ManualExpenseDialog(
            members = uiState.members,
            activeUser = uiState.currentUser,
            strings = strings,
            availableCategories = uiState.allCategories,
            onDismiss = { showManualExpenseDialog = false },
            onSave = { merchant, date, category, vatRate, vatAmount, total, memberId, memberName, pm, items, note ->
                viewModel.saveReceipt(
                    merchantName = merchant,
                    receiptDate = date,
                    category = category,
                    vatRate = vatRate,
                    vatAmount = vatAmount,
                    totalAmount = total,
                    familyMemberId = memberId,
                    familyMemberName = memberName,
                    paymentMethod = pm,
                    items = items,
                    note = note
                )
                showManualExpenseDialog = false
            }
        )
    }

    // 4. Add Family Member Dialog
    if (showAddMemberDialog) {
        AddFamilyMemberDialog(
            strings = strings,
            onDismiss = { showAddMemberDialog = false },
            onAdd = { name, code, phone, role, salary, extra, approved ->
                viewModel.addFamilyMember(
                    name = name,
                    countryCode = code,
                    phone = phone,
                    role = role,
                    salary = salary,
                    additionalIncome = extra,
                    autoApprove = approved
                )
                showAddMemberDialog = false
            }
        )
    }

    // 4.5. Edit Family Member Dialog
    editingFamilyMember?.let { member ->
        EditFamilyMemberDialog(
            member = member,
            strings = strings,
            onDismiss = { editingFamilyMember = null },
            onSave = { updatedMember ->
                viewModel.updateFamilyMember(updatedMember)
                editingFamilyMember = null
            },
            onDelete = { memberToDelete ->
                viewModel.deleteFamilyMember(memberToDelete)
                editingFamilyMember = null
            }
        )
    }

    // 5. Income Source Dialog (Add / Edit)
    if (showIncomeDialog) {
        IncomeDialog(
            initialIncome = editingIncomeSource,
            members = uiState.members,
            onDismiss = {
                showIncomeDialog = false
                editingIncomeSource = null
            },
            onSave = { title, type, amount, depositDay, memberId, memberName, note ->
                val current = editingIncomeSource
                if (current == null) {
                    viewModel.addIncomeSource(
                        title = title,
                        type = type,
                        amount = amount,
                        depositDay = depositDay,
                        familyMemberId = memberId,
                        familyMemberName = memberName,
                        note = note
                    )
                } else {
                    viewModel.updateIncomeSource(
                        current.copy(
                            title = title,
                            type = type,
                            amount = amount,
                            depositDay = depositDay,
                            familyMemberId = memberId,
                            familyMemberName = memberName,
                            note = note
                        )
                    )
                }
            },
            onDelete = { income ->
                viewModel.deleteIncomeSource(income)
            }
        )
    }

    // 6. Fixed Expense Dialog (Add / Edit)
    if (showFixedExpenseDialog) {
        FixedExpenseDialog(
            initialExpense = editingFixedExpense,
            members = uiState.members,
            onDismiss = {
                showFixedExpenseDialog = false
                editingFixedExpense = null
            },
            onSave = { title, type, amount, dueDay, memberId, memberName, note ->
                val current = editingFixedExpense
                if (current == null) {
                    viewModel.addFixedExpense(
                        title = title,
                        type = type,
                        amount = amount,
                        dueDay = dueDay,
                        familyMemberId = memberId,
                        familyMemberName = memberName,
                        note = note
                    )
                } else {
                    viewModel.updateFixedExpense(
                        current.copy(
                            title = title,
                            type = type,
                            amount = amount,
                            dueDay = dueDay,
                            familyMemberId = memberId,
                            familyMemberName = memberName,
                            note = note
                        )
                    )
                }
            },
            onDelete = { expense ->
                viewModel.deleteFixedExpense(expense)
            }
        )
    }

    // 7. Receipt Detail Breakdown Dialog
    selectedReceiptForDetail?.let { receipt ->
        ReceiptDetailDialog(
            receipt = receipt,
            items = selectedReceiptItems,
            strings = strings,
            onDismiss = { selectedReceiptForDetail = null },
            onDelete = {
                viewModel.deleteReceipt(it)
                selectedReceiptForDetail = null
            }
        )
    }
}
