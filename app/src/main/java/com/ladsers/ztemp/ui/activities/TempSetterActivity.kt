package com.ladsers.ztemp.ui.activities

import android.content.Intent
import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.ladsers.ztemp.data.enums.TempSetterKey
import com.ladsers.ztemp.data.models.TempSetter
import com.ladsers.ztemp.ui.screens.TempSetterScreen
import com.ladsers.ztemp.ui.theme.ZTempTheme

class TempSetterActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        @Suppress("DEPRECATION") val tempSetter =
            if (Build.VERSION.SDK_INT >= 33) intent.getParcelableExtra(
                TempSetterKey.INPUT_KEY.value,
                TempSetter::class.java
            )
            else intent.getParcelableExtra<TempSetter>(TempSetterKey.INPUT_KEY.value)

        tempSetter?.let {
            setContent {
                ZTempTheme {
                    TempSetterScreen(it, sendResult = fun(setTemp: Double) { sendResult(setTemp) })
                }
            }
        } ?: finish()
    }

    private fun sendResult(setTemp: Double) {
        val intent = Intent().putExtra(
            TempSetterKey.RESULT_KEY.value,
            setTemp
        )
        setResult(RESULT_OK, intent)
        finish()
    }
}