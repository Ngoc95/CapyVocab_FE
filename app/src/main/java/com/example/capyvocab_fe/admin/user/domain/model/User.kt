package com.example.capyvocab_fe.admin.user.domain.model


data class User(
    val id: Int,
    val username: String,
    val email: String,
    val fullName: String,
    val avatar: String?,
    val status: String
    //val role: UserRole
)
//enum class UserRole {
//    ADMIN, PREMIUM, USER;
//
//    companion object {
//        fun from(roleData: RoleData): UserRole {
//            return when (roleData.name.uppercase()) {
//                "ADMIN" -> ADMIN
//                "PREMIUM" -> PREMIUM
//                else -> USER
//            }
//        }
//    }
//}