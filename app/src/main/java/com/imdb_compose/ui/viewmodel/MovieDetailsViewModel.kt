package com.imdb_compose.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imdb_compose.domain.Async
import com.imdb_compose.domain.Images
import com.imdb_compose.domain.MovieApi
import com.imdb_compose.domain.MovieDetail
import com.imdb_compose.domain.PeopleApi
import com.imdb_compose.domain.TvApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieDetailsPageViewModel @Inject constructor(
    private val movieApi: MovieApi,
    private val tvApi: TvApi,
    private val peopleApi: PeopleApi
): ViewModel() {

    private val _movieDetails: MutableStateFlow<Async<MovieDetail>> = MutableStateFlow(Async.Init)
    val movieDetails: StateFlow<Async<MovieDetail>> = _movieDetails.asStateFlow()

    private val _movieImages: MutableStateFlow<Async<Images>> = MutableStateFlow(Async.Init)
    val movieImages: StateFlow<Async<Images>> = _movieImages.asStateFlow()

    fun getMovieDetails(id: Int) {
        viewModelScope.launch {
            _movieDetails.value = Async.Init
            try {
                val result = movieApi.getMovieDetails(id)
                _movieDetails.value = Async.Success(result)
            } catch (e: Exception) {
                _movieDetails.value = Async.Error(e)
            }
        }
    }

    fun getMovieImages(id: Int) {
        viewModelScope.launch {
            _movieImages.value = Async.Loading
            try {
                val result = movieApi.getMovieImages(id)
                _movieImages.value = Async.Success(result)
            } catch (e: Exception) {
                _movieImages.value = Async.Error(e)
            }
        }
    }
}
