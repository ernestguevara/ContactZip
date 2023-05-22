package com.ernestguevara.contactzip.presentation.contactscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ernestguevara.contactzip.BaseFragment
import com.ernestguevara.contactzip.R
import com.ernestguevara.contactzip.data.local.ContactEntity
import com.ernestguevara.contactzip.databinding.FragmentContactListBinding
import com.ernestguevara.contactzip.presentation.components.AddContactDialogFragment
import com.ernestguevara.contactzip.presentation.components.adapters.ContactListAdapter
import com.ernestguevara.contactzip.presentation.interfaces.DialogContactListener
import com.ernestguevara.contactzip.presentation.interfaces.ItemContactListener
import com.ernestguevara.contactzip.util.ContactViewType
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class ContactListFragment : BaseFragment(), ItemContactListener, DialogContactListener {

    private lateinit var binding: FragmentContactListBinding

    private val contactViewModel: ContactListViewModel by viewModels()

    @Inject
    lateinit var contactAdapter: ContactListAdapter

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContactListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        contactAdapter.apply {
            setInitialViewType(ContactViewType.CONTACT_LIST)
            setContactItemListener(this@ContactListFragment)
        }

        setupRv()
        observeViewModels()
    }

    private fun setupRv() = binding.rvContacts.apply {
        adapter = contactAdapter
        layoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
    }

    private fun observeViewModels() {
        contactViewModel.getContactListValue.observe(viewLifecycleOwner) {
            contactAdapter.appendData(it, true)
            Timber.i("ernesthor24 ${Gson().toJson(it)}")
        }
    }


    override fun onResume() {
        super.onResume()
        setToolbarTitle(getString(R.string.label_contact))
    }

    override fun onItemClick(contactEntity: ContactEntity) {
        val addDialog = AddContactDialogFragment.newInstance(contactEntity, getString(R.string.label_update))
        addDialog.setListener(this)
        addDialog.show(parentFragmentManager, "contact_dialog")
    }

    override fun onEmailClick(contactEntity: ContactEntity) {
        Timber.i("ernesthor24 onEmailClick")
    }

    override fun onCallClick(contactEntity: ContactEntity) {
        Timber.i("ernesthor24 onCallClick")
    }

    override fun onMessageClick(contactEntity: ContactEntity) {
        Timber.i("ernesthor24 onMessageClick")
    }

    override fun onConfirmAction(contactEntity: ContactEntity) {
        Timber.i("ernesthor24 updateItem")
    }
}