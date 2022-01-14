package com.javed.todoktmvvm.viewModels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.javed.todoktmvvm.modelClasses.TodoResponseItem
import com.javed.todoktmvvm.repositories.TodoRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class TodayFragmentViewModel(private val todoRepository: TodoRepository) : ViewModel() {


    init {
        viewModelScope.launch(Dispatchers.IO) {
            todoRepository.getTodo()
        }
    }


    private val todoLiveData: LiveData<MutableList<TodoResponseItem>> = todoRepository.todoLiveData

    val todoMutableLiveData: MutableLiveData<MutableList<TodoResponseItem>>
        get() = todoLiveData as MutableLiveData<MutableList<TodoResponseItem>>

    fun swipe(position :Int){
        Log.d("TAG2", "swipe: check1  "+todoMutableLiveData.value?.get(position)?.status+" "+todoMutableLiveData.value?.get(position)?.description)

        if(todoMutableLiveData.value?.get(position)?.status.equals("PENDING")){
            todoMutableLiveData.value?.get(position)?.status = "COMPLETED"
            todoMutableLiveData.value = todoMutableLiveData.value
            Log.d("TAG2", "swipe: check2   "+todoMutableLiveData.value?.get(position)?.status+" "+todoMutableLiveData.value?.get(position)?.description)
        }
        Log.d("TAG2", "swipe: ${todoMutableLiveData.value?.forEach { 
            it.description
        }}")
    }

//        get() {
//            for (i in 0..todoMutableLiveData.value?.size!!) {
//                if (todoMutableLiveData.value?.get(i)?.status == "COMPLETED") {
//                    todayCompletedMutableLiveData.value = listOf(todoMutableLiveData.value!![i])
//                }
//            }
//            return todayCompletedMutableLiveData
//        }


//    fun todayCompletedListFormation() : MutableLiveData<List<TodoResponseItem>>{
//        for (i in 0..todoMutableLiveData.value?.size!!) {
//            if (todoMutableLiveData.value?.get(i)?.status == "PENDING") {
//                todayCompletedMutableLiveData.value = listOf(todoMutableLiveData.value!![i])
//            }
//        }
//        return todayCompletedMutableLiveData
//    }

}