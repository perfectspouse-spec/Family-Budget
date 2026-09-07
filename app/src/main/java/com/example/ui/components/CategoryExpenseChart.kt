package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.filled.DirectionsCar
import androidx.compose.material.icons.filled.Fastfood
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.LocalHospital
import androidx.compose.material.icons.filled.LocalPharmacy
import androidx.compose.material.icons.filled.Receipt
import androidx.compose.material.icons.filled.Restaurant
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material.icons.filled.TheaterComedy
import androidx.compose.material.icons.filled.Watch
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.CategoryMonthlyStat
import com.example.data.model.CategoryWeeklyStat
import com.example.ui.i18n.StringsProvider
import java.util.Locale

val CategoryColors = listOf(
    Color(0xFF00897B),
    Color(0xFFE53935),
    Color(0xFF3949AB),
    Color(0xFFFB8C00),
    Color(0xFF8E24AA),
    Color(0xFF43A047),
    Color(0xFF00ACC1),
    Color(0xFFFFB300),
    Color(0xFF6D4C41),
    Color(0xFF5E35B1)
)

fun getCategoryIcon(category: String): ImageVector {
    return when {
        category.contains("Market", ignoreCase = true) || category.contains("Gıda", ignoreCase = true) -> Icons.Default.ShoppingCart
        category.contains("Akaryakıt", ignoreCase = true) || category.contains("Benzin", ignoreCase = true) || category.contains("Ulaşım", ignoreCase = true) -> Icons.Default.DirectionsCar
        category.contains("Giyim", ignoreCase = true) || category.contains("Moda", ignoreCase = true) -> Icons.Default.ShoppingBag
        category.contains("Fatura", ignoreCase = true) || category.contains("Aidat", ignoreCase = true) -> Icons.Default.Receipt
        category.contains("Sağlık", ignoreCase = true) || category.contains("Eczane", ignoreCase = true) -> Icons.Default.LocalPharmacy
        category.contains("Restoran", ignoreCase = true) || category.contains("Kafe", ignoreCase = true) -> Icons.Default.Restaurant
        category.contains("Ev", ignoreCase = true) -> Icons.Default.Home
        category.contains("Eğlence", ignoreCase = true) -> Icons.Default.TheaterComedy
        else -> Icons.Default.Fastfood
    }
}

@Composable
fun MonthlyCategoryDonutChart(
    stats: List<CategoryMonthlyStat>,
    strings: StringsProvider
) {
    val animatedProgress = remember { Animatable(0f) }
    var selectedCategoryIndex by remember { mutableStateOf<Int?>(null) }

    LaunchedEffect(stats) {
        animatedProgress.snapTo(0f)
        animatedProgress.animateTo(1f, animationSpec = tween(900))
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = strings.tabCategoryMonthly,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface
                )
                if (stats.isNotEmpty()) {
                    Text(
                        text = "${stats.size} Kategori",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (stats.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(24.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Bu ay için henüz harcama kaydı bulunmuyor.",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 13.sp
                    )
                }
            } else {
                val totalSpent = stats.sumOf { it.totalSpent }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // Pie/Donut Chart Canvas
                    Box(
                        modifier = Modifier
                            .size(140.dp)
                            .padding(4.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Canvas(modifier = Modifier.fillMaxWidth()) {
                            var startAngle = -90f
                            val strokeWidth = 26.dp.toPx()

                            if (totalSpent <= 0.0) {
                                drawCircle(
                                    color = Color.LightGray.copy(alpha = 0.3f),
                                    style = Stroke(width = strokeWidth)
                                )
                            } else {
                                stats.forEachIndexed { index, item ->
                                    val sweep = ((item.totalSpent / totalSpent) * 360f).toFloat() * animatedProgress.value
                                    val isSelected = selectedCategoryIndex == index
                                    val color = CategoryColors[index % CategoryColors.size]
                                    val actualStroke = if (isSelected) strokeWidth + 6.dp.toPx() else strokeWidth

                                    drawArc(
                                        color = color,
                                        startAngle = startAngle,
                                        sweepAngle = sweep,
                                        useCenter = false,
                                        style = Stroke(width = actualStroke, cap = StrokeCap.Butt),
                                        topLeft = Offset(strokeWidth / 2, strokeWidth / 2),
                                        size = Size(size.width - strokeWidth, size.height - strokeWidth)
                                    )
                                    startAngle += sweep
                                }
                            }
                        }

                        // Center Info Box
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            val activeStat = selectedCategoryIndex?.let { stats.getOrNull(it) }
                            if (activeStat != null) {
                                Text(
                                    text = activeStat.category.take(12),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Medium,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                                    maxLines = 1
                                )
                                Text(
                                    text = "%${(activeStat.percentage * 100).toInt()}",
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            } else {
                                Text(
                                    text = "Toplam",
                                    fontSize = 10.sp,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                                Text(
                                    text = String.format(Locale.getDefault(), "%,.0f₺", totalSpent),
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.width(14.dp))

                    // Category Legend List
                    Column(modifier = Modifier.weight(1f)) {
                        stats.forEachIndexed { index, stat ->
                            val color = CategoryColors[index % CategoryColors.size]
                            val isSelected = selectedCategoryIndex == index

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(if (isSelected) MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.4f) else Color.Transparent)
                                    .clickable {
                                        selectedCategoryIndex = if (selectedCategoryIndex == index) null else index
                                    }
                                    .padding(vertical = 4.dp, horizontal = 4.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.weight(1f, fill = false)
                                ) {
                                    Box(
                                        modifier = Modifier
                                            .size(9.dp)
                                            .background(color, CircleShape)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Icon(
                                        imageVector = getCategoryIcon(stat.category),
                                        contentDescription = null,
                                        modifier = Modifier.size(13.dp),
                                        tint = color
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = stat.category,
                                        fontSize = 11.sp,
                                        fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                        color = MaterialTheme.colorScheme.onSurface,
                                        maxLines = 1
                                    )
                                }
                                Text(
                                    text = String.format(Locale.getDefault(), "%,.0f₺ (%%%d)", stat.totalSpent, (stat.percentage * 100).toInt()),
                                    fontSize = 11.sp,
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

@Composable
fun WeeklyCategoryBarChart(
    weeklyStats: List<CategoryWeeklyStat>,
    strings: StringsProvider
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(20.dp)) {
            Text(
                text = strings.tabCategoryWeekly,
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(16.dp))

            val weeks = listOf(1, 2, 3, 4)
            val weekTotals = weeks.map { w ->
                w to weeklyStats.filter { it.weekNumber == w }.sumOf { it.totalSpent }
            }
            val maxSpent = weekTotals.maxOfOrNull { it.second }?.takeIf { it > 0 } ?: 1000.0

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(140.dp)
                    .padding(horizontal = 8.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.Bottom
            ) {
                weekTotals.forEach { (weekNum, total) ->
                    val barHeightFraction = (total / maxSpent).toFloat().coerceIn(0.05f, 1f)

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.width(60.dp)
                    ) {
                        Text(
                            text = String.format(Locale.getDefault(), "%.0f₺", total),
                            fontSize = 9.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Box(
                            modifier = Modifier
                                .width(28.dp)
                                .height((100 * barHeightFraction).dp)
                                .clip(RoundedCornerShape(topStart = 6.dp, topEnd = 6.dp))
                                .background(
                                    if (weekNum == 4) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.primary.copy(alpha = 0.45f)
                                )
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "$weekNum. Hafta",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}
