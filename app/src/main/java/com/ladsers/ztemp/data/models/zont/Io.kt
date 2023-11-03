package com.ladsers.ztemp.data.models.zont

import com.google.gson.annotations.SerializedName


data class Io(

    @SerializedName("voltage") var voltage: Double? = null,
    @SerializedName("power-source") var powerSource: String? = null,
    @SerializedName("last-boiler-state") var lastBoilerState: LastBoilerState? = LastBoilerState()

)