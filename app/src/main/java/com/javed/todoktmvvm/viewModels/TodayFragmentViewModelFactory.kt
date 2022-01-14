package com.javed.todoktmvvm.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.javed.todoktmvvm.repositories.TodoRepository

class TodayFragmentViewModelFactory(private val todoRepository: TodoRepository): ViewModelProvider.Factory  {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return TodayFragmentViewModel(todoRepository) as T
    }


}
