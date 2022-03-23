package com.geelong.user.Response

data class EditPResponse(
    val `data`: EditPData,
    val error: Int,
    val msg: String,
    val service: String,
    val success: String
)