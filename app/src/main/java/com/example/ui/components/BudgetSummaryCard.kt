package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.Group
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.NotificationsActive
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.Today
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.i18n.StringsProvider
import com.example.ui.viewmodel.AlertType
import com.example.ui.viewmodel.BudgetUiState
import java.util.Locale

@Composable
fun BudgetSummaryCard(
    state: BudgetUiState,
    strings: StringsProvider,
    onFamilyClick: () -> Unit,
    onLimitClick: () -> Unit,
    modifier: Modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp, vertical = 6.dp)
) {
    var showFormulaDetails by remember { mutableStateOf(false) }

    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF00382E),
            Color(0xFF004D40),
            Color(0xFF00695C)
        )
    )

    Card(
        modifier = modifier
            .testTag("budget_summary_card"),
        shape = RoundedCornerShape(24.dp),
        colors = CardDefaults.cardColors(containerColor = Color.Transparent),
        elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .background(gradientBrush)
                .padding(18.dp)
        ) {
            Column {
                // Top Bar: Active User badge & Family members count
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0x33FFFFFF))
                            .clickable { onFamilyClick() }
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(9.dp)
                                .background(Color(0xFF69F0AE), CircleShape)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = state.currentUser?.name ?: "Aile Bütçesi",
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }

                    Surface(
                        color = Color(0x33FFFFFF),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.clickable { onLimitClick() }
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.Group,
                                contentDescription = null,
                                tint = Color.White,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(5.dp))
                            Text(
                                text = "${state.members.size} Üye",
                                color = Color.White,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Formula Header Badge
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0x2E000000))
                        .clickable { showFormulaDetails = !showFormulaDetails }
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = Color(0xFF69F0AE),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Kalan Bütçe = Gelir - Sabitler - Fişler",
                            color = Color(0xFFE0F2F1),
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                    Text(
                        text = if (showFormulaDetails) "Gizle ▴" else "Detay ▾",
                        color = Color(0xFF80CBC4),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                // Interactive Formula Breakdown (Expandable)
                AnimatedVisibility(visible = showFormulaDetails) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 8.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0x3D000000))
                            .padding(10.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("1. Toplam Aylık Gelir:", color = Color(0xFFB2DFDB), fontSize = 11.sp)
                            Text(String.format(Locale.getDefault(), "+%,.0f ₺", state.totalIncome), color = Color(0xFF69F0AE), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("2. Sabit Giderler (Kira+3 Kart+Aidat):", color = Color(0xFFB2DFDB), fontSize = 11.sp)
                            Text(String.format(Locale.getDefault(), "-%,.0f ₺", state.totalFixedExpenses), color = Color(0xFFFF8A80), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("3. Taranan Fişler & Market Harcamaları:", color = Color(0xFFB2DFDB), fontSize = 11.sp)
                            Text(String.format(Locale.getDefault(), "-%,.0f ₺", state.totalSpentThisMonth), color = Color(0xFFFFCC80), fontSize = 11.sp, fontWeight = FontWeight.Bold)
                        }
                        Box(modifier = Modifier.fillMaxWidth().height(1.dp).padding(vertical = 4.dp).background(Color(0x33FFFFFF)))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text("= Kalan Net Kullanılabilir Bütçe:", color = Color.White, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            Text(String.format(Locale.getDefault(), "= %,.0f ₺", state.remainingBudget), color = if (state.remainingBudget < 0) Color(0xFFFF5252) else Color(0xFF69F0AE), fontSize = 12.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Main Remaining Budget Large KPI
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Bottom
                ) {
                    Column {
                        Text(
                            text = strings.remainingBudget,
                            color = Color(0xFFB2DFDB),
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = String.format(Locale.getDefault(), "%,.0f ₺", state.remainingBudget),
                            color = if (state.remainingBudget < 0) Color(0xFFFF5252) else Color.White,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    // Daily Spendable Budget Pill
                    Surface(
                        color = Color(0x33000000),
                        shape = RoundedCornerShape(12.dp),
                        border = androidx.compose.foundation.BorderStroke(1.dp, Color(0x33FFFFFF))
                    ) {
                        Column(
                            modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                            horizontalAlignment = Alignment.End
                        ) {
                            Text(
                                text = "Günlük Harcanabilir",
                                color = Color(0xFF80CBC4),
                                fontSize = 10.sp
                            )
                            Text(
                                text = String.format(Locale.getDefault(), "%,.0f ₺/gün", state.dailyAvailableBudget),
                                color = Color(0xFF69F0AE),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "Kalan ${state.daysRemainingInMonth} gün",
                                color = Color(0xAAFFFFFF),
                                fontSize = 9.sp
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // 3-Segment Proportional Budget Flow Bar
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "Toplam Çıkış: ${String.format(Locale.getDefault(), "%,.0f ₺", state.totalOutflow)}",
                        color = Color(0xFFE0F2F1),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "Kullanım: %${state.usagePercentage.toInt()}",
                        color = if (state.usagePercentage > 100) Color(0xFFFF8A80) else Color(0xFFE0F2F1),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Segmented Progress Bar (Sabit Giderler + Taranan Fişler + Kalan)
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(10.dp)
                        .clip(RoundedCornerShape(5.dp))
                        .background(Color(0x33FFFFFF))
                ) {
                    val fixedWeight = (state.fixedExpensesRatio / 100f).coerceIn(0f, 1f)
                    val receiptsWeight = (state.receiptsExpensesRatio / 100f).coerceIn(0f, 1f)
                    val remainingWeight = (state.savingsRatio / 100f).coerceIn(0f, 1f)

                    Row(modifier = Modifier.fillMaxWidth().height(10.dp)) {
                        if (fixedWeight > 0.01f) {
                            Box(
                                modifier = Modifier
                                    .weight(fixedWeight)
                                    .height(10.dp)
                                    .background(Color(0xFFE57373))
                            )
                        }
                        if (receiptsWeight > 0.01f) {
                            Box(
                                modifier = Modifier
                                    .weight(receiptsWeight)
                                    .height(10.dp)
                                    .background(Color(0xFFFFB74D))
                            )
                        }
                        if (remainingWeight > 0.01f) {
                            Box(
                                modifier = Modifier
                                    .weight(remainingWeight)
                                    .height(10.dp)
                                    .background(Color(0xFF69F0AE))
                            )
                        }
                    }
                }

                // Legend for Segmented Bar
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 4.dp),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(7.dp).background(Color(0xFFE57373), CircleShape))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Sabit (%${state.fixedExpensesRatio.toInt()})", color = Color(0xCCFFFFFF), fontSize = 9.sp)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(7.dp).background(Color(0xFFFFB74D), CircleShape))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Fişler (%${state.receiptsExpensesRatio.toInt()})", color = Color(0xCCFFFFFF), fontSize = 9.sp)
                    }
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(modifier = Modifier.size(7.dp).background(Color(0xFF69F0AE), CircleShape))
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Kalan Net (%${state.savingsRatio.toInt()})", color = Color(0xCCFFFFFF), fontSize = 9.sp)
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Three Columns Grid: Total Income vs Fixed Expenses vs Tracked Receipts
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    // 1. Total Monthly Income
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0x22000000))
                            .clickable { onFamilyClick() }
                            .padding(8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.ArrowDownward,
                                contentDescription = null,
                                tint = Color(0xFF69F0AE),
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "Aylık Gelir",
                                color = Color(0xFFB2DFDB),
                                fontSize = 10.sp,
                                maxLines = 1
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = String.format(Locale.getDefault(), "%,.0f ₺", state.totalIncome),
                            color = Color.White,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                        Text(
                            text = "Maaş + Kira",
                            color = Color(0xAAFFFFFF),
                            fontSize = 8.sp,
                            maxLines = 1
                        )
                    }

                    // 2. Fixed Expenses
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0x22000000))
                            .clickable { onFamilyClick() }
                            .padding(8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CreditCard,
                                contentDescription = null,
                                tint = Color(0xFFFF8A80),
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "Sabit Gider",
                                color = Color(0xFFB2DFDB),
                                fontSize = 10.sp,
                                maxLines = 1
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = String.format(Locale.getDefault(), "%,.0f ₺", state.totalFixedExpenses),
                            color = Color(0xFFFFCDD2),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                        Text(
                            text = "Kira + 3 Kart",
                            color = Color(0xAAFFFFFF),
                            fontSize = 8.sp,
                            maxLines = 1
                        )
                    }

                    // 3. Tracked Receipts & Variable Expenses
                    Column(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0x22000000))
                            .clickable { onLimitClick() }
                            .padding(8.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.Receipt,
                                contentDescription = null,
                                tint = Color(0xFFFFD54F),
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(2.dp))
                            Text(
                                text = "Fiş Harcama",
                                color = Color(0xFFB2DFDB),
                                fontSize = 10.sp,
                                maxLines = 1
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = String.format(Locale.getDefault(), "%,.0f ₺", state.totalSpentThisMonth),
                            color = Color(0xFFFFE082),
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 1
                        )
                        Text(
                            text = "${state.receipts.size} Taranan Fiş",
                            color = Color(0xAAFFFFFF),
                            fontSize = 8.sp,
                            maxLines = 1
                        )
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Net Disposable Line (Income - Fixed Expenses before variable spending)
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(Color(0x2E000000))
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Net Harcanabilir Havuz (Gelir - Sabitler):",
                        color = Color(0xFFE0F2F1),
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = String.format(Locale.getDefault(), "%,.0f ₺", state.netDisposableIncome),
                        color = Color(0xFF69F0AE),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                // Alert Notification Box
                Spacer(modifier = Modifier.height(10.dp))
                val alertBgColor = when (state.alertType) {
                    AlertType.NORMAL -> Color(0x332E7D32)
                    AlertType.WARNING -> Color(0x44F57C00)
                    AlertType.EXCEEDED -> Color(0x55D32F2F)
                }
                val alertIcon = when (state.alertType) {
                    AlertType.NORMAL -> Icons.Default.CheckCircle
                    AlertType.WARNING -> Icons.Default.NotificationsActive
                    AlertType.EXCEEDED -> Icons.Default.Warning
                }
                val alertTextColor = when (state.alertType) {
                    AlertType.NORMAL -> Color(0xFFB9F6CA)
                    AlertType.WARNING -> Color(0xFFFFE57F)
                    AlertType.EXCEEDED -> Color(0xFFFF8A80)
                }

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(alertBgColor)
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = alertIcon,
                        contentDescription = null,
                        tint = alertTextColor,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = state.alertMessage,
                        color = alertTextColor,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        lineHeight = 15.sp
                    )
                }
            }
        }
    }
}
