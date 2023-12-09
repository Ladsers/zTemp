package com.ladsers.ztemp.domain.viewModels

import android.os.Bundle
import androidx.compose.runtime.*
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistryOwner
import com.ladsers.ztemp.data.enums.StatusError
import com.ladsers.ztemp.data.models.AuthData
import com.ladsers.ztemp.data.repositories.DataStoreRepository
import com.ladsers.ztemp.data.repositories.ZontRepository
import com.ladsers.ztemp.domain.states.DeviceStatusState
import com.ladsers.ztemp.domain.states.UserInfoState
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException

class ZontViewModel(
    private val zontRepository: ZontRepository,
    private val dataStoreRepository: DataStoreRepository
) : ViewModel(), DefaultLifecycleObserver {

    private var authData: AuthData? = null

    var userInfoState: UserInfoState by mutableStateOf(UserInfoState.InProcessing)
        private set

    var deviceStatusState: DeviceStatusState by mutableStateOf(DeviceStatusState.InProgress)
        private set

    private val _targetTempState: MutableState<Double> = mutableStateOf(value = 0.0)
    val targetTempState: State<Double> = _targetTempState

    //todo
    fun updateTargetTempState(newValue: Double) {
        _targetTempState.value = newValue
    }

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

    override fun onResume(owner: LifecycleOwner) {
        super.onResume(owner)

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
                val deviceStatus = zontRepository.getDeviceStatus(
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
                if (e.code() == 403) {
                    deviceStatusState = DeviceStatusState.Error(
                        error = StatusError.AUTH_ERROR,
                        fixAction = ::logOut
                    )
                } else {
                    deviceStatusState = DeviceStatusState.Error(
                        error = StatusError.SERVER_ERROR,
                        fixAction = ::getStatus
                    )
                }
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
                DeviceStatusState.NoDeviceSelected(
                    devices = zontRepository.getDevices(authData!!.token),
                    onDeviceSelected = ::selectDevice,
                    onLogOutClicked = ::logOut
                )
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

    private fun selectDevice(deviceId: Int) {
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

    private fun logOut() {
        viewModelScope.launch {
            deviceStatusState = DeviceStatusState.InProgress
            authData = AuthData(token = "", deviceId = 0)
            dataStoreRepository.saveAuthData(authData!!)
            getStatus()
        }
    }

    fun setTemp(token: String, deviceId: Int, targetTemp: Double) {
        viewModelScope.launch {
            zontRepository.setTemp(token, deviceId, targetTemp)
        }
    }

    companion object {
        fun provideFactory(
            zontRepository: ZontRepository,
            dataStoreRepository: DataStoreRepository,
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
                    return ZontViewModel(zontRepository, dataStoreRepository) as T
                }
            }
    }
}