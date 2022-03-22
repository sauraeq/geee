package com.geelong.user.Response

data class EditProfileResponse(
    val error: Int,
    val msg: String,
    val service: String,
    val success: String
)