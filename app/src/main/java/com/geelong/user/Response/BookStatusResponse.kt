package com.geelong.user.Response

data class BookStatusResponse(
    val `data`: List<BookstatusData>,
    val error: Int,
    val msg: String,
    val service: String,
    val success: String
)