package com.geelong.user.Response

data class RideLaterResponse(
    val `data`: Int,
    val error: Int,
    val msg: String,
    val service: String,
    val success: String
)