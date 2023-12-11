package com.ladsers.ztemp.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.ActivityResultRegistry
import androidx.wear.activity.ConfirmationActivity
import com.ladsers.ztemp.data.enums.TempSetterKey
import com.ladsers.ztemp.data.models.TempSetter
import com.ladsers.ztemp.domain.contracts.TempSetterContract
import com.ladsers.ztemp.domain.viewModels.ZontViewModel
import com.ladsers.ztemp.ui.activitycontents.MainActivityContent
import com.ladsers.ztemp.ui.theme.ZTempTheme

class MainActivity : ComponentActivity() {

    private lateinit var viewModel: ZontViewModel
    private lateinit var tempSetterLauncher: ActivityResultLauncher<TempSetter>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        tempSetterLauncher = activityResultRegistry.register(
            TempSetterKey.INPUT_KEY.value,
            this,
            TempSetterContract()
        ) { temp -> temp?.let { if (viewModel.setTemp(it)) showConfirmation(it) else showFailure() } }

        setContent {
            ZTempTheme {
                viewModel = MainActivityContent(
                    startTempSetterActivity = fun(tempSetter: TempSetter) {
                        startTempSetterActivity(tempSetter)
                    },
                    startSettingsActivity = fun(
                        updateAvailable: Boolean,
                        deviceName: String,
                        tempStep: Double
                    ) {
                        startSettingsActivity(
                            updateAvailable,
                            deviceName,
                            tempStep
                        )
                    })

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

    private fun startTempSetterActivity(tempSetter: TempSetter) =
        tempSetterLauncher.launch(tempSetter)

    private fun showConfirmation(setTemp: Double) {
        startActivity(
            Intent(this, ConfirmationActivity::class.java)
                .putExtra(
                    ConfirmationActivity.EXTRA_ANIMATION_TYPE,
                    ConfirmationActivity.SUCCESS_ANIMATION
                )
                .putExtra(
                    ConfirmationActivity.EXTRA_MESSAGE,
                    "Установлено $setTemp°C"
                )
        )
    }

    private fun showFailure() {
        startActivity(
            Intent(this, ConfirmationActivity::class.java)
                .putExtra(
                    ConfirmationActivity.EXTRA_ANIMATION_TYPE,
                    ConfirmationActivity.FAILURE_ANIMATION
                )
                .putExtra(
                    ConfirmationActivity.EXTRA_ANIMATION_DURATION_MILLIS,
                    1700
                )
                .putExtra(
                    ConfirmationActivity.EXTRA_MESSAGE,
                    "Сбой установки температуры"
                )
        )
    }
}