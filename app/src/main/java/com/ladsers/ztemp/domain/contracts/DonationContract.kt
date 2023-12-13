package com.ladsers.ztemp.domain.contracts

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.ladsers.ztemp.data.enums.ResultActivityKey
import com.ladsers.ztemp.ui.activities.DonationActivity

class DonationContract : ActivityResultContract<Unit, String?>() {
    override fun createIntent(context: Context, input: Unit): Intent {
        return Intent(context, DonationActivity::class.java)
    }

    override fun parseResult(resultCode: Int, intent: Intent?): String? {
        if (intent == null || resultCode != Activity.RESULT_OK) return null
        return intent.getStringExtra(ResultActivityKey.RESULT_KEY.value)
    }

}