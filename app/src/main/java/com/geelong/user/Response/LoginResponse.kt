package com.geelong.user.Response

data class LoginResponse(
    val `data`: List<LoginData>,
    val error: Int,
    val msg: String,
    val service: String,
    val success: String
)