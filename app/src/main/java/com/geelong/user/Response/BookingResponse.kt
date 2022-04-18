package com.geelong.user.Response

data class BookingResponse(
    val `data`: Int,
    val error: Int,
    val msg: String,
    val service: String,
    val success: String
)