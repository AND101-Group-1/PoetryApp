package com.example.poe

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface PoetryApiService {
    // Example: https://poetrydb.org/author/Emily Dickinson
    @GET("author/{authorName}")
    fun getPoemsByAuthor(@Path("authorName") authorName: String): Call<List<Poem>>

    // Example: https://poetrydb.org/title/Ozymandias
    @GET("title/{titleName}")
    fun getPoemsByTitle(@Path("titleName") titleName: String): Call<List<Poem>>
    @GET("random/20")
    fun getRandomPoems(): Call<List<Poem>>

}
