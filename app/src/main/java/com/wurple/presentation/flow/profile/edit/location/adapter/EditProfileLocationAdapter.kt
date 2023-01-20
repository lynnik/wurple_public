package com.wurple.presentation.flow.profile.edit.location.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import com.wurple.databinding.ItemLocationBinding
import com.wurple.domain.model.location.LocationSearch
import com.wurple.presentation.flow.base.adapter.BaseAdapter

class EditProfileLocationAdapter(private val onItemClickListener: (item: LocationSearch) -> Unit) :
    BaseAdapter<LocationSearch, EditProfileLocationViewHolder>(DiffCallback()) {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): EditProfileLocationViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = ItemLocationBinding.inflate(inflater, parent, false)
        return EditProfileLocationViewHolder(binding, onItemClickListener)
    }

    private class DiffCallback : DiffUtil.ItemCallback<LocationSearch>() {
        override fun areItemsTheSame(
            oldItem: LocationSearch,
            newItem: LocationSearch
        ): Boolean = oldItem == newItem

        override fun areContentsTheSame(
            oldItem: LocationSearch,
            newItem: LocationSearch
        ): Boolean = oldItem == newItem
    }
}