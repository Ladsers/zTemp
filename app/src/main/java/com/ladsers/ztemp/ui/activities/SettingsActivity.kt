package com.ladsers.ztemp.ui.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.wear.activity.ConfirmationActivity
import androidx.wear.remote.interactions.RemoteActivityHelper
import com.ladsers.ztemp.ui.activitycontents.SettingsActivityContent
import com.ladsers.ztemp.ui.theme.ZTempTheme
import java.util.concurrent.Executors

class SettingsActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val updateAvailable = intent.getBooleanExtra("updateAvailable", false)
        val deviceName = intent.getStringExtra("deviceName") ?: ""
        val tempStep = intent.getDoubleExtra("tempStep", 1.0)

        setContent {
            ZTempTheme {
                SettingsActivityContent(
                    updateAvailable,
                    deviceName,
                    tempStep,
                    finishActivity = { finish() },
                    goToWebsite = { url -> goToWebsite(url) })
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
}