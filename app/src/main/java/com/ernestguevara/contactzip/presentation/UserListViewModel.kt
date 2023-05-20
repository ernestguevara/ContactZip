package com.ernestguevara.contactzip.presentation

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.ernestguevara.contactzip.domain.model.UserModel
import com.ernestguevara.contactzip.domain.usecase.ApiUseCaseGetUsers
import com.ernestguevara.contactzip.domain.usecase.DbUseCaseInsertContact
import com.ernestguevara.contactzip.util.RequestState
import com.ernestguevara.contactzip.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import timber.log.Timber
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

    private var queryJob: Job? = null

    var currentPage: Int = 1

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
                                list.forEach { user ->
                                    dbUseCaseInsertContact.execute(user.toEntity())
                                }
                            }
                        }

                        is Resource.Error -> {
                            _state.value = RequestState.Failed
                            results.message?.let {
                                _getUserError.value = it
                            }
                        }

                        is Resource.Loading -> {
                            _state.value = RequestState.Loading
                        }
                    }
                }
            } catch (e: Exception) {
                _state.value = RequestState.Failed
                _getUserError.value = e.localizedMessage
            }
        }
    }
}