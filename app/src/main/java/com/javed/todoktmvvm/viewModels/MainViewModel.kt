package com.javed.todoktmvvm.viewModels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.javed.todoktmvvm.modelClasses.TodoResponse
import com.javed.todoktmvvm.modelClasses.TodoResponseItem
import com.javed.todoktmvvm.repositories.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MainViewModel(private val todoRepository: TodoRepository) : ViewModel() {


        init {
            viewModelScope.launch(Dispatchers.IO) {
                todoRepository.getTodo()
            }
        }


        val todoLiveData: LiveData<MutableList<TodoResponseItem>> = todoRepository.todoLiveData




}