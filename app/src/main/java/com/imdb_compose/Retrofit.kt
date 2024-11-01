package com.imdb_compose

import com.imdb_compose.Retrofit.AIRING_TODAY_TV_PATH
import com.imdb_compose.Retrofit.MOVIES_OF_WEEK_PATH
import com.imdb_compose.Retrofit.POPULAR_PERSONS_PATH
import com.imdb_compose.Retrofit.TRENDING_MOVIES_DAY_PATH
import com.imdb_compose.Retrofit.TRENDING_PERSONS_DAY_PATH
import com.imdb_compose.Retrofit.TRENDING_TV_DAY_PATH
import com.imdb_compose.Retrofit.UPCOMING_MOVIE_PATH
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
    const val BASE_URL = "https://api.themoviedb.org/"
    const val BASE_IMAGE_URL = "https://image.tmdb.org/"

    const val UPCOMING_MOVIE_PATH = "3/movie/upcoming"
    const val MOVIES_OF_WEEK_PATH = "3/trending/movie/week"
    const val TRENDING_MOVIES_DAY_PATH = "3/trending/movie/day"

    const val TRENDING_TV_DAY_PATH = "3/trending/tv/day"
    const val AIRING_TODAY_TV_PATH = "3/tv/airing_today"

    const val POPULAR_PERSONS_PATH = "3/person/popular"
    const val TRENDING_PERSONS_DAY_PATH = "3/trending/person/day"
    const val TRENDING_PERSONS_WEEK_PATH = "3/trending/person/week"

    const val IMAGE_PATH =  "t/p/original/"
    const val IMAGE_PATH_w500 = "t/p/w500/"

    fun getMovieImgUrl(id: Int): String {
        return "3/movie/${id}/images"
    }

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
    @GET("${ MOVIES_OF_WEEK_PATH }?language=en-US&api_key=${ BuildConfig.API_KEY }")
    suspend fun getMoviesOfWeekList(): MovieList

    @GET("${ TRENDING_MOVIES_DAY_PATH }?language=en-US&api_key=${ BuildConfig.API_KEY }")
    suspend fun getTrendingMovies(): MovieList

    @GET("${ UPCOMING_MOVIE_PATH }?language=en-US&api_key=${ BuildConfig.API_KEY }")
    suspend fun getUpcomingMovies(): MovieList

    @GET("3/movie/{id}?language=en-US&api_key=${ BuildConfig.API_KEY }")
    suspend fun getMovieDetails(@Path("id") id: Int): MovieDetail
}

interface TvApi {
    @GET("${ TRENDING_TV_DAY_PATH }?language=en-US&api_key=${ BuildConfig.API_KEY }")
    suspend fun getTrendingTv(): TvList

    @GET("${ AIRING_TODAY_TV_PATH }?language=en-US&api_key=${ BuildConfig.API_KEY }")
    suspend fun getAiringTodayTv(): TvList

    @GET("3/tv/{series_id}/images/{img_path}")
    suspend fun getTvImg(@Path("series_id") id: Int, @Path("img_path") imgPath: String): ImageResults
}

interface PeopleApi {
    @GET("${ POPULAR_PERSONS_PATH }?language=en-US&api_key=${ BuildConfig.API_KEY }")
    suspend fun getPopularPersons(): ActorList

    @GET("${ TRENDING_PERSONS_DAY_PATH }?language=en-US&append_to_response=details&api_key=${ BuildConfig.API_KEY }")
    suspend fun getTrendingPersons(): ActorList

    @GET("3/person/{id}?language=en-US&api_key=${ BuildConfig.API_KEY }")
    suspend fun getPersonDetails(@Path("id") id: Int): ActorDetail
}
