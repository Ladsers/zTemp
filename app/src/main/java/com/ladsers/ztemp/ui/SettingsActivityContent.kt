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
import com.ladsers.ztemp.domain.viewModels.SettingsViewModel
import com.ladsers.ztemp.ui.screens.SettingsScreen

@Composable
fun SettingsActivityContent(updateAvailable: Boolean, deviceName: String, tempStep: Double) {
    val settingsViewModel: SettingsViewModel = viewModel(
        factory = SettingsViewModel.provideFactory(
            (LocalContext.current.applicationContext as ZtempApplication).container.dataStoreRepository,
            tempStep = tempStep,
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
        SettingsScreen(settingsViewModel, updateAvailable, deviceName)
    }
}