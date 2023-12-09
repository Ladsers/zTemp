package com.ladsers.ztemp.domain.viewModels

import android.os.Bundle
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.AbstractSavedStateViewModelFactory
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.savedstate.SavedStateRegistryOwner
import com.ladsers.ztemp.data.models.AppParams
import com.ladsers.ztemp.data.models.AuthData
import com.ladsers.ztemp.data.repositories.DataStoreRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import kotlin.math.round

class SettingsViewModel(
    private val dataStoreRepository: DataStoreRepository,
    private val tempStep: Double
) : ViewModel() {

    private lateinit var _appParams: AppParams

    private val _addFeatures: MutableState<Boolean> = mutableStateOf(value = false)
    val addFeatures: State<Boolean> = _addFeatures

    private val _presetTemp1: MutableState<String> = mutableStateOf(value = "")
    val presetTemp1: State<String> = _presetTemp1
    fun updatePresetTemp1(newValue: String) {
        val newTemp = prepareTemp(newValue) ?: return
        if (_appParams.presetTemp1 == newTemp) return

        _presetTemp1.value = "${newTemp}°C"
        _appParams.presetTemp1 = newTemp
        viewModelScope.launch {
            dataStoreRepository.saveAppParams(_appParams)
        }
    }

    private val _presetTemp2: MutableState<String> = mutableStateOf(value = "")
    val presetTemp2: State<String> = _presetTemp2
    fun updatePresetTemp2(newValue: String) {

        val newTemp = prepareTemp(newValue) ?: return // todo error?
        if (_appParams.presetTemp2 == newTemp) return

        _presetTemp2.value = "${newTemp}°C"
        _appParams.presetTemp2 = newTemp
        viewModelScope.launch {
            dataStoreRepository.saveAppParams(_appParams)
        }
    }

    init {
        loadAppParams()
    }

    fun resetDevice() {
        runBlocking {
            val authData = dataStoreRepository.getAuthData().first()
            authData.deviceId = 0
            dataStoreRepository.saveAuthData(authData)
        }
    }

    fun logOut() {
        runBlocking {
            val authData = AuthData(token = "", deviceId = 0)
            dataStoreRepository.saveAuthData(authData)
        }
    }

    private fun loadAppParams() {
        viewModelScope.launch {
            _appParams = dataStoreRepository.getAppParams().first()
            _addFeatures.value = _appParams.addFeatures
            _presetTemp1.value = "${_appParams.presetTemp1}°C"
            _presetTemp2.value = "${_appParams.presetTemp2}°C"
        }
    }

    private fun prepareTemp(newValue: String): Double? {
        val tempStr = if (newValue.endsWith("°C")) newValue.dropLast(2) else newValue
        val tempValue = tempStr.replace(',', '.').toDoubleOrNull() ?: return null

        if (tempValue <= 5.0) {
            return 5.0
        }

        if (tempValue >= 35.0) {
            return 35.0
        }

        val multiplier = 10000
        return if ((tempValue * multiplier).toInt() % (tempStep * multiplier).toInt() == 0) {
            tempValue
        } else {
            round(tempValue)
        }
    }

    companion object {
        fun provideFactory(
            dataStoreRepository: DataStoreRepository,
            tempStep: Double,
            owner: SavedStateRegistryOwner,
            defaultArgs: Bundle? = null
        ): AbstractSavedStateViewModelFactory =
            object : AbstractSavedStateViewModelFactory(owner, defaultArgs) {
                @Suppress("UNCHECKED_CAST")
                override fun <T : ViewModel> create(
                    key: String,
                    modelClass: Class<T>,
                    handle: SavedStateHandle
                ): T {
                    return SettingsViewModel(dataStoreRepository, tempStep) as T
                }
            }
    }
}