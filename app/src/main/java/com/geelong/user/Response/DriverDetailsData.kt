package com.geelong.user.Response

data class DriverDetailsData(
    val id: String,
    val name: String,
    val otp: Int,
    val profile_photo: String,
    val rating: String,
    val vehicle_image: String,
    val vehicle_name: String,
    val vehicle_no: String,
    val booking_id:Int,
    val latitude:String,
    val longitude:String
)