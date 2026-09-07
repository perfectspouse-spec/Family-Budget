package com.example.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.ExpandLess
import androidx.compose.material.icons.filled.ExpandMore
import androidx.compose.material.icons.filled.Percent
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
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
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.ExpenseCategoryTag
import com.example.data.model.FamilyMember
import com.example.data.model.ParsedReceiptItem
import com.example.ui.i18n.StringsProvider
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

data class VatRateItem(
    val rate: Double,
    val label: String,
    val description: String
)

val standardVatRates = listOf(
    VatRateItem(1.0, "%1", "Temel Gıda / Ekmek / Un"),
    VatRateItem(8.0, "%8", "Sağlık / İlaç"),
    VatRateItem(10.0, "%10", "Gıda / Restoran / Tekstil"),
    VatRateItem(18.0, "%18", "Eski Genel"),
    VatRateItem(20.0, "%20", "Genel / Yakıt / Temizlik"),
    VatRateItem(0.0, "%0", "KDV Muaf")
)

fun cleanParseDouble(str: String): Double? {
    return str.trim().replace(',', '.').toDoubleOrNull()
}

fun cleanFormatDouble(num: Double): String {
    if (num.isNaN() || num.isInfinite() || num <= 0.0) return ""
    return if (num == num.toLong().toDouble()) {
        num.toLong().toString()
    } else {
        String.format(Locale.US, "%.2f", num).trimEnd('0').trimEnd('.')
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ManualExpenseDialog(
    members: List<FamilyMember>,
    activeUser: FamilyMember?,
    strings: StringsProvider,
    availableCategories: List<ExpenseCategoryTag> = emptyList(),
    onAddNewCategory: ((name: String, icon: String, colorHex: Long) -> Unit)? = null,
    onDismiss: () -> Unit,
    onSave: (
        merchant: String,
        date: String,
        category: String,
        vatRate: Double,
        vatAmount: Double,
        total: Double,
        memberId: Long,
        memberName: String,
        paymentMethod: String,
        items: List<ParsedReceiptItem>,
        note: String?
    ) -> Unit
) {
    val todayStr = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(Date())

    var merchantName by remember { mutableStateOf("") }
    var receiptDate by remember { mutableStateOf(todayStr) }
    var selectedCategory by remember { mutableStateOf("Market & Gıda") }
    var categoryDropdownExpanded by remember { mutableStateOf(false) }

    var selectedVatRate by remember { mutableStateOf(10.0) }
    var manualTotalAmount by remember { mutableStateOf("") }
    var quantityInput by remember { mutableStateOf("1") }
    var unitPriceInput by remember { mutableStateOf("") }
    var selectedUnit by remember { mutableStateOf("Adet") }
    var isDetailedMode by remember { mutableStateOf(false) }
    var note by remember { mutableStateOf("") }

    var selectedMemberId by remember { mutableStateOf(activeUser?.id ?: members.firstOrNull()?.id ?: 1L) }
    var selectedMemberName by remember { mutableStateOf(activeUser?.name ?: members.firstOrNull()?.name ?: "Aile") }
    var memberDropdownExpanded by remember { mutableStateOf(false) }

    var paymentMethod by remember { mutableStateOf("Kredi Kartı") }

    // Dynamic Items list (for multi-item mode)
    val itemsList = remember {
        mutableStateListOf(
            ParsedReceiptItem(
                productName = "Ürün 1",
                quantity = 1.0,
                unitPrice = 0.0,
                totalPrice = 0.0,
                vatRate = 10.0,
                category = "Market & Gıda"
            )
        )
    }

    val parsedManualTotal = cleanParseDouble(manualTotalAmount) ?: 0.0
    val parsedQuantity = cleanParseDouble(quantityInput) ?: 1.0
    val parsedUnitPrice = cleanParseDouble(unitPriceInput) ?: 0.0

    val calculatedItemsSum = itemsList.sumOf { it.totalPrice }
    val effectiveTotal = if (isDetailedMode && itemsList.isNotEmpty()) {
        calculatedItemsSum
    } else {
        parsedManualTotal
    }

    val calculatedVatAmount = if (isDetailedMode && itemsList.isNotEmpty()) {
        itemsList.sumOf { it.totalPrice * (it.vatRate / (100.0 + it.vatRate)) }
    } else {
        effectiveTotal * (selectedVatRate / (100.0 + selectedVatRate))
    }

    val calculatedNetAmount = (effectiveTotal - calculatedVatAmount).coerceAtLeast(0.0)

    val defaultCategoriesList = listOf(
        "Market & Gıda",
        "Kira",
        "Eğitim",
        "Eğlence",
        "Akaryakıt",
        "Gıda & Restoran",
        "Giyim & Aksesuar",
        "Fatura & Aidat",
        "Sağlık & Eczane",
        "Ev & Yaşam",
        "Diğer"
    )

    val categoryNames = remember(availableCategories) {
        if (availableCategories.isNotEmpty()) {
            availableCategories.map { it.name }
        } else {
            defaultCategoriesList
        }
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(
            usePlatformDefaultWidth = false,
            decorFitsSystemWindows = false
        )
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.6f))
                .systemBarsPadding()
                .imePadding(),
            contentAlignment = Alignment.Center
        ) {
            Surface(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 10.dp, vertical = 6.dp),
                shape = RoundedCornerShape(22.dp),
                color = MaterialTheme.colorScheme.surface,
                tonalElevation = 6.dp,
                shadowElevation = 10.dp
            ) {
                Column(
                    modifier = Modifier.fillMaxSize()
                ) {
                    // Header (Fixed at top with Save Button)
                    Surface(
                        color = MaterialTheme.colorScheme.surface,
                        tonalElevation = 2.dp,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 12.dp, vertical = 10.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f, fill = false)
                            ) {
                                IconButton(
                                    onClick = onDismiss,
                                    modifier = Modifier.testTag("cancel_manual_expense_button")
                                ) {
                                    Icon(Icons.Default.Close, contentDescription = strings.cancel)
                                }
                                Spacer(modifier = Modifier.width(4.dp))
                                Column {
                                    Text(
                                        text = strings.manualExpense,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Text(
                                        text = "Fiş detaylarını girin",
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                                        fontSize = 11.sp,
                                        maxLines = 1
                                    )
                                }
                            }

                            Button(
                                onClick = {
                                    val finalMerchant = if (merchantName.isNotBlank()) merchantName else "Manuel Gider"
                                    val finalItems = if (isDetailedMode && itemsList.isNotEmpty()) {
                                        itemsList.toList()
                                    } else {
                                        listOf(
                                            ParsedReceiptItem(
                                                productName = merchantName.ifBlank { "Fiş Harcaması" },
                                                quantity = parsedQuantity,
                                                unitPrice = if (parsedUnitPrice > 0) parsedUnitPrice else effectiveTotal,
                                                totalPrice = effectiveTotal,
                                                vatRate = selectedVatRate,
                                                category = selectedCategory
                                            )
                                        )
                                    }
                                    onSave(
                                        finalMerchant,
                                        receiptDate,
                                        selectedCategory,
                                        selectedVatRate,
                                        calculatedVatAmount,
                                        effectiveTotal,
                                        selectedMemberId,
                                        selectedMemberName,
                                        paymentMethod,
                                        finalItems,
                                        note.takeIf { it.isNotBlank() }
                                    )
                                },
                                shape = RoundedCornerShape(12.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Color(0xFF00695C),
                                    contentColor = Color.White
                                ),
                                contentPadding = PaddingValues(horizontal = 14.dp, vertical = 8.dp),
                                elevation = ButtonDefaults.buttonElevation(defaultElevation = 2.dp),
                                modifier = Modifier
                                    .height(44.dp)
                                    .testTag("save_manual_expense_button")
                            ) {
                                Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(18.dp))
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = strings.saveReceipt,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(1.dp)
                            .background(MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.5f))
                    )

                    // Scrollable form fields
                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                    // 1. Merchant Name
                    item {
                        OutlinedTextField(
                            value = merchantName,
                            onValueChange = { merchantName = it },
                            label = { Text(strings.merchantName) },
                            placeholder = { Text("Örn: Migros, Shell, LC Waikiki") },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("manual_merchant_name_input"),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }

                    // 2. Receipt Date & Category in a Row
                    item {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            OutlinedTextField(
                                value = receiptDate,
                                onValueChange = { receiptDate = it },
                                label = { Text(strings.receiptDate) },
                                modifier = Modifier
                                    .weight(1f)
                                    .testTag("manual_date_input"),
                                shape = RoundedCornerShape(12.dp)
                            )

                            ExposedDropdownMenuBox(
                                expanded = categoryDropdownExpanded,
                                onExpandedChange = { categoryDropdownExpanded = it },
                                modifier = Modifier.weight(1.2f)
                            ) {
                                OutlinedTextField(
                                    value = selectedCategory,
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text(strings.category) },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = categoryDropdownExpanded) },
                                    modifier = Modifier
                                        .menuAnchor()
                                        .fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                ExposedDropdownMenu(
                                    expanded = categoryDropdownExpanded,
                                    onDismissRequest = { categoryDropdownExpanded = false }
                                ) {
                                    if (availableCategories.isNotEmpty()) {
                                        availableCategories.forEach { catTag ->
                                            DropdownMenuItem(
                                                text = {
                                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                                        Text(text = catTag.icon)
                                                        Spacer(modifier = Modifier.width(8.dp))
                                                        Text(text = catTag.name)
                                                    }
                                                },
                                                onClick = {
                                                    selectedCategory = catTag.name
                                                    categoryDropdownExpanded = false
                                                }
                                            )
                                        }
                                    } else {
                                        categoryNames.forEach { cat ->
                                            DropdownMenuItem(
                                                text = { Text(cat) },
                                                onClick = {
                                                    selectedCategory = cat
                                                    categoryDropdownExpanded = false
                                                }
                                            )
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // 3. Family Member Selection & Payment Method
                    item {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            ExposedDropdownMenuBox(
                                expanded = memberDropdownExpanded,
                                onExpandedChange = { memberDropdownExpanded = it },
                                modifier = Modifier.weight(1f)
                            ) {
                                OutlinedTextField(
                                    value = selectedMemberName,
                                    onValueChange = {},
                                    readOnly = true,
                                    label = { Text("Aile Bireyi") },
                                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = memberDropdownExpanded) },
                                    modifier = Modifier
                                        .menuAnchor()
                                        .fillMaxWidth(),
                                    shape = RoundedCornerShape(12.dp)
                                )
                                ExposedDropdownMenu(
                                    expanded = memberDropdownExpanded,
                                    onDismissRequest = { memberDropdownExpanded = false }
                                ) {
                                    members.forEach { m ->
                                        DropdownMenuItem(
                                            text = { Text(m.name) },
                                            onClick = {
                                                selectedMemberId = m.id
                                                selectedMemberName = m.name
                                                memberDropdownExpanded = false
                                            }
                                        )
                                    }
                                }
                            }

                            // Payment Method Filter Chips
                            Row(
                                modifier = Modifier
                                    .weight(1f)
                                    .padding(top = 8.dp),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                listOf("Kredi Kartı", "Nakit").forEach { pm ->
                                    FilterChip(
                                        selected = paymentMethod == pm,
                                        onClick = { paymentMethod = pm },
                                        label = { Text(pm, fontSize = 11.sp) }
                                    )
                                }
                            }
                        }
                    }

                    // 4. Tutar, KDV Oranı ve Miktar Hesaplama Bölümü
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            ),
                            border = BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f))
                        ) {
                            Column(
                                modifier = Modifier.padding(14.dp),
                                verticalArrangement = Arrangement.spacedBy(10.dp)
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Calculate,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "Tutar, KDV & Miktar Hesaplama",
                                        style = MaterialTheme.typography.titleSmall,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }

                                // 4.1. Toplam Tutar (₺) Girişi
                                OutlinedTextField(
                                    value = manualTotalAmount,
                                    onValueChange = { input ->
                                        manualTotalAmount = input
                                        val total = cleanParseDouble(input)
                                        val qty = cleanParseDouble(quantityInput)
                                        val price = cleanParseDouble(unitPriceInput)
                                        if (total != null && total > 0.0) {
                                            if (qty != null && qty > 0.0) {
                                                unitPriceInput = cleanFormatDouble(total / qty)
                                            } else if (price != null && price > 0.0) {
                                                quantityInput = cleanFormatDouble(total / price)
                                            }
                                        }
                                    },
                                    label = { Text("Toplam Tutar (₺) [KDV Dahil]") },
                                    placeholder = { Text("Örn: 150.00") },
                                    trailingIcon = {
                                        Text(
                                            text = "₺",
                                            fontWeight = FontWeight.Bold,
                                            fontSize = 16.sp,
                                            color = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.padding(end = 12.dp)
                                        )
                                    },
                                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("manual_total_amount_input"),
                                    shape = RoundedCornerShape(12.dp),
                                    singleLine = true
                                )

                                // 4.2. Mevcuttaki KDV Oranları Seçimi
                                Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = strings.vatRate,
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            text = standardVatRates.find { it.rate == selectedVatRate }?.description ?: "KDV Oranı",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }

                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .horizontalScroll(rememberScrollState()),
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        standardVatRates.forEach { vatItem ->
                                            FilterChip(
                                                selected = selectedVatRate == vatItem.rate,
                                                onClick = { selectedVatRate = vatItem.rate },
                                                label = {
                                                    Text(
                                                        vatItem.label,
                                                        fontWeight = if (selectedVatRate == vatItem.rate) FontWeight.Bold else FontWeight.Normal,
                                                        fontSize = 12.sp
                                                    )
                                                },
                                                modifier = Modifier.testTag("manual_vat_rate_chip_${vatItem.rate.toInt()}")
                                            )
                                        }
                                    }
                                }

                                // 4.3. Birim Türü Seçimi (Adet, Kg, Lt, Paket, Porsiyon)
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                                ) {
                                    Text(
                                        text = "Birim:",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .horizontalScroll(rememberScrollState()),
                                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                                    ) {
                                        listOf("Adet", "Kg", "Lt", "Paket", "Porsiyon").forEach { unit ->
                                            FilterChip(
                                                selected = selectedUnit == unit,
                                                onClick = { selectedUnit = unit },
                                                label = { Text(unit, fontSize = 11.sp) },
                                                modifier = Modifier.height(30.dp)
                                            )
                                        }
                                    }
                                }

                                // 4.4. Miktar ve Birim Fiyat (İki Yönlü Akıllı Hesaplama)
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                                ) {
                                    OutlinedTextField(
                                        value = quantityInput,
                                        onValueChange = { input ->
                                            quantityInput = input
                                            val qty = cleanParseDouble(input)
                                            val total = cleanParseDouble(manualTotalAmount)
                                            val price = cleanParseDouble(unitPriceInput)
                                            if (qty != null && qty > 0.0) {
                                                if (total != null && total > 0.0) {
                                                    unitPriceInput = cleanFormatDouble(total / qty)
                                                } else if (price != null && price > 0.0) {
                                                    manualTotalAmount = cleanFormatDouble(qty * price)
                                                }
                                            }
                                        },
                                        label = { Text("Miktar ($selectedUnit)", fontSize = 11.sp) },
                                        placeholder = { Text("1") },
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                        modifier = Modifier
                                            .weight(1f)
                                            .testTag("manual_quantity_input"),
                                        shape = RoundedCornerShape(10.dp),
                                        singleLine = true
                                    )

                                    OutlinedTextField(
                                        value = unitPriceInput,
                                        onValueChange = { input ->
                                            unitPriceInput = input
                                            val price = cleanParseDouble(input)
                                            val total = cleanParseDouble(manualTotalAmount)
                                            val qty = cleanParseDouble(quantityInput)
                                            if (price != null && price > 0.0) {
                                                if (total != null && total > 0.0) {
                                                    quantityInput = cleanFormatDouble(total / price)
                                                } else if (qty != null && qty > 0.0) {
                                                    manualTotalAmount = cleanFormatDouble(qty * price)
                                                }
                                            }
                                        },
                                        label = { Text("Birim Fiyat (₺/$selectedUnit)", fontSize = 11.sp) },
                                        placeholder = { Text("0.00") },
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                        modifier = Modifier
                                            .weight(1.2f)
                                            .testTag("manual_unit_price_input"),
                                        shape = RoundedCornerShape(10.dp),
                                        singleLine = true
                                    )
                                }
                            }
                        }
                    }

                    // 5. Detaylı Kalemler Toggle ve Listesi (Birden Fazla Kalem Ekleme)
                    item {
                        OutlinedButton(
                            onClick = {
                                isDetailedMode = !isDetailedMode
                                if (isDetailedMode && itemsList.isEmpty()) {
                                    itemsList.add(
                                        ParsedReceiptItem(
                                            productName = merchantName.ifBlank { "Ürün 1" },
                                            quantity = parsedQuantity,
                                            unitPrice = if (parsedUnitPrice > 0) parsedUnitPrice else effectiveTotal,
                                            totalPrice = effectiveTotal,
                                            vatRate = selectedVatRate,
                                            category = selectedCategory
                                        )
                                    )
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Icon(
                                imageVector = if (isDetailedMode) Icons.Default.ExpandLess else Icons.Default.ExpandMore,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = if (isDetailedMode) {
                                    "Detaylı Kalemleri Kapat (Tek Tutar Modu)"
                                } else {
                                    "Kalem Kalem Ürün Ekle (${if (itemsList.size > 1) "${itemsList.size} Kalem" else "Opsiyonel"})"
                                },
                                fontSize = 13.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    if (isDetailedMode) {
                        item {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "Ürün / Hizmet Kalemleri",
                                    style = MaterialTheme.typography.titleSmall,
                                    fontWeight = FontWeight.Bold
                                )
                                Button(
                                    onClick = {
                                        itemsList.add(
                                            ParsedReceiptItem(
                                                productName = "Ürün ${itemsList.size + 1}",
                                                quantity = 1.0,
                                                unitPrice = 0.0,
                                                totalPrice = 0.0,
                                                vatRate = selectedVatRate,
                                                category = selectedCategory
                                            )
                                        )
                                    },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                                    contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Add,
                                        contentDescription = null,
                                        tint = MaterialTheme.colorScheme.onPrimaryContainer,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Ürün Ekle",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                            }
                        }

                        itemsIndexed(itemsList) { index, item ->
                            val itemVatAmount = item.totalPrice * (item.vatRate / (100.0 + item.vatRate))
                            val itemNetAmount = (item.totalPrice - itemVatAmount).coerceAtLeast(0.0)

                            Card(
                                modifier = Modifier.fillMaxWidth(),
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                            ) {
                                Column(modifier = Modifier.padding(10.dp), verticalArrangement = Arrangement.spacedBy(6.dp)) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        OutlinedTextField(
                                            value = item.productName,
                                            onValueChange = { newName ->
                                                itemsList[index] = item.copy(productName = newName)
                                            },
                                            placeholder = { Text("Ürün adı (Örn: Süt 1L)") },
                                            modifier = Modifier.weight(1f),
                                            shape = RoundedCornerShape(8.dp),
                                            singleLine = true
                                        )
                                        IconButton(
                                            onClick = {
                                                if (itemsList.size > 1) {
                                                    itemsList.removeAt(index)
                                                }
                                            }
                                        ) {
                                            Icon(
                                                Icons.Default.Delete,
                                                contentDescription = "Sil",
                                                tint = Color(0xFFE53935),
                                                modifier = Modifier.size(20.dp)
                                            )
                                        }
                                    }

                                    // Kalem Bazlı KDV Oranı Seçimi
                                    Row(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .horizontalScroll(rememberScrollState()),
                                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text("KDV:", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        standardVatRates.forEach { vatItem ->
                                            FilterChip(
                                                selected = item.vatRate == vatItem.rate,
                                                onClick = { itemsList[index] = item.copy(vatRate = vatItem.rate) },
                                                label = { Text(vatItem.label, fontSize = 10.sp) },
                                                modifier = Modifier.height(26.dp)
                                            )
                                        }
                                    }

                                    // Miktar, Birim Fiyat ve Toplam Tutarı Girişi (Akıllı İki Yönlü Hesaplama)
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                                    ) {
                                        OutlinedTextField(
                                            value = if (item.quantity > 0) cleanFormatDouble(item.quantity) else "",
                                            onValueChange = { qStr ->
                                                val q = cleanParseDouble(qStr) ?: 1.0
                                                val newPrice = if (item.totalPrice > 0) item.totalPrice / q else item.unitPrice
                                                val newTotal = if (item.totalPrice > 0) item.totalPrice else q * newPrice
                                                itemsList[index] = item.copy(quantity = q, unitPrice = newPrice, totalPrice = newTotal)
                                            },
                                            label = { Text("Miktar", fontSize = 10.sp) },
                                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                            modifier = Modifier.weight(1f),
                                            shape = RoundedCornerShape(8.dp),
                                            singleLine = true
                                        )

                                        OutlinedTextField(
                                            value = if (item.unitPrice > 0) cleanFormatDouble(item.unitPrice) else "",
                                            onValueChange = { pStr ->
                                                val p = cleanParseDouble(pStr) ?: 0.0
                                                val newTotal = item.quantity * p
                                                itemsList[index] = item.copy(unitPrice = p, totalPrice = newTotal)
                                            },
                                            label = { Text("Birim ₺", fontSize = 10.sp) },
                                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                            modifier = Modifier.weight(1.1f),
                                            shape = RoundedCornerShape(8.dp),
                                            singleLine = true
                                        )

                                        OutlinedTextField(
                                            value = if (item.totalPrice > 0) cleanFormatDouble(item.totalPrice) else "",
                                            onValueChange = { tStr ->
                                                val t = cleanParseDouble(tStr) ?: 0.0
                                                val uPrice = if (item.quantity > 0) t / item.quantity else t
                                                itemsList[index] = item.copy(totalPrice = t, unitPrice = uPrice)
                                            },
                                            label = { Text("Toplam ₺", fontSize = 10.sp) },
                                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                            modifier = Modifier.weight(1.2f),
                                            shape = RoundedCornerShape(8.dp),
                                            singleLine = true
                                        )
                                    }

                                    // Kalem Özeti (KDV & Net)
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween
                                    ) {
                                        Text(
                                            text = "Net: ${String.format(Locale.getDefault(), "%.2f ₺", itemNetAmount)}",
                                            fontSize = 10.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            text = "KDV (%${item.vatRate.toInt()}): ${String.format(Locale.getDefault(), "%.2f ₺", itemVatAmount)}",
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // 6. Canlı Hesaplama Sonuç Kartı (Net Tutar, KDV Tutarı, Toplam Tutar)
                    item {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer),
                            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "KDV Hariç Net Tutar:",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                                    )
                                    Text(
                                        text = String.format(Locale.getDefault(), "%.2f ₺", calculatedNetAmount),
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = if (isDetailedMode) "Hesaplanan Toplam KDV:" else "Hesaplanan KDV Tutarı (%${selectedVatRate.toInt()}):",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                                    )
                                    Text(
                                        text = String.format(Locale.getDefault(), "%.2f ₺", calculatedVatAmount),
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                                if (!isDetailedMode && parsedUnitPrice > 0) {
                                    Spacer(modifier = Modifier.height(4.dp))
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text(
                                            text = "Birim Maliyet:",
                                            fontSize = 12.sp,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.85f)
                                        )
                                        Text(
                                            text = String.format(Locale.getDefault(), "%.2f ₺ / %s", parsedUnitPrice, selectedUnit),
                                            fontSize = 13.sp,
                                            fontWeight = FontWeight.Medium,
                                            color = MaterialTheme.colorScheme.onPrimaryContainer
                                        )
                                    }
                                }
                                Spacer(modifier = Modifier.height(8.dp))
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(1.dp)
                                        .background(MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.2f))
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = strings.totalAmount,
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                    Text(
                                        text = String.format(Locale.getDefault(), "%,.2f ₺", effectiveTotal),
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }

                    // 7. Optional Note
                    item {
                        OutlinedTextField(
                            value = note,
                            onValueChange = { note = it },
                            label = { Text("Not (İsteğe bağlı)") },
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }
                    }
                }
            }
        }
    }
}
