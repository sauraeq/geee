package com.geelong.user.Response

data class CancelTripHistoryResponse(
    val `data`: List<CancelTripHistoryResData>,
    val error: Int,
    val msg: String,
    val service: String,
    val success: String
)