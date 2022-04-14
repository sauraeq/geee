package com.geelong.user.Response

data class BookingStsResponse(
    val `data`: List<BookingStsRespData>,
    val error: Int,
    val msg: String,
    val service: String,
    val success: String
)