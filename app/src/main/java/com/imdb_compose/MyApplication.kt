package com.imdb_compose

import android.app.Application
import coil.ImageLoader
import coil.ImageLoaderFactory
import coil.disk.DiskCache
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.util.DebugLogger
import okhttp3.Call
import okhttp3.OkHttpClient

class MyApplication: Application(), ImageLoaderFactory {
    override fun newImageLoader(): ImageLoader {
        val okHttpClient = OkHttpClient.Builder().build()

        return ImageLoader(this).newBuilder()
            .memoryCachePolicy(CachePolicy.ENABLED)
            .memoryCache {
                MemoryCache.Builder(this)
                    .maxSizePercent(0.1)
                    .strongReferencesEnabled(true)
                    .build()
            }
            .diskCachePolicy(CachePolicy.ENABLED)
            .diskCache {
                DiskCache.Builder()
                    .maxSizePercent(.03)
                    .directory(cacheDir)
                    .build()
            }
            .callFactory {
                Call.Factory {
                    okHttpClient.newCall(
                        it.newBuilder().addHeader(
                            "Authorization",
                            "Bearer ${ BuildConfig.API_READ_ACCESS_TOKEN }")
                            .build()
                    )
                }
            }
            .logger(DebugLogger())
            .build()
    }
}
