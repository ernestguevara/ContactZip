package com.ernestguevara.contactzip.data.repository

import com.ernestguevara.contactzip.data.local.ContactDatabase
import com.ernestguevara.contactzip.data.local.ContactEntity
import com.ernestguevara.contactzip.data.remote.UserApiService
import com.ernestguevara.contactzip.domain.model.UserModel
import com.ernestguevara.contactzip.domain.repository.ContactRepository
import com.ernestguevara.contactzip.util.Constants.ERROR_EMPTY
import com.ernestguevara.contactzip.util.Constants.ERROR_GENERIC
import com.ernestguevara.contactzip.util.Constants.ERROR_HTTP
import com.ernestguevara.contactzip.util.Constants.ERROR_IO
import com.ernestguevara.contactzip.util.Constants.ERROR_PAGINATION
import com.ernestguevara.contactzip.util.Constants.PAGE_SIZE
import com.ernestguevara.contactzip.util.Resource
import com.google.gson.Gson
import kotlinx.coroutines.flow.*
import retrofit2.HttpException
import timber.log.Timber
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

        val userEntities = dao.getUsers()
        val userModels = if (userEntities.isNotEmpty()) {
            userEntities.map {
                it.toModel()
            }
        } else {
            null
        }

        emit(Resource.Loading(userModels))

        try {
            val result = apiService.getUsers(page, PAGE_SIZE)
            if (result.totalPages >= result.page) {
                if (!result.userDto.isNullOrEmpty()) {
                    //Emit to ViewModel
                    emit(Resource.Success(result.userDto.map { it.dtoToModel() }))

                    //Delete and Insert Users for persistence
                    dao.insertUsers(result.userDto.map {
                        it.dtoToModel().toUserEntity()
                    })
                } else {
                    emit(Resource.Error(message = ERROR_EMPTY, data = userModels))
                }
            } else {
                emit(Resource.Error(message = ERROR_PAGINATION))
            }
        } catch (e: HttpException) {
            emit(Resource.Error(message = ERROR_HTTP, data = userModels))
        } catch (e: IOException) {
            emit(Resource.Error(message = ERROR_IO, data = userModels))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Resource.Error(message = ERROR_GENERIC, data = userModels))
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

    override fun getContactList(): Flow<List<ContactEntity>> {
        return dao.getAllContacts()
    }
}