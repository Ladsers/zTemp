package com.ladsers.ztemp.ui.screens

import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import com.ladsers.ztemp.domain.states.DeviceStatusState
import com.ladsers.ztemp.domain.viewModels.ZontViewModel

//@Composable
//fun MainScreen(userInfoState: UserInfoState) {
//    when(userInfoState) {
//        is UserInfoState.InProcessing -> InProcessScreen()
//        is UserInfoState.Success -> {
//            StatusScreen(token = userInfoState.userInfo.token ?: "")
//        }
//        is UserInfoState.Error -> ErrorScreen(userInfoState.errorCode.toString())
//    }
//}

@Composable
fun MainScreen(
    viewModel: ZontViewModel,
    deviceStatusState: DeviceStatusState,
    targetTempState: State<Double>,
    onUpdateTargetTemp: (Double) -> Unit,
    onDecreaseBtnClicked: () -> Unit,
    onIncreaseBtnClicked: () -> Unit,
    onAcceptBtnClicked: () -> Unit
) {
    when (deviceStatusState) {
        is DeviceStatusState.InProcessing -> InProcessScreen()
        is DeviceStatusState.NotSignedIn -> SignInScreen(viewModel = viewModel)
        is DeviceStatusState.NoDeviceSelected -> DevicesScreen(
            deviceStatusState.devices,
            deviceStatusState.onDeviceSelected,
            deviceStatusState.onLogOutClicked
        )
        is DeviceStatusState.SignInError -> SignInErrorScreen {
            viewModel.logOut()
        }
        is DeviceStatusState.GettingStatus -> StatusScreen2(deviceStatus = null)
        is DeviceStatusState.Success -> StatusScreen2(
            deviceStatus = deviceStatusState.deviceStatus,
            onRefreshAction = { viewModel.getStatus(refreshing = true) })
        is DeviceStatusState.Error -> ErrorScreen(
            deviceStatusState.icon,
            deviceStatusState.message,
            deviceStatusState.retryAction
        )
    }
}