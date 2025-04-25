package com.example.capyvocab_fe.admin.user.data.mapper

import com.example.capyvocab_fe.admin.user.data.model.UserData
import com.example.capyvocab_fe.admin.user.domain.model.User

fun UserData.toDomain(): User = User(
    id = id,
    email = email,
    username = username,
    avatar = avatar,
    status = status,
    roleId = role.id,

    // sửa lại sau khi be update
    streak = 0,
    lastStudyDate = "18/04/2025",
    totalStudyDay = 16,
    totalLearnedCard = 100,
    totalMasteredCard = 70,

//    streak = streak,
//    lastStudyDate = lastStudyDate,
//    totalStudyDay = totalStudyDay,
//    totalLearnedCard = totalLearnedCard,
//    totalMasteredCard = totalMasteredCard,
//    roleId = roleId
)

//fun User.toUserData(): UserData = UserData(
//    id = id,
//    email = email,
//    username = username,
//    avatar = avatar,
//    status = status
//)