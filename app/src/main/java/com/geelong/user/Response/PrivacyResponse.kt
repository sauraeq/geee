package com.geelong.user.Response

data class PrivacyResponse(
    val `data`: List<DataX>,
    val error: Int,
    val msg: String,
    val service: String,
    val success: String
)