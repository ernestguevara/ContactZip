package com.ernestguevara.contactzip.data.remote.dto

import com.ernestguevara.contactzip.domain.model.UserModel
import com.google.gson.annotations.SerializedName

data class UserDTO(
    val id: Int,
    @SerializedName("first_name")
    val firstName: String,
    @SerializedName("last_name")
    val lastName: String,
    val avatar: String,
    val email: String
) {
    fun dtoToModel(): UserModel {
        return UserModel(
            id = id,
            firstName = firstName,
            lastName = lastName,
            avatar = avatar,
            email = email
        )
    }
}