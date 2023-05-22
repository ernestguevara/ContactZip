package com.ernestguevara.contactzip.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UserResultsDTO(
    @SerializedName("data")
    val userDto: List<UserDTO>,
    val page: Int,
    @SerializedName("per_page")
    val perPage: Int,
    val total: Int,
    @SerializedName("total_pages")
    val totalPages: Int
)