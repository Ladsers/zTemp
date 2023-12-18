package com.ladsers.ztemp.ui.screens

import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.scrollBy
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.rotary.onRotaryScrollEvent
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.CardDefaults
import androidx.wear.compose.material.ListHeader
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.PositionIndicator
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.TimeTextDefaults
import androidx.wear.compose.material.TitleCard
import androidx.wear.compose.material.Vignette
import androidx.wear.compose.material.VignettePosition
import androidx.wear.compose.material.rememberScalingLazyListState
import com.ladsers.ztemp.BuildConfig
import com.ladsers.ztemp.R
import com.ladsers.ztemp.domain.viewModels.SettingsViewModel
import com.ladsers.ztemp.ui.components.ItemCard
import com.ladsers.ztemp.ui.components.LogOutDialog
import com.ladsers.ztemp.ui.components.TextInputCard
import com.ladsers.ztemp.ui.theme.AlmostBlack
import com.ladsers.ztemp.ui.theme.DarkOrange
import com.ladsers.ztemp.ui.theme.Teal
import kotlinx.coroutines.launch

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    updateAvailable: Boolean,
    deviceName: String,
    finishActivity: () -> Unit,
    goToWebsite: (String) -> Unit,
    startDonationActivity: () -> Unit
) {
    val logOutDialogState = remember { mutableStateOf(false) }
    val listState = rememberScalingLazyListState()

    Scaffold(
        timeText = {
            TimeText(
                timeTextStyle = TimeTextDefaults.timeTextStyle(
                    fontSize = 12.sp
                )
            )
        },
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
                ListHeader {
                    Text(
                        text = stringResource(id = R.string.title_activity_settings),
                        fontSize = 18.sp
                    )
                }
            }
            if (updateAvailable) {
                item {
                    TitleCard(
                        title = {
                            Text(
                                stringResource(id = R.string.updateAvailable),
                                fontSize = 18.sp,
                                color = Color.White
                            )
                        },
                        backgroundPainter = CardDefaults.cardBackgroundPainter(
                            startBackgroundColor = DarkOrange,
                            endBackgroundColor = MaterialTheme.colors.surface
                        ),
                        onClick = { goToWebsite("https://www.ladsers.com/ztemp/download") }
                    ) {}
                }
            }
            item {
                ChangeDeviceCard(
                    deviceName = deviceName,
                    enabled = viewModel.addFeatures.value,
                    action = {
                        viewModel.resetDevice()
                        finishActivity()
                    },
                    disabledAction = {
                        coroutineScope.launch {
                            listState.animateScrollToItem(
                                if (updateAvailable) 5 else 4, // lazy scrolling depending on update card visibility
                                0
                            )
                        }
                    })
            }
            item {
                TextInputCard(
                    label = stringResource(id = R.string.presetTemp1),
                    valueState = viewModel.presetTemp1,
                    onUpdateValue = viewModel::updatePresetTemp1,
                    enabled = viewModel.addFeatures.value,
                    disabledAction = {
                        coroutineScope.launch {
                            listState.animateScrollToItem(
                                if (updateAvailable) 5 else 4,
                                0
                            )
                        }
                    }
                )
            }
            item {
                TextInputCard(
                    label = stringResource(id = R.string.presetTemp2),
                    valueState = viewModel.presetTemp2,
                    onUpdateValue = viewModel::updatePresetTemp2,
                    enabled = viewModel.addFeatures.value,
                    disabledAction = {
                        coroutineScope.launch {
                            listState.animateScrollToItem(
                                if (updateAvailable) 5 else 4,
                                0
                            )
                        }
                    }
                )
            }
            item {
                ItemCard(
                    title = stringResource(id = R.string.supportDeveloper),
                    content = if (viewModel.addFeatures.value) null
                    else stringResource(id = R.string.getAdditionalFeatures),
                    enabled = true,
                    onClickAction = {
                        if (viewModel.addFeatures.value) {
                            // User has already supported the developer and entered the code
                            goToWebsite("https://pay.cloudtips.ru/p/9da1c376")
                        } else {
                            // User has not yet supported the developer
                            startDonationActivity()
                        }
                    }
                )
            }
            item {
                ItemCard(
                    title = stringResource(id = R.string.appWebsite),
                    enabled = true,
                    onClickAction = { goToWebsite("https://www.ladsers.com/ztemp") })
            }
            item {
                ItemCard(title = stringResource(id = R.string.createdBy))
            }
            item {
                ItemCard(title = stringResource(id = R.string.licenseThirdParty),
                    enabled = true,
                    onClickAction = { goToWebsite("https://www.ladsers.com/ztemp/license") })
            }
            item {
                ItemCard(
                    title = stringResource(id = R.string.version),
                    content = BuildConfig.VERSION_NAME
                )
            }
            item {
                ItemCard(title = stringResource(id = R.string.logOut),
                    enabled = true,
                    onClickAction = { logOutDialogState.value = true })
            }
        }
        LaunchedEffect(Unit) {
            focusRequester.requestFocus()
        }
    }

    LogOutDialog(dialogState = logOutDialogState) { viewModel.logOut() }
}

@Composable
fun ChangeDeviceCard(
    deviceName: String,
    enabled: Boolean,
    action: () -> Unit,
    disabledAction: () -> Unit
) {
    TitleCard(
        title = {
            Text(
                stringResource(id = R.string.changeDevice),
                fontSize = 18.sp,
                color = if (enabled) Color.White else Color.Gray
            )
        },
        backgroundPainter = CardDefaults.cardBackgroundPainter(
            startBackgroundColor = if (enabled) MaterialTheme.colors.surface else AlmostBlack,
            endBackgroundColor = if (enabled) MaterialTheme.colors.surface else AlmostBlack
        ),
        onClick = { if (enabled) action() else disabledAction() },
        enabled = true
    ) {
        Text(text = deviceName, color = if (enabled) Teal else Color.Gray)
    }
}