package com.ernestguevara.contactzip.presentation.contactscreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.ernestguevara.contactzip.databinding.FragmentContactListBinding

class ContactListFragment : Fragment() {

    private lateinit var binding: FragmentContactListBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentContactListBinding.inflate(inflater, container, false)
        return binding.root
    }
}