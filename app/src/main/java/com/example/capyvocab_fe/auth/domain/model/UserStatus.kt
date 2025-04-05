package com.example.capyvocab_fe.auth.domain.model

enum class UserStatus(val value: Int) {
    NOT_VERIFIED(0),
    ACTIVE(1),
    BAN(2),
    DELETED(3);

    companion object {
        fun fromValue(value: Int): UserStatus {
            return entries.find { it.value == value } ?: NOT_VERIFIED
        }
    }
}