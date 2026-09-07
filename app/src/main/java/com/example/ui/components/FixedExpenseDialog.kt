package com.example.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocationCity
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Security
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.data.model.FamilyMember
import com.example.data.model.FixedExpense

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun FixedExpenseDialog(
    initialExpense: FixedExpense? = null,
    members: List<FamilyMember>,
    onDismiss: () -> Unit,
    onSave: (title: String, type: String, amount: Double, dueDay: Int, memberId: Long, memberName: String, note: String?) -> Unit,
    onDelete: ((FixedExpense) -> Unit)? = null
) {
    var title by remember { mutableStateOf(initialExpense?.title ?: "") }
    var type by remember { mutableStateOf(initialExpense?.type ?: "KIRA") }
    var amountStr by remember { mutableStateOf(initialExpense?.amount?.let { if (it > 0) it.toString() else "" } ?: "") }
    var dueDay by remember { mutableFloatStateOf(initialExpense?.dueDay?.toFloat() ?: 15f) }
    var selectedMemberId by remember { mutableStateOf(initialExpense?.familyMemberId ?: members.firstOrNull()?.id ?: 0L) }
    var note by remember { mutableStateOf(initialExpense?.note ?: "") }

    var memberDropdownExpanded by remember { mutableStateOf(false) }
    val selectedMember = members.firstOrNull { it.id == selectedMemberId } ?: members.firstOrNull()

    val expenseCategories = listOf(
        "KIRA" to "🏡 Ev Kirası",
        "KREDI_KARTI_1" to "💳 Kredi Kartı 1",
        "KREDI_KARTI_2" to "💳 Kredi Kartı 2",
        "KREDI_KARTI_3" to "💳 Kredi Kartı 3",
        "AIDAT" to "🏢 Site Aidatı",
        "FATURA" to "⚡ Faturalar",
        "SIGORTA" to "🛡️ Sigorta / Kasko",
        "DIGER" to "📌 Diğer Sabit"
    )

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = when {
                            type == "KIRA" -> Icons.Default.Home
                            type.startsWith("KREDI_KARTI") -> Icons.Default.CreditCard
                            type == "AIDAT" -> Icons.Default.LocationCity
                            type == "FATURA" -> Icons.Default.Receipt
                            type == "SIGORTA" -> Icons.Default.Security
                            else -> Icons.Default.CreditCard
                        },
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = if (initialExpense == null) "Sabit Gider Ekle" else "Sabit Gideri Düzenle",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                if (initialExpense != null && onDelete != null) {
                    IconButton(
                        onClick = {
                            onDelete(initialExpense)
                            onDismiss()
                        },
                        modifier = Modifier.testTag("delete_fixed_expense_button")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Sil",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(rememberScrollState()),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Category Selector Chips
                Text(
                    text = "Gider Kalemi Şablonu",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    items(expenseCategories) { (typeKey, label) ->
                        val isSelected = type == typeKey
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                type = typeKey
                                if (title.isBlank() || expenseCategories.any { it.second == title }) {
                                    title = when (typeKey) {
                                        "KIRA" -> "Ev Kirası"
                                        "KREDI_KARTI_1" -> "Kredi Kartı 1 (Garanti Bonus)"
                                        "KREDI_KARTI_2" -> "Kredi Kartı 2 (İş Maximum)"
                                        "KREDI_KARTI_3" -> "Kredi Kartı 3 (Yapı Kredi World)"
                                        "AIDAT" -> "Apartman & Site Aidatı"
                                        "FATURA" -> "Elektrik, Su & İnternet Faturaları"
                                        "SIGORTA" -> "Özel Sağlık & Kasko Sigortası"
                                        else -> "Diğer Sabit Abonelik"
                                    }
                                }
                            },
                            label = { Text(label, style = MaterialTheme.typography.bodySmall) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.errorContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.onErrorContainer
                            )
                        )
                    }
                }

                // Title Input
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Gider Başlığı") },
                    placeholder = { Text("Örn: Ev Kirası, Garanti BBVA Bonus Kredi Kartı") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("fixed_expense_title_input")
                )

                // Amount Input
                OutlinedTextField(
                    value = amountStr,
                    onValueChange = { amountStr = it },
                    label = { Text("Aylık Tutar (₺)") },
                    placeholder = { Text("15000") },
                    leadingIcon = {
                        Icon(Icons.Default.AttachMoney, contentDescription = null)
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("fixed_expense_amount_input")
                )

                // Due Day of Month Slider (1 to 31)
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.errorContainer.copy(alpha = 0.3f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(12.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.CalendarMonth,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.error,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Son Ödeme Günü:",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.error,
                                contentColor = MaterialTheme.colorScheme.onError
                            ) {
                                Text(
                                    text = "Her ayın ${dueDay.toInt()}'i",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }
                        Slider(
                            value = dueDay,
                            onValueChange = { dueDay = it },
                            valueRange = 1f..31f,
                            steps = 29,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("fixed_expense_day_slider")
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Ayın 1'i", style = MaterialTheme.typography.labelSmall)
                            Text("Ayın 15'i", style = MaterialTheme.typography.labelSmall)
                            Text("Ayın 31'i", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }

                // Family Member Responsible
                if (members.isNotEmpty()) {
                    ExposedDropdownMenuBox(
                        expanded = memberDropdownExpanded,
                        onExpandedChange = { memberDropdownExpanded = !memberDropdownExpanded },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = selectedMember?.name ?: "Seçiniz",
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Ödemeden Sorumlu Aile Bireyi") },
                            leadingIcon = {
                                Icon(Icons.Default.Person, contentDescription = null)
                            },
                            trailingIcon = {
                                ExposedDropdownMenuDefaults.TrailingIcon(expanded = memberDropdownExpanded)
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .menuAnchor()
                        )
                        ExposedDropdownMenu(
                            expanded = memberDropdownExpanded,
                            onDismissRequest = { memberDropdownExpanded = false }
                        ) {
                            members.forEach { member ->
                                DropdownMenuItem(
                                    text = { Text("${member.name} (${member.role})") },
                                    onClick = {
                                        selectedMemberId = member.id
                                        memberDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                // Note
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Açıklama / Not (Opsiyonel)") },
                    placeholder = { Text("Örn: Otomatik ödeme talimatı var") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val amount = amountStr.replace(",", ".").toDoubleOrNull() ?: 0.0
                    val finalTitle = title.ifBlank {
                        when (type) {
                            "KIRA" -> "Ev Kirası"
                            "KREDI_KARTI_1" -> "Kredi Kartı 1"
                            "KREDI_KARTI_2" -> "Kredi Kartı 2"
                            "KREDI_KARTI_3" -> "Kredi Kartı 3"
                            "AIDAT" -> "Site Aidatı"
                            "FATURA" -> "Faturalar"
                            else -> "Sabit Gider"
                        }
                    }
                    onSave(
                        finalTitle,
                        type,
                        amount,
                        dueDay.toInt(),
                        selectedMemberId,
                        selectedMember?.name ?: "",
                        note.ifBlank { null }
                    )
                    onDismiss()
                },
                modifier = Modifier.testTag("save_fixed_expense_button")
            ) {
                Text("Kaydet")
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("İptal")
            }
        }
    )
}
