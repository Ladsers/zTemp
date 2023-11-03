package com.ladsers.ztemp.data.models.zont

import com.google.gson.annotations.SerializedName


data class LastBoilerState(

    @SerializedName("time") var time: Int? = null,
    @SerializedName("boiler_work_time") var boilerWorkTime: Int? = null,
    @SerializedName("thermostat_mode") var thermostatMode: String? = null,
    @SerializedName("thermostat_mode_int") var thermostatModeInt: Int? = null,
    @SerializedName("gate") var gate: Boolean? = null,
    @SerializedName("target_temp") var targetTemp: Double? = null,
    @SerializedName("power") var power: Boolean? = null,
    @SerializedName("fail") var fail: Boolean? = null,

    )