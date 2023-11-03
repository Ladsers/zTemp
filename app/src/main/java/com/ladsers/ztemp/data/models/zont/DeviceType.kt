package com.ladsers.ztemp.data.models.zont

import com.google.gson.annotations.SerializedName


data class DeviceType(

    @SerializedName("code") var code: String? = null,
    @SerializedName("name") var name: String? = null

)