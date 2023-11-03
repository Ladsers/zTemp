package com.ladsers.ztemp.domain.viewModels

import android.os.Bundle
import androidx.compose.runtime.*
import androidx.lifecycle.*
import androidx.savedstate.SavedStateRegistryOwner
import com.ladsers.ztemp.data.repositories.ZontRepository
import com.ladsers.ztemp.domain.states.UserInfoState
import kotlinx.coroutines.launch
import okio.IOException
import retrofit2.HttpException

class ZontViewModel(
    private val zontRepository: ZontRepository
) : ViewModel() {

    var userInfoState: UserInfoState by mutableStateOf(UserInfoState.InProcessing)
        private set

    //TODO
    var login: String = ""
    var password: String = ""
    var token: String = ""

    //TODO
    init {
        getUserInfo(login, password)
    }

    fun getUserInfo(login: String, password: String) {
        viewModelScope.launch {
            userInfoState = UserInfoState.InProcessing
            userInfoState = try {
                UserInfoState.Success(zontRepository.getUserInfo(login, password))
            } catch (e: IOException) {
                UserInfoState.Error(-1) //todo
            } catch (e: HttpException) {
                UserInfoState.Error(e.code())
            }
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