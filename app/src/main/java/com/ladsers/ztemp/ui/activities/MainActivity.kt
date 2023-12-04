package com.ladsers.ztemp.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.ladsers.ztemp.ui.MainActivityContent
import com.ladsers.ztemp.ui.theme.ZTempTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ZTempTheme {
                MainActivityContent { updateAvailable, deviceName, tempStep ->
                    startSettingsActivity(
                        updateAvailable,
                        deviceName,
                        tempStep
                    )
                }
            }
        }
    }

    private fun startSettingsActivity(
        updateAvailable: Boolean,
        deviceName: String,
        tempStep: Double
    ) {
        val intent = Intent(this, SettingsActivity::class.java)

        intent.putExtra("updateAvailable", updateAvailable)
        intent.putExtra("deviceName", deviceName)
        intent.putExtra("tempStep", tempStep)

        startActivity(intent)
    }
}