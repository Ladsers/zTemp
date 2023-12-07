package com.ladsers.ztemp.ui.components

import android.app.RemoteInput
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.view.inputmethod.EditorInfo
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.CardDefaults
import androidx.wear.compose.material.MaterialTheme
import androidx.wear.compose.material.Text
import androidx.wear.compose.material.TitleCard
import androidx.wear.input.RemoteInputIntentHelper
import androidx.wear.input.wearableExtender
import com.ladsers.ztemp.R
import com.ladsers.ztemp.ui.theme.AlmostBlack
import com.ladsers.ztemp.ui.theme.Teal

@Composable
fun TextInputCard(
    label: String,
    valueState: State<String>,
    onUpdateValue: (String) -> Unit,
    enabled: Boolean = true,
    disabledAction: () -> Unit = {}
) {

    val launcher =
        rememberLauncherForActivityResult(ActivityResultContracts.StartActivityForResult()) {
            it.data?.let { data ->
                val results: Bundle = RemoteInput.getResultsFromIntent(data)
                val result = SpannableString(results.getCharSequence("value")).toString()
                onUpdateValue(result)
            }
        }

    TitleCard(
        title = { Text(label, fontSize = 18.sp, color = if (enabled) Color.White else Color.Gray) },
        content = {
            Text(
                valueState.value.ifEmpty { stringResource(id = R.string.enter) },
                color = if (enabled) Teal else Color.Gray
            )
        },
        modifier = Modifier.fillMaxWidth(),
        backgroundPainter = CardDefaults.cardBackgroundPainter(
            startBackgroundColor = if (enabled) MaterialTheme.colors.surface else AlmostBlack,
            endBackgroundColor = if (enabled) MaterialTheme.colors.surface else AlmostBlack
        ),
        enabled = true,
        onClick = {
            if (enabled) {
                val intent: Intent = RemoteInputIntentHelper.createActionRemoteInputIntent();
                val remoteInputs: List<RemoteInput> = listOf(
                    RemoteInput.Builder("value")
                        .setLabel(label)
                        .wearableExtender {
                            setEmojisAllowed(false)
                            setInputActionType(EditorInfo.IME_ACTION_DONE)
                        }.build()
                )

                RemoteInputIntentHelper.putRemoteInputsExtra(intent, remoteInputs)
                launcher.launch(intent)
            } else {
                disabledAction()
            }
        }
    )
}