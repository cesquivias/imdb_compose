package com.imdb_compose.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imdb_compose.domain.Async
import com.imdb_compose.domain.Images
import com.imdb_compose.domain.MovieApi
import com.imdb_compose.domain.PeopleApi
import com.imdb_compose.domain.TvApi
import com.imdb_compose.domain.TvDetails
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class TvDetailsPageViewModel @Inject constructor(
    private val movieApi: MovieApi,
    private val tvApi: TvApi,
    private val peopleApi: PeopleApi
): ViewModel() {
    // Television
    private val _tvDetails: MutableStateFlow<Async<TvDetails>> = MutableStateFlow(Async.Init)
    val tvDetails: StateFlow<Async<TvDetails>> = _tvDetails.asStateFlow()

    private val _tvImages: MutableStateFlow<Async<Images>> = MutableStateFlow(Async.Init)
    val tvImages: StateFlow<Async<Images>> = _tvImages.asStateFlow()

    // Television
    fun getTvSeriesDetails(id: Int) {
        viewModelScope.launch {
            _tvDetails.value = Async.Loading
            try {
                val result = tvApi.getTvSeriesDetails(id)
                _tvDetails.value = Async.Success(result)
            } catch (e: Exception) {
                _tvDetails.value = Async.Error(e)
            }
        }
    }

    fun getTvSeriesImages(id: Int) {
        viewModelScope.launch {
            _tvImages.value = Async.Loading
            try {
                val result = tvApi.getTvSeriesImages(id)
                _tvImages.value = Async.Success(result)
            } catch (e: Exception) {
                _tvImages.value = Async.Error(e)
            }
        }
    }
}
