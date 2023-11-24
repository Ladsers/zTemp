package com.ladsers.ztemp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.*
import com.ladsers.ztemp.R
import com.ladsers.ztemp.data.models.DeviceStatus
import com.ladsers.ztemp.ui.theme.wearColorPalette
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

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
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (deviceStatus?.online == false) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Rounded.CloudOff,
                        contentDescription = null,
                        modifier = Modifier
                            .width(24.dp)
                            .padding(top = 4.dp, bottom = 5.dp),
                        tint = Color.Red
                    )
                }
            } else {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Thermostat,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier
                            .width(20.dp)
                            .padding(top = 2.dp)
                    )
                    Text(
                        text = deviceStatus?.currentTemp?.let { "$it°C" } ?: "—",
                        color = Color.White,
                        fontSize = 25.sp,
                        modifier = Modifier.padding(start = 2.dp)
                    )

                    if (deviceStatus?.mainPower == false) {
                        Icon(
                            imageVector = Icons.Rounded.BatteryChargingFull,
                            contentDescription = null,
                            modifier = Modifier
                                .width(22.dp)
                                .padding(top = 2.dp),
                            tint = Color.Red
                        )
                    }
                }
            }

            Row(
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = Icons.Rounded.TaskAlt,
                    contentDescription = null,
                    modifier = Modifier
                        .width(18.dp)
                        .padding(top = 2.dp),
                    tint = wearColorPalette.secondary
                )
                Text(
                    text = deviceStatus?.targetTemp?.let { "$it°C" } ?: "—",
                    color = wearColorPalette.secondary,
                    fontSize = 23.sp,
                    modifier = Modifier.padding(start = 4.dp)
                )
            }

            if (deviceStatus != null) {
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Refresh,
                        contentDescription = null,
                        modifier = Modifier
                            .width(16.dp)
                            .padding(top = 2.dp),
                        tint = Color.Gray
                    )
                    Text(
                        text = LocalDateTime.now().format(DateTimeFormatter.ofPattern("HH:mm")),
                        color = Color.Gray,
                        fontSize = 16.sp,
                        modifier = Modifier.padding(start = 2.dp)
                    )
                }
            } else {
                CircularProgressIndicator(
                    modifier = Modifier.width(16.dp),
                    indicatorColor = wearColorPalette.primary,
                    trackColor = Color.Black,
                )
            }
        }

        Column(
            modifier = Modifier
                .fillMaxHeight()
                .fillMaxWidth(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {

                Button(
                    onClick = { /* todo */ },
                    colors = ButtonDefaults.primaryButtonColors(),
                    modifier = Modifier.padding(end = 8.dp),
                    enabled = deviceStatus?.online == true
                ) {
                    Icon(
                        imageVector = Icons.Rounded.LocalFireDepartment,
                        contentDescription = stringResource(id = R.string.cd_setTemp)
                    )
                }
                Button(
                    onClick = { /* todo */ },
                    colors = ButtonDefaults.secondaryButtonColors(),
                    modifier = Modifier.padding(start = 8.dp)
                ) {
                    Icon(
                        imageVector = Icons.Rounded.Settings,
                        contentDescription = stringResource(id = R.string.cd_settings)
                    )
                }
            }
            Spacer(modifier = Modifier.height(18.dp))
        }

    }
}