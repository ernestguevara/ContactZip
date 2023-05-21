package com.ernestguevara.contactzip.data.local

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.ernestguevara.contactzip.domain.model.UserModel
import kotlinx.parcelize.Parcelize

@Entity(tableName = "contact_items")
@Parcelize
data class ContactEntity(
    @PrimaryKey(autoGenerate = true)
    val dbId: Int? = null,
    val id: Int? = null,
    var firstName: String? = null,
    var lastName: String? = null,
    var avatar: String? = null,
    var email: String? = null,
    var number: String? = null
) : Parcelable {
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
