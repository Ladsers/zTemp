package com.ladsers.ztemp.data.models

data class DeviceStatus(
    val id: Int,
    val name: String,
    val tempStep: Double,
    val currentTemp: Double?,
    val targetTemp: Double?,
    val targetThermostatId: Int?,
    val mainPower: Boolean?,
    val online: Boolean?
)