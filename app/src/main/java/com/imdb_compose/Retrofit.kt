package com.imdb_compose

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Header
import retrofit2.http.Headers


object Retrofit {
    private const val BASE_URL ="https://api.themoviedb.org/"

    fun getInstance(): Retrofit {
        val client = OkHttpClient()
        val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        val clientBuilder: OkHttpClient.Builder = client.newBuilder().addInterceptor(interceptor)

        return Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(clientBuilder.build())
            .build()
    }
}

interface MovieApi {
    @GET("3/trending/movie/week?language=en-US&api_key=")
    suspend fun getMoviesOfWeekList() : MovieList

    @GET("3/person/popular?language=en-US&api_key=")
    suspend fun getPopularPersons() : ActorList
}
