package com.geelong.user.Response

data class NewNotificationResponse(
    val `data`: List<NewNotiData>,
    val error: Int,
    val msg: String,
    val service: String,
    val success: String
)