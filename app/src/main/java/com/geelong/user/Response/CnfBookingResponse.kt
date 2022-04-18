package com.geelong.user.Response

data class CnfBookingResponse(
    val `data`: String,
    val error: Int,
    val msg: String,
    val service: String,
    val success: String
)