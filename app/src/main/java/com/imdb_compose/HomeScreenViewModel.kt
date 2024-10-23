package com.imdb_compose

import android.util.Log
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
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
    val movieApi = Retrofit.getInstance().create(MovieApi::class.java)
    val tvApi = Retrofit.getInstance().create(TvApi::class.java)
    val peopleApi = Retrofit.getInstance().create(PeopleApi::class.java)

    val catagories: List<String> = listOf(
        "Top box office",
        "Popular actors",
        "Trending tv",
        "Tv airing today",
        "Movies of the week",
        "Trending movies",
        "Upcoming movies",
    ).shuffled()

    val noMovies: MutableStateFlow<MovieList?> = MutableStateFlow(null)

    val boxOffice = listOf(
        mapOf("Average" to "$7,968", "Change" to "-403", "Distributor" to "Warner Bros.", "Gross" to "$33,244,516", "LW" to "1", "Rank" to "1", "Release" to "Beetlejuice Beetlejuice", "Response" to "False", "Theaters" to "4,172", "Total Gross" to "$234,092,898", "Weeks" to "3", "date range" to "September 16 - September 22"),
        mapOf("Average" to "$7,507", "Change" to "-", "Distributor" to "Paramount Pictures", "Gross" to "$29,864,458", "LW" to "-", "Rank" to "2", "Release" to "Transformers One", "Response" to "False", "Theaters" to "3,978", "Total Gross" to "$29,864,458", "Weeks" to "1", "date range" to "September 16 - September 22"),
        mapOf("Average" to "$2,454", "Change" to "-", "Distributor" to "Universal Pictures", "Gross" to "$8,285,530", "LW" to "2", "Rank" to "3", "Release" to "Speak No Evil", "Response" to "False", "Theaters" to "3,375", "Total Gross" to "$23,840,120", "Weeks" to "2", "date range" to "September 16 - September 22"),
        mapOf("Average" to "$2,269", "Change" to "-", "Distributor" to "Lionsgate Films", "Gross" to "$6,052,523", "LW" to "-", "Rank" to "4", "Release" to "Never Let Go", "Response" to "False", "Theaters" to "2,667", "Total Gross" to "$6,052,523", "Weeks" to "1", "date range" to "September 16 - September 22"),
        mapOf("Average" to "$2,128", "Change" to "-625", "Distributor" to "Walt Disney Studios Motion Pictures", "Gross" to "$5,215,484", "LW" to "3", "Rank" to "5", "Release" to "Deadpool & Wolverine", "Response" to "False", "Theaters" to "2,450", "Total Gross" to "$628,600,109", "Weeks" to "9", "date range" to "September 16 - September 22"),
        mapOf("Average" to "$2,595", "Change" to "-", "Distributor" to "MUBI", "Gross" to "$5,058,960", "LW" to "-", "Rank" to "6", "Release" to "The Substance", "Response" to "False", "Theaters" to "1,949", "Total Gross" to "$5,058,960", "Weeks" to "1", "date range" to "September 16 - September 22"),
        mapOf("Average" to "$2,181", "Change" to "+83", "Distributor" to "SDG Releasing", "Gross" to "$3,489,975", "LW" to "4", "Rank" to "7", "Release" to "Am I Racist?", "Response" to "False", "Theaters" to "1,600", "Total Gross" to "$9,961,482", "Weeks" to "2", "date range" to "September 16 - September 22"),
        mapOf("Average" to "$1,410", "Change" to "-600", "Distributor" to "-", "Gross" to "$2,609,564", "LW" to "5", "Rank" to "8", "Release" to "Reagan", "Response" to "False", "Theaters" to "1,850", "Total Gross" to "$27,470,165", "Weeks" to "4", "date range" to "September 16 - September 22"),
        mapOf("Average" to "$1,396", "Change" to "-600", "Distributor" to "Walt Disney Studios Motion Pictures", "Gross" to "$1,885,189", "LW" to "7", "Rank" to "9", "Release" to "Alien to Romulus", "Response" to "False", "Theaters" to "1,350", "Total Gross" to "$104,180,339", "Weeks" to "6", "date range" to "September 16 - September 22"),
        mapOf("Average" to "$1,511", "Change" to "-433", "Distributor" to "Affirm Films", "Gross" to "$1,785,380", "LW" to "9", "Rank" to "10", "Release" to "The Forge", "Response" to "False", "Theaters" to "1,181", "Total Gross" to "$26,856,127", "Weeks" to "5", "date range" to "September 16 - September 22")
    )

    // Movies
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

    private val _trendingMovies: MutableStateFlow<MovieList?> = MutableStateFlow(null)
    val trendingMovies: StateFlow<MovieList?> = _trendingMovies.asStateFlow()
    /**
     * {
     *       "backdrop_path": "/rbEsJL59iW5mr4s1YnLocEFdO3Q.jpg",
     *       "id": 748167,
     *       "title": "Uglies",
     *       "original_title": "Uglies",
     *       "overview": "In a futuristic dystopia with enforced beauty standards, a teen boy love to play with their friend mandatory cosmetic surgery embarks on a journey to find her missing friend.",
     *       "poster_path": "/og1SH6we0UQx5XNIDSfxDD4S1Sp.jpg",
     *       "media_type": "movie",
     *       "adult": false,
     *       "original_language": "en",
     *       "genre_ids": [
     *         878,
     *         18,
     *         12
     *       ],
     *       "popularity": 98.42,
     *       "release_date": "2024-09-12",
     *       "video": false,
     *       "vote_average": 6.429,
     *       "vote_count": 84
     * }
     */

    private val _upcomingMovies: MutableStateFlow<MovieList?> = MutableStateFlow(null)
    val upcomingMovies: StateFlow<MovieList?> = _upcomingMovies.asStateFlow()
    /**
     * {
     *       "adult": false,
     *       "backdrop_path": "/18wozP6NjPSNBSgCga5bN7yUSzl.jpg",
     *       "genre_ids": [
     *         35,
     *         14,
     *         27
     *       ],
     *       "id": 917496,
     *       "original_language": "en",
     *       "original_title": "Beetlejuice Beetlejuice",
     *       "overview": "After a family tragedy, three generations of the Deetz family return home to Winter River. Still haunted by Beetlejuice, Lydia's life is turned upside down when her teenage daughter, Astrid, accidentally opens the portal to the Afterlife.",
     *       "popularity": 1273.171,
     *       "poster_path": "/kKgQzkUCnQmeTPkyIwHly2t6ZFI.jpg",
     *       "release_date": "2024-09-04",
     *       "title": "Beetlejuice Beetlejuice",
     *       "video": false,
     *       "vote_average": 7.21,
     *       "vote_count": 403
     * }
     */

    // Television
    private val _trendingTv: MutableStateFlow<TvList?> = MutableStateFlow(null)
    val trendingTv: StateFlow<TvList?> = _trendingTv.asStateFlow()
    /**
     * {
     *       "backdrop_path": "/AvH03Lj5lMYxmlPc7prNQLWw6JY.jpg",
     *       "id": 67557,
     *       "name": "The Grand Tour",
     *       "original_name": "The Grand Tour",
     *       "overview": "Jeremy Clarkson, Richard Hammond and James May are back with a show about adventure, excitement and friendship... as long as you accept that the people you call friends are also the ones you find extremely annoying. Sometimes it's even a show about cars. Follow them on their global adventure.",
     *       "poster_path": "/3Pcqu6QliBWJ8vsOVClVLddPnZw.jpg",
     *       "media_type": "tv",
     *       "adult": false,
     *       "original_language": "en",
     *       "genre_ids": [
     *         99,
     *         35,
     *         10767
     *       ],
     *       "popularity": 119.506,
     *       "first_air_date": "2016-11-17",
     *       "vote_average": 8,
     *       "vote_count": 698,
     *       "origin_country": [
     *         "GB"
     *       ]
     * }
     */

    private val _airingTodayTv: MutableStateFlow<TvList?> = MutableStateFlow(null)
    val airingTodayTv: StateFlow<TvList?> = _airingTodayTv.asStateFlow()
    /**
     * {
     *       "adult": false,
     *       "backdrop_path": "/dasgPx3OgkxHSQyncKlApfZkpi2.jpg",
     *       "genre_ids": [
     *         10767,
     *         10763
     *       ],
     *       "id": 224,
     *       "origin_country": [
     *         "GB"
     *       ],
     *       "original_language": "en",
     *       "original_name": "Match of the Day",
     *       "overview": "BBC's football highlights and analysis.\n\n\"The longest-running football television programme in the world\" as recognised by Guinness World Records in 2015.",
     *       "popularity": 6896.13,
     *       "poster_path": "/aA25JrHXj8ZPTJYj2iSIueyb34C.jpg",
     *       "first_air_date": "1964-08-22",
     *       "name": "Match of the Day",
     *       "vote_average": 7.375,
     *       "vote_count": 40
     * }
     */

    // Actors && Actresses
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

    private val _trendingPersons: MutableStateFlow<ActorList?> = MutableStateFlow(null)
    val trendingPersons: StateFlow<ActorList?> = _trendingPersons.asStateFlow()
    /**
     * {
     *       "id": 15152,
     *       "name": "James Earl Jones",
     *       "original_name": "James Earl Jones",
     *       "media_type": "person",
     *       "adult": false,
     *       "popularity": 101.101,
     *       "gender": 2,
     *       "known_for_department": "Acting",
     *       "profile_path": "/sgc8yxYr8ecNn1TXjWXWx3wmUYA.jpg",
     *       "known_for": [
     *         {
     *           "backdrop_path": "/wXfAvsPZqBDJ8U78F2IAzsUxtNh.jpg",
     *           "id": 8587,
     *           "title": "The Lion King",
     *           "original_title": "The Lion King",
     *           "overview": "A young lion prince is cast out of his pride by his cruel uncle, who claims he killed his father. While the uncle rules with an iron paw, the prince grows up beyond the Savannah, living by a philosophy: No worries for the rest of your days. But when his past comes to haunt him, the young prince must decide his fate: Will he remain an outcast or face his demons and become what he needs to be?",
     *           "poster_path": "/sKCr78MXSLixwmZ8DyJLrpMsd15.jpg",
     *           "media_type": "movie",
     *           "adult": false,
     *           "original_language": "en",
     *           "genre_ids": [
     *             10751,
     *             16,
     *             18
     *           ],
     *           "popularity": 113.046,
     *           "release_date": "1994-06-24",
     *           "video": false,
     *           "vote_average": 8.257,
     *           "vote_count": 18062
     * }
     */

    private val _personDetails: MutableStateFlow<ActorDetail?> = MutableStateFlow(null)
    val personDetails: StateFlow<ActorDetail?> = _personDetails.asStateFlow()
    /**
     * {
     *   "adult": false,
     *   "also_known_as": [
     *     "James Earl Boggins Jones",
     *     "Todd Jones",
     *     "Jimmy"
     *   ],
     *   "biography": "James Earl Jones (January 17, 1931 – September 9, 2024) was an American actor. He was described as \"one of America's most distinguished and versatile\" actors for his performances on stage and screen, and \"one of the greatest actors in American history\". Over his career, he received three Tony Awards, two Emmy Awards, and a Grammy Award. He was inducted into the American Theater Hall of Fame in 1985. He was honored with the National Medal of Arts in 1992, the Kennedy Center Honor in 2002, the Screen Actors Guild Life Achievement Award in 2009 and the Honorary Academy Award in 2011. His deep voice has been praised as a \"stirring basso profondo that has lent gravel and gravitas\" to his projects.",
     *   "birthday": "1931-01-17",
     *   "deathday": "2024-09-09",
     *   "gender": 2,
     *   "homepage": null,
     *   "id": 15152,
     *   "imdb_id": "nm0000469",
     *   "known_for_department": "Acting",
     *   "name": "James Earl Jones",
     *   "place_of_birth": "Arkabutla, Mississippi, USA",
     *   "popularity": 161.979,
     *   "profile_path": "/sgc8yxYr8ecNn1TXjWXWx3wmUYA.jpg"
     * }
     */

    init {
        // Movies
        viewModelScope.launch {
            val result = movieApi.getMoviesOfWeekList()
            _movieListOfWeek.value = result
        }
        viewModelScope.launch {
            val result = movieApi.getTrendingMovies()
            _trendingMovies.value = result
        }
        viewModelScope.launch {
            val result = movieApi.getUpcomingMovies()
            _upcomingMovies.value = result
        }
        // Television
        viewModelScope.launch {
            val result = tvApi.getTrendingTv()
            _trendingTv.value = result
        }
        viewModelScope.launch {
            val result = tvApi.getAiringTodayTv()
            _airingTodayTv.value = result
        }
        // People
        viewModelScope.launch {
            val result = peopleApi.getPopularPersons()
            _popularPersons.value = result
        }
        viewModelScope.launch {
            val result = peopleApi.getTrendingPersons()
            _trendingPersons.value = result
        }
    }

    suspend fun getPersonDetails(id: Int) {
        viewModelScope.launch {
            val result = peopleApi.getPersonDetails(id)
            _personDetails.value = result
        }
    }
}

data class TvList(
    val results: List<TvResult>
)

data class TvResult(
    val id: Int,
    val name: String,
    val original_name: String,
    val overview: String,
    val poster_path: String,
    val backdrop_path: String,
    val popularity: String,
    val vote_average: String,
    val first_air_date: String
)

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

data class ActorDetail(
    val name: String,
    val id: Int,
    val biography: String,
    val birthday: String,
    val deathDay: String,
    val place_of_birth: String,
    val profile_path: String,
    val popularity: String,
    val known_for_department: String
)
