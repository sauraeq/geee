package com.geelong.user.Response

data class BookingStatusResponse(
    val `data`: List<BookingStatusResData>,
    val error: Int,
    val msg: String,
    val service: String,
    val success: String
)