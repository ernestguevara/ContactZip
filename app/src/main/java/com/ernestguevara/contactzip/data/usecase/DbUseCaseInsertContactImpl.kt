package com.ernestguevara.contactzip.data.usecase

import com.ernestguevara.contactzip.data.local.ContactEntity
import com.ernestguevara.contactzip.domain.repository.ContactRepository
import com.ernestguevara.contactzip.domain.usecase.DbUseCaseInsertContact
import javax.inject.Inject

class DbUseCaseInsertContactImpl @Inject constructor(private val contactRepository: ContactRepository) :
    DbUseCaseInsertContact {
    override suspend fun execute(contactEntity: ContactEntity) {
        contactRepository.insertContactsToDb(contactEntity)
    }
}