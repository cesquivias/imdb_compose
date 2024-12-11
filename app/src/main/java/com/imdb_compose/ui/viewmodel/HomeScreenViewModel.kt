package com.imdb_compose.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imdb_compose.domain.MovieApi
import com.imdb_compose.domain.PeopleApi
import com.imdb_compose.domain.TvApi
import com.imdb_compose.domain.ActorList
import com.imdb_compose.domain.MovieList
import com.imdb_compose.domain.TvList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class HomeScreenViewModel @Inject constructor(
    private val movieApi: MovieApi,
    private val tvApi: TvApi,
    private val peopleApi: PeopleApi
) : ViewModel() {

    val catagories: List<String> = listOf(
        "Popular actors",
        "Tv airing today",
        "Upcoming movies",
        "Movies of the week",
        "Trending movies",
        "Trending tv",
        "Trending people",
        "Top box office",
    )

    private val _noMovies: MutableStateFlow<MovieList?> = MutableStateFlow(null)
    val noMovies: StateFlow<MovieList?> = _noMovies.asStateFlow()

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

    private val _trendingMovies: MutableStateFlow<MovieList?> = MutableStateFlow(null)
    val trendingMovies: StateFlow<MovieList?> = _trendingMovies.asStateFlow()

    private val _upcomingMovies: MutableStateFlow<MovieList?> = MutableStateFlow(null)
    val upcomingMovies: StateFlow<MovieList?> = _upcomingMovies.asStateFlow()

    // Television
    private val _trendingTv: MutableStateFlow<TvList?> = MutableStateFlow(null)
    val trendingTv: StateFlow<TvList?> = _trendingTv.asStateFlow()

    private val _airingTodayTv: MutableStateFlow<TvList?> = MutableStateFlow(null)
    val airingTodayTv: StateFlow<TvList?> = _airingTodayTv.asStateFlow()

    // Actors && Actresses
    private val _popularPersons: MutableStateFlow<ActorList?> = MutableStateFlow(null)
    val popularPersons: StateFlow<ActorList?> = _popularPersons.asStateFlow()

    private val _trendingPersons: MutableStateFlow<ActorList?> = MutableStateFlow(null)
    val trendingPersons: StateFlow<ActorList?> = _trendingPersons.asStateFlow()


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
}
