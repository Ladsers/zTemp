package com.ladsers.ztemp.data.repositories

import android.content.Context
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.doublePreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.intPreferencesKey
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import com.ladsers.ztemp.data.models.AppParams
import com.ladsers.ztemp.data.models.AuthData
import kotlinx.coroutines.flow.map

private val Context.dataStore by preferencesDataStore("store")

class AppDataStoreRepository(
    private val context: Context
) : DataStoreRepository {

    private val token = stringPreferencesKey("token")
    private val deviceId = intPreferencesKey("device_id")

    private val presetTemp1 = doublePreferencesKey("preset_temp_1")
    private val presetTemp2 = doublePreferencesKey("preset_temp_2")
    private val addFeatures = booleanPreferencesKey("additional_features")

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

    override suspend fun saveAppParams(appParams: AppParams) {
        context.dataStore.edit { pref ->
            pref[presetTemp1] = appParams.presetTemp1
            pref[presetTemp2] = appParams.presetTemp2
            pref[addFeatures] = appParams.addFeatures
        }
    }

    override fun getAppParams() = context.dataStore.data.map { pref ->
        return@map AppParams(
            presetTemp1 = pref[presetTemp1] ?: 10.0,
            presetTemp2 = pref[presetTemp2] ?: 24.0,
            addFeatures = pref[addFeatures] ?: false
        )
    }
}