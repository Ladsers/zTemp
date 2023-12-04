package com.ladsers.ztemp.ui.screens

import android.app.RemoteInput
import android.content.Intent
import android.os.Bundle
import android.text.SpannableString
import android.view.inputmethod.EditorInfo
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Refresh
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.sp
import androidx.wear.compose.material.*
import androidx.wear.input.RemoteInputIntentHelper
import androidx.wear.input.wearableExtender
import com.ladsers.ztemp.domain.viewModels.ZontViewModel
import com.ladsers.ztemp.ui.theme.Teal

@Composable
fun SignInScreen(viewModel: ZontViewModel) {
    ScalingLazyColumn(
        modifier = Modifier.fillMaxWidth()
    ) {
        item {
            ListHeader {
                Text(text = "Вход в ZONT")
            }
        }
        item {
            TextInputChip(
                label = "Логин",
                valueState = viewModel.username,
                onUpdateValue = viewModel::updateUsername
            )
        }
        item {
            TextInputChip(
                label = "Пароль",
                valueState = viewModel.password,
                onUpdateValue = viewModel::updatePassword
            )
        }
        item {
            Button(onClick = viewModel::signIn) {
                Icon(imageVector = Icons.Rounded.Refresh, contentDescription = "retry")
            }
        }
    }
}

@Composable
fun TextInputChip(label: String, valueState: State<String>, onUpdateValue: (String) -> Unit, enabled: Boolean = true) {

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
                valueState.value.ifEmpty { "Ввод" },
                color = if (enabled) Teal else Color.Gray
            )
        },
        modifier = Modifier.fillMaxWidth(),
        backgroundPainter = CardDefaults.cardBackgroundPainter(
            startBackgroundColor = MaterialTheme.colors.surface,
            endBackgroundColor = MaterialTheme.colors.surface
        ),
        enabled = enabled,
        onClick = {
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
        }
    )
}

