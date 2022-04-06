package com.geelong.user.Response

data class ViewNotificationResponse(
    val `data`: Boolean,
    val error: Int,
    val msg: String,
    val service: String,
    val success: String
)