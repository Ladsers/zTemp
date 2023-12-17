package com.ladsers.ztemp.ui.screens

import androidx.compose.runtime.Composable
import com.ladsers.ztemp.data.models.TempSetter
import com.ladsers.ztemp.domain.states.DeviceStatusState
import com.ladsers.ztemp.domain.viewModels.ZontViewModel

@Composable
fun MainScreen(
    deviceStatusState: DeviceStatusState,
    viewModel: ZontViewModel,
    onTempSetterBtnClicked: (TempSetter) -> Unit,
    onSettingsBtnClicked: (Boolean, String, Double) -> Unit
) {
    when (deviceStatusState) {
        is DeviceStatusState.InProgress -> InProgressScreen()

        is DeviceStatusState.NotSignedIn -> SignInScreen(viewModel)

        is DeviceStatusState.NoDeviceSelected -> DevicesScreen(
            devices = deviceStatusState.devices,
            onDeviceSelected = viewModel::selectDevice,
            onLogOutClicked = viewModel::logOut
        )

        is DeviceStatusState.GettingStatus -> StatusScreen(
            deviceStatus = null,
            updateAvailable = viewModel.updateAvailable,
            onSettingsBtnClicked = onSettingsBtnClicked,
            prepareTempSetter = viewModel::prepareTempSetter,
            startTempSetterActivity = onTempSetterBtnClicked
        )

        is DeviceStatusState.Success -> StatusScreen(
            deviceStatus = deviceStatusState.deviceStatus,
            updateAvailable = viewModel.updateAvailable,
            onRefreshAction = { viewModel.getStatus(refreshing = true) },
            onSettingsBtnClicked = onSettingsBtnClicked,
            prepareTempSetter = viewModel::prepareTempSetter,
            startTempSetterActivity = onTempSetterBtnClicked
        )

        is DeviceStatusState.Error -> ErrorScreen(
            icon = deviceStatusState.error.icon,
            messageRes = deviceStatusState.error.messageRes,
            btnTextRes = deviceStatusState.error.btnTextRes,
            fixAction = deviceStatusState.fixAction
        )
    }
}