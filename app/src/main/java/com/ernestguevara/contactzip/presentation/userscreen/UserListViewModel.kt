package com.ernestguevara.contactzip.presentation.userscreen

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ernestguevara.contactzip.data.local.ContactEntity
import com.ernestguevara.contactzip.domain.model.UserModel
import com.ernestguevara.contactzip.domain.usecase.ApiUseCaseGetUsers
import com.ernestguevara.contactzip.domain.usecase.DbUseCaseGetContactList
import com.ernestguevara.contactzip.domain.usecase.DbUseCaseInsertContact
import com.ernestguevara.contactzip.presentation.BaseViewModel
import com.ernestguevara.contactzip.util.Constants.ERROR_PAGINATION
import com.ernestguevara.contactzip.util.Constants.STARTING_PAGE
import com.ernestguevara.contactzip.util.RequestState
import com.ernestguevara.contactzip.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.CancellationException
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserListViewModel @Inject constructor(
    private val apiUseCaseGetUsers: ApiUseCaseGetUsers,
    private val dbUseCaseInsertContact: DbUseCaseInsertContact
) : BaseViewModel() {

    private val _getUserValue = MutableLiveData<List<UserModel>>()
    val getUserValue: MutableLiveData<List<UserModel>> = _getUserValue

    private val _getUserError = MutableLiveData<String>()
    val getUserError: MutableLiveData<String> = _getUserError

    private val _endOfPaginationValue = MutableLiveData<Boolean>()
    val endOfPaginationValue: MutableLiveData<Boolean> = _endOfPaginationValue

    private val _refreshValue = MutableLiveData<Boolean>(false)
    val refreshValue: MutableLiveData<Boolean> = _refreshValue

    private val _showEmptyError = MutableLiveData<Boolean>(false)
    val showEmptyError: MutableLiveData<Boolean> = _showEmptyError

    private var queryJob: Job? = null

    private var currentPage: Int = STARTING_PAGE

    init {
        getUsers()
    }

    fun getUsers() {
        queryJob?.cancel()
        queryJob = viewModelScope.launch {
            _state.value = RequestState.Loading

            try {
                val data = apiUseCaseGetUsers.execute(currentPage)

                data.collect { results ->
                    when (results) {
                        is Resource.Success -> {
                            _state.value = RequestState.Finished
                            results.data?.let { list ->
                                _getUserValue.value = list
                                currentPage++
                            }
                            _showEmptyError.value = false
                        }

                        is Resource.Error -> {
                            _state.value = RequestState.Failed
                            results.message?.let {
                                _getUserError.value = it

                                if (it == ERROR_PAGINATION) {
                                    //Return the flow collector
                                    _endOfPaginationValue.value = false
                                    return@collect
                                } else {
                                    _endOfPaginationValue.value = true
                                }
                            }

                            //Check if theres a persistence data
                            results.data?.let {
                                //Have a persistence data

                                _getUserValue.value = it
                                _showEmptyError.value = false
                            } ?: run {
                                //No persistence data
                                if (_getUserValue.value == null) {
                                    //Verify if totally empty
                                    _showEmptyError.value = true
                                }
                            }
                            _endOfPaginationValue.value = false
                        }

                        is Resource.Loading -> {
                            _state.value = RequestState.Loading
                        }
                    }
                }
            } catch (e: CancellationException) {
                //Do nothing cancel just cancels the job
            } catch (e: Exception) {
                _state.value = RequestState.Failed
                _getUserError.value = e.localizedMessage
            }
            _refreshValue.value = false
        }
    }

    fun addContact(contactEntity: ContactEntity) = viewModelScope.launch {
        dbUseCaseInsertContact.execute(contactEntity)
    }

    fun resetPagination() {
        currentPage = STARTING_PAGE
        _refreshValue.value = true
        _endOfPaginationValue.value = true
        getUsers()
    }
}

