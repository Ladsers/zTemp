package com.ladsers.ztemp.data.models.zont

import com.google.gson.annotations.SerializedName


data class ThermostatTargetTemps1(

    @SerializedName("manual") var manual: Boolean? = null,
    @SerializedName("temp") var temp: Double? = null

)