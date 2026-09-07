package com.example.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.TrendingDown
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
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
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ProductTrendSummary
import com.example.ui.i18n.StringsProvider
import java.util.Locale

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductPriceTrendChart(
    summary: ProductTrendSummary?,
    allProductNames: List<String>,
    selectedProduct: String,
    onSelectProduct: (String) -> Unit,
    strings: StringsProvider
) {
    var expandedDropdown by remember { mutableStateOf(false) }
    val animatedProgress = remember { Animatable(0f) }

    LaunchedEffect(summary?.productName) {
        animatedProgress.snapTo(0f)
        animatedProgress.animateTo(1f, animationSpec = tween(900))
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp)
            .testTag("product_price_trend_card"),
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
                    text = strings.tabPriceTrend,
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )

                if (summary != null) {
                    val isPriceIncreased = summary.changePercent >= 0
                    val badgeBg = if (isPriceIncreased) Color(0xFFFFEBEE) else Color(0xFFE8F5E9)
                    val badgeColor = if (isPriceIncreased) Color(0xFFD32F2F) else Color(0xFF2E7D32)

                    Surface(
                        color = badgeBg,
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (isPriceIncreased) Icons.Default.TrendingUp else Icons.Default.TrendingDown,
                                contentDescription = null,
                                tint = badgeColor,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = String.format(Locale.getDefault(), "%s%%%.1f", if (isPriceIncreased) "+" else "", summary.changePercent),
                                color = badgeColor,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Product Selector Exposed Dropdown Menu
            ExposedDropdownMenuBox(
                expanded = expandedDropdown,
                onExpandedChange = { expandedDropdown = it },
                modifier = Modifier.fillMaxWidth()
            ) {
                OutlinedTextField(
                    value = selectedProduct,
                    onValueChange = {},
                    readOnly = true,
                    label = { Text(strings.selectProductForTrend) },
                    trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedDropdown) },
                    modifier = Modifier
                        .menuAnchor()
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(12.dp)
                )

                ExposedDropdownMenu(
                    expanded = expandedDropdown,
                    onDismissRequest = { expandedDropdown = false }
                ) {
                    val defaultProducts = listOf(
                        "Süt 1L (Tam Yağlı)",
                        "Yumurta 30'lu",
                        "Beyaz Peynir 1Kg",
                        "Dana Kıyma 500g",
                        "Benzin 95 Oktan",
                        "Zeytinyağı 1L",
                        "Ekmek 250g"
                    )
                    val combinedNames = (defaultProducts + allProductNames).distinct()

                    combinedNames.forEach { name ->
                        DropdownMenuItem(
                            text = { Text(name) },
                            onClick = {
                                onSelectProduct(name)
                                expandedDropdown = false
                            }
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (summary == null || summary.points.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(180.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "Seçilen ürün için geçmiş fiyat verisi toplanıyor...",
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 12.sp
                    )
                }
            } else {
                // Price Trend Overview row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = "Başlangıç Fiyatı (Mart)",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = String.format(Locale.getDefault(), "%.2f ₺", summary.previousPrice),
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "Güncel Fiyat (Ağustos)",
                            fontSize = 10.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Text(
                            text = String.format(Locale.getDefault(), "%.2f ₺", summary.currentPrice),
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Custom Canvas Chart with Line, Gradient Fill and Data Points
                val points = summary.points
                val minPrice = (points.minOfOrNull { it.averageUnitPrice } ?: 0.0) * 0.85
                val maxPrice = (points.maxOfOrNull { it.averageUnitPrice } ?: 100.0) * 1.15
                val priceRange = (maxPrice - minPrice).coerceAtLeast(1.0)

                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(190.dp)
                        .padding(horizontal = 4.dp)
                ) {
                    Canvas(modifier = Modifier.fillMaxSize()) {
                        val width = size.width
                        val height = size.height - 35.dp.toPx()
                        val stepX = width / (points.size - 1).coerceAtLeast(1)

                        // Draw dotted horizontal grid lines
                        val gridPaint = PathEffect.dashPathEffect(floatArrayOf(10f, 10f), 0f)
                        for (i in 1..3) {
                            val y = height * (i / 4f)
                            drawLine(
                                color = Color.LightGray.copy(alpha = 0.4f),
                                start = Offset(0f, y),
                                end = Offset(width, y),
                                pathEffect = gridPaint,
                                strokeWidth = 1.dp.toPx()
                            )
                        }

                        // Build line and fill paths
                        val linePath = Path()
                        val fillPath = Path()
                        val computedPoints = mutableListOf<Offset>()

                        points.forEachIndexed { index, p ->
                            val x = index * stepX
                            val normalized = ((p.averageUnitPrice - minPrice) / priceRange).toFloat()
                            val y = height - (normalized * height * animatedProgress.value)
                            computedPoints.add(Offset(x, y))

                            if (index == 0) {
                                linePath.moveTo(x, y)
                                fillPath.moveTo(x, height)
                                fillPath.lineTo(x, y)
                            } else {
                                linePath.lineTo(x, y)
                                fillPath.lineTo(x, y)
                            }
                        }

                        fillPath.lineTo(computedPoints.last().x, height)
                        fillPath.close()

                        // Gradient fill under the trend curve
                        drawPath(
                            path = fillPath,
                            brush = Brush.verticalGradient(
                                colors = listOf(
                                    Color(0x5500897B),
                                    Color(0x0500897B)
                                )
                            )
                        )

                        // Main trend line
                        drawPath(
                            path = linePath,
                            color = Color(0xFF00796B),
                            style = Stroke(width = 3.dp.toPx(), cap = StrokeCap.Round)
                        )

                        // Draw point circles and price labels
                        val textPaint = android.graphics.Paint().apply {
                            color = android.graphics.Color.DKGRAY
                            textSize = 26f
                            textAlign = android.graphics.Paint.Align.CENTER
                            isAntiAlias = true
                        }

                        val monthPaint = android.graphics.Paint().apply {
                            color = android.graphics.Color.GRAY
                            textSize = 24f
                            textAlign = android.graphics.Paint.Align.CENTER
                            isAntiAlias = true
                        }

                        computedPoints.forEachIndexed { index, offset ->
                            val point = points[index]
                            // Inner filled dot
                            drawCircle(
                                color = Color.White,
                                radius = 5.dp.toPx(),
                                center = offset
                            )
                            drawCircle(
                                color = Color(0xFF004D40),
                                radius = 5.dp.toPx(),
                                center = offset,
                                style = Stroke(width = 2.dp.toPx())
                            )

                            // Price text above dot
                            drawContext.canvas.nativeCanvas.drawText(
                                String.format(Locale.getDefault(), "%.1f₺", point.averageUnitPrice),
                                offset.x,
                                offset.y - 12.dp.toPx(),
                                textPaint
                            )

                            // Month text at bottom
                            drawContext.canvas.nativeCanvas.drawText(
                                point.monthLabel.split(" ").firstOrNull() ?: point.monthLabel,
                                offset.x,
                                size.height - 4.dp.toPx(),
                                monthPaint
                            )
                        }
                    }
                }
            }
        }
    }
}
