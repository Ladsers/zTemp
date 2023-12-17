package com.ladsers.ztemp.domain.viewModels

import android.os.Bundle
import androidx.compose.runtime.*
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistryOwner
import com.ladsers.web.update.Channel
import com.ladsers.web.update.Platform
import com.ladsers.web.update.Updater
import com.ladsers.ztemp.BuildConfig
import com.ladsers.ztemp.data.enums.ConfirmationType
import com.ladsers.ztemp.data.enums.StatusError
import com.ladsers.ztemp.data.models.AppParams
import com.ladsers.ztemp.data.models.AuthData
import com.ladsers.ztemp.data.models.DeviceStatus
import com.ladsers.ztemp.data.models.TempSetter
import com.ladsers.ztemp.data.repositories.DataStoreRepository
import com.ladsers.ztemp.data.repositories.ZontRepository
import com.ladsers.ztemp.domain.states.DeviceStatusState
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import okio.IOException
import retrofit2.HttpException
import kotlin.math.round

class ZontViewModel(
    private val zontRepository: ZontRepository,
    private val dataStoreRepository: DataStoreRepository,
    private val showConfirmationActivity: (ConfirmationType, Double?) -> Unit
) : ViewModel(), DefaultLifecycleObserver {

    private lateinit var deviceStatus: DeviceStatus
    private var authData: AuthData? = null

    private var _updateChecked = false

    private val _updateAvailable: MutableState<Boolean> = mutableStateOf(value = false)
    val updateAvailable: State<Boolean> = _updateAvailable

    var deviceStatusState: DeviceStatusState by mutableStateOf(DeviceStatusState.InProgress)
        private set

    private val _username: MutableState<String> = mutableStateOf(value = "")
    val username: State<String> = _username
    fun updateUsername(newValue: String) {
        _username.value = newValue
    }

    private var _password = ""
    private val _passwordHidden: MutableState<String> = mutableStateOf(value = "")
    val passwordHidden: State<String> = _passwordHidden
    fun updatePassword(newValue: String) {
        _password = newValue
        _passwordHidden.value = if (newValue.isNotEmpty()) "********" else ""
    }

    private var _skipOnResume = false

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)

        if (_skipOnResume) {
            _skipOnResume = false
            return
        }

        authData = null
        getStatus()
    }

    fun getStatus(refreshing: Boolean = false) {
        viewModelScope.launch {

            if (!refreshing) {
                deviceStatusState = DeviceStatusState.InProgress
                authData = authData ?: dataStoreRepository.getAuthData().first()

                if (authData?.token.isNullOrEmpty()) {
                    deviceStatusState = DeviceStatusState.NotSignedIn
                    return@launch
                }

                if (authData!!.deviceId == 0) {
                    getDevices()
                    return@launch
                }
            }

            deviceStatusState = DeviceStatusState.GettingStatus

            try {
                deviceStatus = zontRepository.getDeviceStatus(
                    authData!!.token,
                    authData!!.deviceId
                )

                deviceStatusState =
                    if (deviceStatus.id != 0) DeviceStatusState.Success(deviceStatus)
                    else DeviceStatusState.Error(
                        error = StatusError.DEVICE_NOT_FOUND,
                        fixAction = ::resetDevice
                    )
            } catch (e: IOException) {
                deviceStatusState = DeviceStatusState.Error(
                    error = StatusError.NO_INTERNET_CONNECTION,
                    fixAction = ::getStatus
                )
            } catch (e: HttpException) {
                deviceStatusState = if (e.code() == 403) {
                    DeviceStatusState.Error(
                        error = StatusError.AUTH_ERROR,
                        fixAction = ::logOut
                    )
                } else {
                    DeviceStatusState.Error(
                        error = StatusError.SERVER_ERROR,
                        fixAction = ::getStatus
                    )
                }
            }

            if (!_updateChecked) {
                viewModelScope.launch(Dispatchers.IO) {
                    Updater.getNewVersionTag(
                        "ztemp",
                        Platform.WEAROS,
                        BuildConfig.VERSION_NAME,
                        Channel.STABLE
                    )?.run {
                        _updateAvailable.value = true
                    }
                }
                _updateChecked = true
            }
        }
    }

    fun signIn() {
        if (username.value.isEmpty() || _password.isEmpty()) return

        viewModelScope.launch {
            deviceStatusState = DeviceStatusState.InProgress
            try {
                val userInfo = zontRepository.getUserInfo(username.value, _password)

                userInfo.token?.let { t ->
                    authData?.let { d ->
                        d.token = t
                        dataStoreRepository.saveAuthData(d)
                    }
                }

                getStatus()

            } catch (e: IOException) {
                deviceStatusState = DeviceStatusState.Error(
                    error = StatusError.NO_INTERNET_CONNECTION,
                    fixAction = ::signIn
                )
            } catch (e: HttpException) {
                if (e.code() == 403) {
                    deviceStatusState = DeviceStatusState.Error(
                        error = StatusError.INVALID_AUTH_DATA,
                        fixAction = { deviceStatusState = DeviceStatusState.NotSignedIn }
                    )
                } else {
                    deviceStatusState = DeviceStatusState.Error(
                        error = StatusError.SERVER_ERROR,
                        fixAction = ::signIn
                    )
                }
            }
        }
    }

    fun getDevices() {
        viewModelScope.launch {
            deviceStatusState = DeviceStatusState.InProgress
            deviceStatusState = try {
                DeviceStatusState.NoDeviceSelected(devices = zontRepository.getDevices(authData!!.token))
            } catch (e: IOException) {
                DeviceStatusState.Error(
                    error = StatusError.NO_INTERNET_CONNECTION,
                    fixAction = ::getDevices
                )
            } catch (e: HttpException) {
                if (e.code() == 403) {
                    DeviceStatusState.Error(
                        error = StatusError.AUTH_ERROR,
                        fixAction = ::logOut
                    )
                } else {
                    DeviceStatusState.Error(
                        error = StatusError.SERVER_ERROR,
                        fixAction = ::getDevices
                    )
                }
            }
        }
    }

    fun selectDevice(deviceId: Int) {
        viewModelScope.launch {
            deviceStatusState = DeviceStatusState.InProgress
            authData?.let { d ->
                d.deviceId = deviceId
                dataStoreRepository.saveAuthData(d)
            }
            getStatus()
        }
    }

    private fun resetDevice() {
        viewModelScope.launch {
            deviceStatusState = DeviceStatusState.InProgress
            authData = dataStoreRepository.getAuthData().first()
            authData!!.deviceId = 0
            dataStoreRepository.saveAuthData(authData!!)
            getStatus()
        }
    }

    fun logOut() {
        viewModelScope.launch {
            deviceStatusState = DeviceStatusState.InProgress
            authData = AuthData(token = "", deviceId = 0)
            dataStoreRepository.saveAuthData(authData!!)
            getStatus()
        }
    }

    fun prepareTempSetter(): TempSetter {
        val appParams: AppParams

        runBlocking {
            appParams = dataStoreRepository.getAppParams().first()
        }

        fun prepareTemp(tempValue: Double): Double {
            val multiplier = 10000
            return if ((tempValue * multiplier).toInt() % (tempValue * multiplier).toInt() == 0) {
                tempValue
            } else {
                round(tempValue)
            }
        }

        return TempSetter(
            targetTemp = deviceStatus.targetTemp!!,
            tempStep = deviceStatus.tempStep,
            presetTemp1 = prepareTemp(appParams.presetTemp1),
            presetTemp2 = prepareTemp(appParams.presetTemp2),
            addFeatures = appParams.addFeatures
        )
    }

    fun setTemp(targetTemp: Double) {

        _skipOnResume = true
        deviceStatusState = DeviceStatusState.InProgress

        viewModelScope.launch {
            try {
                deviceStatus.targetThermostatId?.let { targetThermostatId ->
                    zontRepository.setTemp(
                        authData!!.token,
                        authData!!.deviceId,
                        targetThermostatId,
                        targetTemp
                    )
                    showConfirmationActivity(ConfirmationType.SUCCESS, targetTemp)
                } ?: showConfirmationActivity(ConfirmationType.FAILURE, null)
            } catch (e: IOException) {
                showConfirmationActivity(ConfirmationType.FAILURE, null)
            } catch (e: HttpException) {
                showConfirmationActivity(ConfirmationType.FAILURE, null)
            }
        }
    }

    companion object {
        fun provideFactory(
            zontRepository: ZontRepository,
            dataStoreRepository: DataStoreRepository,
            showConfirmationActivity: (ConfirmationType, Double?) -> Unit,
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
                    return ZontViewModel(
                        zontRepository,
                        dataStoreRepository,
                        showConfirmationActivity
                    ) as T
                }
            }
    }
}