package com.ladsers.ztemp.data.models.zont

import com.google.gson.annotations.SerializedName


data class DevicesRequest(

    @SerializedName("load_io") var loadIo: Boolean = true

)