package com.ladsers.ztemp.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import com.ladsers.ztemp.R
import com.ladsers.ztemp.data.models.TempSetter
import com.ladsers.ztemp.ui.theme.wearColorPalette

@Composable
fun TempSetterScreen(
    tempSetter: TempSetter,
    sendResult: (Double) -> Unit
) {

    val targetTemp = run {
        val state = remember { mutableStateOf(tempSetter.targetTemp) }
        object : MutableState<Double> by state {
            override var value: Double
                get() = state.value
                set(value) {
                    if (value <= 5.0) {
                        state.value = 5.0
                    } else if (value >= 35.0) {
                        state.value = 35.0
                    } else {
                        state.value = value
                    }
                }
        }
    } as MutableState<Double>

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "  ${targetTemp.value}°C",
            color = wearColorPalette.secondary,
            fontSize = 23.sp,
            modifier = Modifier.padding(bottom = 15.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { targetTemp.value -= tempSetter.tempStep },
                colors = ButtonDefaults.secondaryButtonColors(),
                modifier = Modifier
                    .padding(end = 6.dp)
                    .width(47.dp)
                    .height(47.dp),
            ) {
                Icon(
                    imageVector = Icons.Rounded.Remove,
                    contentDescription = stringResource(id = R.string.cd_decreaseTemp)
                )
            }
            Button(
                onClick = { sendResult(targetTemp.value) },
                colors = ButtonDefaults.primaryButtonColors(),
                modifier = Modifier.padding(start = 8.dp, end = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Done,
                    contentDescription = stringResource(id = R.string.cd_confirm)
                )
            }
            Button(
                onClick = { targetTemp.value += tempSetter.tempStep },
                colors = ButtonDefaults.secondaryButtonColors(),
                modifier = Modifier
                    .padding(start = 6.dp)
                    .width(47.dp)
                    .height(47.dp),
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = stringResource(id = R.string.cd_increaseTemp)
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 15.dp)
        ) {
            Button(
                onClick = { targetTemp.value = tempSetter.presetTemp1 },
                colors = ButtonDefaults.secondaryButtonColors(),
                enabled = tempSetter.addFeatures,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .width(47.dp)
                    .height(37.dp)
            ) {
                Text(text = tempSetter.presetTemp1.toString().trimEnd { it == '0' }
                    .trimEnd { it == '.' })
            }
            Button(
                onClick = { targetTemp.value = tempSetter.presetTemp2 },
                colors = ButtonDefaults.secondaryButtonColors(),
                enabled = tempSetter.addFeatures,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .width(47.dp)
                    .height(37.dp),
            ) {
                Text(text = tempSetter.presetTemp2.toString().trimEnd { it == '0' }
                    .trimEnd { it == '.' })
            }
        }
    }
}