package com.ladsers.ztemp.ui.activities

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.wear.activity.ConfirmationActivity
import androidx.wear.widget.ConfirmationOverlay
import com.ladsers.ztemp.R
import com.ladsers.ztemp.data.enums.ConfirmationType
import com.ladsers.ztemp.data.enums.ResultActivityKey
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
            ResultActivityKey.INPUT_KEY.value,
            this,
            TempSetterContract()
        ) { temp -> temp?.let { viewModel.setTemp(it) } }

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
                    },
                    showConfirmationActivity = fun(
                        confirmationType: ConfirmationType,
                        setTemp: Double?
                    ) {
                        showConfirmationActivity(confirmationType, setTemp)
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

    private fun showConfirmationActivity(confirmationType: ConfirmationType, setTemp: Double?) {
        startActivity(
            Intent(this, ConfirmationActivity::class.java)
                .putExtra(
                    ConfirmationActivity.EXTRA_ANIMATION_TYPE,
                    if (confirmationType == ConfirmationType.SUCCESS) ConfirmationActivity.SUCCESS_ANIMATION
                    else ConfirmationActivity.FAILURE_ANIMATION
                )
                .putExtra(
                    ConfirmationActivity.EXTRA_ANIMATION_DURATION_MILLIS,
                    if (confirmationType == ConfirmationType.SUCCESS) ConfirmationOverlay.DEFAULT_ANIMATION_DURATION_MS
                    else 1700
                )
                .putExtra(
                    ConfirmationActivity.EXTRA_MESSAGE,
                    if (confirmationType == ConfirmationType.SUCCESS) getString(
                        R.string.setTempSuccess,
                        setTemp
                    ).replace(',', '.')
                    else getString(R.string.setTempFailure)
                )
        )
    }
}