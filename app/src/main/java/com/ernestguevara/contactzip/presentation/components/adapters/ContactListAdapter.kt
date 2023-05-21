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
import javax.inject.Inject

class ContactListAdapter @Inject constructor(
    private val context: Context,
    private val glide: RequestManager
) : RecyclerView.Adapter<ContactListAdapter.ViewHolder>() {

    class ViewHolder(val binding: ItemContactBinding) : RecyclerView.ViewHolder(binding.root)

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

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(ItemContactBinding.inflate(LayoutInflater.from(context), parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val contactItem = contactList[position]
        holder.binding.apply {
            contactItem.run {
                glide.load(avatar)
                    .transform((CircleCrop())).into(ivContact)
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

    override fun getItemCount(): Int {
        return contactList.size
    }

    private var onItemClickListener: ((ContactEntity) -> Unit)? = null

    fun setItemClickListener(listener: (ContactEntity) -> Unit) {
        onItemClickListener = listener
    }

    fun appendData(newData: List<ContactEntity>) {
        val currentList = differ.currentList.toMutableList()

        // Filter out duplicates from the new data
        val filteredData = newData.filterNot { newContact ->
            currentList.any { existingContact ->
                existingContact.id == newContact.id
            }
        }.sortedBy {
            it.id
        }

        currentList.addAll(filteredData)
        differ.submitList(currentList)
    }
}
