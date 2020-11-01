package com.example.cakeapp.todo.data.remote

import retrofit2.http.*
import com.example.cakeapp.core.Api
import com.example.cakeapp.todo.data.Cake
import retrofit2.Response

object CakeApi {
    interface Service {
        @GET("/api/cake")
        suspend fun find(): List<Cake>

        @GET("/api/cake/{id}")
        suspend fun read(@Path("id") itemId: String): Cake;

        @Headers("Content-Type: application/json")
        @POST("/api/cake")
        suspend fun create(@Body item: Cake): Cake

        @Headers("Content-Type: application/json")
        @PUT("/api/cake/{id}")
        suspend fun update(@Path("id") itemId: String, @Body item: Cake): Cake

        @DELETE("/api/cake/{id}")
        suspend fun delete(@Path("id") itemId: String): Response<Unit>
    }

    val service: Service = Api.retrofit.create(Service::class.java)
}