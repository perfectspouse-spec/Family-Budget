package com.example.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AttachMoney
import androidx.compose.material.icons.filled.Business
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.HomeWork
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.material3.Slider
import androidx.compose.material3.Surface
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
import com.example.data.model.IncomeSource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncomeDialog(
    initialIncome: IncomeSource? = null,
    members: List<FamilyMember>,
    onDismiss: () -> Unit,
    onSave: (title: String, type: String, amount: Double, depositDay: Int, memberId: Long, memberName: String, note: String?) -> Unit,
    onDelete: ((IncomeSource) -> Unit)? = null
) {
    var title by remember { mutableStateOf(initialIncome?.title ?: "") }
    var type by remember { mutableStateOf(initialIncome?.type ?: "MAAS") }
    var amountStr by remember { mutableStateOf(initialIncome?.amount?.let { if (it > 0) it.toString() else "" } ?: "") }
    var depositDay by remember { mutableFloatStateOf(initialIncome?.depositDay?.toFloat() ?: 1f) }
    var selectedMemberId by remember { mutableStateOf(initialIncome?.familyMemberId ?: members.firstOrNull()?.id ?: 0L) }
    var note by remember { mutableStateOf(initialIncome?.note ?: "") }

    var memberDropdownExpanded by remember { mutableStateOf(false) }
    val selectedMember = members.firstOrNull { it.id == selectedMemberId } ?: members.firstOrNull()

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
                        imageVector = when (type) {
                            "MAAS" -> Icons.Default.AccountBalance
                            "KIRA_GELIRI" -> Icons.Default.HomeWork
                            else -> Icons.Default.TrendingUp
                        },
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.primary,
                        modifier = Modifier.size(28.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = if (initialIncome == null) "Yeni Gelir Kalemi Ekle" else "Gelir Kalemini Düzenle",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                }
                if (initialIncome != null && onDelete != null) {
                    IconButton(
                        onClick = {
                            onDelete(initialIncome)
                            onDismiss()
                        },
                        modifier = Modifier.testTag("delete_income_button")
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
                // Type Selector Chips (Maaş, Kira Geliri, Ek Gelir)
                Text(
                    text = "Gelir Türü",
                    style = MaterialTheme.typography.labelMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    val types = listOf(
                        "MAAS" to "💼 Maaş",
                        "KIRA_GELIRI" to "🏠 Kira Geliri",
                        "EK_GELIR" to "📈 Ek Gelir"
                    )
                    types.forEach { (typeKey, label) ->
                        val isSelected = type == typeKey
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                type = typeKey
                                if (title.isBlank() || title.contains("Maaş") || title.contains("Kira") || title.contains("Gelir")) {
                                    title = when (typeKey) {
                                        "MAAS" -> if (selectedMember != null) "${selectedMember.name} - Maaş" else "Maaş"
                                        "KIRA_GELIRI" -> "Kadıköy Daire Kira Geliri"
                                        else -> "Danışmanlık / Freelance Ek Gelir"
                                    }
                                }
                            },
                            label = { Text(label, style = MaterialTheme.typography.bodySmall) },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.onPrimaryContainer
                            )
                        )
                    }
                }

                // Quick Templates for Salary and Rent
                Text(
                    text = "Hızlı Şablonlar",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                type = "MAAS"
                                title = if (selectedMember != null) "${selectedMember.name} - Şirket Maaşı" else "Aylık Maaş"
                                depositDay = 1f
                            }
                    ) {
                        Text(
                            text = "💼 1. Gün Maaş",
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 6.dp)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                type = "MAAS"
                                title = if (selectedMember != null) "${selectedMember.name} - 15'i Maaşı" else "Kamu / 15'i Maaşı"
                                depositDay = 15f
                            }
                    ) {
                        Text(
                            text = "💼 15. Gün Maaş",
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 6.dp)
                        )
                    }

                    Surface(
                        shape = RoundedCornerShape(8.dp),
                        color = MaterialTheme.colorScheme.surfaceVariant,
                        modifier = Modifier
                            .weight(1f)
                            .clickable {
                                type = "KIRA_GELIRI"
                                title = "Daire Kira Geliri"
                                depositDay = 5f
                            }
                    ) {
                        Text(
                            text = "🏠 5. Gün Kira",
                            style = MaterialTheme.typography.labelSmall,
                            modifier = Modifier.padding(horizontal = 6.dp, vertical = 6.dp)
                        )
                    }
                }

                // Title Input
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Gelir Tanımı / Başlık") },
                    placeholder = { Text("Örn: Şirket Maaşı, Kadıköy Daire Kira Geliri") },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("income_title_input")
                )

                // Amount Input
                OutlinedTextField(
                    value = amountStr,
                    onValueChange = { amountStr = it },
                    label = { Text("Aylık Tutar (₺)") },
                    placeholder = { Text("45000") },
                    leadingIcon = {
                        Icon(Icons.Default.AttachMoney, contentDescription = null)
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("income_amount_input")
                )

                // Expected Deposit Day of Month (Quick Chips + Slider)
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
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
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Beklenen Hesaba Geçiş Günü:",
                                    style = MaterialTheme.typography.bodyMedium,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = MaterialTheme.colorScheme.primary,
                                contentColor = MaterialTheme.colorScheme.onPrimary
                            ) {
                                Text(
                                    text = "Her ayın ${depositDay.toInt()}'i",
                                    style = MaterialTheme.typography.labelLarge,
                                    fontWeight = FontWeight.Bold,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        // Quick Day Chips
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            listOf(1, 5, 10, 15, 20, 25, 30).forEach { day ->
                                val isSelected = depositDay.toInt() == day
                                Surface(
                                    shape = RoundedCornerShape(6.dp),
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surface,
                                    contentColor = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onSurface,
                                    modifier = Modifier
                                        .weight(1f)
                                        .clickable { depositDay = day.toFloat() }
                                ) {
                                    Text(
                                        text = "$day",
                                        style = MaterialTheme.typography.labelSmall,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                        modifier = Modifier.padding(vertical = 4.dp),
                                        textAlign = androidx.compose.ui.text.style.TextAlign.Center
                                    )
                                }
                            }
                        }

                        Slider(
                            value = depositDay,
                            onValueChange = { depositDay = it },
                            valueRange = 1f..31f,
                            steps = 29,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("income_day_slider")
                        )
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("Ayın 1'i (Ay Başı)", style = MaterialTheme.typography.labelSmall)
                            Text("Ayın 15'i (Memur/Kamu)", style = MaterialTheme.typography.labelSmall)
                            Text("Ayın 31'i", style = MaterialTheme.typography.labelSmall)
                        }
                    }
                }

                // Family Member Selector
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
                            label = { Text("İlgili Aile Bireyi") },
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
                    placeholder = { Text("Örn: Banka havalesi veya elden") },
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
                            "MAAS" -> "Maaş"
                            "KIRA_GELIRI" -> "Kira Geliri"
                            else -> "Ek Gelir"
                        }
                    }
                    onSave(
                        finalTitle,
                        type,
                        amount,
                        depositDay.toInt(),
                        selectedMemberId,
                        selectedMember?.name ?: "",
                        note.ifBlank { null }
                    )
                    onDismiss()
                },
                modifier = Modifier.testTag("save_income_button")
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
