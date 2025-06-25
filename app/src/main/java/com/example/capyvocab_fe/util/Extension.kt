package com.example.capyvocab_fe.util

import android.icu.text.NumberFormat
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import java.text.SimpleDateFormat
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.util.Calendar
import java.util.Date
import java.util.Locale

fun formatCurrency(amount: Double): String {
    val formatter = NumberFormat.getCurrencyInstance(Locale("vi", "VN"))
    return formatter.format(amount)
}

@RequiresApi(Build.VERSION_CODES.O)
fun formatDate(dateString: String?): String {
    return try {
        if (dateString.isNullOrBlank()) return "Không rõ"
        val zonedDateTime = ZonedDateTime.parse(dateString)
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        zonedDateTime.format(formatter)
    } catch (e: Exception) {
        "Không rõ"
    }
}

object DateUtils {
    private val defaultDateFormat = SimpleDateFormat("MMM dd, yyyy HH:mm", Locale.getDefault())

    /**
     * Format a date to a readable string
     */
    fun formatDate(date: Date?): String {
        return date?.let { defaultDateFormat.format(it) } ?: ""
    }

    /**
     * Get a relative time string (e.g., "2 hours ago")
     */
    fun getRelativeTimeSpan(date: Date?): String {
        if (date == null) return ""

        val now = Calendar.getInstance().time
        val diff = now.time - date.time
        Log.d("DateUtils", "createdAt: $date, now: $now")
        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24

        return when {
            seconds < 60 -> "vừa xong"
            minutes < 60 -> "$minutes phút trước"
            hours < 24 -> "$hours giờ trước"
            days == 1L -> "hôm qua"
            days < 7 -> "$days ngày trước"
            days < 30 -> "${days / 7} tuần trước"
            else -> defaultDateFormat.format(date)
        }
    }
}