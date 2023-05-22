package com.ernestguevara.contactzip

import androidx.fragment.app.Fragment
import com.ernestguevara.contactzip.data.local.ContactEntity
import com.ernestguevara.contactzip.presentation.MainActivityListener
import com.ernestguevara.contactzip.presentation.components.UserDetailDialogFragment
import com.ernestguevara.contactzip.presentation.interfaces.DialogContactListener

open class BaseFragment : Fragment(), MainActivityListener {
    override fun setToolbarTitle(title: String) {
        if (activity is MainActivityListener) {
            (activity as MainActivityListener).setToolbarTitle(title)
        }
    }

    override fun showLoadingDialog() {
        if (activity is MainActivityListener) {
            (activity as MainActivityListener).showLoadingDialog()
        }
    }

    override fun dismissLoadingDialog() {
        if (activity is MainActivityListener) {
            (activity as MainActivityListener).dismissLoadingDialog()
        }
    }

    fun showDetailDialogFragment(
        contactEntity: ContactEntity,
        listener: DialogContactListener,
        text: String
    ) {
        val addDialog = UserDetailDialogFragment.newInstance(contactEntity, text)
        addDialog.setListener(listener)
        addDialog.show(parentFragmentManager, "contact_dialog")
    }
}