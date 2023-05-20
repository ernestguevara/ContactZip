package com.ernestguevara.contactzip.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ernestguevara.contactzip.domain.model.ContactModel

@Entity(tableName = "contact_items")
data class ContactEntity(
    @PrimaryKey(autoGenerate = true)
    val dbId: Int? = null,
    val id: Int? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var avatar: String? = null,
    var email: String? = null,
    var number: String? = null,
    var isLocallyStored: Boolean? = null
) {
    fun toModel(): ContactModel {
        return ContactModel(
            id = id,
            firstName = firstName,
            lastName = lastName,
            avatar = avatar,
            email = email,
            isLocallyStored = isLocallyStored
        )
    }
}
