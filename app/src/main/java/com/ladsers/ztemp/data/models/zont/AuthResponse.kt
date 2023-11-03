package com.ladsers.ztemp.data.models.zont

import com.google.gson.annotations.SerializedName


data class AuthResponse(

    @SerializedName("token") var token: String? = null,
    @SerializedName("id") var id: Int? = null,
    @SerializedName("username") var username: String? = null,
    @SerializedName("ok") var ok: Boolean? = null

)