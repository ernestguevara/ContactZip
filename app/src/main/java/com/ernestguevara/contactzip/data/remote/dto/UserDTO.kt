package com.ernestguevara.contactzip.data.remote.dto

import com.google.gson.annotations.SerializedName

data class UserDTO(
    val avatar: String,
    val email: String,
    @SerializedName("first_name")
    val firstName: String,
    val id: Int,
    @SerializedName("last_name")
    val lastName: String
)