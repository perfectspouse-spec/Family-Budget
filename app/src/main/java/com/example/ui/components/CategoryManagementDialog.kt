package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Label
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.ExpenseCategoryTag
import com.example.ui.i18n.StringsProvider

private val CATEGORY_COLOR_PALETTE = listOf(
    0xFF4CAF50, // Yeşil
    0xFF2196F3, // Mavi
    0xFF3F51B5, // İndigo / Mor
    0xFF9C27B0, // Mor
    0xFFFF9800, // Turuncu
    0xFFE91E63, // Pembe
    0xFFE53935, // Kırmızı
    0xFF009688, // Turkuaz
    0xFF795548, // Kahverengi
    0xFF607D8B, // Gri/Mavi
    0xFF00BCD4, // Camgöbeği
    0xFF8BC34A  // Açık Yeşil
)

private val CATEGORY_EMOJI_PALETTE = listOf(
    "🍎", "🛒", "🏠", "🎓", "🎭", "⛽", "⚡", "💊", "👗", "📌",
    "☕", "✈️", "🚗", "👶", "🐾", "💻", "🍔", "🏋️", "🎁", "🔧"
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun CategoryManagementDialog(
    categories: List<ExpenseCategoryTag>,
    strings: StringsProvider,
    onDismiss: () -> Unit,
    onAddCategory: (name: String, icon: String, colorHex: Long) -> Unit,
    onUpdateCategory: (ExpenseCategoryTag) -> Unit,
    onDeleteCategory: (ExpenseCategoryTag) -> Unit
) {
    var isAddingNew by remember { mutableStateOf(false) }
    var editingCategory by remember { mutableStateOf<ExpenseCategoryTag?>(null) }

    var categoryNameInput by remember { mutableStateOf("") }
    var selectedEmoji by remember { mutableStateOf("🏷️") }
    var selectedColorHex by remember { mutableStateOf(0xFF4CAF50) }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(8.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp)
            ) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Surface(
                            shape = CircleShape,
                            color = MaterialTheme.colorScheme.primaryContainer,
                            modifier = Modifier.size(36.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    Icons.Default.Label,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Kategori & Etiket Yönetimi",
                            style = MaterialTheme.typography.titleMedium,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
                        Icon(Icons.Default.Close, contentDescription = "Kapat")
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Add or Edit Form
                if (isAddingNew || editingCategory != null) {
                    Card(
                        shape = RoundedCornerShape(16.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Text(
                                text = if (editingCategory != null) "Kategoriyi Düzenle" else "Yeni Kategori / Etiket Ekle",
                                style = MaterialTheme.typography.labelLarge,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.primary
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = categoryNameInput,
                                onValueChange = { categoryNameInput = it },
                                label = { Text("Kategori Adı (örn. Eğitim, Eğlence, Kira)") },
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("category_name_input")
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "Simge Seçin",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(4.dp))

                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                CATEGORY_EMOJI_PALETTE.forEach { emoji ->
                                    val isSelected = selectedEmoji == emoji
                                    Surface(
                                        shape = RoundedCornerShape(8.dp),
                                        color = if (isSelected) MaterialTheme.colorScheme.primaryContainer else MaterialTheme.colorScheme.surface,
                                        border = if (isSelected) androidx.compose.foundation.BorderStroke(2.dp, MaterialTheme.colorScheme.primary) else null,
                                        modifier = Modifier
                                            .size(36.dp)
                                            .clickable { selectedEmoji = emoji }
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(text = emoji, fontSize = 16.sp)
                                        }
                                    }
                                }
                            }

                            Spacer(modifier = Modifier.height(10.dp))

                            Text(
                                text = "Renk Seçin",
                                style = MaterialTheme.typography.labelMedium,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(4.dp))

                            FlowRow(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp),
                                verticalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                CATEGORY_COLOR_PALETTE.forEach { colorHex ->
                                    val isSelected = selectedColorHex == colorHex
                                    Box(
                                        modifier = Modifier
                                            .size(30.dp)
                                            .clip(CircleShape)
                                            .background(Color(colorHex))
                                            .border(
                                                width = if (isSelected) 3.dp else 1.dp,
                                                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.White.copy(alpha = 0.6f),
                                                shape = CircleShape
                                            )
                                            .clickable { selectedColorHex = colorHex }
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(14.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.End
                            ) {
                                OutlinedButton(
                                    onClick = {
                                        isAddingNew = false
                                        editingCategory = null
                                        categoryNameInput = ""
                                    },
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.padding(end = 8.dp)
                                ) {
                                    Text("İptal")
                                }

                                Button(
                                    onClick = {
                                        if (categoryNameInput.isNotBlank()) {
                                            if (editingCategory != null) {
                                                onUpdateCategory(
                                                    editingCategory!!.copy(
                                                        name = categoryNameInput.trim(),
                                                        icon = selectedEmoji,
                                                        colorHex = selectedColorHex
                                                    )
                                                )
                                            } else {
                                                onAddCategory(
                                                    categoryNameInput.trim(),
                                                    selectedEmoji,
                                                    selectedColorHex
                                                )
                                            }
                                            isAddingNew = false
                                            editingCategory = null
                                            categoryNameInput = ""
                                        }
                                    },
                                    enabled = categoryNameInput.isNotBlank(),
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.testTag("save_category_button")
                                ) {
                                    Text("Kaydet")
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                } else {
                    Button(
                        onClick = {
                            isAddingNew = true
                            editingCategory = null
                            categoryNameInput = ""
                            selectedEmoji = "🏷️"
                            selectedColorHex = 0xFF4CAF50
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("add_new_category_button"),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(Icons.Default.Add, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Yeni Kategori / Etiket Ekle")
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                }

                // Categories list
                Text(
                    text = "Mevcut Kategoriler (${categories.size})",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(6.dp))

                LazyColumn(
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(max = 280.dp)
                ) {
                    items(categories, key = { it.id }) { cat ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f))
                                .padding(horizontal = 10.dp, vertical = 8.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(CircleShape)
                                        .background(Color(cat.colorHex).copy(alpha = 0.2f)),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Text(text = cat.icon, fontSize = 14.sp)
                                }
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = cat.name,
                                        style = MaterialTheme.typography.bodyMedium,
                                        fontWeight = FontWeight.SemiBold
                                    )
                                    if (cat.isDefault) {
                                        Text(
                                            text = "Varsayılan",
                                            style = MaterialTheme.typography.labelSmall,
                                            color = MaterialTheme.colorScheme.outline
                                        )
                                    }
                                }
                            }

                            Row {
                                IconButton(
                                    onClick = {
                                        editingCategory = cat
                                        categoryNameInput = cat.name
                                        selectedEmoji = cat.icon
                                        selectedColorHex = cat.colorHex
                                        isAddingNew = false
                                    },
                                    modifier = Modifier.size(28.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Edit,
                                        contentDescription = "Düzenle",
                                        tint = MaterialTheme.colorScheme.primary,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }

                                if (!cat.isDefault) {
                                    Spacer(modifier = Modifier.width(4.dp))
                                    IconButton(
                                        onClick = { onDeleteCategory(cat) },
                                        modifier = Modifier.size(28.dp)
                                    ) {
                                        Icon(
                                            Icons.Default.Delete,
                                            contentDescription = "Sil",
                                            tint = MaterialTheme.colorScheme.error,
                                            modifier = Modifier.size(16.dp)
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
