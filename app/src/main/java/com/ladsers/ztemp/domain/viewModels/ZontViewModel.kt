package com.ladsers.ztemp.domain.viewModels

import android.os.Bundle
import androidx.compose.runtime.*
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistryOwner
import com.ladsers.ztemp.data.repositories.ZontRepository
import com.ladsers.ztemp.domain.states.DeviceStatusState
import com.ladsers.ztemp.domain.states.UserInfoState
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException

class ZontViewModel(
    private val zontRepository: ZontRepository
) : ViewModel() {

    var userInfoState: UserInfoState by mutableStateOf(UserInfoState.InProcessing)
        private set

    var deviceStatusState: DeviceStatusState by mutableStateOf(DeviceStatusState.InProcessing)
        private set

    private val _targetTempState: MutableState<Double> = mutableStateOf(value = 0.0)
    val targetTempState : State<Double> = _targetTempState

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


    //TODO
    var login: String = ""
    var password: String = ""
    var token: String = ""
    var deviceId: Int = 0

    //TODO
    init {
        getDeviceStatus(token)
    }

    fun getUserInfo(login: String, password: String) {
        viewModelScope.launch {
            userInfoState = UserInfoState.InProcessing
            userInfoState = try {
                //todo
                UserInfoState.Success(zontRepository.getUserInfo(login, password))
            } catch (e: IOException) {
                UserInfoState.Error(-1) //todo
            } catch (e: HttpException) {
                UserInfoState.Error(e.code())
            }
        }
    }

    fun getDeviceStatus(token: String) {
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
    }

    fun setTemp(token: String, deviceId: Int, targetTemp: Double) {
        viewModelScope.launch {
            zontRepository.setTemp(token, deviceId, targetTemp)
        }
    }

    companion object {
        fun provideFactory(
            myRepository: ZontRepository,
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
                    return ZontViewModel(myRepository) as T
                }
            }
    }
}