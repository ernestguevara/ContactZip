package com.ernestguevara.contactzip.data.remote

import com.ernestguevara.contactzip.data.remote.dto.UserResultsDTO
import retrofit2.http.GET
import retrofit2.http.Query

interface UserApiService {

    @GET("api/users")
    suspend fun getUsers(
        @Query("page") page: Int,
        @Query("per_page") count: Int
    ): UserResultsDTO
}