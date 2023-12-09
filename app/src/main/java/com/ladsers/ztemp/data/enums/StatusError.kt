package com.ladsers.ztemp.data.enums

import androidx.annotation.StringRes
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.icons.rounded.SearchOff
import androidx.compose.material.icons.rounded.VpnKeyOff
import androidx.compose.material.icons.rounded.WifiOff
import androidx.compose.ui.graphics.vector.ImageVector
import com.ladsers.ztemp.R

enum class StatusError(
    val icon: ImageVector,
    @StringRes val messageRes: Int,
    @StringRes val btnTextRes: Int? = null
) {
    NO_INTERNET_CONNECTION(
        icon = Icons.Rounded.WifiOff,
        messageRes = R.string.err_noInternetConnection
    ),
    SERVER_ERROR(
        icon = Icons.Rounded.Error,
        messageRes = R.string.err_serverDidNotRespond
    ),
    INVALID_AUTH_DATA(
        icon = Icons.Rounded.VpnKeyOff,
        messageRes = R.string.err_invalidUsernamePassword
    ),
    AUTH_ERROR(
        icon = Icons.Rounded.VpnKeyOff,
        messageRes = R.string.err_authDataExpired,
        btnTextRes = R.string.signIn
    ),
    DEVICE_NOT_FOUND(
        icon = Icons.Rounded.SearchOff,
        messageRes = R.string.err_deviceNotFound,
        btnTextRes = R.string.choose
    )
}