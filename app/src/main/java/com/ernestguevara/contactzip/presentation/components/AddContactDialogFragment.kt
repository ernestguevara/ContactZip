package com.ernestguevara.contactzip.presentation.components

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import android.view.View
import androidx.fragment.app.DialogFragment
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.ernestguevara.contactzip.data.local.ContactEntity
import com.ernestguevara.contactzip.databinding.DialogAddContactBinding
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class AddContactDialogFragment :
    DialogFragment() {
    private lateinit var binding: DialogAddContactBinding

    @Inject
    lateinit var glide: RequestManager

    private var addContactListener: AddContactListener? = null

    private var contactEntity: ContactEntity? = null

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        binding = DialogAddContactBinding.inflate(layoutInflater)
        val builder = AlertDialog.Builder(requireContext())
            .setView(binding.root)
        contactEntity = requireArguments().getParcelable(CONTACT_ITEM)

        setupViews()

        return builder.create()
    }

    private fun setupViews() {
        binding.apply {
            contactEntity?.let { contact ->
                //Inflate views with data
                glide.load(contact.avatar)
                    .transform((CircleCrop())).into(ivDialog)
                etName.setText("${contact.firstName} ${contact.lastName}")
                etEmail.setText("${contact.email}")

                //Request Focus upon init
                etNumber.requestFocus()

                //Set button click listeners
                btnCancel.setOnClickListener {
                    dismiss()
                }

                btnConfirm.setOnClickListener {
                    contact.number = if (etNumber.text.isNullOrEmpty()) {
                        etNumber.text.toString()
                    } else {
                        null
                    }

                    addContactListener?.onContactAdded(contact)
                    dismiss()
                }
            }
        }
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        addContactListener = activity as? AddContactListener
    }

    override fun onDestroyView() {
        if (addContactListener != null) {
            addContactListener = null
        }
        super.onDestroyView()
    }

    companion object {
        const val CONTACT_ITEM = "contact_entity"

        fun newInstance(
            contactEntity: ContactEntity
        ): AddContactDialogFragment {
            return AddContactDialogFragment().apply {
                arguments = Bundle().apply {
                    putParcelable(CONTACT_ITEM, contactEntity)
                }
            }
        }

    }
}

interface AddContactListener {
    fun onContactAdded(contactEntity: ContactEntity)
}