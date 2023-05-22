package com.ernestguevara.contactzip.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ernestguevara.contactzip.MainCoroutineRule
import com.ernestguevara.contactzip.data.MockContactRepository
import com.ernestguevara.contactzip.data.local.ContactEntity
import com.ernestguevara.contactzip.data.usecase.DbUseCaseDeleteContactImpl
import com.ernestguevara.contactzip.data.usecase.DbUseCaseGetContactListImpl
import com.ernestguevara.contactzip.data.usecase.DbUseCaseInsertContactImpl
import com.ernestguevara.contactzip.getOrAwaitValue
import com.ernestguevara.contactzip.presentation.contactscreen.ContactListViewModel
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class ContactListViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var mockListViewModel: ContactListViewModel
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

        mockListViewModel =
            ContactListViewModel(
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
        mockListViewModel.insertContact(contactItem)

        //Get inserted values
        mockListViewModel.getContactList()

        val expectedValue = mutableListOf<ContactEntity>()

        //Observe the value
        mockListViewModel.getContactListValue.getOrAwaitValue().apply {
            expectedValue.addAll(this)
        }

        //Assert
        assertThat(expectedValue).contains(contactItem)
    }

    @Test
    fun `should delete contact`() = mainCoroutineRule.runBlockingTest {
        //Prepare Data
        val contactItem = provideContact(1)[0]
        mockListViewModel.insertContact(contactItem)

        //Call the ViewModel method
        mockListViewModel.deleteContact(contactItem)

        //Get inserted values
        mockListViewModel.getContactList()

        val expectedValue = mutableListOf<ContactEntity>()

        //Observe the value
        mockListViewModel.getContactListValue.getOrAwaitValue().apply {
            expectedValue.addAll(this)
        }

        //Assert
        assertThat(expectedValue).doesNotContain(contactItem)
    }

    @Test
    fun `should get contact list locally`() = mainCoroutineRule.runBlockingTest {
        //Prepare and call ViewModel method
        val contactList = provideContact(4)
        contactList.forEach {
            mockListViewModel.insertContact(it)
        }

        //Get inserted values
        mockListViewModel.getContactList()

        val expectedValue = mutableListOf<ContactEntity>()

        //Observe the value
        mockListViewModel.getContactListValue.getOrAwaitValue().apply {
            expectedValue.addAll(this)
        }

        //Assert
        assertThat(expectedValue).hasSize(4)
    }

    @Test
    fun `should get contact list online`() = mainCoroutineRule.runBlockingTest {
        //Prepare and call ViewModel method
        val contactList = provideContact(4)
        contactList.forEach {
            mockListViewModel.insertContact(it)
        }


        //Get inserted values
        mockListViewModel.getContactList()

        val expectedValue = mutableListOf<ContactEntity>()

        //Observe the value
        mockListViewModel.getContactListValue.getOrAwaitValue().apply {
            expectedValue.addAll(this)
        }

        //Assert
        assertThat(expectedValue).hasSize(4)
    }

    //Create a dynamic entity provider
    private fun provideContact(count: Int): List<ContactEntity> {
        val list = mutableListOf<ContactEntity>()
        for (i in 1..count) {
            list.add(
                ContactEntity(
                    id = i,
                    firstName = "firstName$i",
                    lastName = "lastName$i",
                    avatar = "avatar$i",
                    email = "email$i",
                    number = "number$i"
                )
            )
        }

        return list
    }
}