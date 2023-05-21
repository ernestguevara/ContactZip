package com.ernestguevara.contactzip.data.repository

import com.ernestguevara.contactzip.data.local.ContactDatabase
import com.ernestguevara.contactzip.data.local.ContactEntity
import com.ernestguevara.contactzip.data.remote.UserApiService
import com.ernestguevara.contactzip.domain.model.UserModel
import com.ernestguevara.contactzip.domain.repository.ContactRepository
import com.ernestguevara.contactzip.util.Constants.ERROR_GENERIC
import com.ernestguevara.contactzip.util.Constants.ERROR_HTTP
import com.ernestguevara.contactzip.util.Constants.ERROR_IO
import com.ernestguevara.contactzip.util.Constants.ERROR_PAGINATION
import com.ernestguevara.contactzip.util.Constants.PAGE_SIZE
import com.ernestguevara.contactzip.util.Resource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.HttpException
import java.io.IOException
import java.lang.Exception
import javax.inject.Inject

class ContactRepositoryImpl @Inject constructor(
    private val apiService: UserApiService,
    private val contactDatabase: ContactDatabase
) : ContactRepository {

    private val dao = contactDatabase.contactDao

    /*
    API Call Section
     */
    override fun getUsers(page: Int): Flow<Resource<List<UserModel>>> = flow {
        emit(Resource.Loading())

        try {
            val result = apiService.getUsers(page, PAGE_SIZE)
            if (result.totalPages >= result.page) {
                emit(Resource.Success(result.userDto.map { it.dtoToModel() }))
            } else {
                emit(Resource.Error(message = ERROR_PAGINATION))
            }
        } catch (e: HttpException) {
            emit(Resource.Error(message = ERROR_HTTP))
        } catch (e: IOException) {
            emit(Resource.Error(message = ERROR_IO))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.Error(message = ERROR_GENERIC))
        }
    }

    /*
    Database Call Section
    */

    override suspend fun insertContactsToDb(contactEntity: ContactEntity) {
        dao.insertContact(contactEntity)
    }

    override suspend fun deleteContactFromDb(contactEntity: ContactEntity) {
        dao.deleteContact(contactEntity)
    }

    override fun getContactList(isLocal: Boolean): Flow<List<ContactEntity>> {
        return dao.getAllContacts(isLocal)
    }
}