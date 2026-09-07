package com.example.ui.screens

import android.content.Intent
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ClearAll
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.KeyboardArrowUp
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.outlined.CheckCircle
import androidx.compose.material.icons.outlined.RadioButtonUnchecked
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.ui.window.PopupProperties
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.FamilyMember
import com.example.data.model.MarketProductTemplate
import com.example.data.model.ShoppingCatalog
import com.example.data.model.ShoppingItem
import com.example.ui.components.AddShoppingItemDialog
import com.example.ui.components.VoiceAddShoppingDialog
import com.example.ui.i18n.StringsProvider
import com.example.ui.viewmodel.BudgetUiState
import com.example.ui.viewmodel.FamilyBudgetViewModel
import kotlinx.coroutines.launch
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ShoppingListScreen(
    uiState: BudgetUiState,
    viewModel: FamilyBudgetViewModel,
    strings: StringsProvider
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    var selectedTabIndex by remember { mutableIntStateOf(0) } // 0: Alınacaklar, 1: Sepet (Alınanlar), 2: Tümü
    var showAddDialog by remember { mutableStateOf(false) }
    var showVoiceDialog by remember { mutableStateOf(false) }
    var itemToEdit by remember { mutableStateOf<ShoppingItem?>(null) }
    var showConvertDialog by remember { mutableStateOf(false) }
    var showClearPurchasedDialog by remember { mutableStateOf(false) }

    // Quick add bar states
    var quickItemName by remember { mutableStateOf("") }
    var quickCategory by remember { mutableStateOf("Süt & Kahvaltılık") }
    var quickQuantity by remember { mutableDoubleStateOf(1.0) }
    var quickUnit by remember { mutableStateOf("Adet") }
    var selectedGroupForCatalog by remember { mutableStateOf(ShoppingCatalog.GROUPS.first()) }

    val shoppingItems = uiState.shoppingItems
    val pendingItems = shoppingItems.filter { !it.isPurchased }
    val purchasedItems = shoppingItems.filter { it.isPurchased }

    val displayedItems = when (selectedTabIndex) {
        0 -> pendingItems
        1 -> purchasedItems
        else -> shoppingItems
    }

    val totalEstimatedPrice = shoppingItems.sumOf { it.estimatedPrice ?: 0.0 }
    val pendingEstimatedPrice = pendingItems.sumOf { it.estimatedPrice ?: 0.0 }
    val purchasedEstimatedPrice = purchasedItems.sumOf { it.estimatedPrice ?: 0.0 }

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = strings.shoppingListTitle,
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = "${pendingItems.size} ${strings.tabPending} • ${purchasedItems.size} ${strings.tabPurchased}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                },
                actions = {
                    // Voice Add Shopping Item button
                    IconButton(
                        onClick = { showVoiceDialog = true },
                        modifier = Modifier.testTag("voice_shopping_top_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Mic,
                            contentDescription = strings.voiceAddShoppingTitle,
                            tint = MaterialTheme.colorScheme.primary
                        )
                    }

                    // Share List button
                    if (shoppingItems.isNotEmpty()) {
                        IconButton(
                            onClick = {
                                val shareText = buildShoppingListShareText(shoppingItems)
                                val intent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_SUBJECT, "Aile Alışveriş Listesi")
                                    putExtra(Intent.EXTRA_TEXT, shareText)
                                }
                                context.startActivity(Intent.createChooser(intent, "Alışveriş Listesini Paylaş"))
                            },
                            modifier = Modifier.testTag("share_shopping_list_button")
                        ) {
                            Icon(Icons.Default.Share, contentDescription = "Listeyi Paylaş")
                        }
                    }

                    // Clear Purchased Button
                    if (purchasedItems.isNotEmpty()) {
                        IconButton(
                            onClick = { showClearPurchasedDialog = true },
                            modifier = Modifier.testTag("clear_purchased_button")
                        ) {
                            Icon(Icons.Default.ClearAll, contentDescription = strings.clearPurchasedItems)
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        floatingActionButton = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Voice Shopping Floating Button
                FloatingActionButton(
                    onClick = { showVoiceDialog = true },
                    containerColor = MaterialTheme.colorScheme.secondaryContainer,
                    contentColor = MaterialTheme.colorScheme.onSecondaryContainer,
                    modifier = Modifier.testTag("voice_shopping_fab")
                ) {
                    Icon(
                        imageVector = Icons.Default.Mic,
                        contentDescription = strings.voiceAddShoppingTitle
                    )
                }

                // Add Shopping Item FAB
                FloatingActionButton(
                    onClick = {
                        itemToEdit = null
                        showAddDialog = true
                    },
                    containerColor = MaterialTheme.colorScheme.primary,
                    contentColor = MaterialTheme.colorScheme.onPrimary,
                    modifier = Modifier.testTag("add_shopping_item_fab")
                ) {
                    Icon(Icons.Default.Add, contentDescription = strings.addShoppingItem)
                }
            }
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(bottom = 88.dp)
        ) {
            // 1. Live Summary Cards
            item {
                Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        SummaryStatBox(
                            title = "Alınacak",
                            value = "${pendingItems.size} ürün",
                            subValue = if (pendingEstimatedPrice > 0) String.format(Locale.getDefault(), "~%,.0f ₺", pendingEstimatedPrice) else null,
                            color = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.weight(1f)
                        )
                        SummaryStatBox(
                            title = "Sepette",
                            value = "${purchasedItems.size} ürün",
                            subValue = if (purchasedEstimatedPrice > 0) String.format(Locale.getDefault(), "%,.0f ₺", purchasedEstimatedPrice) else null,
                            color = Color(0xFF2E7D32),
                            modifier = Modifier.weight(1f)
                        )
                        SummaryStatBox(
                            title = "Tahmini Tutar",
                            value = String.format(Locale.getDefault(), "%,.0f ₺", totalEstimatedPrice),
                            subValue = "Toplam",
                            color = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.weight(1.1f)
                        )
                    }
                }
            }

            // 2. Convert to Receipt Banner (When items are in cart)
            if (purchasedItems.isNotEmpty()) {
                item {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFE8F5E9)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                            .testTag("convert_to_receipt_card")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    Icons.Default.Receipt,
                                    contentDescription = null,
                                    tint = Color(0xFF2E7D32),
                                    modifier = Modifier.size(28.dp)
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = "Alışveriş Tamamlandı mı?",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF1B5E20)
                                    )
                                    Text(
                                        text = "Sepetteki ${purchasedItems.size} ürünü otomatik fişe/harcamaya dönüştürün",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = Color(0xFF2E7D32)
                                    )
                                }
                            }
                            Button(
                                onClick = { showConvertDialog = true },
                                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("Fişe Aktar", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            // 3. Quick Shopping Entry
            item {
                var groupDropdownExpanded by remember { mutableStateOf(false) }
                var productDropdownExpanded by remember { mutableStateOf(false) }
                var unitDropdownExpanded by remember { mutableStateOf(false) }
                var selectedTemplate by remember { mutableStateOf<MarketProductTemplate?>(null) }

                Card(
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        // Section Header
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(text = "🛒", fontSize = 18.sp)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = strings.addShoppingItem,
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                            }

                            uiState.currentUser?.let { user ->
                                Surface(
                                    shape = RoundedCornerShape(8.dp),
                                    color = Color(user.avatarColorHex).copy(alpha = 0.15f)
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 3.dp)
                                    ) {
                                        Surface(
                                            shape = CircleShape,
                                            color = Color(user.avatarColorHex),
                                            modifier = Modifier.size(10.dp)
                                        ) {}
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = user.name,
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = Color(user.avatarColorHex)
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // 1. Ürün Grupları ve Ürünler ComboBox Satırı
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            // Ürün Grubu ComboBox (Dropdown)
                            Box(modifier = Modifier.weight(1f)) {
                                OutlinedCard(
                                    onClick = { groupDropdownExpanded = true },
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("product_group_combobox")
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 10.dp, vertical = 9.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text(text = selectedGroupForCatalog.icon, fontSize = 16.sp)
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Column {
                                                Text(
                                                    text = strings.productGroupComboBox,
                                                    style = MaterialTheme.typography.labelSmall,
                                                    fontSize = 9.sp,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                                Text(
                                                    text = selectedGroupForCatalog.nameTr,
                                                    style = MaterialTheme.typography.bodySmall,
                                                    fontWeight = FontWeight.Bold,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                            }
                                        }
                                        Icon(
                                            imageVector = if (groupDropdownExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.ArrowDropDown,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }

                                DropdownMenu(
                                    expanded = groupDropdownExpanded,
                                    onDismissRequest = { groupDropdownExpanded = false },
                                    properties = PopupProperties(focusable = true, dismissOnClickOutside = true, dismissOnBackPress = true),
                                    modifier = Modifier.heightIn(max = 400.dp)
                                ) {
                                    ShoppingCatalog.GROUPS.forEach { group ->
                                        DropdownMenuItem(
                                            leadingIcon = { Text(group.icon, fontSize = 18.sp) },
                                            text = {
                                                Text(
                                                    text = group.nameTr,
                                                    fontWeight = if (group.id == selectedGroupForCatalog.id) FontWeight.Bold else FontWeight.Normal
                                                )
                                            },
                                            onClick = {
                                                selectedGroupForCatalog = group
                                                quickCategory = group.nameTr
                                                selectedTemplate = null
                                                groupDropdownExpanded = false
                                            }
                                        )
                                    }
                                }
                            }

                            // Ürünler ComboBox (Dropdown - Popüler Ürünler)
                            Box(modifier = Modifier.weight(1.1f)) {
                                OutlinedCard(
                                    onClick = { productDropdownExpanded = true },
                                    shape = RoundedCornerShape(12.dp),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("products_combobox")
                                ) {
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(horizontal = 10.dp, vertical = 9.dp),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Text(text = selectedTemplate?.icon ?: "🛒", fontSize = 16.sp)
                                            Spacer(modifier = Modifier.width(6.dp))
                                            Column {
                                                Text(
                                                    text = strings.productsComboBox,
                                                    style = MaterialTheme.typography.labelSmall,
                                                    fontSize = 9.sp,
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                                Text(
                                                    text = selectedTemplate?.name ?: "Ürün Seçin...",
                                                    style = MaterialTheme.typography.bodySmall,
                                                    fontWeight = if (selectedTemplate != null) FontWeight.Bold else FontWeight.Normal,
                                                    maxLines = 1,
                                                    overflow = TextOverflow.Ellipsis
                                                )
                                            }
                                        }
                                        Icon(
                                            imageVector = if (productDropdownExpanded) Icons.Default.KeyboardArrowUp else Icons.Default.ArrowDropDown,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    }
                                }

                                DropdownMenu(
                                    expanded = productDropdownExpanded,
                                    onDismissRequest = { productDropdownExpanded = false },
                                    properties = PopupProperties(focusable = true, dismissOnClickOutside = true, dismissOnBackPress = true),
                                    modifier = Modifier.heightIn(max = 420.dp)
                                ) {
                                    selectedGroupForCatalog.popularProducts.forEach { template ->
                                        DropdownMenuItem(
                                            leadingIcon = { Text(template.icon, fontSize = 18.sp) },
                                            text = {
                                                Column {
                                                    Text(template.name, fontWeight = FontWeight.Medium)
                                                    template.estimatedPrice?.let { ep ->
                                                        Text(
                                                            text = "~$ep ₺ (${template.defaultQuantity.toInt()} ${template.defaultUnit})",
                                                            fontSize = 10.sp,
                                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                                        )
                                                    }
                                                }
                                            },
                                            onClick = {
                                                selectedTemplate = template
                                                quickItemName = template.name
                                                quickQuantity = template.defaultQuantity
                                                quickUnit = template.defaultUnit
                                                quickCategory = selectedGroupForCatalog.nameTr
                                                productDropdownExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        // 2. Ürün Adı Girişi
                        OutlinedTextField(
                            value = quickItemName,
                            onValueChange = {
                                quickItemName = it
                                if (it.isBlank()) selectedTemplate = null
                            },
                            label = { Text("Ürün Adı") },
                            placeholder = { Text(strings.searchOrAddHint, fontSize = 12.sp) },
                            singleLine = true,
                            trailingIcon = {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    if (quickItemName.isNotEmpty()) {
                                        IconButton(
                                            onClick = {
                                                quickItemName = ""
                                                selectedTemplate = null
                                            },
                                            modifier = Modifier.size(24.dp)
                                        ) {
                                            Icon(Icons.Default.Clear, contentDescription = "Temizle", modifier = Modifier.size(16.dp))
                                        }
                                    }
                                    IconButton(
                                        onClick = { showVoiceDialog = true },
                                        modifier = Modifier
                                            .size(28.dp)
                                            .testTag("quick_mic_button")
                                    ) {
                                        Icon(
                                            imageVector = Icons.Default.Mic,
                                            contentDescription = strings.voiceAddShoppingTitle,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }
                            },
                            shape = RoundedCornerShape(12.dp),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("quick_shopping_input"),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                            keyboardActions = KeyboardActions(onDone = {
                                if (quickItemName.isNotBlank()) {
                                    viewModel.addShoppingItem(
                                        name = quickItemName.trim(),
                                        category = quickCategory,
                                        quantity = quickQuantity,
                                        unit = quickUnit,
                                        estimatedPrice = selectedTemplate?.estimatedPrice,
                                        familyMemberId = uiState.currentUser?.id ?: 0L,
                                        familyMemberName = uiState.currentUser?.name ?: "Aile Üyesi",
                                        avatarColor = uiState.currentUser?.avatarColorHex ?: 0xFF00897B
                                    )
                                    scope.launch {
                                        snackbarHostState.showSnackbar("${quickItemName.trim()} listeye eklendi")
                                    }
                                    quickItemName = ""
                                    quickQuantity = 1.0
                                    selectedTemplate = null
                                }
                            })
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // 3. Altında Birim Girişi ve "+"
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            // Birim & Miktar Bölümü
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(8.dp),
                                modifier = Modifier.weight(1f)
                            ) {
                                // Miktar Stepper
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                    border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                                ) {
                                    Row(
                                        verticalAlignment = Alignment.CenterVertically,
                                        modifier = Modifier.padding(horizontal = 4.dp, vertical = 2.dp)
                                    ) {
                                        IconButton(
                                            onClick = {
                                                val step = if (quickUnit == "Gram") 50.0 else if (quickQuantity <= 1.0) 0.5 else 1.0
                                                if (quickQuantity > step) quickQuantity = (quickQuantity - step).coerceAtLeast(if (quickUnit == "Gram") 50.0 else 0.5)
                                            },
                                            modifier = Modifier.size(34.dp)
                                        ) {
                                            Text("-", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                        }
                                        Text(
                                            text = if (quickQuantity % 1.0 == 0.0) "${quickQuantity.toInt()}" else "$quickQuantity",
                                            style = MaterialTheme.typography.titleMedium,
                                            fontWeight = FontWeight.ExtraBold,
                                            modifier = Modifier.padding(horizontal = 4.dp)
                                        )
                                        IconButton(
                                            onClick = {
                                                val step = if (quickUnit == "Gram") 50.0 else if (quickQuantity < 1.0) 0.5 else 1.0
                                                quickQuantity += step
                                            },
                                            modifier = Modifier.size(34.dp)
                                        ) {
                                            Text("+", fontWeight = FontWeight.Bold, fontSize = 18.sp)
                                        }
                                    }
                                }

                                // Birim Seçimi ComboBox
                                Box {
                                    Surface(
                                        shape = RoundedCornerShape(12.dp),
                                        color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.45f),
                                        border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.primary.copy(alpha = 0.3f)),
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(12.dp))
                                            .clickable { unitDropdownExpanded = true }
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp)
                                        ) {
                                            Text(
                                                text = quickUnit,
                                                style = MaterialTheme.typography.labelLarge,
                                                fontWeight = FontWeight.Bold,
                                                color = MaterialTheme.colorScheme.primary
                                            )
                                            Spacer(modifier = Modifier.width(4.dp))
                                            Icon(
                                                imageVector = Icons.Default.ArrowDropDown,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.size(18.dp)
                                            )
                                        }
                                    }

                                    DropdownMenu(
                                        expanded = unitDropdownExpanded,
                                        onDismissRequest = { unitDropdownExpanded = false },
                                        properties = PopupProperties(focusable = true, dismissOnClickOutside = true, dismissOnBackPress = true)
                                    ) {
                                        listOf("Adet", "Kg", "Lt", "Gram", "Paket", "Koli", "Demet", "Kutu", "Rulo").forEach { u ->
                                            DropdownMenuItem(
                                                text = {
                                                    Text(
                                                        text = u,
                                                        fontWeight = if (quickUnit == u) FontWeight.Bold else FontWeight.Normal
                                                    )
                                                },
                                                onClick = {
                                                    quickUnit = u
                                                    unitDropdownExpanded = false
                                                }
                                            )
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            // "🎙️" Sesli Ekle Butonu
                            OutlinedButton(
                                onClick = { showVoiceDialog = true },
                                shape = RoundedCornerShape(12.dp),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 10.dp),
                                modifier = Modifier.testTag("quick_voice_action_button")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Mic,
                                    contentDescription = strings.voiceAddShoppingTitle,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }

                            Spacer(modifier = Modifier.width(6.dp))

                            // "+" Ekle Butonu
                            Button(
                                onClick = {
                                    if (quickItemName.isNotBlank()) {
                                        viewModel.addShoppingItem(
                                            name = quickItemName.trim(),
                                            category = quickCategory,
                                            quantity = quickQuantity,
                                            unit = quickUnit,
                                            estimatedPrice = selectedTemplate?.estimatedPrice,
                                            familyMemberId = uiState.currentUser?.id ?: 0L,
                                            familyMemberName = uiState.currentUser?.name ?: "Aile Üyesi",
                                            avatarColor = uiState.currentUser?.avatarColorHex ?: 0xFF00897B
                                        )
                                        scope.launch {
                                            snackbarHostState.showSnackbar("${quickItemName.trim()} listeye eklendi")
                                        }
                                        quickItemName = ""
                                        quickQuantity = 1.0
                                        selectedTemplate = null
                                    }
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary),
                                contentPadding = PaddingValues(horizontal = 20.dp, vertical = 10.dp),
                                modifier = Modifier.testTag("quick_add_button")
                            ) {
                                Text(
                                    text = "Ekle",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }
            }

            // 4. Tabs (Alınacaklar, Sepetim, Tümü)
            item {
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    containerColor = MaterialTheme.colorScheme.surface,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 6.dp)
                ) {
                    Tab(
                        selected = selectedTabIndex == 0,
                        onClick = { selectedTabIndex = 0 },
                        text = { Text("${strings.tabPending} (${pendingItems.size})", fontSize = 12.sp, fontWeight = if (selectedTabIndex == 0) FontWeight.Bold else FontWeight.Normal) }
                    )
                    Tab(
                        selected = selectedTabIndex == 1,
                        onClick = { selectedTabIndex = 1 },
                        text = { Text("${strings.tabPurchased} (${purchasedItems.size})", fontSize = 12.sp, fontWeight = if (selectedTabIndex == 1) FontWeight.Bold else FontWeight.Normal) }
                    )
                    Tab(
                        selected = selectedTabIndex == 2,
                        onClick = { selectedTabIndex = 2 },
                        text = { Text("${strings.tabAll} (${shoppingItems.size})", fontSize = 12.sp, fontWeight = if (selectedTabIndex == 2) FontWeight.Bold else FontWeight.Normal) }
                    )
                }
            }

            // 5. Shopping Items List
            if (displayedItems.isEmpty()) {
                item {
                    EmptyShoppingListView(
                        tabIndex = selectedTabIndex,
                        strings = strings,
                        onAddClick = { showAddDialog = true }
                    )
                }
            } else {
                items(displayedItems, key = { it.id }) { item ->
                    ShoppingItemCard(
                        item = item,
                        onTogglePurchased = { isPurchased ->
                            viewModel.toggleShoppingItemPurchased(item.id, isPurchased)
                        },
                        onEdit = {
                            itemToEdit = item
                            showAddDialog = true
                        },
                        onDelete = {
                            viewModel.deleteShoppingItem(item)
                        }
                    )
                }
            }
        }
    }

    // Dialogs
    if (showVoiceDialog) {
        VoiceAddShoppingDialog(
            strings = strings,
            members = uiState.members,
            currentUser = uiState.currentUser,
            onDismiss = { showVoiceDialog = false },
            onAddItems = { items, member ->
                viewModel.addMultipleVoiceShoppingItems(items, member)
                scope.launch {
                    val count = items.size
                    snackbarHostState.showSnackbar("$count ürün sesle alışveriş listesine/sepetine eklendi")
                }
            }
        )
    }

    if (showAddDialog) {
        AddShoppingItemDialog(
            strings = strings,
            members = uiState.members,
            currentUser = uiState.currentUser,
            initialItem = itemToEdit,
            onDismiss = {
                showAddDialog = false
                itemToEdit = null
            },
            onSave = { name, category, quantity, unit, estPrice, note, memberId, memberName, avatarColor ->
                if (itemToEdit != null) {
                    val updated = itemToEdit!!.copy(
                        name = name,
                        category = category,
                        quantity = quantity,
                        unit = unit,
                        estimatedPrice = estPrice,
                        note = note,
                        familyMemberId = memberId,
                        familyMemberName = memberName,
                        addedByAvatarColor = avatarColor
                    )
                    viewModel.updateShoppingItem(updated)
                } else {
                    viewModel.addShoppingItem(
                        name = name,
                        category = category,
                        quantity = quantity,
                        unit = unit,
                        estimatedPrice = estPrice,
                        note = note,
                        familyMemberId = memberId,
                        familyMemberName = memberName,
                        avatarColor = avatarColor
                    )
                }
            }
        )
    }

    if (showConvertDialog && purchasedItems.isNotEmpty()) {
        ConvertToExpenseDialog(
            purchasedItems = purchasedItems,
            currentUser = uiState.currentUser,
            members = uiState.members,
            onDismiss = { showConvertDialog = false },
            onConfirm = { merchant, category, paymentMethod, memberId, memberName ->
                viewModel.convertPurchasedShoppingItemsToExpense(
                    merchantName = merchant,
                    category = category,
                    paymentMethod = paymentMethod,
                    memberId = memberId,
                    memberName = memberName
                )
                showConvertDialog = false
                scope.launch {
                    snackbarHostState.showSnackbar("${purchasedItems.size} ürün harcamalara aktarıldı ve sepetten temizlendi")
                }
            }
        )
    }

    if (showClearPurchasedDialog) {
        AlertDialog(
            onDismissRequest = { showClearPurchasedDialog = false },
            title = { Text("Sepeti Temizle") },
            text = { Text("Sepetteki ${purchasedItems.size} adet satın alınmış ürün listeden silinecek. Emin misiniz?") },
            confirmButton = {
                Button(
                    onClick = {
                        viewModel.clearPurchasedShoppingItems()
                        showClearPurchasedDialog = false
                        scope.launch {
                            snackbarHostState.showSnackbar("Alınan ürünler temizlendi")
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Temizle", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearPurchasedDialog = false }) {
                    Text("İptal")
                }
            }
        )
    }
}

@Composable
fun SummaryStatBox(
    title: String,
    value: String,
    subValue: String?,
    color: Color,
    modifier: Modifier = Modifier
) {
    Surface(
        shape = RoundedCornerShape(14.dp),
        color = color.copy(alpha = 0.1f),
        modifier = modifier
    ) {
        Column(
            modifier = Modifier.padding(10.dp),
            horizontalAlignment = Alignment.Start
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.labelSmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = value,
                style = MaterialTheme.typography.titleSmall,
                fontWeight = FontWeight.Bold,
                color = color
            )
            if (subValue != null) {
                Text(
                    text = subValue,
                    style = MaterialTheme.typography.bodySmall,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
fun ShoppingItemCard(
    item: ShoppingItem,
    onTogglePurchased: (Boolean) -> Unit,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    val catalogGroup = ShoppingCatalog.GROUPS.find { it.nameTr == item.category }
    val groupColor = catalogGroup?.colorHex?.let { Color(it) } ?: MaterialTheme.colorScheme.primary

    val cardBg by animateColorAsState(
        targetValue = if (item.isPurchased) MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f) else MaterialTheme.colorScheme.surface,
        animationSpec = spring(),
        label = "cardBg"
    )

    Card(
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = cardBg),
        elevation = CardDefaults.cardElevation(defaultElevation = if (item.isPurchased) 0.dp else 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 5.dp)
            .testTag("shopping_item_card_${item.id}")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Checkbox Icon Button
            IconButton(
                onClick = { onTogglePurchased(!item.isPurchased) },
                modifier = Modifier
                    .size(36.dp)
                    .testTag("toggle_item_${item.id}")
            ) {
                if (item.isPurchased) {
                    Icon(
                        Icons.Default.CheckCircle,
                        contentDescription = "Alındı",
                        tint = Color(0xFF2E7D32),
                        modifier = Modifier.size(26.dp)
                    )
                } else {
                    Icon(
                        Icons.Outlined.RadioButtonUnchecked,
                        contentDescription = "Alınacak",
                        tint = MaterialTheme.colorScheme.outline,
                        modifier = Modifier.size(26.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Main info
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Text(
                        text = item.name,
                        style = MaterialTheme.typography.bodyLarge,
                        fontWeight = if (item.isPurchased) FontWeight.Normal else FontWeight.SemiBold,
                        textDecoration = if (item.isPurchased) TextDecoration.LineThrough else TextDecoration.None,
                        color = if (item.isPurchased) MaterialTheme.colorScheme.onSurfaceVariant else MaterialTheme.colorScheme.onSurface,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )

                    // Quantity Pill
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.secondaryContainer.copy(alpha = 0.5f)
                    ) {
                        Text(
                            text = "${if (item.quantity % 1.0 == 0.0) item.quantity.toInt().toString() else item.quantity.toString()} ${item.unit}",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSecondaryContainer,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(4.dp))

                // Category & Member Row
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Category Badge
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = groupColor.copy(alpha = 0.15f)
                    ) {
                        Text(
                            text = "${catalogGroup?.icon ?: "🛒"} ${item.category}",
                            fontSize = 10.sp,
                            color = groupColor,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 2.dp)
                        )
                    }

                    // Member Chip
                    if (item.familyMemberName.isNotBlank()) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Surface(
                                shape = CircleShape,
                                color = Color(item.addedByAvatarColor),
                                modifier = Modifier.size(14.dp)
                            ) {
                                Box(contentAlignment = Alignment.Center) {
                                    Text(
                                        text = item.familyMemberName.take(1).uppercase(),
                                        fontSize = 8.sp,
                                        color = Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = item.familyMemberName,
                                fontSize = 10.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    // Estimated price
                    item.estimatedPrice?.let { price ->
                        Text(
                            text = String.format(Locale.getDefault(), "~%,.2f ₺", price),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.tertiary
                        )
                    }
                }

                // Note if present
                item.note?.let { noteText ->
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = "💬 $noteText",
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            }

            // Action Buttons
            Row(verticalAlignment = Alignment.CenterVertically) {
                IconButton(onClick = onEdit, modifier = Modifier.size(32.dp)) {
                    Icon(
                        Icons.Default.Edit,
                        contentDescription = "Düzenle",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
                IconButton(onClick = onDelete, modifier = Modifier.size(32.dp)) {
                    Icon(
                        Icons.Default.Delete,
                        contentDescription = "Sil",
                        modifier = Modifier.size(16.dp),
                        tint = MaterialTheme.colorScheme.error.copy(alpha = 0.7f)
                    )
                }
            }
        }
    }
}

@Composable
fun EmptyShoppingListView(
    tabIndex: Int,
    strings: StringsProvider,
    onAddClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Surface(
            shape = CircleShape,
            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f),
            modifier = Modifier.size(72.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Icon(
                    imageVector = when (tabIndex) {
                        1 -> Icons.Outlined.CheckCircle
                        else -> Icons.Default.ShoppingCart
                    },
                    contentDescription = null,
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.size(36.dp)
                )
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Text(
            text = when (tabIndex) {
                0 -> "Alınacak ürün bulunmuyor 🎉"
                1 -> "Sepetiniz henüz boş"
                else -> strings.noShoppingItems
            },
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )
        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = when (tabIndex) {
                0 -> "Kategorilerden ürün seçin veya yeni ürün ekleyin."
                1 -> "Alınacaklar listesinden aldığınız ürünleri işaretledikçe burada toplanır."
                else -> strings.shoppingListSubtitle
            },
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = androidx.compose.ui.text.style.TextAlign.Center
        )
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = onAddClick,
            shape = RoundedCornerShape(12.dp)
        ) {
            Text(strings.addShoppingItem)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ConvertToExpenseDialog(
    purchasedItems: List<ShoppingItem>,
    currentUser: FamilyMember?,
    members: List<FamilyMember>,
    onDismiss: () -> Unit,
    onConfirm: (
        merchantName: String,
        category: String,
        paymentMethod: String,
        memberId: Long,
        memberName: String
    ) -> Unit
) {
    var merchantName by remember { mutableStateOf("Süpermarket") }
    var selectedCategory by remember { mutableStateOf("Market & Gıda") }
    var selectedPaymentMethod by remember { mutableStateOf("Kredi Kartı") }
    var selectedMemberId by remember {
        mutableStateOf(currentUser?.id ?: members.firstOrNull()?.id ?: 0L)
    }

    val totalCost = purchasedItems.sumOf { it.estimatedPrice ?: 0.0 }
    val categories = listOf("Market & Gıda", "Meyve & Sebze", "Temizlik", "Kişisel Bakım", "Diğer")
    val paymentMethods = listOf("Kredi Kartı", "Nakit", "Banka Kartı")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(Icons.Default.Receipt, contentDescription = null, tint = Color(0xFF2E7D32))
                Spacer(modifier = Modifier.width(8.dp))
                Text("Harcama / Fişe Aktar")
            }
        },
        text = {
            Column(modifier = Modifier.fillMaxWidth()) {
                Text(
                    text = "Sepetteki ${purchasedItems.size} adet ürün (${if (totalCost > 0) String.format(Locale.getDefault(), "%,.2f ₺", totalCost) else "Fiyat belirtilmemiş"}) bütçe harcamalarına eklenecektir.",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(14.dp))

                OutlinedTextField(
                    value = merchantName,
                    onValueChange = { merchantName = it },
                    label = { Text("Market / Mağaza Adı") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(10.dp))

                Text("Ödeme Yöntemi", style = MaterialTheme.typography.labelSmall)
                Spacer(modifier = Modifier.height(4.dp))
                Row(horizontalArrangement = Arrangement.spacedBy(6.dp)) {
                    paymentMethods.forEach { method ->
                        FilterChip(
                            selected = selectedPaymentMethod == method,
                            onClick = { selectedPaymentMethod = method },
                            label = { Text(method, fontSize = 11.sp) },
                            shape = RoundedCornerShape(8.dp)
                        )
                    }
                }
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val member = members.find { it.id == selectedMemberId } ?: currentUser
                    onConfirm(
                        merchantName.trim().ifBlank { "Süpermarket" },
                        selectedCategory,
                        selectedPaymentMethod,
                        member?.id ?: 0L,
                        member?.name ?: "Aile Üyesi"
                    )
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32))
            ) {
                Text("Harcamalara Ekle")
            }
        },
        dismissButton = {
            OutlinedButton(onClick = onDismiss) {
                Text("İptal")
            }
        }
    )
}

private fun buildShoppingListShareText(items: List<ShoppingItem>): String {
    val pending = items.filter { !it.isPurchased }
    val purchased = items.filter { it.isPurchased }
    val sb = StringBuilder()
    sb.appendLine("🛒 *Aile Alışveriş Listesi*")
    sb.appendLine("========================")
    if (pending.isNotEmpty()) {
        sb.appendLine("\n📝 *Alınacaklar (${pending.size}):*")
        pending.forEach {
            val priceStr = it.estimatedPrice?.let { p -> " (~${p}₺)" } ?: ""
            val memberStr = if (it.familyMemberName.isNotBlank()) " [${it.familyMemberName}]" else ""
            sb.appendLine("• [ ] ${it.name} (${if (it.quantity % 1.0 == 0.0) it.quantity.toInt() else it.quantity} ${it.unit})$priceStr$memberStr")
        }
    }
    if (purchased.isNotEmpty()) {
        sb.appendLine("\n✅ *Alınanlar (${purchased.size}):*")
        purchased.forEach {
            sb.appendLine("• [✓] ${it.name}")
        }
    }
    sb.appendLine("\n_Aile Bütçesi ve Fiş Takibi ile paylaşıldı_")
    return sb.toString()
}
