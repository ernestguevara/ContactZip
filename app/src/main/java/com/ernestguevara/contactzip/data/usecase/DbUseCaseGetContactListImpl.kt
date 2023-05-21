package com.ernestguevara.contactzip.data.usecase

import com.ernestguevara.contactzip.data.local.ContactEntity
import com.ernestguevara.contactzip.domain.repository.ContactRepository
import com.ernestguevara.contactzip.domain.usecase.DbUseCaseGetContactList
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DbUseCaseGetContactListImpl @Inject constructor(private val contactRepository: ContactRepository) :
    DbUseCaseGetContactList {
    override fun execute(): Flow<List<ContactEntity>> {
        return contactRepository.getContactList()
    }
}