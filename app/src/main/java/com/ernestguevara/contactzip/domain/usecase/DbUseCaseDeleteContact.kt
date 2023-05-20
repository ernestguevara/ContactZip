package com.ernestguevara.contactzip.domain.usecase

import com.ernestguevara.contactzip.data.local.ContactEntity

interface DbUseCaseDeleteContact {
    suspend fun execute(contactEntity: ContactEntity)
}