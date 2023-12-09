package com.ladsers.ztemp.ui.screens

import androidx.compose.runtime.Composable
import com.ladsers.ztemp.domain.states.DeviceStatusState
import com.ladsers.ztemp.domain.viewModels.ZontViewModel

@Composable
fun MainScreen(
    deviceStatusState: DeviceStatusState,
    viewModel: ZontViewModel,
    onSettingsBtnClicked: (Boolean, String, Double) -> Unit
) {
    when (deviceStatusState) {
        is DeviceStatusState.InProgress -> InProgressScreen()

        is DeviceStatusState.NotSignedIn -> SignInScreen(viewModel)

        is DeviceStatusState.NoDeviceSelected -> DevicesScreen(
            deviceStatusState.devices,
            deviceStatusState.onDeviceSelected,
            deviceStatusState.onLogOutClicked
        )

        is DeviceStatusState.GettingStatus -> StatusScreen(
            deviceStatus = null,
            onSettingsBtnClicked = onSettingsBtnClicked
        )

        is DeviceStatusState.Success -> StatusScreen(
            deviceStatus = deviceStatusState.deviceStatus,
            onRefreshAction = { viewModel.getStatus(refreshing = true) },
            onSettingsBtnClicked = onSettingsBtnClicked
        )

        is DeviceStatusState.Error -> ErrorScreen(
            deviceStatusState.error.icon,
            deviceStatusState.error.messageRes,
            deviceStatusState.error.btnTextRes,
            deviceStatusState.fixAction
        )
    }
}