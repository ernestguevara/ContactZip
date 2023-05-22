package com.ernestguevara.contactzip.domain.usecase

import com.ernestguevara.contactzip.data.local.ContactEntity

interface DbUseCaseInsertContact {
    suspend fun execute(contactEntity: ContactEntity)
}