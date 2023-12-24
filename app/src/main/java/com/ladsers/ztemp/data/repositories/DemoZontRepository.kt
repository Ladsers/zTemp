package com.ladsers.ztemp.data.repositories

import com.ladsers.ztemp.data.models.DeviceStatus
import com.ladsers.ztemp.data.models.UserInfo
import kotlinx.coroutines.delay
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.ResponseBody.Companion.toResponseBody
import retrofit2.HttpException
import retrofit2.Response

class DemoZontRepository : ZontRepository {

    private var _targetTemp : Double? = null

    override suspend fun getUserInfo(login: String, password: String): UserInfo {
        delay(750) // Internet imitation

        if (login != "z123456" && password != "qqww1234") {
            throw HttpException(
                Response.error<Any>(
                    403,
                    "HTTP 403 Forbidden".toResponseBody("plain/text".toMediaTypeOrNull())
                )
            )
        }

        return UserInfo(token = "superToken")
    }

    override suspend fun getDevices(token: String): List<DeviceStatus> {
        delay(750) // Internet imitation

        if (token != "superToken") {
            throw HttpException(
                Response.error<Any>(
                    403,
                    "HTTP 403 Forbidden".toResponseBody("plain/text".toMediaTypeOrNull())
                )
            )
        }

        _targetTemp = null

        return listOf(
            DeviceStatus(
                id = 1122,
                name = "Котел в доме", // Boiler in the house
                tempStep = 1.0,
                currentTemp = null,
                targetTemp = null,
                targetThermostatId = null,
                mainPower = null,
                online = null
            ), DeviceStatus(
                id = 3344,
                name = "Котел на даче", // Boiler at the cottage
                tempStep = 1.0,
                currentTemp = null,
                targetTemp = null,
                targetThermostatId = null,
                mainPower = null,
                online = null
            ), DeviceStatus(
                id = 5566,
                name = "Котел в квартире", // Boiler in the apartment
                tempStep = 1.0,
                currentTemp = null,
                targetTemp = null,
                targetThermostatId = null,
                mainPower = null,
                online = null
            )
        )
    }

    override suspend fun getDeviceStatus(token: String, deviceId: Int): DeviceStatus {
        delay(750) // Internet imitation

        if (token != "superToken") {
            throw HttpException(
                Response.error<Any>(
                    403,
                    "HTTP 403 Forbidden".toResponseBody("plain/text".toMediaTypeOrNull())
                )
            )
        }

        val name = when (deviceId) {
            1122 -> "Котел в доме"
            3344 -> "Котел на даче"
            5566 -> "Котел в квартире"
            else -> ""
        }

        return if (deviceId == 1122) {
            DeviceStatus(
                id = deviceId,
                name = name,
                tempStep = 0.5,
                currentTemp = 10.8,
                targetTemp = _targetTemp ?: 11.0,
                targetThermostatId = 1,
                mainPower = _targetTemp != 14.0,
                online = true
            )
        } else {
            DeviceStatus(
                id = deviceId,
                name = name,
                tempStep = 0.5,
                currentTemp = 24.8,
                targetTemp = _targetTemp ?: 25.0,
                targetThermostatId = 1,
                mainPower = _targetTemp != 14.0,
                online = true
            )
        }
    }

    override suspend fun setTemp(
        token: String,
        deviceId: Int,
        targetThermostatId: Int,
        targetTemp: Double
    ) {
        delay(750) // Internet imitation

        _targetTemp = targetTemp
    }
}