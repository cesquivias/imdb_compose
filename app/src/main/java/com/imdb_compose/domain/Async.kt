package com.imdb_compose.domain

sealed class Async<out T>(
    val isDone: Boolean,
    open val data: T? = null,
) {
    object Init: Async<Nothing>(false)

    object Loading : Async<Nothing>(false)

    class Success<T>(
        override val data: T,
    ): Async<T>(true, data)

    class Error<T>(val t: Throwable): Async<T>(true)
}
