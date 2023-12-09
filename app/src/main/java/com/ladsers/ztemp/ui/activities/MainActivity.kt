package com.ladsers.ztemp.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.ladsers.ztemp.domain.viewModels.ZontViewModel
import com.ladsers.ztemp.ui.MainActivityContent
import com.ladsers.ztemp.ui.theme.ZTempTheme

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: ZontViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ZTempTheme {
                viewModel = MainActivityContent { updateAvailable, deviceName, tempStep ->
                    startSettingsActivity(
                        updateAvailable,
                        deviceName,
                        tempStep
                    )
                }
                lifecycle.addObserver(viewModel)
            }
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        lifecycle.removeObserver(viewModel)
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