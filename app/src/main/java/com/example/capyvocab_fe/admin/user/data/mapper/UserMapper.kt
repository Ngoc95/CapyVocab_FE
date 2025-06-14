package com.example.capyvocab_fe.admin.user.data.mapper

import android.os.Build
import androidx.annotation.RequiresApi
import com.example.capyvocab_fe.admin.user.data.model.UserData
import com.example.capyvocab_fe.admin.user.domain.model.User
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@RequiresApi(Build.VERSION_CODES.O)
fun UserData.toDomain(): User = User(
    id = id,
    username = username,
    email = email,
    avatar = avatar,
    status = status,
    streak = streak,
    lastStudyDate = formatLastStudyDate(lastStudyDate),
    totalStudyDay = totalStudyDay,
    totalLearnedCard = 100,
    totalMasteredCard = 70,
    roleId = role.id
    // sửa lại sau khi be update
//    totalLearnedCard = totalLearnedCard,
//    totalMasteredCard = totalMasteredCard,
)

@RequiresApi(Build.VERSION_CODES.O)
fun formatLastStudyDate(isoDate: String?): String {
    return try {
        val zonedDateTime = ZonedDateTime.parse(isoDate)
        val formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy")
        zonedDateTime.format(formatter)
    } catch (e: Exception) {
        "N/A" // hoặc trả về chuỗi mặc định nếu parse lỗi
    }
}