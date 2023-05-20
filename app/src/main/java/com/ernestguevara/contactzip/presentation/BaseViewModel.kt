package com.ernestguevara.contactzip.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ernestguevara.contactzip.util.RequestState

abstract class BaseViewModel : ViewModel() {

    protected val _state: MutableLiveData<RequestState> = MutableLiveData(null)
    val state: MutableLiveData<RequestState> = _state

}