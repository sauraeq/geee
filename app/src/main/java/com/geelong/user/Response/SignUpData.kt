package com.geelong.user.Response

data class SignUpData(
    val address: String,
    val email: String,
    val gender: String,
    val name: String,
    val otp: Int,
    val phone: String
)