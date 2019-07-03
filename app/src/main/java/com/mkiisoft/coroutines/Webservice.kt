package com.mkiisoft.coroutines

import androidx.annotation.WorkerThread
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Path

interface Webservice {

    @GET("/todos/{id}")
    suspend fun getTodo(@Path(value = "id") todoId: Int): Todo

    @WorkerThread
    @GET("comments")
    suspend fun getComments(): List<Comment>

    @GET("comments")
    fun getCommentsRx(): Single<List<Comment>>

    @GET("E1mjra7eD")
    suspend fun getAlbums(): Response<List<Album>>

    @WorkerThread
    @GET("E1mjra7eD")
    suspend fun getAlbumsAnnonation(): List<Album>

    @GET("E1mjra7eD")
    fun getAlbumsRx(): Single<List<Album>>
}