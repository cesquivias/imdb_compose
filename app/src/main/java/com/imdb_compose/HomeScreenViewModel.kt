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
    val catagories: MutableList<String> = mutableListOf("Movies of the week", "Popular actors")
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

    private val _popularPersons: MutableStateFlow<ActorList?> = MutableStateFlow(null)
    val popularPersons: StateFlow<ActorList?> = _popularPersons.asStateFlow()
    /**
     * {
     *   "page": 1,
     *   "results": [
     *     {
     *       "adult": false,
     *       "gender": 2,
     *       "id": 64,
     *       "known_for_department": "Acting",
     *       "name": "Gary Oldman",
     *       "original_name": "Gary Oldman",
     *       "popularity": 343.021,
     *       "profile_path": "/2v9FVVBUrrkW2m3QOcYkuhq9A6o.jpg",
     *       "known_for": [
     *         {
     *           "backdrop_path": "/nMKdUUepR0i5zn0y1T4CsSB5chy.jpg",
     *           "id": 155,
     *           "title": "The Dark Knight",
     *           "original_title": "The Dark Knight",
     *           "overview": "Batman raises the stakes in his war on crime. With the help of Lt. Jim Gordon and District Attorney Harvey Dent, Batman sets out to dismantle the remaining criminal organizations that plague the streets. The partnership proves to be effective, but they soon find themselves prey to a reign of chaos unleashed by a rising criminal mastermind known to the terrified citizens of Gotham as the Joker.",
     *           "poster_path": "/qJ2tW6WMUDux911r6m7haRef0WH.jpg",
     *           "media_type": "movie",
     *           "adult": false,
     *           "original_language": "en",
     *           "genre_ids": [
     *             18,
     *             28,
     *             80,
     *             53
     *           ],
     *           "popularity": 167.257,
     *           "release_date": "2008-07-16",
     *           "video": false,
     *           "vote_average": 8.516,
     *           "vote_count": 32523
     *         }
     *     ]
     * }
     */

    init {
        viewModelScope.launch {
            val result = movieApi.getMoviesOfWeekList()
            _movieListOfWeek.value = result
        }
        viewModelScope.launch {
            val result = movieApi.getPopularPersons()
            _popularPersons.value = result
        }
    }
}

data class MovieList(
    val results: List<MovieResult>
)

data class MovieResult(
    val id: Int,
    val title: String,
    val original_title: String,
    val overview: String,
    val backdrop_path: String,
    val poster_path: String,
    val popularity: String,
    val vote_average: String,
    val release_date: String
)

data class ActorList(
    val results: List<ActorResult>
)

data class ActorResult(
    val id: Int,
    val name: String,
    val original_name: String,
    val known_for_department: String,
    val profile_path: String,
    val popularity: String,
    val vote_average: String,
    val known_for: List<MovieResult>
)
