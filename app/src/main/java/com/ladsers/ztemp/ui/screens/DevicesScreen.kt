package com.ladsers.ztemp.ui.screens

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Logout
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.wear.compose.material.*
import com.ladsers.ztemp.data.models.DeviceStatus

@Composable
fun DevicesScreen(
    devices: List<DeviceStatus>,
    onDeviceSelected: (Int) -> Unit,
    onLogOutClicked: () -> Unit
) {
    ScalingLazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        item {
            ListHeader {
                Text(text = "Доступные устройства")
            }
        }
        items(devices) { device ->
            Chip(
                onClick = { onDeviceSelected(device.id) },
                label = { Text(device.name) },
                colors = ChipDefaults.secondaryChipColors(),
                modifier = Modifier.fillMaxWidth()
            )
        }
        item {
            Button(onClick = onLogOutClicked) {
                Icon(imageVector = Icons.Rounded.Logout, contentDescription = "logout")
            }
        }
    }
}