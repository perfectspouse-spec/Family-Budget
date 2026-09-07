package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Equalizer
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingFlat
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.WeeklyComparisonStat
import com.example.ui.i18n.StringsProvider
import java.util.Locale
import kotlin.math.abs

@Composable
fun Last7DaysSummaryCard(
    stat: WeeklyComparisonStat,
    strings: StringsProvider,
    onDetailsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val isIncrease = stat.differenceAmount > 0
    val isDecrease = stat.differenceAmount < 0
    val isZero = stat.differenceAmount == 0.0

    val isDark = MaterialTheme.colorScheme.surface.let {
        // Simple luminance check or dynamic theme check
        it.red * 0.299 + it.green * 0.587 + it.blue * 0.114 < 0.5
    }

    // Visual colors for comparison
    val trendColor = when {
        isIncrease -> if (isDark) Color(0xFFFF8A80) else Color(0xFFD32F2F)
        isDecrease -> if (isDark) Color(0xFF81C784) else Color(0xFF2E7D32)
        else -> if (isDark) Color(0xFF90A4AE) else Color(0xFF546E7A)
    }

    val trendBgColor = when {
        isIncrease -> if (isDark) Color(0xFF3E1414) else Color(0xFFFFEBEE)
        isDecrease -> if (isDark) Color(0xFF143319) else Color(0xFFE8F5E9)
        else -> if (isDark) Color(0xFF1E282C) else Color(0xFFECEFF1)
    }

    val maxDailyAmount = (stat.dailyBreakdown.maxOfOrNull { it.amount } ?: 0.0).coerceAtLeast(1.0)

    Card(
        modifier = modifier
            .testTag("last_7_days_summary_card")
            .clickable { onDetailsClick() },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            // Header: Title & Action
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .background(
                                color = MaterialTheme.colorScheme.primaryContainer,
                                shape = RoundedCornerShape(10.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = strings.last7DaysTitle,
                            style = MaterialTheme.typography.titleSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "${strings.previousWeekComparison} Karşılaştırmalı",
                            style = MaterialTheme.typography.bodySmall,
                            fontSize = 11.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = MaterialTheme.colorScheme.primary.copy(alpha = 0.08f),
                    modifier = Modifier.clickable { onDetailsClick() }
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Raporlar",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Icon(
                            imageVector = Icons.Default.ArrowForward,
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.primary,
                            modifier = Modifier.size(12.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Main Metrics Section: Total Spent + Comparison Badge
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = strings.last7DaysSpending,
                        style = MaterialTheme.typography.labelMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 11.sp
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = String.format(Locale.getDefault(), "%,.0f ₺", stat.currentWeekSpent),
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.onSurface,
                        letterSpacing = (-0.5).sp
                    )
                    Text(
                        text = "${stat.currentWeekReceiptCount} işlem • Ort. ${String.format(Locale.getDefault(), "%,.0f ₺/gün", stat.currentWeekAverageDaily)}",
                        style = MaterialTheme.typography.bodySmall,
                        fontSize = 11.sp,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }

                // Comparison Badge
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = trendBgColor,
                    modifier = Modifier.testTag("weekly_change_badge")
                ) {
                    Column(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
                        horizontalAlignment = Alignment.End
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = when {
                                    isIncrease -> Icons.Default.ArrowUpward
                                    isDecrease -> Icons.Default.ArrowDownward
                                    else -> Icons.Default.TrendingFlat
                                },
                                contentDescription = null,
                                tint = trendColor,
                                modifier = Modifier.size(15.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = when {
                                    isIncrease -> "+%${String.format(Locale.getDefault(), "%.1f", stat.changePercentage)}"
                                    isDecrease -> "-%${String.format(Locale.getDefault(), "%.1f", abs(stat.changePercentage))}"
                                    else -> "%0.0"
                                },
                                fontWeight = FontWeight.ExtraBold,
                                fontSize = 13.sp,
                                color = trendColor
                            )
                        }
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = when {
                                isIncrease -> "+${String.format(Locale.getDefault(), "%,.0f ₺", stat.differenceAmount)}"
                                isDecrease -> "-${String.format(Locale.getDefault(), "%,.0f ₺", abs(stat.differenceAmount))}"
                                else -> "0 ₺"
                            },
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = trendColor
                        )
                        Text(
                            text = "Önceki: ${String.format(Locale.getDefault(), "%,.0f ₺", stat.previousWeekSpent)}",
                            fontSize = 9.sp,
                            color = trendColor.copy(alpha = 0.85f)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Mini 7-Day Bar Chart / Sparkline Breakdown
            if (stat.dailyBreakdown.isNotEmpty()) {
                Surface(
                    shape = RoundedCornerShape(14.dp),
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 10.dp)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(64.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            stat.dailyBreakdown.forEach { point ->
                                val ratio = if (maxDailyAmount > 0) (point.amount / maxDailyAmount).toFloat() else 0f
                                val animatedHeightRatio by animateFloatAsState(
                                    targetValue = ratio.coerceIn(0.08f, 1f),
                                    animationSpec = tween(durationMillis = 500),
                                    label = "bar_height"
                                )

                                val isToday = point == stat.dailyBreakdown.lastOrNull()

                                Column(
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    modifier = Modifier.weight(1f)
                                ) {
                                    // Amount text above bar if spent > 0
                                    if (point.amount > 0) {
                                        Text(
                                            text = if (point.amount >= 1000) "${(point.amount / 1000).toInt()}k" else "${point.amount.toInt()}",
                                            fontSize = 8.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = if (isToday) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant
                                        )
                                    } else {
                                        Spacer(modifier = Modifier.height(10.dp))
                                    }

                                    Spacer(modifier = Modifier.height(2.dp))

                                    // Bar
                                    Box(
                                        modifier = Modifier
                                            .width(16.dp)
                                            .height((40 * animatedHeightRatio).dp)
                                            .clip(RoundedCornerShape(topStart = 4.dp, topEnd = 4.dp, bottomStart = 2.dp, bottomEnd = 2.dp))
                                            .background(
                                                if (point.amount > 0) {
                                                    if (isToday) {
                                                        Brush.verticalGradient(
                                                            listOf(
                                                                Color(0xFF00897B),
                                                                Color(0xFF004D40)
                                                            )
                                                        )
                                                    } else {
                                                        Brush.verticalGradient(
                                                            listOf(
                                                                MaterialTheme.colorScheme.primary.copy(alpha = 0.75f),
                                                                MaterialTheme.colorScheme.primary
                                                            )
                                                        )
                                                    }
                                                } else {
                                                    Brush.verticalGradient(
                                                        listOf(
                                                            MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f),
                                                            MaterialTheme.colorScheme.outlineVariant.copy(alpha = 0.4f)
                                                        )
                                                    )
                                                }
                                            )
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        // Day Labels Row
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            stat.dailyBreakdown.forEach { point ->
                                val isToday = point == stat.dailyBreakdown.lastOrNull()
                                Text(
                                    text = if (isToday) "Bugün" else point.dayLabel,
                                    fontSize = 9.sp,
                                    fontWeight = if (isToday) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isToday) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onSurfaceVariant,
                                    textAlign = TextAlign.Center,
                                    modifier = Modifier.weight(1f)
                                )
                            }
                        }
                    }
                }
            }

            // Bottom highlight tag (e.g. Top category or savings notice)
            if (stat.topCategoryThisWeek.isNotBlank()) {
                Spacer(modifier = Modifier.height(10.dp))
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f),
                            shape = RoundedCornerShape(8.dp)
                        )
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("🏷️", fontSize = 11.sp)
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "En çok harcanan kategori:",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = stat.topCategoryThisWeek,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }

                    Text(
                        text = String.format(Locale.getDefault(), "%,.0f ₺", stat.topCategorySpentThisWeek),
                        fontSize = 11.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }
        }
    }
}
