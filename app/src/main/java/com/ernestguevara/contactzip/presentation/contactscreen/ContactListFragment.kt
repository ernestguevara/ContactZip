package com.ernestguevara.contactzip.presentation.contactscreen

import android.content.Intent
import android.net.Uri
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
        }
    }


    override fun onResume() {
        super.onResume()
        setToolbarTitle(getString(R.string.label_contact))
    }

    override fun onItemClick(contactEntity: ContactEntity) {
        showDetailDialogFragment(contactEntity, this, getString(R.string.label_update))
    }

    override fun onEmailClick(contactEntity: ContactEntity) {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("mailto:${contactEntity.email}")

        if (context?.let { intent.resolveActivity(it.packageManager) } != null) {
            startActivity(intent)
        }
    }

    override fun onCallClick(contactEntity: ContactEntity) {
        val intent = Intent(Intent.ACTION_DIAL)
        intent.data = Uri.parse("tel:${contactEntity.number}")

        startActivity(intent)
    }

    override fun onMessageClick(contactEntity: ContactEntity) {
        val intent = Intent(Intent.ACTION_SENDTO)
        intent.data = Uri.parse("smsto:${contactEntity.number}")

        startActivity(intent)
    }

    override fun onConfirmAction(contactEntity: ContactEntity) {
        contactViewModel.insertContact(contactEntity)
    }
}