package com.geelong.user.Response

data class ConfirmBookingResponse(
    val `data`: List<Any>,
    val error: Int,
    val msg: String,
    val service: String,
    val success: String
)