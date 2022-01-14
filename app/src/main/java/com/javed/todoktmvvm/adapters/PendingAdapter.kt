package com.javed.todoktmvvm.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.javed.todoktmvvm.databinding.PendingListItemsBinding
import com.javed.todoktmvvm.modelClasses.TodoResponseItem

class PendingAdapter() :
    ListAdapter<TodoResponseItem, PendingAdapter.RecyclerAdapterViewHolder>(DiffUtil()) {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapterViewHolder {
        val binding =
            PendingListItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return RecyclerAdapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerAdapterViewHolder, position: Int) {
        val currentItem = getItem(position)
        holder.bind(currentItem)

    }

    class RecyclerAdapterViewHolder(private val binding: PendingListItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(todoResponseItem: TodoResponseItem) {
            binding.apply {
                tvTodoTitle.text = todoResponseItem.description

            }
            Log.d("TAG2", "bind: ")
        }
    }

    class DiffUtil : androidx.recyclerview.widget.DiffUtil.ItemCallback<TodoResponseItem>() {
        override fun areItemsTheSame(
            oldItem: TodoResponseItem,
            newItem: TodoResponseItem
        ): Boolean {
            return oldItem.id == newItem.id
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(
            oldItem: TodoResponseItem,
            newItem: TodoResponseItem
        ): Boolean {
            return oldItem.id == newItem.id && oldItem.status == newItem.status && oldItem.scheduledDate == newItem.scheduledDate && oldItem.description == newItem.description
        }
    }
}

