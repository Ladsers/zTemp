package com.ladsers.ztemp.data.repositories

import com.ladsers.ztemp.data.models.DeviceStatus
import com.ladsers.ztemp.data.models.UserInfo

interface ZontRepository {
    suspend fun getUserInfo(login: String, password: String): UserInfo
    suspend fun getDevices(token: String): DeviceStatus
    suspend fun setTemp(token: String, targetTemp: Double)
}