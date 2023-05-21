package com.ernestguevara.contactzip.presentation.userscreen

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.ernestguevara.contactzip.BaseFragment
import com.ernestguevara.contactzip.R
import com.ernestguevara.contactzip.databinding.FragmentUserListBinding
import com.ernestguevara.contactzip.presentation.components.adapters.ContactListAdapter
import com.ernestguevara.contactzip.util.Constants.STARTING_PAGE
import com.google.gson.Gson
import dagger.hilt.android.AndroidEntryPoint
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class UserListFragment : BaseFragment() {

    private lateinit var binding: FragmentUserListBinding

    private val viewModel: UserListViewModel by viewModels()

    @Inject
    lateinit var contactAdapter: ContactListAdapter

    private lateinit var mLayoutManager: LinearLayoutManager
    private var isLoading = false
    private var shouldPaginate = true

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentUserListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding.swipeRefresh.setOnRefreshListener {
            contactAdapter.contactList = emptyList()
            viewModel.resetPagination()
        }

        setupRv()
        observeViewModel()

        contactAdapter.setItemClickListener {
            Timber.i("ernesthor24 itemClick ${Gson().toJson(it)}")
        }
    }

    private fun setupRv() = binding.rvUserScreen.apply {
        adapter = contactAdapter
        mLayoutManager = LinearLayoutManager(context, RecyclerView.VERTICAL, false)
        layoutManager = mLayoutManager

        addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)

                val visibleItemCount = mLayoutManager.childCount
                val totalItemCount = mLayoutManager.itemCount
                val firstVisibleItemPosition = mLayoutManager.findFirstVisibleItemPosition()
                if (dy > 0 && !isLoading && shouldPaginate && (visibleItemCount + firstVisibleItemPosition) >= totalItemCount && firstVisibleItemPosition >= 0) {
                    // Load more data
                    viewModel.getUsers()
                }
            }
        })
    }

    private fun observeViewModel() {
        viewModel.getUserValue.observe(viewLifecycleOwner) { list ->
            Timber.i("ernesthor24 observe ${Gson().toJson(list)}")
            if (!list.isNullOrEmpty()) {
                contactAdapter.appendData(list.map {
                    it.toEntity()
                })

                binding.swipeRefresh.isRefreshing = false
            }
        }

        viewModel.getUserError.observe(viewLifecycleOwner) {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
        }

        viewModel.endOfPaginationValue.observe(viewLifecycleOwner) {
            shouldPaginate = it
        }
    }

    override fun onResume() {
        super.onResume()
        setToolbarTitle(getString(R.string.label_users))
    }
}