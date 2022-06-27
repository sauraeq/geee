package com.geelong.user.Response

data class BookingStsRespData(
    val booking_id: String,
    val device_tokanid: String,
    val id: String,
    val latitude: String,
    val longitude: String,
    val name: String,
    val distance:String,
    val otp: String,
    val profile_photo: String,
    val rating: String,
    val status: String,
    val cancel:String,
    val vehicle_image: String,
    val vehicle_name: String,
    val vehicle_no: String
)