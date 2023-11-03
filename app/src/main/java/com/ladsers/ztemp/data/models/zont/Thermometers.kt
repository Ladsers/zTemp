package com.ladsers.ztemp.data.models.zont

import com.google.gson.annotations.SerializedName


data class Thermometers(

    @SerializedName("is_assigned_to_slot") var isAssignedToSlot: Boolean? = null,
    @SerializedName("slot") var slot: Int? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("sort") var sort: Int? = null,
    @SerializedName("last_state") var lastState: String? = null,
    @SerializedName("last_value") var lastValue: Double? = null,
    @SerializedName("last_value_time") var lastValueTime: Int? = null

)