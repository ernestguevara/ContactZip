package com.ernestguevara.contactzip.data.usecase

import com.ernestguevara.contactzip.domain.model.UserModel
import com.ernestguevara.contactzip.domain.repository.ContactRepository
import com.ernestguevara.contactzip.domain.usecase.ApiUseCaseGetUsers
import com.ernestguevara.contactzip.util.Resource
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class ApiUseCaseGetUsersImpl @Inject constructor(private val contactRepository: ContactRepository) :
    ApiUseCaseGetUsers {
    override fun execute(page: Int): Flow<Resource<List<UserModel>>> {
        return contactRepository.getUsers(page)
    }
}