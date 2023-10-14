package com.jlndev.facaseupedido.ui.orderhistory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class OrderHistoryViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is order history Fragment"
    }
    val text: LiveData<String> = _text
}