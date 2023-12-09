package com.ladsers.ztemp.ui

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSavedStateRegistryOwner
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.wear.compose.material.Scaffold
import androidx.wear.compose.material.TimeText
import androidx.wear.compose.material.TimeTextDefaults
import com.ladsers.ztemp.domain.ZtempApplication
import com.ladsers.ztemp.domain.viewModels.ZontViewModel
import com.ladsers.ztemp.ui.screens.MainScreen

@Composable
fun MainActivityContent(
    startSettingsActivity: (Boolean, String, Double) -> Unit
): ZontViewModel {
    val zontViewModel: ZontViewModel = viewModel(
        factory = ZontViewModel.provideFactory(
            (LocalContext.current.applicationContext as ZtempApplication).container.zontRepository,
            (LocalContext.current.applicationContext as ZtempApplication).container.dataStoreRepository,
            owner = LocalSavedStateRegistryOwner.current
        )
    )

    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        timeText = {
            TimeText(
                timeTextStyle = TimeTextDefaults.timeTextStyle(
                    fontSize = 12.sp
                )
            )
        }
    ) {
        MainScreen(
            zontViewModel,
            deviceStatusState = zontViewModel.deviceStatusState,
            targetTempState = zontViewModel.targetTempState,
            onIncreaseBtnClicked = zontViewModel::increaseTargetTempState,
            onDecreaseBtnClicked = zontViewModel::decreaseTargetTempState,
            onAcceptBtnClicked = {
                zontViewModel.setTemp(
                    zontViewModel.token,
                    zontViewModel.deviceId,
                    zontViewModel.targetTempState.value
                )
            },
            onUpdateTargetTemp = zontViewModel::updateTargetTempState,
            onSettingsBtnClicked = startSettingsActivity
        )
    }

    return zontViewModel
}