package com.geelong.user.Response

data class PYResponse(
    val `data`: DataX,
    val error: String,
    val msg: String,
    val previous_expirydate: String,
    val service: String,
    val success: String
)