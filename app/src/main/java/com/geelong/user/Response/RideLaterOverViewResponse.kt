package com.geelong.user.Response

data class RideLaterOverViewResponse(
    val `data`: List<Any>,
    val error: Int,
    val msg: String,
    val service: String,
    val success: String
)