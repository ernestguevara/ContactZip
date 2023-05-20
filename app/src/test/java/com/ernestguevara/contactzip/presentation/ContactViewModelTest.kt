package com.ernestguevara.contactzip.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ernestguevara.contactzip.MainCoroutineRule
import com.ernestguevara.contactzip.data.MockContactRepository
import com.ernestguevara.contactzip.data.local.ContactEntity
import com.ernestguevara.contactzip.data.usecase.DbUseCaseDeleteContactImpl
import com.ernestguevara.contactzip.data.usecase.DbUseCaseGetContactListImpl
import com.ernestguevara.contactzip.data.usecase.DbUseCaseInsertContactImpl
import com.ernestguevara.contactzip.getOrAwaitValue
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class ContactViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var mockViewModel: ContactViewModel
    private lateinit var mockContactRepository: MockContactRepository

    private lateinit var dbUseCaseInsertContact: DbUseCaseInsertContactImpl
    private lateinit var dbUseCaseDeleteContact: DbUseCaseDeleteContactImpl
    private lateinit var dbUseCaseGetContactList: DbUseCaseGetContactListImpl

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        mockContactRepository = MockContactRepository()
        dbUseCaseInsertContact = DbUseCaseInsertContactImpl(mockContactRepository)
        dbUseCaseDeleteContact = DbUseCaseDeleteContactImpl(mockContactRepository)
        dbUseCaseGetContactList = DbUseCaseGetContactListImpl(mockContactRepository)

        mockViewModel =
            ContactViewModel(
                dbUseCaseInsertContact,
                dbUseCaseDeleteContact,
                dbUseCaseGetContactList
            )
    }

    @Test
    fun `should insert contact`() = mainCoroutineRule.runBlockingTest {
        //Prepare Data
        val contactItem = provideContact(1)[0]

        //Call the ViewModel method
        mockViewModel.insertContact(contactItem)

        //Get inserted values
        mockViewModel.getContactList()

        val expectedValue = mutableListOf<ContactEntity>()

        //Observe the value
        mockViewModel.getContactListValue.getOrAwaitValue().apply {
            expectedValue.addAll(this)
        }

        //Assert
        assertThat(expectedValue).contains(contactItem)
    }

    @Test
    fun `should delete contact`() = mainCoroutineRule.runBlockingTest {
        //Prepare Data
        val contactItem = provideContact(1)[0]
        mockViewModel.insertContact(contactItem)

        //Call the ViewModel method
        mockViewModel.deleteContact(contactItem)

        //Get inserted values
        mockViewModel.getContactList()

        val expectedValue = mutableListOf<ContactEntity>()

        //Observe the value
        mockViewModel.getContactListValue.getOrAwaitValue().apply {
            expectedValue.addAll(this)
        }

        //Assert
        assertThat(expectedValue).doesNotContain(contactItem)
    }

    @Test
    fun `should get contact list locally`() = mainCoroutineRule.runBlockingTest {
        //Prepare and call ViewModel method
        val contactList = provideContact(4, true)
        contactList.forEach {
            mockViewModel.insertContact(it)
        }

        //Get inserted values
        mockViewModel.getContactList()

        val expectedValue = mutableListOf<ContactEntity>()

        //Observe the value
        mockViewModel.getContactListValue.getOrAwaitValue().apply {
            expectedValue.addAll(this)
        }

        //Assert
        assertThat(expectedValue).hasSize(4)
    }

    @Test
    fun `should get contact list online`() = mainCoroutineRule.runBlockingTest {
        //Prepare and call ViewModel method
        val contactList = provideContact(4, true)
        contactList.forEach {
            mockViewModel.insertContact(it)
        }


        //Get inserted values
        mockViewModel.getContactList()

        val expectedValue = mutableListOf<ContactEntity>()

        //Observe the value
        mockViewModel.getContactListValue.getOrAwaitValue().apply {
            expectedValue.addAll(this)
        }

        //Assert
        assertThat(expectedValue).hasSize(4)
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