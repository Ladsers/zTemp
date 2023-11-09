package com.ladsers.ztemp.domain

import android.app.Application
import com.ladsers.ztemp.domain.containers.AppContainer
import com.ladsers.ztemp.domain.containers.DefaultAppContainer

class ZtempApplication : Application() {
    lateinit var container: AppContainer
    override fun onCreate() {
        super.onCreate()
        container = DefaultAppContainer(applicationContext)
    }
}