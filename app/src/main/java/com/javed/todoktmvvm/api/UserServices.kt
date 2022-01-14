package com.javed.todoktmvvm.api

import com.javed.todoktmvvm.modelClasses.TodoResponse
import com.javed.todoktmvvm.modelClasses.TodoResponseItem
import retrofit2.Response
import retrofit2.http.GET

interface UserServices {

    @GET("582695f5100000560464ca40")
    suspend fun todo(): Response<MutableList<TodoResponseItem>>
}