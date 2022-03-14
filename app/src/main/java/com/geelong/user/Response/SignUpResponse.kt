package com.geelong.user.Response

data class SignUpResponse(
    val `data`: List<Any>,
    val error: Int,
    val msg: String,
    val service: String,
    val success: String
)