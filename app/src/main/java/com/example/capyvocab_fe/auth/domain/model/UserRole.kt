package com.example.capyvocab_fe.auth.domain.model

import com.example.capyvocab_fe.core.data.model.RoleData


enum class UserRole {
    ADMIN, PREMIUM, FREE;

    companion object {
        fun from(roleData: RoleData): UserRole {
            return when (roleData.name.uppercase()) {
                "ADMIN" -> ADMIN
                "PREMIUM" -> PREMIUM
                else -> FREE
            }
        }
    }
}