package com.ladsers.ztemp.data.models.zont

import com.google.gson.annotations.SerializedName


data class ThermostatTargetTemps(

    @SerializedName("0") var thermostatTargetTemps0: ThermostatTargetTemps0? = ThermostatTargetTemps0(),
    @SerializedName("1") var thermostatTargetTemps1: ThermostatTargetTemps1? = ThermostatTargetTemps1()

)