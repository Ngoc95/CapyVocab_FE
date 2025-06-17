package com.example.capyvocab_fe.util

import android.icu.text.NumberFormat
import android.os.Build
import androidx.annotation.RequiresApi
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
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