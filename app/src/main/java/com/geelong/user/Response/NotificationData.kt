package com.geelong.user.Response

data class NotificationData(
    val created_date: String,
    val description: String,
    val id: String,
    val title: String,
    val type: String,
    val user_driver_id: String
)