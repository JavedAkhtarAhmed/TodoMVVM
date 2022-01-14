package com.javed.todoktmvvm.viewModels

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.javed.todoktmvvm.modelClasses.TodoResponseItem
import com.javed.todoktmvvm.repositories.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class LaterFragmentViewModel(private val todoRepository: TodoRepository) : ViewModel() {


    init {
        viewModelScope.launch(Dispatchers.IO) {
            todoRepository.getTodo()
        }
    }


    private val todoLiveData: LiveData<MutableList<TodoResponseItem>> = todoRepository.todoLiveData

    val todoMutableLiveData: MutableLiveData<MutableList<TodoResponseItem>>
        get() = todoLiveData as MutableLiveData<MutableList<TodoResponseItem>>


}