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
fun ZtempApp() {
    val zontViewModel: ZontViewModel = viewModel(
        factory = ZontViewModel.provideFactory(
            (LocalContext.current.applicationContext as ZtempApplication).container.zontRepository,
            owner = LocalSavedStateRegistryOwner.current
        )
    )

    Scaffold(
        modifier = Modifier.fillMaxWidth(),
        timeText = {
            TimeText(timeTextStyle = TimeTextDefaults.timeTextStyle(
                fontSize = 10.sp
            ))
        }
    ) {
        MainScreen(userInfoState = zontViewModel.userInfoState)
    }
}