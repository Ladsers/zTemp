package com.ladsers.ztemp.ui.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.launch
import androidx.annotation.StringRes
import androidx.wear.activity.ConfirmationActivity
import androidx.wear.remote.interactions.RemoteActivityHelper
import androidx.wear.widget.ConfirmationOverlay
import com.ladsers.ztemp.data.enums.ConfirmationType
import com.ladsers.ztemp.data.enums.ResultActivityKey
import com.ladsers.ztemp.domain.contracts.DonationContract
import com.ladsers.ztemp.domain.viewModels.SettingsViewModel
import com.ladsers.ztemp.ui.activitycontents.settingsActivityContent
import com.ladsers.ztemp.ui.theme.ZTempTheme
import java.util.concurrent.Executors

class SettingsActivity : ComponentActivity() {

    private lateinit var viewModel: SettingsViewModel
    private lateinit var donationLauncher: ActivityResultLauncher<Unit>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        donationLauncher = activityResultRegistry.register(
            ResultActivityKey.INPUT_KEY.value,
            this,
            DonationContract()
        ) { code -> code?.let { viewModel.checkCode(it) } } // action after RESULT_OK

        val updateAvailable = intent.getBooleanExtra("updateAvailable", false)
        val deviceName = intent.getStringExtra("deviceName") ?: ""
        val tempStep = intent.getDoubleExtra("tempStep", 1.0)

        setContent {
            ZTempTheme {
                viewModel = settingsActivityContent(
                    updateAvailable = updateAvailable,
                    deviceName = deviceName,
                    tempStep = tempStep,
                    finishActivity = ::finish,
                    goToWebsite = { url -> goToWebsite(url) },
                    startDonationActivity = ::startDonationActivity,
                    showConfirmationActivity = { confirmationType, message ->
                        showConfirmationActivity(
                            confirmationType,
                            message
                        )
                    }
                )
            }
        }
    }

    private fun goToWebsite(url: String) {
        val remoteActivityHelper = RemoteActivityHelper(this, Executors.newSingleThreadExecutor())
        remoteActivityHelper.startRemoteActivity(
            Intent(Intent.ACTION_VIEW)
                .addCategory(Intent.CATEGORY_BROWSABLE)
                .setData(
                    Uri.parse(url)
                ),
            null
        )

        startActivity(
            Intent(this, ConfirmationActivity::class.java)
                .putExtra(
                    ConfirmationActivity.EXTRA_ANIMATION_TYPE,
                    ConfirmationActivity.OPEN_ON_PHONE_ANIMATION
                )
        )
    }

    private fun startDonationActivity() = donationLauncher.launch()

    private fun showConfirmationActivity(
        confirmationType: ConfirmationType,
        @StringRes resId: Int
    ) {
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
                .putExtra(ConfirmationActivity.EXTRA_MESSAGE, getString(resId))
        )
    }
}