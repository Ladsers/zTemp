package com.ladsers.ztemp.domain.viewModels

import android.os.Bundle
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Error
import androidx.compose.material.icons.rounded.VpnKeyOff
import androidx.compose.material.icons.rounded.WifiOff
import androidx.compose.runtime.*
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistryOwner
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
) : ViewModel() {

    private var authData: AuthData? = null

    var userInfoState: UserInfoState by mutableStateOf(UserInfoState.InProcessing)
        private set

    var deviceStatusState: DeviceStatusState by mutableStateOf(DeviceStatusState.InProcessing)
        private set

    private val _targetTempState: MutableState<Double> = mutableStateOf(value = 0.0)
    val targetTempState: State<Double> = _targetTempState

    //todo
    fun updateTargetTempState(newValue: Double) {
        _targetTempState.value = newValue
    }

    fun decreaseTargetTempState() {
        _targetTempState.value -= 0.5 //todo
    }

    fun increaseTargetTempState() {
        _targetTempState.value += 0.5 // todo
    }

    private val _username: MutableState<String> = mutableStateOf(value = "")
    val username: State<String> = _username
    fun updateUsername(newValue: String) {
        _username.value = newValue
    }

    private val _password: MutableState<String> = mutableStateOf(value = "")
    val password: State<String> = _password
    fun updatePassword(newValue: String) {
        _password.value = newValue
    }


    //TODO
    var login: String = ""
    var password1: String = ""
    var token: String = ""
    var deviceId: Int = 0

    //TODO
    init {
        //getDeviceStatus(token)
        getStatus()
    }

    fun getStatus() {
        viewModelScope.launch {
            deviceStatusState = DeviceStatusState.InProcessing
            authData = authData ?: dataStoreRepository.getAuthData().first()

            if (authData?.token.isNullOrEmpty()) {
                deviceStatusState = DeviceStatusState.NotSignedIn
                return@launch
            }

            if (authData!!.deviceId == 0) {
                getDevices()
                return@launch
            }

            deviceStatusState = DeviceStatusState.GettingStatus
        }
    }

    fun signIn() {
        if (username.value.isEmpty() || password.value.isEmpty()) return

        viewModelScope.launch {
            deviceStatusState = DeviceStatusState.InProcessing
            try {
                val userInfo = zontRepository.getUserInfo(username.value, password.value)

                userInfo.token?.let { t ->
                    authData?.let { d ->
                        d.token = t
                        dataStoreRepository.saveAuthData(d)
                    }
                }

                getStatus()

            } catch (e: IOException) {
                deviceStatusState = DeviceStatusState.Error(
                    icon = Icons.Rounded.WifiOff,
                    message = "Нет подключения к Интернету",
                    retryAction = { signIn() }
                )
            } catch (e: HttpException) {
                if (e.code() == 403) {
                    deviceStatusState = DeviceStatusState.Error(
                        icon = Icons.Rounded.VpnKeyOff,
                        message = "Неверный логин и/или пароль",
                        retryAction = { deviceStatusState = DeviceStatusState.NotSignedIn }
                    )
                } else {
                    deviceStatusState = DeviceStatusState.Error(
                        icon = Icons.Rounded.Error,
                        message = "Ошибка сервера",
                        retryAction = { signIn() }
                    )
                }
            }
        }
    }

    fun getDevices() {
        viewModelScope.launch {
            deviceStatusState = DeviceStatusState.InProcessing
            deviceStatusState = try {
                DeviceStatusState.NoDeviceSelected(
                    devices = zontRepository.getDevices(authData!!.token),
                    onDeviceSelected = ::selectDevice,
                    onLogOutClicked = { logOut() }
                )
            } catch (e: IOException) {
                DeviceStatusState.Error(
                    icon = Icons.Rounded.WifiOff,
                    message = "Нет подключения к Интернету",
                    retryAction = { getDevices() }
                )
            } catch (e: HttpException) {
                if (e.code() == 403) {
                    DeviceStatusState.SignInError
                } else {
                    DeviceStatusState.Error(
                        icon = Icons.Rounded.Error,
                        message = "Ошибка сервера",
                        retryAction = { getDevices() }
                    )
                }
            }
        }
    }

    fun selectDevice(deviceId: Int) {
        viewModelScope.launch {
            authData?.let { d ->
                d.deviceId = deviceId
                dataStoreRepository.saveAuthData(d)
            }
            getStatus()
        }
    }

    fun logOut() {
        viewModelScope.launch {
            authData?.let { d ->
                d.token = ""
                d.deviceId = 0
                dataStoreRepository.saveAuthData(d)
            }
            getStatus()
        }
    }

    /*fun getDeviceStatus(token: String) {
        viewModelScope.launch {
            deviceStatusState = DeviceStatusState.InProcessing
            deviceStatusState = try {
                DeviceStatusState.Success(zontRepository.getDevices(token))
            } catch (e: IOException) {
                DeviceStatusState.Error(-1) //todo
            } catch (e: HttpException) {
                DeviceStatusState.Error(e.code())
            }
        }
    }*/

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