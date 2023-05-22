package com.ernestguevara.contactzip.presentation.interfaces

import com.ernestguevara.contactzip.data.local.ContactEntity

interface ItemContactListener {
    fun onItemClick(contactEntity: ContactEntity)
    fun onEmailClick(contactEntity: ContactEntity)
    fun onCallClick(contactEntity: ContactEntity)
    fun onMessageClick(contactEntity: ContactEntity)
}