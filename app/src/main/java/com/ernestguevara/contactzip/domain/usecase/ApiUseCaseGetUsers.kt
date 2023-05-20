package com.ernestguevara.contactzip.domain.usecase

import com.ernestguevara.contactzip.domain.model.UserModel
import com.ernestguevara.contactzip.util.Resource
import kotlinx.coroutines.flow.Flow

interface ApiUseCaseGetUsers {
    fun execute(page: Int): Flow<Resource<List<UserModel>>>
}