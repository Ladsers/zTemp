package com.ladsers.ztemp.ui.screens

import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Logout
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.ListHeader
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition
import androidx.wear.compose.material.items
import androidx.wear.compose.material.rememberScalingLazyListState
import com.ladsers.ztemp.R
import com.ladsers.ztemp.data.models.DeviceStatus
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun DevicesScreen(
    devices: List<DeviceStatus>,
    onDeviceSelected: (Int) -> Unit,
    onLogOutClicked: () -> Unit
) {
    val listState = rememberScalingLazyListState()

    Scaffold(
        vignette = {
            Vignette(vignettePosition = VignettePosition.TopAndBottom)
        },
        positionIndicator = {
            PositionIndicator(scalingLazyListState = listState)
        }
    ) {

        val focusRequester = remember { FocusRequester() }
        val coroutineScope = rememberCoroutineScope()

        ScalingLazyColumn(
            modifier = Modifier
                .onRotaryScrollEvent {
                    coroutineScope.launch {
                        listState.scrollBy(it.verticalScrollPixels)
                        listState.animateScrollBy(0f)
                    }
                    true
                }
                .focusRequester(focusRequester)
                .focusable(),
            state = listState
        ) {
            item {
                ListHeader(modifier = Modifier.padding(bottom = 5.dp)) {
                    Text(
                        text = stringResource(id = R.string.title_availableDevices),
                        fontSize = 18.sp,
                        textAlign = TextAlign.Center
                    )
                }
            }
            items(devices) { device ->
                SettingsCard(
                    title = device.name,
                    enabled = true,
                    action = { onDeviceSelected(device.id) })
            }
            item {
                Button(modifier = Modifier.padding(top = 30.dp), onClick = onLogOutClicked) {
                    Icon(
                        imageVector = Icons.Rounded.Logout,
                        contentDescription = stringResource(id = R.string.cd_logOut)
                    )
                }
            }
        }
    }
}