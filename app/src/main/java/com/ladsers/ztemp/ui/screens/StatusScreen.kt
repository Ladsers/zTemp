package com.ladsers.ztemp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Done
import androidx.compose.material.icons.filled.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
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
fun StatusScreen(
    deviceStatus: DeviceStatus,
    targetTempState: State<Double>,
    onDecreaseBtnClicked: () -> Unit,
    onIncreaseBtnClicked: () -> Unit,
    onAcceptBtnClicked: () -> Unit,
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.padding(top = 22.dp))
        Text(text = "${deviceStatus.currentTemp ?: "--"}", color = Color.White, fontSize = 24.sp)
        Text(
            text = "${targetTempState.value}",
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
                onClick = onDecreaseBtnClicked,
                colors = ButtonDefaults.secondaryButtonColors()
            ) {
                Icon(imageVector = Icons.Filled.Remove, contentDescription = "decrease")
            }
            Button(onClick = onAcceptBtnClicked, colors = ButtonDefaults.primaryButtonColors()) {
                Icon(imageVector = Icons.Filled.Done, contentDescription = "done")
            }
            Button(
                onClick = onIncreaseBtnClicked,
                colors = ButtonDefaults.secondaryButtonColors()
            ) {
                Icon(imageVector = Icons.Filled.Add, contentDescription = "increase")
            }
        }
        Spacer(modifier = Modifier.padding(bottom = 22.dp))
    }
}