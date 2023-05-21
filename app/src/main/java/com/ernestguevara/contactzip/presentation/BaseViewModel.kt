package com.ernestguevara.contactzip.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.ernestguevara.contactzip.data.local.ContactEntity
import com.ernestguevara.contactzip.util.RequestState

abstract class BaseViewModel : ViewModel() {

    protected val _state: MutableLiveData<RequestState> = MutableLiveData(null)
    val state: MutableLiveData<RequestState> = _state

    protected val _getContactListValue = MutableLiveData<List<ContactEntity>>()
    val getContactListValue: MutableLiveData<List<ContactEntity>> = _getContactListValue

}