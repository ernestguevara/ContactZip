package com.ernestguevara.contactzip.domain.repository

import com.ernestguevara.contactzip.data.local.ContactEntity
import com.ernestguevara.contactzip.domain.model.UserModel
import com.ernestguevara.contactzip.util.Resource
import kotlinx.coroutines.flow.Flow

interface ContactRepository {
    /*
    API Call Section
     */
    fun getUsers(page: Int): Flow<Resource<List<UserModel>>>

    /*
    Database Call Section
     */
    suspend fun insertContactsToDb(contactEntity: ContactEntity)
    suspend fun deleteContactFromDb(contactEntity: ContactEntity)
    fun getContactList(isLocal: Boolean): Flow<List<ContactEntity>>
}