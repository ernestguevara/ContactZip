package com.ernestguevara.contactzip.domain.repository

interface ContactRepository {
    /*
    API Call Section
     */

    /*
    Dataase Call Section
     */
    suspend fun insertContactsToDb()
}