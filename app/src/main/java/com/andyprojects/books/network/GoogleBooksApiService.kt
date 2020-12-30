package com.andyprojects.books.network

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import kotlinx.coroutines.Deferred
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

private const val BASE_URL = "https://www.googleapis.com/books/v1/"
private const val API_KEY = "AIzaSyCuNLEGUZyg-XiMiVeB72qsQP1ax82wvso/"
private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .addCallAdapterFactory(CoroutineCallAdapterFactory())
    .baseUrl(BASE_URL)
    .build()

interface GoogleBooksApiService {
    @GET("volumes")
    fun getBooksAsync(@Query("q") keyword: String, @Query("maxResults") max: Int = 40): Deferred<Library>
}

object BooksAPi {
    val retrofitService: GoogleBooksApiService by lazy {
        retrofit.create(GoogleBooksApiService::class.java)
    }
}