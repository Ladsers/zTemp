package com.ladsers.ztemp.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Add
import androidx.compose.material.icons.rounded.Done
import androidx.compose.material.icons.rounded.Remove
import androidx.compose.runtime.Composable
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
import com.ladsers.ztemp.ui.theme.wearColorPalette

@Composable
fun SetTempScreen(
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight(),
        verticalArrangement = Arrangement.Center, //todo
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "23.0°C",
            color = wearColorPalette.secondary,
            fontSize = 23.sp,
            modifier = Modifier.padding(bottom = 15.dp)
        )

        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { /* todo */ },
                colors = ButtonDefaults.secondaryButtonColors(),
                modifier = Modifier
                    .padding(end = 6.dp)
                    .width(47.dp)
                    .height(47.dp),
            ) {
                Icon(
                    imageVector = Icons.Rounded.Remove,
                    contentDescription = stringResource(id = R.string.cd_setTemp)
                )
            }
            Button(
                onClick = { /* todo */ },
                colors = ButtonDefaults.primaryButtonColors(),
                modifier = Modifier.padding(start = 8.dp, end = 8.dp)
            ) {
                Icon(
                    imageVector = Icons.Rounded.Done,
                    contentDescription = stringResource(id = R.string.cd_settings)
                )
            }
            Button(
                onClick = { /* todo */ },
                colors = ButtonDefaults.secondaryButtonColors(),
                modifier = Modifier
                    .padding(start = 6.dp)
                    .width(47.dp)
                    .height(47.dp),
            ) {
                Icon(
                    imageVector = Icons.Rounded.Add,
                    contentDescription = stringResource(id = R.string.cd_setTemp)
                )
            }
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(top = 15.dp)
        ) {
            Button(
                onClick = { /* todo */ },
                colors = ButtonDefaults.secondaryButtonColors(),
                enabled = false,
                modifier = Modifier
                    .padding(end = 8.dp)
                    .width(47.dp)
                    .height(37.dp)
            ) {
                Text(text = "10")
            }
            Button(
                onClick = { /* todo */ },
                colors = ButtonDefaults.secondaryButtonColors(),
                enabled = false,
                modifier = Modifier
                    .padding(start = 8.dp)
                    .width(47.dp)
                    .height(37.dp),
            ) {
                Text(text = "23.5")
            }
        }
    }
}