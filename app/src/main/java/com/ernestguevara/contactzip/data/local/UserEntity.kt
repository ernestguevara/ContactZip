package com.ernestguevara.contactzip.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ernestguevara.contactzip.domain.model.UserModel

@Entity(tableName = "user_item")
data class UserEntity(
    @PrimaryKey(autoGenerate = false)
    val id: Int? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var avatar: String? = null,
    var email: String? = null,
) {
    fun toModel(): UserModel {
        return UserModel(
            id = id,
            firstName = firstName,
            lastName = lastName,
            avatar = avatar,
            email = email
        )
    }
}