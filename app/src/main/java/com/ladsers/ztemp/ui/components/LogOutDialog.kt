package com.ladsers.ztemp.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Close
import androidx.compose.material.icons.rounded.Done
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.res.stringResource
import androidx.wear.compose.material.Button
import androidx.wear.compose.material.ButtonDefaults
import androidx.wear.compose.material.Icon
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.dialog.Alert
import com.ladsers.ztemp.R

/**
 * Dialog to confirm logging out.
 */
@Composable
fun LogOutDialog(
    dialogState: MutableState<Boolean>,
    positiveAction: () -> Unit
) {
    if (!dialogState.value) return // Dialog is currently hidden.

    Alert(
        title = { Text(text = stringResource(id = R.string.title_logOutDialog)) },
        content = { Text(text = stringResource(id = R.string.areYouSure)) },
        positiveButton = {
            Button(
                onClick = {
                    positiveAction() // Do action
                    dialogState.value = false // Hide dialog
                },
                colors = ButtonDefaults.primaryButtonColors()
            ) {
                Icon(
                    imageVector = Icons.Rounded.Done,
                    contentDescription = stringResource(id = R.string.cd_yes)
                )
            }
        },
        negativeButton = {
            Button(
                onClick = { dialogState.value = false }, // Just hide dialog
                colors = ButtonDefaults.secondaryButtonColors()
            ) {
                Icon(
                    imageVector = Icons.Rounded.Close,
                    contentDescription = stringResource(id = R.string.cd_no)
                )
            }
        })
}