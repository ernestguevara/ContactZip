package com.ernestguevara.contactzip.data.usecase

import com.ernestguevara.contactzip.data.local.ContactEntity
import com.ernestguevara.contactzip.domain.repository.ContactRepository
import com.ernestguevara.contactzip.domain.usecase.DbUseCaseDeleteContact
import javax.inject.Inject

class DbUseCaseDeleteContactImpl @Inject constructor(private val contactRepository: ContactRepository) :
    DbUseCaseDeleteContact {
    override suspend fun execute(contactEntity: ContactEntity) {
        contactRepository.deleteContactFromDb(contactEntity)
    }
}