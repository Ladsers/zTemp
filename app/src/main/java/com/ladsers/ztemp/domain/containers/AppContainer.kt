package com.ladsers.ztemp.domain.containers

import com.ladsers.ztemp.data.repositories.DataStoreRepository
import com.ladsers.ztemp.data.repositories.ZontRepository

interface AppContainer {
    val zontRepository: ZontRepository
    val dataStoreRepository: DataStoreRepository
}