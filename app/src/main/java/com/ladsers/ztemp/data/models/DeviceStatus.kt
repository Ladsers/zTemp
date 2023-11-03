package com.ladsers.ztemp.data.models

data class DeviceStatus(
    val id: Int?,
    val name: String?,
    val currentTemp: Double?,
    val targetTemp: Double?
)