package com.ladsers.ztemp.domain.contracts

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.activity.result.contract.ActivityResultContract
import com.ladsers.ztemp.data.enums.TempSetterKey
import com.ladsers.ztemp.data.models.TempSetter
import com.ladsers.ztemp.ui.activities.TempSetterActivity

class TempSetterContract : ActivityResultContract<TempSetter, Double?>() {
    override fun createIntent(context: Context, input: TempSetter?): Intent {
        return Intent(context, TempSetterActivity::class.java).putExtra(
            TempSetterKey.INPUT_KEY.value,
            input
        )
    }

    override fun parseResult(resultCode: Int, intent: Intent?): Double? {
        if (intent == null || resultCode != Activity.RESULT_OK) return null

        val res = intent.getDoubleExtra(TempSetterKey.RESULT_KEY.value, 0.0)
        return if (res != 0.0) res else null
    }
}