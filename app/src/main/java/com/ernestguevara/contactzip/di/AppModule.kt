package com.ernestguevara.contactzip.di

import android.content.Context
import androidx.room.Room
import com.ernestguevara.contactzip.data.local.ContactDatabase
import com.ernestguevara.contactzip.util.Constants.DB_NAME
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object AppModule {

    @Singleton
    @Provides
    fun provideContactDatabase(
        @ApplicationContext context: Context
    ) = Room.databaseBuilder(context, ContactDatabase::class.java, DB_NAME).build()
}