package com.example.android.politicalpreparedness.election.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.android.politicalpreparedness.databinding.ElectionListHeaderBinding
import com.example.android.politicalpreparedness.databinding.ElectionListItemBinding
import com.example.android.politicalpreparedness.network.models.Election

private const val ITEM_VIEW_TYPE_HEADER = 0
private const val ITEM_VIEW_TYPE_ITEM = 1

class ElectionListAdapter(
        private val header: String,
        private val onClickItem: (election: Election) -> Unit
) : ListAdapter<DataItem, RecyclerView.ViewHolder>(ElectionDiffCallback()) {

    fun updateData(list: List<Election>?) {
        val items = when (list) {
            null -> listOf(DataItem.Header)
            else -> listOf(DataItem.Header) + list.map { DataItem.Item(it) }
        }
        submitList(items)
    }

    override fun getItemViewType(position: Int): Int {
        return when (getItem(position)) {
            is DataItem.Header -> ITEM_VIEW_TYPE_HEADER
            is DataItem.Item -> ITEM_VIEW_TYPE_ITEM
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is HeaderViewHolder -> {
                holder.bind(header)
            }
            is ItemViewHolder -> {
                val nightItem = getItem(position) as DataItem.Item
                holder.bind(nightItem.election, onClickItem)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return when (viewType) {
            ITEM_VIEW_TYPE_HEADER -> HeaderViewHolder.from(parent)
            ITEM_VIEW_TYPE_ITEM -> ItemViewHolder.from(parent)
            else -> throw ClassCastException("Unknown viewType $viewType")
        }
    }

    class HeaderViewHolder(private val binding: ElectionListHeaderBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(header: String) {
            binding.header = header
            binding.executePendingBindings()
        }

        companion object {
            fun from(parent: ViewGroup): HeaderViewHolder {
                val binding = ElectionListHeaderBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return HeaderViewHolder(binding)
            }
        }
    }

    class ItemViewHolder(private val binding: ElectionListItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(election: Election, onClickItem: (election: Election) -> Unit) {
            binding.election = election
            binding.executePendingBindings()
            binding.root.setOnClickListener { onClickItem(election) }
        }

        companion object {
            fun from(parent: ViewGroup): ItemViewHolder {
                val binding = ElectionListItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
                return ItemViewHolder(binding)
            }
        }
    }

}

class ElectionDiffCallback : DiffUtil.ItemCallback<DataItem>() {
    override fun areItemsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem === newItem
    }

    override fun areContentsTheSame(oldItem: DataItem, newItem: DataItem): Boolean {
        return oldItem.id == newItem.id
    }
}

sealed class DataItem {
    abstract val id: Int

    data class Item(val election: Election) : DataItem() {
        override val id = election.id
    }

    object Header : DataItem() {
        override val id = Int.MIN_VALUE
    }

}
