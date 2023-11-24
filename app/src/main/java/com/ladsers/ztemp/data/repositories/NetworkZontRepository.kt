package com.ladsers.ztemp.data.repositories

import com.ladsers.ztemp.data.apiservices.ZontService
import com.ladsers.ztemp.data.models.DeviceStatus
import com.ladsers.ztemp.data.models.UserInfo
import com.ladsers.ztemp.data.models.zont.DevicesRequest
import com.ladsers.ztemp.data.models.zont.ThermostatTargetRequest
import com.ladsers.ztemp.data.models.zont.ThermostatTargetTemps
import com.ladsers.ztemp.data.models.zont.ThermostatTargetTemps1
import okhttp3.Credentials

class NetworkZontRepository(
    private val zontService: ZontService
) : ZontRepository {
    override suspend fun getUserInfo(login: String, password: String) = UserInfo(
        token = zontService.getAuth(Credentials.basic(login, password)).token
    )

    override suspend fun getDevices(token: String): List<DeviceStatus> {
        val devicesRequest = DevicesRequest(loadIo = false)

        val devices = zontService.getDevices(token, devicesRequest).devices.filter { d ->
            (d.id ?: 0) > 0 && !d.name.isNullOrEmpty() && d.online == true &&
                    (d.thermostatTargetTemps?.thermostatTargetTemps1?.manual == true
                            && (d.thermostatTargetTemps?.thermostatTargetTemps1?.temp ?: 0.0) > 0.0)
                    || (d.thermostatTargetTemps?.thermostatTargetTemps0?.manual == true
                            && (d.thermostatTargetTemps?.thermostatTargetTemps0?.temp ?: 0.0) > 0.0)
        }

        return devices.map { device ->
            DeviceStatus(
                id = device.id!!,
                name = device.name!!,
                currentTemp = device.thermometers.firstOrNull()?.lastValue,
                targetTemp = device.io?.lastBoilerState?.targetTemp,
                mainPower = null,
                online = null
            )
        }
    }

    override suspend fun getDeviceStatus(token: String, deviceId: Int): DeviceStatus {
        val device = zontService.getDevices(token).devices.firstOrNull { d -> d.id == deviceId }

        return DeviceStatus(
            id = device?.id ?: 0, //todo remove
            name = device?.name ?: "", //todo remove
            currentTemp = device?.thermometers?.firstOrNull()?.lastValue,
            targetTemp = device?.io?.lastBoilerState?.targetTemp,
            mainPower = device?.io?.powerSource == "main",
            online = device?.online
        )
    }

    override suspend fun setTemp(token: String, deviceId: Int, targetTemp: Double) {
        val thermostatTargetRequest = ThermostatTargetRequest(
            id = deviceId,
            thermostatTargetTemps = ThermostatTargetTemps(
                thermostatTargetTemps1 = ThermostatTargetTemps1(
                    temp = targetTemp
                )
            )
        )

        zontService.setTemp(token, thermostatTargetRequest)
    }
}