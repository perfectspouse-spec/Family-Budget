package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Sms
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Switch
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.data.model.FamilyMember
import com.example.ui.i18n.StringsProvider

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddFamilyMemberDialog(
    strings: StringsProvider,
    onDismiss: () -> Unit,
    onAdd: (name: String, countryCode: String, phone: String, role: String, salary: Double, additionalIncome: Double, autoApprove: Boolean) -> Unit
) {
    var name by remember { mutableStateOf("") }
    var countryCode by remember { mutableStateOf("+90") }
    var countryCodeExpanded by remember { mutableStateOf(false) }
    var phoneNumber by remember { mutableStateOf("") }
    var role by remember { mutableStateOf("Aile Bireyi") }
    var roleDropdownExpanded by remember { mutableStateOf(false) }
    var salaryStr by remember { mutableStateOf("") }
    var additionalIncomeStr by remember { mutableStateOf("") }
    var autoApprove by remember { mutableStateOf(false) }

    var showSmsCodeSimulation by remember { mutableStateOf(false) }
    var smsVerificationCode by remember { mutableStateOf("4829") }
    var enteredSmsCode by remember { mutableStateOf("") }
    var isPhoneVerified by remember { mutableStateOf(false) }

    val countryCodes = listOf(
        "+90" to "Türkiye 🇹🇷 (+90)",
        "+1" to "USA / Canada 🇺🇸 (+1)",
        "+49" to "Germany 🇩🇪 (+49)",
        "+44" to "UK 🇬🇧 (+44)",
        "+33" to "France 🇫🇷 (+33)",
        "+34" to "Spain 🇪🇸 (+34)",
        "+31" to "Netherlands 🇳🇱 (+31)",
        "+971" to "UAE 🇦🇪 (+971)"
    )

    val roles = listOf(
        "Yönetici (Admin)",
        "Ortak Yönetici",
        "Aile Bireyi",
        "Çocuk",
        "Diğer"
    )

    if (showSmsCodeSimulation) {
        AlertDialog(
            onDismissRequest = { showSmsCodeSimulation = false },
            title = {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(Icons.Default.Sms, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text("SMS Doğrulama Onayı")
                }
            },
            text = {
                Column {
                    Text(
                        text = "$countryCode $phoneNumber numarasına onay kodu gönderildi. Aile bireyinin ortak bütçeyi kullanabilmesi için kodu girin:",
                        fontSize = 13.sp
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Surface(
                        color = MaterialTheme.colorScheme.primaryContainer,
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Text(
                            text = "Simüle Edilen SMS Kodu: 4829",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onPrimaryContainer,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                        )
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    OutlinedTextField(
                        value = enteredSmsCode,
                        onValueChange = { enteredSmsCode = it },
                        label = { Text("4 Haneli SMS Onay Kodu") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            },
            confirmButton = {
                Button(
                    onClick = {
                        if (enteredSmsCode == "4829" || enteredSmsCode.length >= 4) {
                            isPhoneVerified = true
                            autoApprove = true
                            showSmsCodeSimulation = false
                        }
                    }
                ) {
                    Text("Onayla")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showSmsCodeSimulation = false }) {
                    Text(strings.cancel)
                }
            }
        )
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
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
                    Text(
                        text = strings.addMember,
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.primary
                    )
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = strings.cancel)
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Name
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Ad Soyad / Unvan (Örn: Mehmet)") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("member_name_input"),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Country code & Phone Number with Country Code Dropdown
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ExposedDropdownMenuBox(
                        expanded = countryCodeExpanded,
                        onExpandedChange = { countryCodeExpanded = it },
                        modifier = Modifier.weight(1.1f)
                    ) {
                        OutlinedTextField(
                            value = countryCode,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Ülke Kodu") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = countryCodeExpanded) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = countryCodeExpanded,
                            onDismissRequest = { countryCodeExpanded = false }
                        ) {
                            countryCodes.forEach { (code, label) ->
                                DropdownMenuItem(
                                    text = { Text(label, fontSize = 12.sp) },
                                    onClick = {
                                        countryCode = code
                                        countryCodeExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = { phoneNumber = it },
                        label = { Text("Telefon Numarası") },
                        placeholder = { Text("5551234567") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier
                            .weight(1.6f)
                            .testTag("member_phone_input"),
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Role Dropdown
                ExposedDropdownMenuBox(
                    expanded = roleDropdownExpanded,
                    onExpandedChange = { roleDropdownExpanded = it },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = role,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Ailedeki Rolü") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = roleDropdownExpanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = roleDropdownExpanded,
                        onDismissRequest = { roleDropdownExpanded = false }
                    ) {
                        roles.forEach { r ->
                            DropdownMenuItem(
                                text = { Text(r) },
                                onClick = {
                                    role = r
                                    roleDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Monthly Salary & Additional Income
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = salaryStr,
                        onValueChange = { salaryStr = it },
                        label = { Text(strings.salary) },
                        placeholder = { Text("40000 ₺") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = additionalIncomeStr,
                        onValueChange = { additionalIncomeStr = it },
                        label = { Text(strings.additionalIncome) },
                        placeholder = { Text("5000 ₺") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Phone Verification / Approval section
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isPhoneVerified) Color(0xFFE8F5E9) else MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isPhoneVerified) "Telefon Onaylandı ✓" else "SMS Onay Durumu",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = if (isPhoneVerified) Color(0xFF2E7D32) else MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = if (isPhoneVerified) "Bu üye ortak bütçeyi kullanabilir." else "Numara sahibine SMS onayı gönderin.",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        if (!isPhoneVerified) {
                            Button(
                                onClick = {
                                    showSmsCodeSimulation = true
                                },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                            ) {
                                Text("SMS Gönder", fontSize = 12.sp)
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Action buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(strings.cancel)
                    }

                    Button(
                        onClick = {
                            val sal = salaryStr.toDoubleOrNull() ?: 0.0
                            val addInc = additionalIncomeStr.toDoubleOrNull() ?: 0.0
                            val finalName = if (name.isNotBlank()) name else "Aile Bireyi"
                            onAdd(
                                finalName,
                                countryCode,
                                phoneNumber,
                                role,
                                sal,
                                addInc,
                                isPhoneVerified
                            )
                        },
                        modifier = Modifier
                            .weight(1.3f)
                            .testTag("save_member_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(Icons.Default.Save, contentDescription = null)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("Bireyi Ekle")
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditFamilyMemberDialog(
    member: FamilyMember,
    strings: StringsProvider,
    onDismiss: () -> Unit,
    onSave: (FamilyMember) -> Unit,
    onDelete: (FamilyMember) -> Unit
) {
    var name by remember { mutableStateOf(member.name) }
    var countryCode by remember { mutableStateOf(member.countryCode) }
    var countryCodeExpanded by remember { mutableStateOf(false) }
    var phoneNumber by remember { mutableStateOf(member.phoneNumber) }
    var role by remember { mutableStateOf(member.role) }
    var roleDropdownExpanded by remember { mutableStateOf(false) }
    var salaryStr by remember { mutableStateOf(if (member.monthlySalary > 0) member.monthlySalary.toLong().toString() else "") }
    var additionalIncomeStr by remember { mutableStateOf(if (member.additionalIncome > 0) member.additionalIncome.toLong().toString() else "") }
    var isApproved by remember { mutableStateOf(member.isApproved) }
    var selectedColorHex by remember { mutableStateOf(member.avatarColorHex) }
    var showDeleteConfirmDialog by remember { mutableStateOf(false) }

    val colorOptions = listOf(
        0xFF00695C, // Teal
        0xFF8E24AA, // Purple
        0xFFFB8C00, // Orange
        0xFF0288D1, // Blue
        0xFFE91E63, // Pink
        0xFF43A047, // Green
        0xFF5E35B1, // Indigo
        0xFFC2185B  // Deep Rose
    )

    val countryCodes = listOf(
        "+90" to "Türkiye 🇹🇷 (+90)",
        "+1" to "USA / Canada 🇺🇸 (+1)",
        "+49" to "Germany 🇩🇪 (+49)",
        "+44" to "UK 🇬🇧 (+44)",
        "+33" to "France 🇫🇷 (+33)",
        "+34" to "Spain 🇪🇸 (+34)",
        "+31" to "Netherlands 🇳🇱 (+31)",
        "+971" to "UAE 🇦🇪 (+971)"
    )

    val roles = listOf(
        "Yönetici (Admin)",
        "Ortak Yönetici",
        "Eş",
        "Aile Bireyi",
        "Çocuk",
        "Diğer"
    )

    if (showDeleteConfirmDialog) {
        AlertDialog(
            onDismissRequest = { showDeleteConfirmDialog = false },
            title = { Text("Üyeyi Sil") },
            text = {
                Text("${member.name} isimli aile bireyini silmek istediğinize emin misiniz? Bu işlem geri alınamaz.")
            },
            confirmButton = {
                Button(
                    onClick = {
                        showDeleteConfirmDialog = false
                        onDelete(member)
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.error)
                ) {
                    Text("Evet, Sil")
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showDeleteConfirmDialog = false }) {
                    Text(strings.cancel)
                }
            }
        )
    }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            shape = RoundedCornerShape(24.dp),
            color = MaterialTheme.colorScheme.surface,
            tonalElevation = 6.dp
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
                        Box(
                            modifier = Modifier
                                .size(36.dp)
                                .background(Color(selectedColorHex), CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = (if (name.isNotBlank()) name else "A").take(1).uppercase(),
                                color = Color.White,
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Profili Düzenle",
                            style = MaterialTheme.typography.titleLarge,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    IconButton(onClick = onDismiss) {
                        Icon(Icons.Default.Close, contentDescription = strings.cancel)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Avatar Color Palette Selector
                Text(
                    text = "Profil Rengi Seçin:",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(6.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    colorOptions.forEach { col ->
                        val isSelected = selectedColorHex == col
                        Box(
                            modifier = Modifier
                                .size(32.dp)
                                .background(Color(col), CircleShape)
                                .clickable { selectedColorHex = col },
                            contentAlignment = Alignment.Center
                        ) {
                            if (isSelected) {
                                Icon(
                                    Icons.Default.Check,
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Name
                OutlinedTextField(
                    value = name,
                    onValueChange = { name = it },
                    label = { Text("Ad Soyad / Unvan") },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("edit_member_name_input"),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Country code & Phone Number
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    ExposedDropdownMenuBox(
                        expanded = countryCodeExpanded,
                        onExpandedChange = { countryCodeExpanded = it },
                        modifier = Modifier.weight(1.1f)
                    ) {
                        OutlinedTextField(
                            value = countryCode,
                            onValueChange = {},
                            readOnly = true,
                            label = { Text("Ülke") },
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = countryCodeExpanded) },
                            modifier = Modifier
                                .menuAnchor()
                                .fillMaxWidth(),
                            shape = RoundedCornerShape(12.dp)
                        )
                        ExposedDropdownMenu(
                            expanded = countryCodeExpanded,
                            onDismissRequest = { countryCodeExpanded = false }
                        ) {
                            countryCodes.forEach { (code, label) ->
                                DropdownMenuItem(
                                    text = { Text(label, fontSize = 12.sp) },
                                    onClick = {
                                        countryCode = code
                                        countryCodeExpanded = false
                                    }
                                )
                            }
                        }
                    }

                    OutlinedTextField(
                        value = phoneNumber,
                        onValueChange = { phoneNumber = it },
                        label = { Text("Telefon Numarası") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        modifier = Modifier
                            .weight(1.6f)
                            .testTag("edit_member_phone_input"),
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Role Dropdown
                ExposedDropdownMenuBox(
                    expanded = roleDropdownExpanded,
                    onExpandedChange = { roleDropdownExpanded = it },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    OutlinedTextField(
                        value = role,
                        onValueChange = {},
                        readOnly = true,
                        label = { Text("Ailedeki Rolü") },
                        trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = roleDropdownExpanded) },
                        modifier = Modifier
                            .menuAnchor()
                            .fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp)
                    )
                    ExposedDropdownMenu(
                        expanded = roleDropdownExpanded,
                        onDismissRequest = { roleDropdownExpanded = false }
                    ) {
                        roles.forEach { r ->
                            DropdownMenuItem(
                                text = { Text(r) },
                                onClick = {
                                    role = r
                                    roleDropdownExpanded = false
                                }
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Monthly Salary & Additional Income
                Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = salaryStr,
                        onValueChange = { salaryStr = it },
                        label = { Text(strings.salary) },
                        placeholder = { Text("0 ₺") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    )

                    OutlinedTextField(
                        value = additionalIncomeStr,
                        onValueChange = { additionalIncomeStr = it },
                        label = { Text(strings.additionalIncome) },
                        placeholder = { Text("0 ₺") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Approval Status Toggle
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isApproved) Color(0xFFE8F5E9) else MaterialTheme.colorScheme.surfaceVariant
                    )
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = if (isApproved) "Ortak Bütçe Yetkisi: Açık ✓" else "Ortak Bütçe Yetkisi: Beklemede",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = if (isApproved) Color(0xFF2E7D32) else MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = if (isApproved) "Bu üye harcama ekleyebilir ve görebilir." else "Üyenin harcama eklemesi engellidir.",
                                fontSize = 11.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                        Switch(
                            checked = isApproved,
                            onCheckedChange = { isApproved = it }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Action Buttons
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    IconButton(
                        onClick = { showDeleteConfirmDialog = true },
                        modifier = Modifier.size(44.dp)
                    ) {
                        Icon(
                            Icons.Default.Delete,
                            contentDescription = "Üyeyi Sil",
                            tint = MaterialTheme.colorScheme.error
                        )
                    }

                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier.weight(1f),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(strings.cancel)
                    }

                    Button(
                        onClick = {
                            val sal = salaryStr.toDoubleOrNull() ?: 0.0
                            val addInc = additionalIncomeStr.toDoubleOrNull() ?: 0.0
                            val updatedMember = member.copy(
                                name = if (name.isNotBlank()) name else member.name,
                                countryCode = countryCode,
                                phoneNumber = phoneNumber,
                                role = role,
                                monthlySalary = sal,
                                additionalIncome = addInc,
                                avatarColorHex = selectedColorHex,
                                isApproved = isApproved
                            )
                            onSave(updatedMember)
                        },
                        modifier = Modifier
                            .weight(1.3f)
                            .testTag("update_member_button"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                    ) {
                        Icon(Icons.Default.Save, contentDescription = null, modifier = Modifier.size(16.dp))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Güncelle")
                    }
                }
            }
        }
    }
}
