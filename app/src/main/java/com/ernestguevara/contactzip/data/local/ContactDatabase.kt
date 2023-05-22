package com.ernestguevara.contactzip.data.local

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(
    entities = [ContactEntity::class, UserEntity::class],
    version = 1,
    exportSchema = false
)

abstract class ContactDatabase : RoomDatabase() {
    abstract val contactDao: ContactDao
}