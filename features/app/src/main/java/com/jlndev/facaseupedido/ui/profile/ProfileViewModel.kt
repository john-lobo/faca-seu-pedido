package com.jlndev.facaseupedido.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jlndev.baseservice.ext.BaseSchedulerProvider
import com.jlndev.baseservice.ext.disposedBy
import com.jlndev.baseservice.ext.processSingle
import com.jlndev.baseservice.state.ResponseState
import com.jlndev.coreandroid.bases.viewModel.BaseViewModel
import com.jlndev.firebaseservice.data.user.UserRepository
import com.jlndev.firebaseservice.model.User

class ProfileViewModel(
    private val schedulerProvider: BaseSchedulerProvider,
    private val userRepository: UserRepository,
) : BaseViewModel() {

    private val _changePasswordLive = MutableLiveData<ResponseState<Unit>>()
    val changePasswordLive : LiveData<ResponseState<Unit>>
        get() = _changePasswordLive

    private val _userLive = MutableLiveData<ResponseState<User?>>()
    val userLive : LiveData<ResponseState<User?>>
        get() = _userLive

    fun getUser() {
        userRepository.getUser()
            .processSingle(schedulerProvider)
            .doOnSubscribe { _userLive.value = ResponseState.Loading(true)}
            .doOnSuccess {
                _userLive.value = ResponseState.Loading(false)
                _userLive.value = ResponseState.Success(it)
            }
            .doOnError {
                _userLive.value = ResponseState.Loading(false)
                _userLive.value = ResponseState.Error(it)
            }
            .disposedBy(disposables)
    }

    fun changeUsername(newUsername: String) {
        userRepository.changeUsername(newUsername)
            .processSingle(schedulerProvider)
            .doOnSubscribe { _userLive.value = ResponseState.Loading(true)}
            .doOnSuccess {
                _userLive.value = ResponseState.Loading(false)
                _userLive.value = ResponseState.Success(it)
            }
            .doOnError {
                _userLive.value = ResponseState.Loading(false)
                _userLive.value = ResponseState.Error(it)
            }
            .disposedBy(disposables)
    }

    fun changePassword(newPassword: String, lastPassword: String) {
        userRepository.changePassword(newPassword, lastPassword)
            .processSingle(schedulerProvider)
            .doOnSubscribe { _userLive.value = ResponseState.Loading(true)}
            .doOnSuccess {
                _changePasswordLive.value = ResponseState.Loading(false)
                _changePasswordLive.value = ResponseState.Success(Unit)
            }
            .doOnError {
                _changePasswordLive.value = ResponseState.Loading(false)
                _changePasswordLive.value = ResponseState.Error(it)
            }
            .disposedBy(disposables)
    }

    fun logout() {
        userRepository.logout()
            .processSingle(schedulerProvider)
            .doOnSubscribe { _userLive.value = ResponseState.Loading(true)}
            .doOnSuccess {
                _userLive.value = ResponseState.Loading(false)
                _userLive.value = ResponseState.Success(null)
            }
            .doOnError {
                _userLive.value = ResponseState.Loading(false)
                _userLive.value = ResponseState.Error(it)
            }
            .disposedBy(disposables)
    }
}