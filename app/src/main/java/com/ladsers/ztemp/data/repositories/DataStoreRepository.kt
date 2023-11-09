package com.ladsers.ztemp.data.repositories

import com.ladsers.ztemp.data.models.AuthData
import kotlinx.coroutines.flow.Flow

interface DataStoreRepository {
    suspend fun saveAuthData(authData: AuthData)
    fun getAuthData(): Flow<AuthData>
}