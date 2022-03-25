package com.geelong.user

data class ReveiwResponse(
    val `data`: List<Any>,
    val error: Int,
    val msg: String,
    val service: String,
    val success: String
)