package com.ernestguevara.contactzip.presentation

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.ernestguevara.contactzip.MainCoroutineRule
import com.ernestguevara.contactzip.domain.model.UserModel
import com.ernestguevara.contactzip.domain.usecase.ApiUseCaseGetUsers
import com.ernestguevara.contactzip.domain.usecase.DbUseCaseInsertContact
import com.ernestguevara.contactzip.getOrAwaitValue
import com.ernestguevara.contactzip.util.Resource
import com.google.common.truth.Truth.assertThat
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.single
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.MockitoAnnotations
import org.mockito.kotlin.whenever

@ExperimentalCoroutinesApi
class UserListViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainCoroutineRule = MainCoroutineRule()

    private lateinit var mockViewModel: UserListViewModel

    @Mock
    private lateinit var apiUseCaseGetUsers: ApiUseCaseGetUsers

    @Mock
    private lateinit var dbUseCaseInsertContact: DbUseCaseInsertContact

    @Before
    fun setup() {
        MockitoAnnotations.openMocks(this)
        mockViewModel = UserListViewModel(apiUseCaseGetUsers, dbUseCaseInsertContact)
    }

    @Test
    fun `should get user list`() = mainCoroutineRule.runBlockingTest {
        // Prepare the response
        val response = flowOf(
            Resource.Success(
                listOf(
                    UserModel(1, "fn", "ln", "url", "test@email.com", true),
                    UserModel(2, "fn2", "ln2", "url2", "test2@email.com", true)
                )
            )
        )

        // Stub the execute method of apiUseCaseGetUsers
        whenever(apiUseCaseGetUsers.execute(1)).thenReturn(response)

        // Call the getUsers function
        mockViewModel.getUsers()

        // Await for the value to be emitted
        val actualResults = mockViewModel.getUserValue.getOrAwaitValue()

        // Convert the flow to a list
        val expectedResults = response.single().data

        // Assert the values
        assertThat(actualResults).isEqualTo(expectedResults)
    }
}