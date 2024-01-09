package com.ladsers.ztemp.data.repositories

import com.ladsers.ztemp.data.apiservices.ZontService
import com.ladsers.ztemp.data.models.DeviceStatus
import com.ladsers.ztemp.data.models.UserInfo
import com.ladsers.ztemp.data.models.zont.DevicesRequest
import com.ladsers.ztemp.data.models.zont.ThermostatTargetRequest
import com.ladsers.ztemp.data.models.zont.ThermostatTargetTemps
import com.ladsers.ztemp.data.models.zont.ThermostatTargetTemps0
import com.ladsers.ztemp.data.models.zont.ThermostatTargetTemps1
import okhttp3.Credentials

class NetworkZontRepository(
    private val zontService: ZontService
) : ZontRepository {
    override suspend fun getUserInfo(login: String, password: String) = UserInfo(
        token = zontService.getAuth(Credentials.basic(login, password)).token
    )

    override suspend fun getDevices(token: String): List<DeviceStatus> {
        // Selection of supported and online devices.
        val devices = zontService.getDevices(token).devices.filter { d ->
            (d.id ?: 0) > 0 && !d.name.isNullOrEmpty() && d.online == true &&
                    (d.io?.lastBoilerState?.targetTemp ?: 0.0) > 0.0
        }

        return devices.map { device ->
            DeviceStatus(
                id = device.id!!,
                name = device.name!!,
                tempStep = 1.0,
                currentTemp = null,
                targetTemp = null,
                targetThermostatId = null,
                mainPower = null,
                online = null
            )
        }
    }

    override suspend fun getDeviceStatus(token: String, deviceId: Int): DeviceStatus {
        val device = zontService.getDevices(token).devices.firstOrNull { d -> d.id == deviceId }

        val targetTemp: Double?
        val targetThermostatId: Int?

        // Getting the right thermostat.
        if (device?.thermostatTargetTemps?.thermostatTargetTemps1?.manual == true) {
            targetTemp = device.thermostatTargetTemps!!.thermostatTargetTemps1!!.temp
            targetThermostatId = 1
        } else if (device?.thermostatTargetTemps?.thermostatTargetTemps0?.manual == true) {
            targetTemp = device.thermostatTargetTemps!!.thermostatTargetTemps0!!.temp
            targetThermostatId = 0
        } else {
            // it looks like the temperature is set via the temp zone, try taking thermostatTargetTemps1
            targetTemp = device?.thermostatTargetTemps?.thermostatTargetTemps1?.temp
            targetThermostatId = if (targetTemp != null) 1 else null
        }

        return DeviceStatus(
            id = device?.id ?: 0,
            name = device?.name ?: "???",
            currentTemp = device?.thermometers?.firstOrNull()?.lastValue,
            targetTemp = targetTemp,
            targetThermostatId = targetThermostatId,
            tempStep = device?.tempstep ?: 1.0,
            mainPower = device?.io?.powerSource == "main",
            online = device?.online
        )
    }

    override suspend fun setTemp(
        token: String,
        deviceId: Int,
        targetThermostatId: Int,
        targetTemp: Double
    ) {
        val thermostatTargetRequest = ThermostatTargetRequest(
            id = deviceId,
            thermostatTargetTemps = if (targetThermostatId == 1) ThermostatTargetTemps(
                thermostatTargetTemps1 = ThermostatTargetTemps1(
                    manual = true, // this flag must be set to true
                    temp = targetTemp
                )
            ) else ThermostatTargetTemps(
                thermostatTargetTemps0 = ThermostatTargetTemps0(
                    manual = true,
                    temp = targetTemp
                )
            )
        )

        zontService.setTemp(token, thermostatTargetRequest)
    }
}