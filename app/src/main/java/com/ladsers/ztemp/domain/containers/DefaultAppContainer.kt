package com.ladsers.ztemp.domain.containers

import android.content.Context
import com.ladsers.ztemp.data.apiservices.ZontService
import com.ladsers.ztemp.data.repositories.DataStoreRepository
import com.ladsers.ztemp.data.repositories.AppDataStoreRepository
import com.ladsers.ztemp.data.repositories.NetworkZontRepository
import com.ladsers.ztemp.data.repositories.ZontRepository
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class DefaultAppContainer(private val context: Context) : AppContainer {
    private val baseUrl = "https://zont-online.ru/api/"

    private val retrofit: Retrofit = Retrofit.Builder()
        .addConverterFactory(GsonConverterFactory.create())
        .baseUrl(baseUrl)
        .build()

    private val retrofitService: ZontService by lazy {
        retrofit.create(ZontService::class.java)
    }

    override val zontRepository: ZontRepository by lazy {
        NetworkZontRepository(retrofitService)
    }

    override val dataStoreRepository: DataStoreRepository by lazy {
        AppDataStoreRepository(context)
    }
}