package com.ladsers.ztemp.domain.states

import com.ladsers.ztemp.data.enums.StatusError
import com.ladsers.ztemp.data.models.DeviceStatus

interface DeviceStatusState {
    object InProgress : DeviceStatusState

    object NotSignedIn : DeviceStatusState

    data class NoDeviceSelected(val devices: List<DeviceStatus>) : DeviceStatusState

    object GettingStatus : DeviceStatusState

    data class Success(val deviceStatus: DeviceStatus) : DeviceStatusState

    data class Error(val error: StatusError, val fixAction: () -> Unit) : DeviceStatusState
}