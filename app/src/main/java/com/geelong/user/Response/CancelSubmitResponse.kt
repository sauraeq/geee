package com.geelong.user.Response

data class CancelSubmitResponse(
    val `data`: CancelSubmitResponseData,
    val error: Int,
    val msg: String,
    val service: String,
    val success: String
)