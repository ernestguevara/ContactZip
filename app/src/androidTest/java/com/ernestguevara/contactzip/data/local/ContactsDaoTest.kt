package com.ernestguevara.contactzip.data.local

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.test.filters.SmallTest
import com.google.common.truth.Truth.assertThat
import dagger.hilt.android.testing.HiltAndroidRule
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.test.runBlockingTest
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import javax.inject.Inject
import javax.inject.Named

@ExperimentalCoroutinesApi
@SmallTest
@HiltAndroidTest
class ContactsDaoTest {
    @get:Rule
    var hiltRule = HiltAndroidRule(this)

    @get:Rule
    var instantTaskExecutorRule = InstantTaskExecutorRule()

    @Inject
    @Named("test_db")
    lateinit var mockDb: ContactDatabase

    private lateinit var dao: ContactDao

    //Setup all dependencies
    @Before
    fun setup() {
        hiltRule.inject()
        dao = mockDb.contactDao
    }

    //Close db
    @After
    fun teardown() {
        mockDb.close()
    }

    @Test
    fun testInsertContact() = runBlockingTest {
        //Prepare data
        val contact = provideContact(1)[0]

        //Insert to mock database
        dao.insertContact(contact)

        //Get database data
        val result = collectContactFlow(this, dao.getAllContacts())

        //Assert that expected value
        assertThat(result).contains(contact)
    }

    @Test
    fun testDeleteContact() = runBlockingTest {
        //Prepare data
        val contact = provideContact(1)[0]
        dao.insertContact(contact)

        //Delete from mock database
        dao.deleteContact(contact)

        //Get database data
        val result = collectContactFlow(this, dao.getAllContacts())

        //Assert that expected value
        assertThat(result).doesNotContain(contact)
    }

    @Test
    fun testGetLocallyStored() = runBlockingTest {
        //Prepare data
        val contact = provideContact(3)
        contact.forEach {
            dao.insertContact(it)
        }

        //Get database data
        val result = collectContactFlow(this, dao.getAllContacts())

        //Assert that expected value
        assertThat(result).hasSize(3)
    }

    @Test
    fun testGetStoredOnline() = runBlockingTest {
        //Prepare data
        val contact = provideContact(3, false)
        contact.forEach {
            dao.insertContact(it)
        }

        //Get database data and set to false
        val result = collectContactFlow(this, dao.getAllContacts(false))

        //Assert that expected value
        assertThat(result).hasSize(3)
    }

    //Create reusable async parsing
    private suspend fun collectContactFlow(
        scope: CoroutineScope,
        flow: Flow<List<ContactEntity>>
    ): List<ContactEntity> {
        val result = mutableListOf<ContactEntity>()
        val job = scope.launch {
            try {
                flow.collect { data ->
                    result.addAll(data)
                }
                println("Flow collection completed")
            } catch (e: CancellationException) {
                println("Flow collection canceled")
            }
        }
        job.cancelAndJoin()
        return result
    }

    //Create a dynamic entity provider
    private fun provideContact(count: Int, isLocallyStored: Boolean = true): List<ContactEntity> {
        val list = mutableListOf<ContactEntity>()
        for (i in 1..count) {
            list.add(
                ContactEntity(
                    dbId = i,
                    id = i,
                    firstName = "firstName$i",
                    lastName = "lastName$i",
                    avatar = "avatar$i",
                    email = "email$i",
                    number = "number$i",
                    isLocallyStored = isLocallyStored
                )
            )
        }

        return list
    }
}