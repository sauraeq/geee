package com.geelong.user.Response

data class PaylaterResponse(
    val `data`: String,
    val error: Int,
    val msg: String,
    val service: String,
    val success: String
)