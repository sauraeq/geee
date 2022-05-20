package com.geelong.user.Response

data class RideLaterHistoryRes(
    val `data`: List<RideLaterHistoryResData>,
    val error: Int,
    val msg: String,
    val service: String,
    val success: String
)