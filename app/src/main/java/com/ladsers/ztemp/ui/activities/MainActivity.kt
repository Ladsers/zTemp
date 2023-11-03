package com.ladsers.ztemp.ui.activities

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.ladsers.ztemp.ui.ZtempApp
import com.ladsers.ztemp.ui.theme.ZTempTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ZTempTheme {
                ZtempApp()
            }
        }
    }
}