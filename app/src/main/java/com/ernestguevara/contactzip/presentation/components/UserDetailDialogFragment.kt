package com.ernestguevara.contactzip.presentation.components

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.text.Editable
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.ernestguevara.contactzip.data.local.ContactEntity
import com.ernestguevara.contactzip.databinding.DialogAddContactBinding
import com.ernestguevara.contactzip.presentation.interfaces.DialogContactListener
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class UserDetailDialogFragment :
    DialogFragment() {
    private lateinit var binding: DialogAddContactBinding

    @Inject
    lateinit var glide: RequestManager

    private var addContactListener: DialogContactListener? = null

    private var contactEntity: ContactEntity? = null
    private var title: String? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogAddContactBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireContext())
            .setView(binding.root)
        contactEntity = requireArguments().getParcelable(CONTACT_ITEM)
        title = requireArguments().getString(DIALOG_TITLE)

        setupViews()

        return builder.create()
    }

    fun setListener(addContactListener: DialogContactListener) {
        this.addContactListener = addContactListener
    }

    private fun setupViews() {
        binding.apply {
            tvDialogTitle.text = title
            contactEntity?.let { contact ->
                //Inflate views with data
                glide.load(contact.avatar)
                    .transform((CircleCrop())).into(ivDialog)
                etName.setText("${contact.firstName} ${contact.lastName}")
                etEmail.setText("${contact.email}")

                //Request Focus upon init
                contact.number?.let {
                    etNumber.text = Editable.Factory.getInstance().newEditable(it)
                }
                etNumber.requestFocus()

                //Set button click listeners
                btnCancel.setOnClickListener {
                    dismiss()
                }

                btnConfirm.setOnClickListener {
                    contact.number = if (!etNumber.text.isNullOrEmpty()) {
                        etNumber.text.toString()
                    } else {
                        null
                    }

                    addContactListener?.onConfirmAction(contact)
                    dismiss()
                }
            }
        }
    }

    override fun onDestroyView() {
        if (addContactListener != null) {
            addContactListener = null
        }
        super.onDestroyView()
    }

    companion object {
        const val CONTACT_ITEM = "contact_entity"
        const val DIALOG_TITLE = "dialog_title"

        fun newInstance(
            contactEntity: ContactEntity,
            title: String,
        ): UserDetailDialogFragment {
            return UserDetailDialogFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(CONTACT_ITEM, contactEntity)
                    putString(DIALOG_TITLE, title)
                }
            }
        }

    }
}