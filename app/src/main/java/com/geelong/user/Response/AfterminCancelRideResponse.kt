package com.geelong.user.Response

data class AfterminCancelRideResponse(
    val `data`: Boolean,
    val error: Int,
    val msg: String,
    val service: String,
    val success: String
)