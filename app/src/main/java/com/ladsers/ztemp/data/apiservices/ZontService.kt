package com.ladsers.ztemp.data.apiservices

import com.ladsers.ztemp.data.models.zont.AuthResponse
import com.ladsers.ztemp.data.models.zont.DevicesRequest
import com.ladsers.ztemp.data.models.zont.DevicesResponse
import com.ladsers.ztemp.data.models.zont.ThermostatTargetRequest
import retrofit2.http.*

/**
 * ZONT API
 * https://zont-online.ru/api/docs/
 */
interface ZontService {

    @Headers(
        "X-ZONT-Client:report@ladsers.com",
        "Content-Type:application/json"
    )
    @POST("get_authtoken")
    suspend fun getAuth(
        @Header("Authorization") credentials: String
    ): AuthResponse

    @Headers(
        "X-ZONT-Client:report@ladsers.com",
        "Content-Type:application/json"
    )
    @POST("devices")
    suspend fun getDevices(
        @Header("X-ZONT-Token") token: String,
        @Body devicesRequest: DevicesRequest = DevicesRequest()
    ): DevicesResponse

    @Headers(
        "X-ZONT-Client:report@ladsers.com",
        "Content-Type:application/json"
    )
    @POST("update_device")
    suspend fun setTemp(
        @Header("X-ZONT-Token") token: String,
        @Body thermostatTargetRequest: ThermostatTargetRequest
    )

}