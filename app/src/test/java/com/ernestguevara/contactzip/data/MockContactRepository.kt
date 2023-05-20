package com.ernestguevara.contactzip.data

import com.ernestguevara.contactzip.data.local.ContactEntity
import com.ernestguevara.contactzip.domain.model.UserModel
import com.ernestguevara.contactzip.domain.repository.ContactRepository
import com.ernestguevara.contactzip.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class MockContactRepository : ContactRepository {
    var savedContacts = mutableListOf<ContactEntity>()

    override fun getUsers(page: Int): Flow<Resource<List<UserModel>>> {
        return flow { emit(Resource.Success(mutableListOf(UserModel(id = 1), UserModel(id = 2)))) }
    }

    override suspend fun insertContactsToDb(contactEntity: ContactEntity) {
        savedContacts.add(contactEntity)
    }

    override suspend fun deleteContactFromDb(contactEntity: ContactEntity) {
        savedContacts.remove(contactEntity)
    }

    override fun getContactList(isLocal: Boolean): Flow<List<ContactEntity>> {
        return flow { emit(savedContacts) }
    }
}