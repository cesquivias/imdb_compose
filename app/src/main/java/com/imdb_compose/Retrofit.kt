package com.imdb_compose

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.http.GET
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.Path

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
    @GET("3/trending/movie/week?language=en-US&api_key=${ BuildConfig.API_KEY }")
    suspend fun getMoviesOfWeekList(): MovieList

    @GET("3/trending/movie/day?language=en-US&api_key=${ BuildConfig.API_KEY }")
    suspend fun getTrendingMovies(): MovieList

    @GET("3/movie/upcoming?language=en-US&api_key=${ BuildConfig.API_KEY }")
    suspend fun getUpcomingMovies(): MovieList
}

interface TvApi {
    @GET("3/trending/tv/day?language=en-US&api_key=${ BuildConfig.API_KEY }")
    suspend fun getTrendingTv(): TvList

    @GET("3/tv/airing_today?language=en-US&api_key=${ BuildConfig.API_KEY }")
    suspend fun getAiringTodayTv(): TvList

    @GET("3/tv/{series_id}/images/{img_path}")
    suspend fun getTvImg(@Path("series_id") id: Int, @Path("img_path") imgPath: String): ImageResults

}

interface PeopleApi {
    @GET("3/person/popular?language=en-US&api_key=${ BuildConfig.API_KEY }")
    suspend fun getPopularPersons(): ActorList

    @GET("3/trending/person/day?language=en-US&append_to_response=details&api_key=${ BuildConfig.API_KEY }")
    suspend fun getTrendingPersons(): ActorList

    @GET("3/person/{id}?language=en-US&api_key=${ BuildConfig.API_KEY }")
    suspend fun getPersonDetails(@Path("id") id: Int): ActorDetail
}
