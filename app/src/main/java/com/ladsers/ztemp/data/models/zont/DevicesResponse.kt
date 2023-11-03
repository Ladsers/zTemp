package com.ladsers.ztemp.data.models.zont

import com.google.gson.annotations.SerializedName


data class DevicesResponse(

    @SerializedName("devices") var devices: ArrayList<Devices> = arrayListOf(),
    @SerializedName("device_tree") var deviceTree: String? = null,
    @SerializedName("ok") var ok: Boolean? = null

)