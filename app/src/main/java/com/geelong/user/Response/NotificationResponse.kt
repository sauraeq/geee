package com.geelong.user.Response

data class NotificationResponse(
    val `data`: List<NotificationData>,
    val error: Int,
    val msg: String,
    val service: String,
    val success: String
)