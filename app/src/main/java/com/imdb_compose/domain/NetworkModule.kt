package com.imdb_compose.domain

import com.imdb_compose.domain.Retrofit.BASE_URL
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    @Singleton
    @Provides
    fun retrofit(okHttpClient: OkHttpClient): retrofit2.Retrofit {
        return retrofit2.Retrofit.Builder().baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okHttpClient)
            .build()
    }

    @Singleton
    @Provides
    fun okHttp(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BODY)
        return OkHttpClient
            .Builder()
            .addInterceptor(interceptor)
            .build()
    }

    @Provides
    fun movieApi(retrofit: retrofit2.Retrofit): MovieApi = retrofit.create(MovieApi::class.java)

    @Provides
    fun tvApi(retrofit: retrofit2.Retrofit): TvApi = retrofit.create(TvApi::class.java)

    @Provides
    fun peopleApi(retrofit: retrofit2.Retrofit): PeopleApi = retrofit.create(PeopleApi::class.java)
}

