package com.ladsers.ztemp.domain.states

import androidx.compose.ui.graphics.vector.ImageVector
import com.ladsers.ztemp.data.models.DeviceStatus

interface DeviceStatusState {
    object InProcessing : DeviceStatusState
    object NotSignedIn : DeviceStatusState
    object NoDeviceSelected : DeviceStatusState
    object SignInError : DeviceStatusState
    data class Success(val deviceStatus: DeviceStatus) : DeviceStatusState
    data class Error(val icon: ImageVector, val message: String, val retryAction: () -> Unit) :
        DeviceStatusState
}