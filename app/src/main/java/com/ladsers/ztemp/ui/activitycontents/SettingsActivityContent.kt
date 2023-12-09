package com.ladsers.ztemp.ui.activitycontents

import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSavedStateRegistryOwner
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ladsers.ztemp.domain.ZtempApplication
import com.ladsers.ztemp.domain.viewModels.SettingsViewModel
import com.ladsers.ztemp.ui.screens.SettingsScreen

@Composable
fun SettingsActivityContent(
    updateAvailable: Boolean,
    deviceName: String,
    tempStep: Double,
    finishActivity: () -> Unit,
    goToWebsite: (String) -> Unit
) {
    val settingsViewModel: SettingsViewModel = viewModel(
        factory = SettingsViewModel.provideFactory(
            (LocalContext.current.applicationContext as ZtempApplication).container.dataStoreRepository,
            tempStep = tempStep,
            owner = LocalSavedStateRegistryOwner.current
        )
    )

    SettingsScreen(settingsViewModel, updateAvailable, deviceName, finishActivity, goToWebsite)
}