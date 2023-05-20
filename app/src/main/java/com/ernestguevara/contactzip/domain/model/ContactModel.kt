package com.ernestguevara.contactzip.domain.model

import com.ernestguevara.contactzip.data.local.ContactEntity

data class ContactModel(
    val id: Int? = null,
    val firstName: String? = null,
    val lastName: String? = null,
    val avatar: String? = null,
    val email: String? = null,
    val isLocallyStored: Boolean? = null
) {
    fun toEntity(): ContactEntity {
        return ContactEntity(
            id = id,
            firstName = firstName,
            lastName = lastName,
            avatar = avatar,
            email = email
        )
    }
}