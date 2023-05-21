package com.ernestguevara.contactzip.presentation.contactscreen

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import com.ernestguevara.contactzip.BaseFragment
import com.ernestguevara.contactzip.databinding.FragmentContactListBinding
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber

@AndroidEntryPoint
class ContactListFragment : BaseFragment() {

    private lateinit var binding: FragmentContactListBinding

    private val contactViewModel: ContactListViewModel by viewModels()

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
        contactViewModel.getContactList()

        contactViewModel.getContactListValue.observe(viewLifecycleOwner) {
            Timber.i("ernesthor24 ${Gson().toJson(it)}")
        }
    }
}