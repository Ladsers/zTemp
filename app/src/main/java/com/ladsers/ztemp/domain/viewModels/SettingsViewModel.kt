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
import com.ladsers.ztemp.R
import com.ladsers.ztemp.data.enums.ConfirmationType
import com.ladsers.ztemp.data.models.AppParams
import com.ladsers.ztemp.data.models.AuthData
import com.ladsers.ztemp.data.repositories.DataStoreRepository
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import java.security.MessageDigest
import kotlin.math.round

class SettingsViewModel(
    private val dataStoreRepository: DataStoreRepository,
    private val tempStep: Double,
    private val showConfirmationActivity: (ConfirmationType, Int) -> Unit
) : ViewModel() {

    private lateinit var _appParams: AppParams

    private val _addFeatures: MutableState<Boolean> = mutableStateOf(value = false)
    val addFeatures: State<Boolean> = _addFeatures

    private val _presetTemp1: MutableState<String> = mutableStateOf(value = "")
    val presetTemp1: State<String> = _presetTemp1
    fun updatePresetTemp1(newValue: String) {
        val newTemp = prepareTemp(newValue) ?: run {
            showConfirmationActivity(ConfirmationType.FAILURE, R.string.setValueFailure)
            return
        }
        if (_appParams.presetTemp1 == newTemp) {
            showConfirmationActivity(ConfirmationType.SUCCESS, R.string.setValueSuccess)
            return
        }

        _presetTemp1.value = "${newTemp}°C"
        _appParams.presetTemp1 = newTemp
        viewModelScope.launch {
            dataStoreRepository.saveAppParams(_appParams)
            showConfirmationActivity(ConfirmationType.SUCCESS, R.string.setValueSuccess)
        }
    }

    private val _presetTemp2: MutableState<String> = mutableStateOf(value = "")
    val presetTemp2: State<String> = _presetTemp2
    fun updatePresetTemp2(newValue: String) {

        val newTemp = prepareTemp(newValue) ?: run {
            showConfirmationActivity(ConfirmationType.FAILURE, R.string.setValueFailure)
            return
        }
        if (_appParams.presetTemp2 == newTemp) {
            showConfirmationActivity(ConfirmationType.SUCCESS, R.string.setValueSuccess)
            return
        }

        _presetTemp2.value = "${newTemp}°C"
        _appParams.presetTemp2 = newTemp
        viewModelScope.launch {
            dataStoreRepository.saveAppParams(_appParams)
            showConfirmationActivity(ConfirmationType.SUCCESS, R.string.setValueSuccess)
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

    fun checkCode(code: String) {
        val md = MessageDigest.getInstance("SHA-256")
        val digest = md.digest(code.toByteArray())
        val hash =
            digest.fold(StringBuilder()) { sb, it -> sb.append("%02x".format(it)) }.toString()

        if (hash == "cf5dc9d09f9e67c4e01a3dd3085dbae01fef8b77bd80418bb08a521f32749f87") {
            _appParams.addFeatures = true
            viewModelScope.launch {
                dataStoreRepository.saveAppParams(_appParams)
                _addFeatures.value = true
                showConfirmationActivity(ConfirmationType.SUCCESS, R.string.codeAccepted)
            }
        } else showConfirmationActivity(ConfirmationType.FAILURE, R.string.invalidCode)
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
            return 5.0 // min temp in ZONT
        }

        if (tempValue >= 35.0) {
            return 35.0 // max temp in ZONT
        }

        val multiplier = 10000
        return if ((tempValue * multiplier).toInt() % (tempStep * multiplier).toInt() == 0) {
            tempValue
        } else {
            round(tempValue) // Not a multiple of the temp step
        }
    }

    companion object {
        fun provideFactory(
            dataStoreRepository: DataStoreRepository,
            tempStep: Double,
            showConfirmationActivity: (ConfirmationType, Int) -> Unit,
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
                    return SettingsViewModel(
                        dataStoreRepository,
                        tempStep,
                        showConfirmationActivity
                    ) as T
                }
            }
    }
}