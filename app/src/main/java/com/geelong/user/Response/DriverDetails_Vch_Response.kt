package com.geelong.user.Response

data class DriverDetails_Vch_Response(
    val `data`: List<DriverDetailsData>,
    val error: Int,
    val msg: String,
    val service: String,
    val success: String

)