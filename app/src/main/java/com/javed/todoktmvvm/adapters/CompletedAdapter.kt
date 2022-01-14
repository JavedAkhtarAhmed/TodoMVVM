package com.javed.todoktmvvm.adapters

import android.annotation.SuppressLint
import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.javed.todoktmvvm.databinding.CompletedListItemsBinding
import com.javed.todoktmvvm.modelClasses.TodoResponseItem


class CompletedAdapter() :
    ListAdapter<TodoResponseItem, CompletedAdapter.RecyclerAdapterViewHolder>(DiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerAdapterViewHolder {
        val binding =
            CompletedListItemsBinding.inflate(LayoutInflater.from(parent.context), parent, false)

        return RecyclerAdapterViewHolder(binding)
    }

    override fun onBindViewHolder(holder: RecyclerAdapterViewHolder, position: Int) {
        val currentItem = getItem(position)
        if (currentItem.status == "COMPLETED") {
            holder.bind(currentItem)
        }



    }

    class RecyclerAdapterViewHolder(private val binding: CompletedListItemsBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(todoResponseItem: TodoResponseItem) {
            binding.apply {
                tvTodoTitle.text = todoResponseItem.description

            }
            Log.d("TAG2", "bind: ")
        }

    }

    class DiffUtil : androidx.recyclerview.widget.DiffUtil.ItemCallback<TodoResponseItem>() {
        override fun areItemsTheSame(oldItem: TodoResponseItem, newItem: TodoResponseItem) =
            oldItem.id == newItem.id

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: TodoResponseItem, newItem: TodoResponseItem) =
            oldItem == newItem
    }
}

