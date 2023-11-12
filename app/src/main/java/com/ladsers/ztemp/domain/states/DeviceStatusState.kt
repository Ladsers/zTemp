package com.ladsers.ztemp.domain.states

import androidx.compose.ui.graphics.vector.ImageVector
import com.ladsers.ztemp.data.models.DeviceStatus

interface DeviceStatusState {
    object InProcessing : DeviceStatusState
    object NotSignedIn : DeviceStatusState
    data class NoDeviceSelected(
        val devices: List<DeviceStatus>,
        val onDeviceSelected: (Int) -> Unit,
        val onLogOutClicked: () -> Unit
    ) : DeviceStatusState

    object SignInError : DeviceStatusState
    object GettingStatus : DeviceStatusState
    data class Success(val deviceStatus: DeviceStatus) : DeviceStatusState
    data class Error(val icon: ImageVector, val message: String, val retryAction: () -> Unit) :
        DeviceStatusState
}