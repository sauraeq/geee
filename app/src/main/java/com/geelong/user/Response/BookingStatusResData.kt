package com.geelong.user.Response

data class BookingStatusResData(
    val booking_id: String,
    val device_tokanid: String,
    val id: String,
    val latitude: String,
    val longitude: String,
    val name: String,
    val otp: String,
    val profile_photo: String,
    val rating: String,
    val status: String,
    val vehicle_image: String,
    val vehicle_name: Any,
    val vehicle_no: Any
)