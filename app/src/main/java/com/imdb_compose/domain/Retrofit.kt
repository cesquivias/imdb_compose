package com.imdb_compose.domain

import com.imdb_compose.BuildConfig
import com.imdb_compose.domain.Retrofit.AIRING_TODAY_TV_PATH
import com.imdb_compose.domain.Retrofit.MOVIES_OF_WEEK_PATH
import com.imdb_compose.domain.Retrofit.POPULAR_PERSONS_PATH
import com.imdb_compose.domain.Retrofit.TRENDING_MOVIES_DAY_PATH
import com.imdb_compose.domain.Retrofit.TRENDING_PERSONS_DAY_PATH
import com.imdb_compose.domain.Retrofit.TRENDING_TV_DAY_PATH
import com.imdb_compose.domain.Retrofit.UPCOMING_MOVIE_PATH
import com.imdb_compose.ui.ActorDetail
import com.imdb_compose.ui.ActorList
import com.imdb_compose.ui.ImageResults
import com.imdb_compose.ui.Images
import com.imdb_compose.ui.MovieDetail
import com.imdb_compose.ui.MovieList
import com.imdb_compose.ui.TvDetails
import com.imdb_compose.ui.TvList
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
    @GET("${ MOVIES_OF_WEEK_PATH }?language=en-US&api_key=${BuildConfig.API_KEY}")
    suspend fun getMoviesOfWeekList(): MovieList

    @GET("${ TRENDING_MOVIES_DAY_PATH }?language=en-US&api_key=${BuildConfig.API_KEY}")
    suspend fun getTrendingMovies(): MovieList

    @GET("${ UPCOMING_MOVIE_PATH }?language=en-US&api_key=${BuildConfig.API_KEY}")
    suspend fun getUpcomingMovies(): MovieList

    @GET("3/movie/{id}?language=en-US&api_key=${BuildConfig.API_KEY}")
    suspend fun getMovieDetails(@Path("id") id: Int): MovieDetail

    @GET("3/movie/{id}/images?api_key=${BuildConfig.API_KEY}")
    suspend fun getMovieImages(@Path("id") id: Int): Images
}

interface TvApi {
    @GET("${ TRENDING_TV_DAY_PATH }?language=en-US&api_key=${BuildConfig.API_KEY}")
    suspend fun getTrendingTv(): TvList

    @GET("${ AIRING_TODAY_TV_PATH }?language=en-US&api_key=${BuildConfig.API_KEY}")
    suspend fun getAiringTodayTv(): TvList

    @GET("3/tv/{id}/images/{img_path}?api_key=${BuildConfig.API_KEY}")
    suspend fun getTvImg(@Path("id") id: Int, @Path("img_path") imgPath: String): ImageResults

    @GET("/3/tv/{id}?api_key=${BuildConfig.API_KEY}")
    suspend fun getTvSeriesDetails(@Path("id") id: Int): TvDetails

    @GET("/3/tv/{id}/images?api_key=${BuildConfig.API_KEY}")
    suspend fun getTvSeriesImages(@Path("id") id: Int): Images
}

interface PeopleApi {
    @GET("${ POPULAR_PERSONS_PATH }?language=en-US&api_key=${BuildConfig.API_KEY}")
    suspend fun getPopularPersons(): ActorList

    @GET("${ TRENDING_PERSONS_DAY_PATH }?language=en-US&append_to_response=details&api_key=${BuildConfig.API_KEY}")
    suspend fun getTrendingPersons(): ActorList

    @GET("3/person/{id}?language=en-US&api_key=${BuildConfig.API_KEY}")
    suspend fun getPersonDetails(@Path("id") id: Int): ActorDetail
}
