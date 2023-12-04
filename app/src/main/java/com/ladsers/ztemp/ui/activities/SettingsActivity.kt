package com.ladsers.ztemp.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.ladsers.ztemp.ui.SettingsActivityContent
import com.ladsers.ztemp.ui.theme.ZTempTheme

class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val updateAvailable = intent.getBooleanExtra("updateAvailable", false)
        val deviceName = intent.getStringExtra("deviceName") ?: ""
        val tempStep = intent.getDoubleExtra("tempStep", 1.0)

        setContent {
            ZTempTheme {
                SettingsActivityContent(updateAvailable, deviceName, tempStep)
            }
        }
    }
}