package com.ladsers.ztemp.data.models

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * Data for the temp setter in the app.
 */
@Parcelize
data class TempSetter(
    val targetTemp: Double,
    val tempStep: Double,
    var presetTemp1: Double,
    var presetTemp2: Double,
    var addFeatures: Boolean
) : Parcelable