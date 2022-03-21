package com.geelong.user.Response

data class Vechail_detailsResponse(
    val `data`: List<VechData>,
    val error: Int,
    val msg: String,
    val service: String,
    val success: String
)