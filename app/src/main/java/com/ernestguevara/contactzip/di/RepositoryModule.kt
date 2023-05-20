package com.ernestguevara.contactzip.di

import com.ernestguevara.contactzip.data.repository.ContactRepositoryImpl
import com.ernestguevara.contactzip.domain.repository.ContactRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindUserRepository(
        contactRepositoryImpl: ContactRepositoryImpl
    ): ContactRepository
}