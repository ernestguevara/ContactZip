package com.ernestguevara.contactzip.presentation.interfaces

import com.ernestguevara.contactzip.data.local.ContactEntity
interface DialogContactListener {
    fun onConfirmAction(contactEntity: ContactEntity)
    fun onDeleteAction(contactEntity: ContactEntity)
}