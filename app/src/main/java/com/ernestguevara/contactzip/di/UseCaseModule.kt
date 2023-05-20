package com.ernestguevara.contactzip.di

import com.ernestguevara.contactzip.data.usecase.ApiUseCaseGetUsersImpl
import com.ernestguevara.contactzip.data.usecase.DbUseCaseDeleteContactImpl
import com.ernestguevara.contactzip.data.usecase.DbUseCaseGetContactListImpl
import com.ernestguevara.contactzip.data.usecase.DbUseCaseInsertContactImpl
import com.ernestguevara.contactzip.domain.usecase.*
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class UseCaseModule {

    @Binds
    @Singleton
    abstract fun bindApiUseCaseGetUsers(apiUseCaseGetUsersImpl: ApiUseCaseGetUsersImpl): ApiUseCaseGetUsers

    @Binds
    @Singleton
    abstract fun bindDbUseCaseInsertContact(dbUseCaseInsertContactImpl: DbUseCaseInsertContactImpl): DbUseCaseInsertContact

    @Binds
    @Singleton
    abstract fun bindDbUseCaseDeleteContact(dbUseCaseDeleteContactImpl: DbUseCaseDeleteContactImpl): DbUseCaseDeleteContact

    @Binds
    @Singleton
    abstract fun bindDbUseCaseGetContactList(dbUseCaseGetContactListImpl: DbUseCaseGetContactListImpl): DbUseCaseGetContactList
}