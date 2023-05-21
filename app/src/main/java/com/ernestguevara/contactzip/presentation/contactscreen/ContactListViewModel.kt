package com.ernestguevara.contactzip.presentation.contactscreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ernestguevara.contactzip.data.local.ContactEntity
import com.ernestguevara.contactzip.domain.usecase.DbUseCaseDeleteContact
import com.ernestguevara.contactzip.domain.usecase.DbUseCaseGetContactList
import com.ernestguevara.contactzip.domain.usecase.DbUseCaseInsertContact
import com.ernestguevara.contactzip.presentation.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ContactListViewModel @Inject constructor(
    private val dbUseCaseInsertContact: DbUseCaseInsertContact,
    private val dbUseCaseDeleteContact: DbUseCaseDeleteContact,
    private val dbUseCaseGetContactList: DbUseCaseGetContactList
) : BaseViewModel() {


    private val _getContactListValue = MutableLiveData<List<ContactEntity>>(emptyList())
    val getContactListValue: MutableLiveData<List<ContactEntity>> = _getContactListValue


    fun insertContact(contactEntity: ContactEntity) = viewModelScope.launch {
        dbUseCaseInsertContact.execute(contactEntity)
    }

    fun deleteContact(contactEntity: ContactEntity) = viewModelScope.launch {
        dbUseCaseDeleteContact.execute(contactEntity)
    }

    fun getContactList() = viewModelScope.launch {
        dbUseCaseGetContactList.execute()
            .collect { result ->
                _getContactListValue.postValue(result)
            }
    }
}