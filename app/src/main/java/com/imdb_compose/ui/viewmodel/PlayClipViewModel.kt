package com.imdb_compose.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imdb_compose.domain.Async
import com.imdb_compose.domain.MovieApi
import com.imdb_compose.domain.PeopleApi
import com.imdb_compose.domain.SearchApi
import com.imdb_compose.domain.TvApi
import com.imdb_compose.domain.VideoClipApi
import com.imdb_compose.domain.VideoClipList
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayClipViewModel @Inject constructor(
    private val movieApi: MovieApi,
    private val tvApi: TvApi,
    private val peopleApi: PeopleApi,
    private val searchApi: SearchApi,
    private val videoClipApi: VideoClipApi
): ViewModel() {

    private val _movieClip: MutableStateFlow<Async<VideoClipList>> = MutableStateFlow(Async.Init)
    val movieClip: StateFlow<Async<VideoClipList>> = _movieClip.asStateFlow()

    fun getMovieClip(id: Int) {
        _movieClip.value = Async.Loading
        viewModelScope.launch {
            try {
                // 558449
                val result = videoClipApi.getMovieClip(id)
                _movieClip.value = Async.Success(result)
            } catch (e: Exception) {
                _movieClip.value = Async.Error(e)
            }
        }
    }

}
