package com.example.spa_app

data class UpdateInforNotify(
    val id: String = "",
    val userId: String = "",
    val contentForUser: String = "",
    val contentForOwner: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val seenByUser: Boolean = false,
    val type: String = ""
)

