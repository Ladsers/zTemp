package com.ladsers.ztemp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Settings
import androidx.compose.material.icons.rounded.Thermostat
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import com.ladsers.ztemp.data.models.DeviceStatus

@Composable
fun StatusScreen2(
    deviceStatus: DeviceStatus?
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.padding(top = 22.dp))
        Text(
            text = deviceStatus?.currentTemp?.let { "+$it°C" } ?: "--",
            color = Color.White,
            fontSize = 24.sp
        )
        Text(
            text = deviceStatus?.targetTemp?.let { "+$it°C" } ?: "--",
            color = Color(255, 102, 0),
            fontSize = 24.sp
        )
        Spacer(modifier = Modifier.padding(top = 10.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(10.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {

            Button(
                onClick = { /* todo */ },
                colors = ButtonDefaults.primaryButtonColors()
            ) {
                Icon(imageVector = Icons.Rounded.Thermostat, contentDescription = "thermostat")
            }
            Button(
                onClick = { /* todo */ },
                colors = ButtonDefaults.secondaryButtonColors()
            ) {
                Icon(imageVector = Icons.Rounded.Settings, contentDescription = "settings")
            }
        }
        Spacer(modifier = Modifier.padding(bottom = 22.dp))
    }
}