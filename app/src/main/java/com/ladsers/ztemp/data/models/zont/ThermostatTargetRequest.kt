package com.ladsers.ztemp.data.models.zont

import com.google.gson.annotations.SerializedName

data class ThermostatTargetRequest (

    @SerializedName("device_id") var id: Int? = null,
    @SerializedName("thermostat_target_temps") var thermostatTargetTemps: ThermostatTargetTemps? = ThermostatTargetTemps()

)