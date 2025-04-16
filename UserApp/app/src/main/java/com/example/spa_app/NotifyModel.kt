package com.example.myapplication

import java.sql.Time

data class NotifyModel(
    var typeNoti: String,
    var title: String,
    var timeNotified: Time
)