package com.ladsers.ztemp.data.apiservices

import com.ladsers.ztemp.data.models.zont.AuthResponse
import com.ladsers.ztemp.data.models.zont.DevicesRequest
import com.ladsers.ztemp.data.models.zont.DevicesResponse
import com.ladsers.ztemp.data.models.zont.ThermostatTargetRequest
import retrofit2.http.*

interface ZontService {

    //TODO
    @Headers(
        "X-ZONT-Client:report@ladsers.com",
        "Content-Type:application/json")
    @POST("get_authtoken")
    suspend fun getAuth(
        @Header("Authorization") credentials: String
    ): AuthResponse

    //TODO
    @POST("todo")
    suspend fun getDevices(
        token: String,
        @Body devicesRequest: DevicesRequest = DevicesRequest()
    ): DevicesResponse

    //TODO
    @POST("todo")
    suspend fun setTemp(
        token: String,
        @Body thermostatTargetRequest: ThermostatTargetRequest
    )

}