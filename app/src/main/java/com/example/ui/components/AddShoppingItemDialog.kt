package com.example.ui.components

import android.app.Activity
import android.content.Intent
import android.speech.RecognizerIntent
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Mic
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.ui.window.PopupProperties
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableDoubleStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.data.model.FamilyMember
import com.example.data.model.MarketProductOption
import com.example.data.model.MarketProductTemplate
import com.example.data.model.ShoppingCatalog
import com.example.data.model.ShoppingItem
import com.example.data.voice.VoiceShoppingParser
import com.example.ui.i18n.StringsProvider
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun AddShoppingItemDialog(
    strings: StringsProvider,
    members: List<FamilyMember>,
    currentUser: FamilyMember?,
    initialItem: ShoppingItem? = null,
    onDismiss: () -> Unit,
    onSaveMarketOffers: ((List<MarketProductOption>, String, FamilyMember?) -> Unit)? = null,
    onSave: (
        name: String,
        category: String,
        quantity: Double,
        unit: String,
        estimatedPrice: Double?,
        note: String?,
        memberId: Long,
        memberName: String,
        avatarColor: Long
    ) -> Unit
) {
    val context = LocalContext.current
    var name by remember { mutableStateOf(initialItem?.name ?: "") }
    var selectedCategory by remember { mutableStateOf(initialItem?.category ?: "Süt & Kahvaltılık") }
    var quantity by remember { mutableDoubleStateOf(initialItem?.quantity ?: 1.0) }
    var selectedUnit by remember { mutableStateOf(initialItem?.unit ?: "Adet") }
    var estimatedPriceText by remember { mutableStateOf(initialItem?.estimatedPrice?.let { if (it > 0) it.toString() else "" } ?: "") }
    var note by remember { mutableStateOf(initialItem?.note ?: "") }

    var selectedMemberId by remember {
        mutableStateOf(
            initialItem?.familyMemberId ?: currentUser?.id ?: members.firstOrNull()?.id ?: 0L
        )
    }

    var nameError by remember { mutableStateOf(false) }

    // Speech launcher for voice input
    val speechLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK && result.data != null) {
            val spokenMatches = result.data?.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS)
            val spokenText = spokenMatches?.firstOrNull() ?: ""
            if (spokenText.isNotBlank()) {
                val parsedList = VoiceShoppingParser.parseSpokenShoppingText(spokenText)
                if (parsedList.isNotEmpty()) {
                    val first = parsedList.first()
                    name = first.name
                    quantity = first.quantity
                    selectedUnit = first.unit
                    selectedCategory = first.category
                    first.estimatedPrice?.let {
                        estimatedPriceText = it.toString()
                    }
                    nameError = false
                    Toast.makeText(context, "\"${first.name}\" sesle algılandı", Toast.LENGTH_SHORT).show()
                } else {
                    name = spokenText.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale("tr")) else it.toString() }
                    nameError = false
                }
            }
        }
    }

    Dialog(onDismissRequest = onDismiss) {
        Surface(
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp,
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 16.dp)
                .testTag("add_shopping_item_dialog")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp)
                    .verticalScroll(rememberScrollState())
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
                            modifier = Modifier.size(40.dp)
                        ) {
                            Box(contentAlignment = Alignment.Center) {
                                Icon(
                                    imageVector = Icons.Default.ShoppingCart,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(22.dp)
                                )
                            }
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = if (initialItem != null) "Ürünü Düzenle" else strings.addShoppingItem,
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Aile ortak alışveriş listesine ekleyin",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = "Kapat")
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // 1. Ürün Grupları ve Ürünler ComboBox Satırı
                var groupDropdownExpanded by remember { mutableStateOf(false) }
                var productDropdownExpanded by remember { mutableStateOf(false) }
                var selectedGroup by remember {
                    mutableStateOf(
                        ShoppingCatalog.GROUPS.find { it.nameTr == selectedCategory } ?: ShoppingCatalog.GROUPS.first()
                    )
                }
                var selectedTemplate by remember {
                    mutableStateOf<MarketProductTemplate?>(
                        selectedGroup.popularProducts.find { it.name.equals(name, ignoreCase = true) }
                    )
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // Ürün Grubu ComboBox
                    Box(modifier = Modifier.weight(1f)) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { groupDropdownExpanded = true }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp, vertical = 8.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    Text(text = selectedGroup.icon, fontSize = 16.sp)
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Column {
                                        Text(
                                            text = strings.productGroupComboBox,
                                            style = MaterialTheme.typography.labelSmall,
                                            fontSize = 9.sp,
                                            color = MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                        Text(
                                            text = selectedGroup.nameTr,
                                            style = MaterialTheme.typography.bodySmall,
                                            fontWeight = FontWeight.Bold,
                                            maxLines = 1
                                        )
                                    }
                                }
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
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
                                            fontWeight = if (group.id == selectedGroup.id) FontWeight.Bold else FontWeight.Normal
                                        )
                                    },
                                    onClick = {
                                        selectedGroup = group
                                        selectedCategory = group.nameTr
                                        selectedTemplate = null
                                        groupDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    // Ürünler ComboBox (Popüler Ürünler)
                    Box(modifier = Modifier.weight(1.1f)) {
                        Surface(
                            shape = RoundedCornerShape(12.dp),
                            color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            border = androidx.compose.foundation.BorderStroke(1.dp, MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.6f)),
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .clickable { productDropdownExpanded = true }
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 10.dp, vertical = 8.dp),
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
                                            text = selectedTemplate?.name ?: if (name.isNotBlank()) name else strings.selectProductHint,
                                            style = MaterialTheme.typography.bodySmall,
                                            fontWeight = FontWeight.Medium,
                                            maxLines = 1
                                        )
                                    }
                                }
                                Icon(
                                    imageVector = Icons.Default.ArrowDropDown,
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
                            selectedGroup.popularProducts.forEach { template ->
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
                                        name = template.name
                                        quantity = template.defaultQuantity
                                        selectedUnit = template.defaultUnit
                                        selectedCategory = selectedGroup.nameTr
                                        template.estimatedPrice?.let { ep ->
                                            estimatedPriceText = ep.toString()
                                        }
                                        nameError = false
                                        productDropdownExpanded = false
                                    }
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // 2. Product Name Field
                OutlinedTextField(
                    value = name,
                    onValueChange = {
                        name = it
                        nameError = false
                    },
                    label = { Text("Ürün Adı *") },
                    placeholder = { Text("Örn: Domates, Süt 1L, Yumurta...") },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                try {
                                    val intent = Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH).apply {
                                        putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM)
                                        putExtra(RecognizerIntent.EXTRA_LANGUAGE, "tr-TR")
                                        putExtra(RecognizerIntent.EXTRA_PROMPT, strings.voiceSpeakPrompt)
                                    }
                                    speechLauncher.launch(intent)
                                } catch (e: Exception) {
                                    Toast.makeText(context, "Mikrofon başlatılamadı: ${e.message}", Toast.LENGTH_SHORT).show()
                                }
                            },
                            modifier = Modifier.testTag("dialog_voice_input_button")
                        ) {
                            Icon(
                                Icons.Default.Mic,
                                contentDescription = "Sesle Dikte Et",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    },
                    isError = nameError,
                    supportingText = if (nameError) {
                        { Text("Lütfen ürün adını girin", color = MaterialTheme.colorScheme.error) }
                    } else null,
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("shopping_item_name_input"),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next)
                )

                Spacer(modifier = Modifier.height(14.dp))

                // 3. Birim ve Miktar Bölümü
                Text(
                    text = strings.quantityAndUnitLabel,
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(6.dp))

                // Fast Unit Selection Chips
                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    ShoppingCatalog.UNITS.forEach { unitItem ->
                        val isUnitSelected = selectedUnit == unitItem
                        FilterChip(
                            selected = isUnitSelected,
                            onClick = { selectedUnit = unitItem },
                            label = {
                                Text(
                                    text = unitItem,
                                    fontSize = 12.sp,
                                    fontWeight = if (isUnitSelected) FontWeight.Bold else FontWeight.Normal
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = MaterialTheme.colorScheme.primaryContainer,
                                selectedLabelColor = MaterialTheme.colorScheme.primary
                            ),
                            shape = RoundedCornerShape(8.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Quantity Stepper
                    Column(modifier = Modifier.weight(1.2f)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .fillMaxWidth()
                                .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                                .padding(horizontal = 4.dp, vertical = 4.dp)
                        ) {
                            IconButton(
                                onClick = {
                                    val step = if (selectedUnit == "Gram") 50.0 else if (quantity <= 1.0) 0.5 else 1.0
                                    if (quantity > step) quantity = (quantity - step).coerceAtLeast(if (selectedUnit == "Gram") 50.0 else 0.5)
                                },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(Icons.Default.Remove, contentDescription = "Azalt", modifier = Modifier.size(18.dp))
                            }
                            Text(
                                text = "${if (quantity % 1.0 == 0.0) quantity.toInt().toString() else quantity.toString()} $selectedUnit",
                                style = MaterialTheme.typography.titleMedium,
                                fontWeight = FontWeight.Bold,
                                modifier = Modifier.weight(1f),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                            IconButton(
                                onClick = {
                                    val step = if (selectedUnit == "Gram") 50.0 else if (quantity < 1.0) 0.5 else 1.0
                                    quantity += step
                                },
                                modifier = Modifier.size(36.dp)
                            ) {
                                Icon(Icons.Default.Add, contentDescription = "Artır", modifier = Modifier.size(18.dp))
                            }
                        }
                    }
                }

                // Quick Quantity Shortcuts
                Spacer(modifier = Modifier.height(8.dp))
                val quickQuantities = when (selectedUnit) {
                    "Kg" -> listOf(0.5, 1.0, 1.5, 2.0, 3.0, 5.0)
                    "Gram" -> listOf(100.0, 250.0, 400.0, 500.0, 750.0)
                    "Lt" -> listOf(0.5, 1.0, 1.5, 2.0, 5.0)
                    else -> listOf(1.0, 2.0, 3.0, 4.0, 5.0, 10.0, 30.0)
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    quickQuantities.take(6).forEach { qVal ->
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = if (quantity == qVal) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                            modifier = Modifier
                                .weight(1f)
                                .clickable { quantity = qVal }
                        ) {
                            Text(
                                text = if (qVal % 1.0 == 0.0) qVal.toInt().toString() else qVal.toString(),
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (quantity == qVal) Color.White else MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.padding(vertical = 6.dp),
                                textAlign = androidx.compose.ui.text.style.TextAlign.Center
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Estimated Price with Unit Price calculation
                OutlinedTextField(
                    value = estimatedPriceText,
                    onValueChange = { estimatedPriceText = it.filter { ch -> ch.isDigit() || ch == '.' || ch == ',' }.replace(',', '.') },
                    label = { Text("Tahmini Toplam Tutar (₺) (İsteğe Bağlı)") },
                    placeholder = { Text("Örn: 45.00") },
                    singleLine = true,
                    shape = RoundedCornerShape(12.dp),
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal, imeAction = ImeAction.Next),
                    modifier = Modifier.fillMaxWidth()
                )

                // Live Unit Price indicator
                val parsedEstPrice = estimatedPriceText.toDoubleOrNull()
                if (parsedEstPrice != null && parsedEstPrice > 0 && quantity > 0) {
                    val unitPrice = if (selectedUnit == "Gram") (parsedEstPrice / (quantity / 1000.0)) else (parsedEstPrice / quantity)
                    val unitName = if (selectedUnit == "Gram") "Kg" else selectedUnit
                    Spacer(modifier = Modifier.height(4.dp))
                    Surface(
                        shape = RoundedCornerShape(6.dp),
                        color = MaterialTheme.colorScheme.tertiaryContainer.copy(alpha = 0.4f),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = String.format(Locale.getDefault(), "💡 %s: ~₺%,.2f / %s", strings.unitPriceLabel, unitPrice, unitName),
                            style = MaterialTheme.typography.bodySmall,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.tertiary,
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Family Member Selector (Who added / requested)
                Text(
                    text = "Ekleyen / Talep Eden Aile Bireyi",
                    style = MaterialTheme.typography.labelMedium,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(6.dp))

                if (members.isNotEmpty()) {
                    FlowRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        members.forEach { member ->
                            val isSelected = member.id == selectedMemberId
                            Surface(
                                shape = RoundedCornerShape(20.dp),
                                color = if (isSelected) Color(member.avatarColorHex).copy(alpha = 0.2f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f),
                                border = if (isSelected) androidx.compose.foundation.BorderStroke(1.5.dp, Color(member.avatarColorHex)) else null,
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .clickable { selectedMemberId = member.id }
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Surface(
                                        shape = CircleShape,
                                        color = Color(member.avatarColorHex),
                                        modifier = Modifier.size(20.dp)
                                    ) {
                                        Box(contentAlignment = Alignment.Center) {
                                            Text(
                                                text = member.name.take(1).uppercase(),
                                                fontSize = 11.sp,
                                                fontWeight = FontWeight.Bold,
                                                color = Color.White
                                            )
                                        }
                                    }
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = member.name,
                                        fontSize = 12.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                    )
                                }
                            }
                        }
                    }
                } else {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                            .padding(10.dp)
                    ) {
                        Icon(Icons.Default.Person, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Aile Bireyi (Ayarlar ekranından üye ekleyebilirsiniz)",
                            style = MaterialTheme.typography.bodySmall
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Note Field
                OutlinedTextField(
                    value = note,
                    onValueChange = { note = it },
                    label = { Text("Not / Açıklama (İsteğe Bağlı)") },
                    placeholder = { Text("Örn: Yağsız olanından, 2 numara...") },
                    maxLines = 2,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.End,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text("İptal")
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Button(
                        onClick = {
                            if (name.isBlank()) {
                                nameError = true
                                return@Button
                            }
                            val targetMember = members.find { it.id == selectedMemberId } ?: currentUser
                            val estPrice = estimatedPriceText.toDoubleOrNull()
                            onSave(
                                name.trim(),
                                selectedCategory,
                                quantity,
                                selectedUnit,
                                estPrice,
                                note.ifBlank { null },
                                targetMember?.id ?: 0L,
                                targetMember?.name ?: "Aile Üyesi",
                                targetMember?.avatarColorHex ?: 0xFF00897B
                            )
                            onDismiss()
                        },
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.testTag("save_shopping_item_button")
                    ) {
                        Icon(Icons.Default.Check, contentDescription = null, modifier = Modifier.size(18.dp))
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(if (initialItem != null) "Güncelle" else "Listeye Kaydet")
                    }
                }
            }
        }
    }
}
