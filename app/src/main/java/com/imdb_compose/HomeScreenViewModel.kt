package com.imdb_compose

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import retrofit2.Response

/**
 * people
 *  popular
 */

/**
 * movie lists
 *  now playing
 *  popular
 *  top rated
 *  upcoming
 */

/**
 * trending
 *  all
 *  movies
 *  tv shows
 *  peopele
 */

/**
 * tv series lists
 *  airing today
 *  on the air
 *  popular
 *  top rated
 */

class HomeScreenViewModel() : ViewModel() {
    private val movieApi = Retrofit.getInstance().create(MovieApi::class.java)
    val catagories: MutableList<String> = mutableListOf("Movies of the week")
    val noMovies: MutableStateFlow<MovieList?> = MutableStateFlow(null)

    private val _movieListOfWeek: MutableStateFlow<MovieList?> = MutableStateFlow(null)
    val movieListOfWeek: StateFlow<MovieList?> = _movieListOfWeek.asStateFlow()
    /**
     * {
     *   "page": 1,
     *   "results": [
     *     {
     *       "backdrop_path": "/p5kpFS0P3lIwzwzHBOULQovNWyj.jpg",
     *       "id": 1032823,
     *       "title": "Trap",
     *       "original_title": "Trap",
     *       "overview": "A father and teen daughter attend a pop concert, where they realize they're at the center of a dark and sinister event.",
     *       "poster_path": "/jwoaKYVqPgYemFpaANL941EF94R.jpg",
     *       "media_type": "movie",
     *       "adult": false,
     *       "original_language": "en",
     *       "genre_ids": [
     *         80,
     *         53
     *       ],
     *       "popularity": 790.927,
     *       "release_date": "2024-07-31",
     *       "video": false,
     *       "vote_average": 6.531,
     *       "vote_count": 973
     *     }
     *   ],
     *   "total_pages": 1000,
     *   "total_results": 20000
     * }
     */

    init {
        viewModelScope.launch {
            val result = movieApi.getMoviesOfWeekList()
            _movieListOfWeek.value = result
        }
    }
}

data class MovieList(
    val results: List<Result>
)

data class Result(
    val id: String,
    val title: String,
    val overview: String,
    val backdrop_path: String,
    val poster_path: String,
    val vote_average: String
)
