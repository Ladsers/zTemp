package com.ladsers.ztemp.data.repositories

import android.content.Context
import androidx.datastore.preferences.core.*
import androidx.datastore.preferences.preferencesDataStore
import com.ladsers.ztemp.data.models.AuthData
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("store")

class DeviceDataStoreRepository(
    private val context: Context
) : DataStoreRepository {

    private val token = stringPreferencesKey("token")
    private val deviceId = intPreferencesKey("device_id")

    override suspend fun saveAuthData(authData: AuthData) {
        context.dataStore.edit { pref ->
            pref[token] = authData.token
            pref[deviceId] = authData.deviceId
        }
    }

    override fun getAuthData() = context.dataStore.data.map { pref ->
        return@map AuthData(
            token = pref[token] ?: "",
            deviceId = pref[deviceId] ?: 0
        )
    }
}