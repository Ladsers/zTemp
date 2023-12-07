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
import com.ladsers.ztemp.ui.components.TextInputCard
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
            TextInputCard(
                label = "Логин",
                valueState = viewModel.username,
                onUpdateValue = viewModel::updateUsername
            )
        }
        item {
            TextInputCard(
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

