package com.javed.todoktmvvm

import android.app.Application
import com.javed.todoktmvvm.api.RetrofitHelper
import com.javed.todoktmvvm.api.UserServices
import com.javed.todoktmvvm.repositories.TodoRepository

class TodoApplication : Application() {

    lateinit var todoRepository: TodoRepository

    override fun onCreate() {
        super.onCreate()
        initialize()
    }

    private fun initialize() {
        val todoService = RetrofitHelper.getInstance().create(UserServices::class.java)
        todoRepository = TodoRepository(todoService, applicationContext)
    }
}
