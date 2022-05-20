package com.geelong.user.Response

data class RideLaterHistoryResData(
    val date: String,
    val drop_address: String,
    val drop_latitude: String,
    val id: String,
    val passenger: String,
    val pickup_address: String,
    val pickup_latitude: String,
    val pickup_longitude: String,
    val time: String,
    val uid: String
)