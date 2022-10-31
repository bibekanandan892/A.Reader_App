package com.isu.jetareader.network

import com.isu.jetareader.model.Book
import com.isu.jetareader.model.Item
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import javax.inject.Singleton


@Singleton
interface BookApi {
    @GET("volumes")
    suspend fun getAllBooks(@Query("q") query: String): Book

    @GET("volumes/{bookId}")
    suspend fun getBookInfo(@Path("bookId") bookId: String) : Item
}