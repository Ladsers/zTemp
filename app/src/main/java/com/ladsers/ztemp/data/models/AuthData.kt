package com.ladsers.ztemp.data.models

/**
 * Authentication data in ZONT system.
 */
data class AuthData(
    /**
     * Token received from ZONT server.
     */
    var token: String,
    /**
     * Selected device ID.
     */
    var deviceId: Int
)