package com.javed.todoktmvvm.repositories

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.javed.todoktmvvm.api.UserServices
import com.javed.todoktmvvm.modelClasses.TodoResponse
import com.javed.todoktmvvm.modelClasses.TodoResponseItem

class TodoRepository(private val userServices: UserServices,
                     private val applicationContext: Context
) {

    private val todoMutableLiveData = MutableLiveData<MutableList<TodoResponseItem>>()

    val todoLiveData : LiveData<MutableList<TodoResponseItem>>
    get() = todoMutableLiveData

    suspend fun getTodo(){
        val result = userServices.todo()
        if (result?.body() !=null){
            todoMutableLiveData.postValue(result.body())
            Log.d("TAG2", "getTodo: "+result.body().toString())
        }
    }


}