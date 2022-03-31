package com.geelong.user.Response

data class CancelTripReasonResponse(
    val `data`: List<CancelTripResponsedata>,
    val error: Int,
    val msg: String,
    val service: String,
    val success: String
)