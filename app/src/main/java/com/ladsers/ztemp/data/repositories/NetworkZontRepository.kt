package com.ladsers.ztemp.data.repositories

import com.ladsers.ztemp.data.apiservices.ZontService
import com.ladsers.ztemp.data.models.DeviceStatus
import com.ladsers.ztemp.data.models.UserInfo
import okhttp3.Credentials

class NetworkZontRepository(
    private val zontService: ZontService
) : ZontRepository {
    override suspend fun getUserInfo(login: String, password: String) = UserInfo(
        token = zontService.getAuth(Credentials.basic(login, password)).token
    )

    override suspend fun getDevices(token: String): DeviceStatus {
        TODO("Not yet implemented")
    }

    override suspend fun setTemp(token: String, targetTemp: Double) {
        TODO("Not yet implemented")
    }
}