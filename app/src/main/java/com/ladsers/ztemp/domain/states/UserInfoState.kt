package com.ladsers.ztemp.domain.states

import com.ladsers.ztemp.data.models.UserInfo

sealed interface UserInfoState {
    object InProcessing : UserInfoState
    data class Success(val userInfo: UserInfo) : UserInfoState
    data class Error(val errorCode: Int) : UserInfoState
}