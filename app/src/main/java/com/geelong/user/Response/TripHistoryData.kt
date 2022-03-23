package com.geelong.user.Response

data class TripHistoryData(
    val amount: String,
    val cancel: String,
    val created_date: String,
    val cupon_code: String,
    val distance: String,
    val driver_id: String,
    val driver_name: String,
    val driver_photo: String,
    val drop_address: String,
    val drop_latitude: String,
    val drop_longitude: String,
    val id: String,
    val otp: String,
    val pickup_address: String,
    val pickup_latitude: String,
    val pickup_longitude: String,
    val status: String,
    val time: String,
    val uid: String
)