package com.geelong.user.Response

data class PyMentResp(
    val `data`: Paymentresdata,
    val error: String,
    val msg: String,
    val previous_expirydate: Any,
    val service: String,
    val success: String
)