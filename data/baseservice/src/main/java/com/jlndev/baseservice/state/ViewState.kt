package com.jlndev.baseservice.state

import java.io.Serializable

sealed class ViewState<out T> : Serializable {
    data class Loading(val isLoading: Boolean) : ViewState<Nothing>()
    data class Success<out T>(val data: T) : ViewState<T>()
    data class Error(val throwable: Throwable, val action: (() -> Unit)? = null) : ViewState<Nothing>()
}