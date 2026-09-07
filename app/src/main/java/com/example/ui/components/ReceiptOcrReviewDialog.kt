package com.example.ui.components

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.RemoveCircle
import androidx.compose.material.icons.filled.Save
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
import com.example.data.model.CategoryClassifier
import com.example.data.model.FamilyMember
import com.example.data.model.ParsedReceipt
import com.example.data.model.ParsedReceiptItem
import com.example.ui.i18n.StringsProvider
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ReceiptOcrReviewDialog(
    parsedReceipt: ParsedReceipt,
    members: List<FamilyMember>,
    activeUser: FamilyMember?,
    strings: StringsProvider,
    onDismiss: () -> Unit,
    onConfirm: (
        merchant: String,
        date: String,
        category: String,
        vatRate: Double,
        vatAmount: Double,
        total: Double,
        memberId: Long,
        memberName: String,
        paymentMethod: String,
        items: List<ParsedReceiptItem>
    ) -> Unit
) {
    var merchantName by remember { mutableStateOf(parsedReceipt.merchantName) }
    var receiptDate by remember { mutableStateOf(parsedReceipt.receiptDate) }
    var category by remember { mutableStateOf(parsedReceipt.category) }
    var categoryDropdownExpanded by remember { mutableStateOf(false) }

    var vatRate by remember { mutableStateOf(parsedReceipt.vatRate) }
    var vatAmount by remember { mutableStateOf(parsedReceipt.vatAmount) }

    var selectedMemberId by remember { mutableStateOf(activeUser?.id ?: members.firstOrNull()?.id ?: 1L) }
    var selectedMemberName by remember { mutableStateOf(activeUser?.name ?: members.firstOrNull()?.name ?: "Aile") }
    var memberDropdownExpanded by remember { mutableStateOf(false) }

    val itemsList = remember {
        mutableStateListOf<ParsedReceiptItem>().apply {
            addAll(parsedReceipt.items)
        }
    }

    val calculatedTotal = if (itemsList.isNotEmpty()) itemsList.sumOf { it.totalPrice } else parsedReceipt.totalAmount
    val computedVatFromItems = itemsList.filter { !it.isCancelled && it.totalPrice > 0 }
        .sumOf { it.totalPrice * (it.vatRate / (100.0 + it.vatRate)) }
    val activeVatTotal = if (computedVatFromItems > 0) computedVatFromItems
    else (calculatedTotal * (vatRate / (100.0 + vatRate)))
    val netWithoutVat = (calculatedTotal - activeVatTotal).coerceAtLeast(0.0)

    val categories = CategoryClassifier.ALL_CATEGORIES

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
                    // Header with Close Button, Title/Badge and Top Save Button
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
                                    modifier = Modifier.testTag("cancel_ocr_dialog_button")
                                ) {
                                    Icon(Icons.Default.Close, contentDescription = strings.cancel)
                                }
                                Spacer(modifier = Modifier.width(4.dp))
                                Column {
                                    Text(
                                        text = strings.reviewScannedReceipt,
                                        style = MaterialTheme.typography.titleMedium,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        maxLines = 1,
                                        overflow = TextOverflow.Ellipsis
                                    )
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.AutoAwesome,
                                            contentDescription = null,
                                            tint = MaterialTheme.colorScheme.primary,
                                            modifier = Modifier.size(12.dp)
                                        )
                                        Spacer(modifier = Modifier.width(3.dp))
                                        Text(
                                            text = "Gemini AI OCR Ayrıştırma",
                                            fontSize = 11.sp,
                                            fontWeight = FontWeight.SemiBold,
                                            color = MaterialTheme.colorScheme.primary
                                        )
                                    }
                                }
                            }

                            Button(
                                onClick = {
                                    onConfirm(
                                        merchantName,
                                        receiptDate,
                                        category,
                                        vatRate,
                                        activeVatTotal,
                                        calculatedTotal,
                                        selectedMemberId,
                                        selectedMemberName,
                                        "Kredi Kartı",
                                        itemsList.toList()
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
                                    .testTag("save_ocr_receipt_button")
                                    .testTag("confirm_ocr_receipt_button")
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

                    LazyColumn(
                        modifier = Modifier
                            .weight(1f)
                            .fillMaxWidth(),
                        contentPadding = PaddingValues(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                    // Merchant Name
                    item {
                        OutlinedTextField(
                            value = merchantName,
                            onValueChange = { merchantName = it },
                            label = { Text(strings.merchantName) },
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("ocr_merchant_name_input"),
                            shape = RoundedCornerShape(12.dp)
                        )
                    }

                    // Date & Category
                    item {
                        Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            OutlinedTextField(
                                value = receiptDate,
                                onValueChange = { receiptDate = it },
                                label = { Text(strings.receiptDate) },
                                modifier = Modifier.weight(1f),
                                shape = RoundedCornerShape(12.dp)
                            )

                            ExposedDropdownMenuBox(
                                expanded = categoryDropdownExpanded,
                                onExpandedChange = { categoryDropdownExpanded = it },
                                modifier = Modifier.weight(1.2f)
                            ) {
                                OutlinedTextField(
                                    value = category,
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
                                    categories.forEach { cat ->
                                        DropdownMenuItem(
                                            text = { Text(cat) },
                                            onClick = {
                                                category = cat
                                                categoryDropdownExpanded = false
                                            }
                                        )
                                    }
                                }
                            }
                        }
                    }

                    // Family Member assignment
                    item {
                        ExposedDropdownMenuBox(
                            expanded = memberDropdownExpanded,
                            onExpandedChange = { memberDropdownExpanded = it },
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            OutlinedTextField(
                                value = selectedMemberName,
                                onValueChange = {},
                                readOnly = true,
                                label = { Text("Harcamayı Yapan Aile Bireyi") },
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
                    }

                    // Items List Header & Add Button
                    item {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "Ayrıştırılan Ürünler (${itemsList.size} Kalem):",
                                style = MaterialTheme.typography.titleSmall,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            OutlinedButton(
                                onClick = {
                                    itemsList.add(
                                        ParsedReceiptItem(
                                            productName = "Yeni Ürün",
                                            quantity = 1.0,
                                            unitPrice = 0.0,
                                            totalPrice = 0.0
                                        )
                                    )
                                },
                                contentPadding = androidx.compose.foundation.layout.PaddingValues(horizontal = 8.dp, vertical = 2.dp),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(strings.addProductItem, fontSize = 11.sp)
                            }
                        }
                    }

                    // Items list
                    itemsIndexed(itemsList) { index, item ->
                        val isCancelled = item.isCancelled || item.totalPrice < 0
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(
                                containerColor = if (isCancelled) Color(0xFFFFEBEE).copy(alpha = 0.6f)
                                else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                            )
                        ) {
                            Column(modifier = Modifier.padding(10.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    OutlinedTextField(
                                        value = item.productName,
                                        onValueChange = { newName ->
                                            itemsList[index] = item.copy(productName = newName)
                                        },
                                        label = { Text("Ürün / Hizmet Adı", fontSize = 10.sp) },
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(8.dp),
                                        singleLine = true
                                    )
                                    IconButton(
                                        onClick = { itemsList.removeAt(index) }
                                    ) {
                                        Icon(
                                            Icons.Default.Delete,
                                            contentDescription = "Sil",
                                            tint = Color(0xFFE53935),
                                            modifier = Modifier.size(20.dp)
                                        )
                                    }
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    OutlinedTextField(
                                        value = if (item.quantity == item.quantity.toLong().toDouble()) item.quantity.toLong().toString() else item.quantity.toString(),
                                        onValueChange = { qStr ->
                                            val q = qStr.toDoubleOrNull() ?: 1.0
                                            val total = if (isCancelled) -Math.abs(q * item.unitPrice) else q * item.unitPrice
                                            itemsList[index] = item.copy(quantity = q, totalPrice = total)
                                        },
                                        label = { Text("Miktar", fontSize = 10.sp) },
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                        modifier = Modifier.weight(1f),
                                        shape = RoundedCornerShape(8.dp)
                                    )

                                    OutlinedTextField(
                                        value = String.format(Locale.getDefault(), "%.2f", Math.abs(item.unitPrice)),
                                        onValueChange = { pStr ->
                                            val p = pStr.toDoubleOrNull() ?: 0.0
                                            val total = if (isCancelled) -Math.abs(item.quantity * p) else item.quantity * p
                                            itemsList[index] = item.copy(unitPrice = p, totalPrice = total)
                                        },
                                        label = { Text("Birim ₺", fontSize = 10.sp) },
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                        modifier = Modifier.weight(1.2f),
                                        shape = RoundedCornerShape(8.dp)
                                    )

                                    OutlinedTextField(
                                        value = String.format(Locale.getDefault(), "%.2f", item.totalPrice),
                                        onValueChange = { tStr ->
                                            val t = tStr.toDoubleOrNull() ?: 0.0
                                            val unit = if (item.quantity > 0) Math.abs(t / item.quantity) else t
                                            itemsList[index] = item.copy(totalPrice = t, unitPrice = unit, isCancelled = t < 0)
                                        },
                                        label = { Text("Tutar ₺", fontSize = 10.sp) },
                                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                                        modifier = Modifier.weight(1.2f),
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                }

                                Spacer(modifier = Modifier.height(6.dp))

                                // KDV Oranı Seçimi & İptal Çipi
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Row(
                                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Text("KDV:", fontSize = 10.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurfaceVariant)
                                        listOf(1.0, 8.0, 10.0, 18.0, 20.0, 0.0).forEach { rate ->
                                            FilterChip(
                                                selected = item.vatRate == rate,
                                                onClick = { itemsList[index] = item.copy(vatRate = rate) },
                                                label = { Text("%${rate.toInt()}", fontSize = 9.sp) },
                                                modifier = Modifier.height(26.dp)
                                            )
                                        }
                                    }

                                    // Toggle Cancellation Chip
                                    FilterChip(
                                        selected = isCancelled,
                                        onClick = {
                                            val newCancelled = !isCancelled
                                            val newTotal = if (newCancelled) -Math.abs(item.totalPrice) else Math.abs(item.totalPrice)
                                            itemsList[index] = item.copy(isCancelled = newCancelled, totalPrice = newTotal)
                                        },
                                        label = { Text(if (isCancelled) "İptal (-)" else "Normal (+)", fontSize = 10.sp) },
                                        colors = FilterChipDefaults.filterChipColors(
                                            selectedContainerColor = Color(0xFFFFCDD2),
                                            selectedLabelColor = Color(0xFFB71C1C)
                                        ),
                                        modifier = Modifier.height(26.dp)
                                    )
                                }
                            }
                        }
                    }

                    // Total and VAT details
                    item {
                        val computedVatFromItems = itemsList.filter { !it.isCancelled && it.totalPrice > 0 }
                            .sumOf { it.totalPrice * (it.vatRate / (100.0 + it.vatRate)) }
                        val activeVatTotal = if (computedVatFromItems > 0) computedVatFromItems
                        else (calculatedTotal * (vatRate / (100.0 + vatRate)))
                        val netWithoutVat = (calculatedTotal - activeVatTotal).coerceAtLeast(0.0)

                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
                        ) {
                            Column(modifier = Modifier.padding(14.dp)) {
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Varsayılan Fiş KDV Oranı:",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                    Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                                        listOf(1.0, 10.0, 20.0).forEach { rate ->
                                            FilterChip(
                                                selected = vatRate == rate,
                                                onClick = {
                                                    vatRate = rate
                                                    // Also update items that were at previous default
                                                    for (i in itemsList.indices) {
                                                        itemsList[i] = itemsList[i].copy(vatRate = rate)
                                                    }
                                                },
                                                label = { Text("%${rate.toInt()}", fontSize = 10.sp) },
                                                modifier = Modifier.height(28.dp)
                                            )
                                        }
                                    }
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "KDV Hariç Net Tutar:",
                                        fontSize = 12.sp,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer.copy(alpha = 0.8f)
                                    )
                                    Text(
                                        text = String.format(Locale.getDefault(), "%,.2f ₺", netWithoutVat),
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.SemiBold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(
                                        text = "Hesaplanan Toplam KDV:",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF1565C0)
                                    )
                                    Text(
                                        text = String.format(Locale.getDefault(), "%,.2f ₺", activeVatTotal),
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color(0xFF1565C0)
                                    )
                                }
                                Spacer(modifier = Modifier.height(6.dp))
                                Box(modifier = Modifier.fillMaxWidth().height(1.dp).background(MaterialTheme.colorScheme.outlineVariant))
                                Spacer(modifier = Modifier.height(6.dp))
                                Row(
                                    modifier = Modifier.fillMaxWidth(),
                                    horizontalArrangement = Arrangement.SpaceBetween,
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Text(
                                        text = "Toplam Fiş Tutarı (KDV Dahil):",
                                        fontSize = 15.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.onPrimaryContainer
                                    )
                                    Text(
                                        text = String.format(Locale.getDefault(), "%,.2f ₺", calculatedTotal),
                                        fontSize = 20.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
}
