package com.ladsers.ztemp.data.repositories

import com.ladsers.ztemp.data.models.DeviceStatus
import com.ladsers.ztemp.data.models.UserInfo

/**
 * Interactions with the ZONT server.
 */
interface ZontRepository {
    /**
     * Get user info (token) by [login] and [password].
     */
    suspend fun getUserInfo(login: String, password: String): UserInfo

    /**
     * Get list of devices on account with basic information.
     */
    suspend fun getDevices(token: String): List<DeviceStatus>

    /**
     * Get detailed information about the specified device.
     */
    suspend fun getDeviceStatus(token: String, deviceId: Int): DeviceStatus

    /**
     * Set the target temperature for the selected device thermostat.
     */
    suspend fun setTemp(
        token: String,
        deviceId: Int,
        targetThermostatId: Int,
        targetTemp: Double
    )
}