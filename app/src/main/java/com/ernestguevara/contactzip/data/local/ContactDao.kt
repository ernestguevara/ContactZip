package com.ernestguevara.contactzip.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(contactEntity: ContactEntity)

    @Delete
    suspend fun deleteContact(contactEntity: ContactEntity)

    @Query("SELECT * FROM contact_items")
    fun getAllContacts(): Flow<List<ContactEntity>>

    @Query("SELECT * FROM contact_items WHERE id = :mId")
    fun getContactById(mId: Int): ContactEntity?

    @Query("UPDATE contact_items SET number = :mNumber WHERE id = :mId")
    fun updateContactNumber(mId: Int, mNumber: String)

    /*
    Persistence
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUsers(userEntity: List<UserEntity>)

    @Query("SELECT * FROM user_item")
    suspend fun getUsers(): List<UserEntity>

    @Query("DELETE FROM user_item")
    suspend fun deleteUsers()
}