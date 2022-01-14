package com.javed.todoktmvvm.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.javed.todoktmvvm.repositories.TodoRepository

class MainViewModelFactory(private val todoRepository: TodoRepository): ViewModelProvider.Factory  {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(todoRepository) as T
    }


}
