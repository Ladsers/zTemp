package com.ladsers.ztemp.data.repositories

import com.ladsers.ztemp.data.models.AppParams
import com.ladsers.ztemp.data.models.AuthData
import kotlinx.coroutines.flow.Flow

/**
 * Interactions with the internal watch storage.
 */
interface DataStoreRepository {
    suspend fun saveAuthData(authData: AuthData)
    fun getAuthData(): Flow<AuthData>
    suspend fun saveAppParams(appParams: AppParams)
    fun getAppParams(): Flow<AppParams>
}