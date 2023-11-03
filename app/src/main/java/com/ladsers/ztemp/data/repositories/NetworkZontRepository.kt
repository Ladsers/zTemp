package com.ladsers.ztemp.data.repositories

import com.ladsers.ztemp.data.apiservices.ZontService
import com.ladsers.ztemp.data.models.DeviceStatus
import com.ladsers.ztemp.data.models.UserInfo
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

    override suspend fun getDevices(token: String): DeviceStatus {
        val device = zontService.getDevices(token).devices.firstOrNull()

        return DeviceStatus(
            id = device?.id,
            name = device?.name,
            currentTemp = device?.thermometers?.firstOrNull()?.lastValue,
            targetTemp = device?.io?.lastBoilerState?.targetTemp
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