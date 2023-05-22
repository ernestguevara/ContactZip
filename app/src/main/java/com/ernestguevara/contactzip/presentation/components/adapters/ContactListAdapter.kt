package com.ernestguevara.contactzip.presentation.components.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.AsyncListDiffer
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.RequestManager
import com.bumptech.glide.load.resource.bitmap.CircleCrop
import com.ernestguevara.contactzip.data.local.ContactEntity
import com.ernestguevara.contactzip.databinding.ItemContactBinding
import com.ernestguevara.contactzip.databinding.ItemUserBinding
import com.ernestguevara.contactzip.presentation.interfaces.ItemContactListener
import com.ernestguevara.contactzip.util.ContactViewType
import com.ernestguevara.contactzip.util.makeVisibleOrInvisible
import javax.inject.Inject

class ContactListAdapter @Inject constructor(
    private val context: Context,
    private val glide: RequestManager
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var viewType: ContactViewType = ContactViewType.USER_LIST

    fun setInitialViewType(viewType: ContactViewType) {
        this.viewType = viewType
    }

    class UserViewHolder(val binding: ItemUserBinding) : RecyclerView.ViewHolder(binding.root)
    class ContactViewHolder(val binding: ItemContactBinding) :
        RecyclerView.ViewHolder(binding.root)

    private val diffCallback = object : DiffUtil.ItemCallback<ContactEntity>() {
        override fun areItemsTheSame(oldItem: ContactEntity, newItem: ContactEntity): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: ContactEntity, newItem: ContactEntity): Boolean {
            return oldItem.hashCode() == newItem.hashCode()
        }
    }

    private val differ = AsyncListDiffer(this, diffCallback)

    var contactList: List<ContactEntity>
        get() = differ.currentList
        set(value) = differ.submitList(value)

    override fun getItemViewType(position: Int): Int {
        return this.viewType.ordinal
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ContactViewType.USER_LIST.ordinal -> {
                val binding = ItemUserBinding.inflate(LayoutInflater.from(context), parent, false)
                UserViewHolder(binding)
            }
            ContactViewType.CONTACT_LIST.ordinal -> {
                val binding =
                    ItemContactBinding.inflate(LayoutInflater.from(context), parent, false)
                ContactViewHolder(binding)
            }
            else -> throw IllegalArgumentException("Invalid view type: $viewType")
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val contactItem = contactList[position]

        when (holder) {
            is UserViewHolder -> {
                val binding = holder.binding
                binding.apply {
                    contactItem.run {
                        glide.load(avatar)
                            .transform((CircleCrop())).into(ivUser)
                        tvFName.text = firstName
                        tvLName.text = lastName
                    }

                    tvAdd.setOnClickListener {
                        onItemClickListener?.let { click ->
                            click(contactItem)
                        }
                    }
                }
            }

            is ContactViewHolder -> {
                val binding = holder.binding
                binding.run {
                    contactItem.run {
                        glide.load(avatar)
                            .transform((CircleCrop())).into(ivContact)
                        tvName.text = "${firstName} ${lastName}"

                        clContactItem.setOnClickListener {
                            contactItemListener?.onItemClick(contactItem)
                        }

                        ivEmail.apply {
                            makeVisibleOrInvisible(!email.isNullOrEmpty())
                            setOnClickListener {
                                contactItemListener?.onEmailClick(contactItem)
                            }
                        }

                        ivCall.apply {
                            makeVisibleOrInvisible(!number.isNullOrEmpty())
                            setOnClickListener {
                                contactItemListener?.onCallClick(contactItem)
                            }
                        }

                        ivText.apply {
                            makeVisibleOrInvisible(!number.isNullOrEmpty())
                            setOnClickListener {
                                contactItemListener?.onMessageClick(contactItem)
                            }
                        }
                    }
                }

            }
        }
    }

    override fun getItemCount(): Int {
        return contactList.size
    }

    private var onItemClickListener: ((ContactEntity) -> Unit)? = null

    fun setItemClickListener(listener: (ContactEntity) -> Unit) {
        onItemClickListener = listener
    }

    private var contactItemListener: ItemContactListener? = null

    fun setContactItemListener(listener: ItemContactListener) {
        contactItemListener = listener
    }

    fun appendData(newData: List<ContactEntity>) {
        val currentList = differ.currentList.toMutableList()

        // Filter out duplicates from the new data
        val filteredData = newData.filterNot { newContact ->
            currentList.any { existingContact ->
                existingContact.id == newContact.id
            }
        }

        currentList.addAll(filteredData)
        differ.submitList(currentList)
    }

    fun updateData(newList: List<ContactEntity>) {
        differ.submitList(newList.map {
            it.copy()
        })
    }
}
