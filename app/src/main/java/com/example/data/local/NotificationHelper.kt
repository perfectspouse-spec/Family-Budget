package com.example.data.local

import android.Manifest
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.example.MainActivity
import com.example.R
import java.util.Locale

object NotificationHelper {

    const val CHANNEL_ID_BUDGET = "family_budget_alerts_channel"
    const val NOTIFICATION_ID_WARNING = 1001
    const val NOTIFICATION_ID_EXCEEDED = 1002

    fun createNotificationChannel(context: Context) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Bütçe ve Harcama Uyarıları"
            val descriptionText = "Aylık aile bütçesi limitine yaklaşıldığında veya limit aşıldığında gönderilen anlık bildirimler"
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(CHANNEL_ID_BUDGET, name, importance).apply {
                description = descriptionText
                enableVibration(true)
                vibrationPattern = longArrayOf(0, 250, 150, 250)
            }
            val notificationManager: NotificationManager =
                context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }

    fun showApproachingBudgetNotification(
        context: Context,
        currentSpent: Double,
        limit: Double,
        percent: Int,
        remainingAmount: Double = (limit - currentSpent).coerceAtLeast(0.0),
        dailySpendable: Double = 0.0,
        daysLeft: Int = 1
    ) {
        createNotificationChannel(context)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val title = "⚠️ Bütçe Uyarısı: %$percent Limite Ulaşıldı!"
        val dailyAdvice = if (dailySpendable > 0) " Kalan $daysLeft gün için günlük önerilen harcama tavanı: %,.0f ₺.".format(dailySpendable) else ""
        val content = String.format(
            Locale.getDefault(),
            "Aylık toplam çıkış %,.0f ₺ oldu (Limit: %,.0f ₺). Kalan bütçe: %,.0f ₺.%s",
            currentSpent,
            limit,
            remainingAmount,
            dailyAdvice
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID_BUDGET)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(content)
            .setStyle(NotificationCompat.BigTextStyle().bigText(content))
            .setPriority(NotificationCompat.PRIORITY_HIGH)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
            ) {
                NotificationManagerCompat.from(context).notify(NOTIFICATION_ID_WARNING, builder.build())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    fun showExceededBudgetNotification(
        context: Context,
        currentSpent: Double,
        limit: Double,
        overspentAmount: Double
    ) {
        createNotificationChannel(context)

        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent = PendingIntent.getActivity(
            context,
            0,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val title = "🚨 KRİTİK: Aile Bütçe Limiti Aşıldı!"
        val content = String.format(
            Locale.getDefault(),
            "Belirlenen %,.0f ₺ bütçe limiti aşıldı! Toplam harcama: %,.0f ₺. Aşım miktarı: %,.0f ₺. Lütfen acil harcamaları gözden geçirin.",
            limit,
            currentSpent,
            overspentAmount
        )

        val builder = NotificationCompat.Builder(context, CHANNEL_ID_BUDGET)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentTitle(title)
            .setContentText(content)
            .setStyle(NotificationCompat.BigTextStyle().bigText(content))
            .setPriority(NotificationCompat.PRIORITY_MAX)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)

        try {
            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.TIRAMISU ||
                ActivityCompat.checkSelfPermission(context, Manifest.permission.POST_NOTIFICATIONS) == PackageManager.PERMISSION_GRANTED
            ) {
                NotificationManagerCompat.from(context).notify(NOTIFICATION_ID_EXCEEDED, builder.build())
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}
