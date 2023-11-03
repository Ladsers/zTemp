package com.ladsers.ztemp.domain.states

import com.ladsers.ztemp.data.models.DeviceStatus

interface DeviceStatusState {
    object InProcessing : DeviceStatusState
    data class Success(val deviceStatus: DeviceStatus) : DeviceStatusState
    data class Error(val errorCode: Int) : DeviceStatusState
}