package com.ernestguevara.contactzip.domain.usecase

import com.ernestguevara.contactzip.data.local.ContactEntity
import kotlinx.coroutines.flow.Flow

interface DbUseCaseGetContactList {
    fun execute(isLocal: Boolean): Flow<List<ContactEntity>>
}