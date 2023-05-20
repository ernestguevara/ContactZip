package com.ernestguevara.contactzip.data.local

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface ContactDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertContact(contactEntity: ContactEntity)

    @Delete
    suspend fun deleteContact(contactEntity: ContactEntity)

    @Query("SELECT * FROM contact_items WHERE isLocallyStored = :isLocallyStored")
    fun getAllContacts(isLocallyStored: Boolean? = true): Flow<List<ContactEntity>>
}