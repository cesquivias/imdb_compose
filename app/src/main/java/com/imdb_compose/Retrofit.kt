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

/**
 * API key
 * API read access token
 */

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
    // Movies
    @GET("3/trending/movie/week?language=en-US&api_key=${ BuildConfig.API_KEY }")
    suspend fun getMoviesOfWeekList(): MovieList

    @GET("3/trending/movie/day?language=en-US&api_key=${ BuildConfig.API_KEY }")
    suspend fun getTrendingMovies(): MovieList

    @GET("3/movie/upcoming?language=en-US&api_key=${ BuildConfig.API_KEY }")
    suspend fun getUpcomingMovies(): MovieList

    // Television
    @GET("3/trending/tv/day?language=en-US&api_key=${ BuildConfig.API_KEY }")
    suspend fun getTrendingTv(): TvList

    @GET("3/tv/airing_today?language=en-US&api_key=${ BuildConfig.API_KEY }")
    suspend fun getAiringTodayTv(): TvList

    // People
    @GET("3/person/popular?language=en-US&api_key=${ BuildConfig.API_KEY }")
    suspend fun getPopularPersons(): ActorList

    @GET("3/trending/person/day?language=en-US&api_key=${ BuildConfig.API_KEY }")
    suspend fun getTrendingPersons(): ActorList
}
