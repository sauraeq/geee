package com.geelong.user.Response

data class TermsResponse(
    val `data`: List<TermDataRes>,
    val error: Int,
    val msg: String,
    val service: String,
    val success: String
)