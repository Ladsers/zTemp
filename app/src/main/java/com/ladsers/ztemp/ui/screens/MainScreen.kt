package com.ladsers.ztemp.ui.screens

import androidx.compose.runtime.Composable
import com.ladsers.ztemp.domain.states.UserInfoState

@Composable
fun MainScreen(userInfoState: UserInfoState) {
    when(userInfoState) {
        is UserInfoState.InProcessing -> InProcessScreen()
        is UserInfoState.Success -> StatusScreen(token = userInfoState.userInfo.token ?: "")
        is UserInfoState.Error -> ErrorScreen(userInfoState.errorCode.toString())
    }
}