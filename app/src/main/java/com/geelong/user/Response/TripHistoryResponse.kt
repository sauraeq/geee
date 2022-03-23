package com.geelong.user.Response

data class TripHistoryResponse(
    val `data`: List<TripHistoryData>,
    val error: Int,
    val msg: String,
    val service: String,
    val success: String
)