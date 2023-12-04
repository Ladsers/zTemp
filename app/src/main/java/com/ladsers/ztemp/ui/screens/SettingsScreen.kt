package com.ladsers.ztemp.ui.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.CardDefaults
import androidx.wear.compose.material.ListHeader
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.ScalingLazyColumn
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TitleCard
import com.ladsers.ztemp.BuildConfig
import com.ladsers.ztemp.R
import com.ladsers.ztemp.domain.viewModels.SettingsViewModel
import com.ladsers.ztemp.ui.theme.DarkOrange
import com.ladsers.ztemp.ui.theme.Orange
import com.ladsers.ztemp.ui.theme.Teal

@Composable
fun SettingsScreen(
    viewModel: SettingsViewModel,
    updateAvailable: Boolean,
    deviceName: String,
    goToWebsite: (String) -> Unit
) {
    ScalingLazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        item {
            ListHeader {
                Text(text = "Настройки")
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
            TextInputChip(
                label = stringResource(id = R.string.presetTemp1),
                valueState = viewModel.presetTemp1,
                onUpdateValue = viewModel::updatePresetTemp1,
                enabled = viewModel.addFeatures.value
            )
        }
        item {
            TextInputChip(
                label = stringResource(id = R.string.presetTemp2),
                valueState = viewModel.presetTemp2,
                onUpdateValue = viewModel::updatePresetTemp2,
                enabled = viewModel.addFeatures.value
            )
        }
        item {
            SettingsCard(title = stringResource(id = R.string.changeDevice), content = deviceName)
        }
        item {
            SettingsCard(
                title = stringResource(id = R.string.supportProject),
                content = stringResource(id = R.string.supportProjectDescription)
            )
        }
        item {
            SettingsCard(
                title = stringResource(id = R.string.appWebsite),
                enabled = true,
                action = { goToWebsite("https://www.ladsers.com/ztemp") })
        }
        item {
            SettingsCard(title = stringResource(id = R.string.createdBy))
        }
        item {
            SettingsCard(title = stringResource(id = R.string.licenseThirdParty),
                enabled = true,
                action = { goToWebsite("https://www.ladsers.com/ztemp/license") })
        }
        item {
            SettingsCard(
                title = stringResource(id = R.string.version),
                content = BuildConfig.VERSION_NAME
            )
        }
        item {
            SettingsCard(title = stringResource(id = R.string.logOut))
        }
    }
}

@Composable
fun SettingsCard(
    title: String,
    content: String? = null,
    enabled: Boolean = false,
    action: () -> Unit = {}
) {
    TitleCard(
        title = { Text(title, fontSize = 18.sp, color = Color.White) },
        backgroundPainter = CardDefaults.cardBackgroundPainter(
            startBackgroundColor = MaterialTheme.colors.surface,
            endBackgroundColor = MaterialTheme.colors.surface
        ),
        onClick = action,
        enabled = enabled
    ) {
        content?.let { Text(it, color = Teal) }
    }
}