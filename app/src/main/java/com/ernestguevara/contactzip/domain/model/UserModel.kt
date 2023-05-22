package com.ernestguevara.contactzip.domain.model

import com.ernestguevara.contactzip.data.local.ContactEntity
import com.ernestguevara.contactzip.data.local.UserEntity

data class UserModel(
    val id: Int? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val avatar: String? = null,
    val email: String? = null
) {
    fun toContactEntity(): ContactEntity {
        return ContactEntity(
            id = id,
            firstName = firstName,
            lastName = lastName,
            avatar = avatar,
            email = email
        )
    }

    fun toUserEntity(): UserEntity {
        return UserEntity(
            id = id,
            firstName = firstName,
            lastName = lastName,
            avatar = avatar,
            email = email
        )
    }
}