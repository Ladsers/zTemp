package com.ladsers.ztemp.data.models.zont

import com.google.gson.annotations.SerializedName


data class Devices(

    @SerializedName("device_type") var deviceType: DeviceType? = DeviceType(),
    @SerializedName("widget_type") var widgetType: String? = null,
    @SerializedName("appliance_type") var applianceType: String? = null,
    @SerializedName("hardware_type") var hardwareType: HardwareType? = HardwareType(),
    @SerializedName("id") var id: Int? = null,
    @SerializedName("is_active") var isActive: Boolean? = null,
    @SerializedName("online") var online: Boolean? = null,
    @SerializedName("owner_username") var ownerUsername: String? = null,
    @SerializedName("user_id") var userId: Int? = null,
    @SerializedName("last_receive_time") var lastReceiveTime: Int? = null,
    @SerializedName("last_receive_time_relative") var lastReceiveTimeRelative: Int? = null,
    @SerializedName("is_legacy_service_mode_view") var isLegacyServiceModeView: String? = null,
    @SerializedName("name") var name: String? = null,
    @SerializedName("show_heating_tab") var showHeatingTab: Boolean? = null,
    @SerializedName("thermostat_ext_mode") var thermostatExtMode: Int? = null,
    @SerializedName("thermostat_mode") var thermostatMode: String? = null,
    @SerializedName("thermostat_gate") var thermostatGate: Boolean? = null,
    @SerializedName("tempstep") var tempstep: Double? = null,
    @SerializedName("thermostat_hysteresis") var thermostatHysteresis: Double? = null,
    @SerializedName("ot_enabled") var otEnabled: Boolean? = null,
    @SerializedName("thermostat_target_temps") var thermostatTargetTemps: ThermostatTargetTemps? = ThermostatTargetTemps(),
    @SerializedName("thermostat_ext_modes_advanced") var thermostatExtModesAdvanced: Boolean? = null,
    @SerializedName("thermostat_relay_mode") var thermostatRelayMode: String? = null,
    @SerializedName("thermometers") var thermometers: ArrayList<Thermometers> = arrayListOf(),
    @SerializedName("io") var io: Io? = Io()

)