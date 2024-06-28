package com.jlndev.facaseupedido.ui.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.jlndev.baseservice.ext.BaseSchedulerProvider
import com.jlndev.baseservice.ext.disposedBy
import com.jlndev.baseservice.ext.processSingle
import com.jlndev.baseservice.state.ViewState
import com.jlndev.coreandroid.bases.viewModel.BaseViewModel
import com.jlndev.firebaseservice.data.user.UserRepository
import com.jlndev.firebaseservice.model.User

class ProfileViewModel(
    private val schedulerProvider: BaseSchedulerProvider,
    private val userRepository: UserRepository,
) : BaseViewModel() {

    private val _changePasswordLive = MutableLiveData<ViewState<Unit>>()
    val changePasswordLive : LiveData<ViewState<Unit>>
        get() = _changePasswordLive

    private val _userLive = MutableLiveData<ViewState<User?>>()
    val userLive : LiveData<ViewState<User?>>
        get() = _userLive

    fun getUser() {
        userRepository.getUser()
            .processSingle(schedulerProvider)
            .doOnSubscribe { _userLive.value = ViewState.Loading(true)}
            .doOnSuccess {
                _userLive.value = ViewState.Loading(false)
                _userLive.value = ViewState.Success(it)
            }
            .doOnError {
                _userLive.value = ViewState.Loading(false)
                _userLive.value = ViewState.Error(it)
            }
            .disposedBy(disposables)
    }

    fun changeUsername(newUsername: String) {
        userRepository.changeUsername(newUsername)
            .processSingle(schedulerProvider)
            .doOnSubscribe { _userLive.value = ViewState.Loading(true)}
            .doOnSuccess {
                _userLive.value = ViewState.Loading(false)
                _userLive.value = ViewState.Success(it)
            }
            .doOnError {
                _userLive.value = ViewState.Loading(false)
                _userLive.value = ViewState.Error(it)
            }
            .disposedBy(disposables)
    }

    fun changePassword(newPassword: String, lastPassword: String) {
        userRepository.changePassword(newPassword, lastPassword)
            .processSingle(schedulerProvider)
            .doOnSubscribe { _userLive.value = ViewState.Loading(true)}
            .doOnSuccess {
                _changePasswordLive.value = ViewState.Loading(false)
                _changePasswordLive.value = ViewState.Success(Unit)
            }
            .doOnError {
                _changePasswordLive.value = ViewState.Loading(false)
                _changePasswordLive.value = ViewState.Error(it)
            }
            .disposedBy(disposables)
    }

    fun logout() {
        userRepository.logout()
            .processSingle(schedulerProvider)
            .doOnSubscribe { _userLive.value = ViewState.Loading(true)}
            .doOnSuccess {
                _userLive.value = ViewState.Loading(false)
                _userLive.value = ViewState.Success(null)
            }
            .doOnError {
                _userLive.value = ViewState.Loading(false)
                _userLive.value = ViewState.Error(it)
            }
            .disposedBy(disposables)
    }
}