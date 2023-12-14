package com.ladsers.ztemp.ui.activities

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.wear.activity.ConfirmationActivity
import androidx.wear.remote.interactions.RemoteActivityHelper
import com.ladsers.ztemp.data.enums.ResultActivityKey
import com.ladsers.ztemp.ui.screens.DonationScreen
import com.ladsers.ztemp.ui.theme.ZTempTheme
import java.util.concurrent.Executors

class DonationActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            ZTempTheme {
                DonationScreen(
                    sendResult = { code -> sendResult(code) },
                    goToWebDonation = ::goToWebDonation
                )
            }
        }
    }

    private fun sendResult(code: String) {
        if (code.isEmpty()) return

        val intent = Intent().putExtra(ResultActivityKey.RESULT_KEY.value, code)
        setResult(RESULT_OK, intent)
        finish()
    }

    private fun goToWebDonation() {
        val remoteActivityHelper = RemoteActivityHelper(this, Executors.newSingleThreadExecutor())
        remoteActivityHelper.startRemoteActivity(
            Intent(Intent.ACTION_VIEW)
                .addCategory(Intent.CATEGORY_BROWSABLE)
                .setData(
                    Uri.parse("https://pay.cloudtips.ru/p/9da1c376")
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