package com.ladsers.ztemp.data.models

/**
 * ZONT device data received from the server.
 */
data class DeviceStatus(
    /**
     * Device ID.
     */
    val id: Int,
    /**
     * Device name.
     */
    val name: String,
    /**
     * Target temperature change step. The value is set on the server.
     */
    val tempStep: Double,
    /**
     * Current temperature from the device sensor.
     */
    val currentTemp: Double?,
    /**
     * Target temperature value for the thermostat.
     */
    val targetTemp: Double?,
    /**
     * Index of the thermostat, which is responsible for manually setting the temperature.
     */
    val targetThermostatId: Int?,
    /**
     * Device is powered by the mains, not by the battery.
     */
    val mainPower: Boolean?,
    /**
     * The device has access to ZONT servers.
     */
    val online: Boolean?
)