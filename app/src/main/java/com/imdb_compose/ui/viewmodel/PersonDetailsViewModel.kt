package com.imdb_compose.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imdb_compose.domain.ActorDetail
import com.imdb_compose.domain.Async
import com.imdb_compose.domain.MovieApi
import com.imdb_compose.domain.PeopleApi
import com.imdb_compose.domain.TvApi
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PersonDetailsPageViewModel @Inject constructor(
    private val movieApi: MovieApi,
    private val tvApi: TvApi,
    private val peopleApi: PeopleApi
): ViewModel() {

    private val _personDetails: MutableStateFlow<Async<ActorDetail>> = MutableStateFlow(Async.Init)
    val personDetails: StateFlow<Async<ActorDetail>> = _personDetails.asStateFlow()

    fun getPersonDetails(id: Int) {
        viewModelScope.launch {
            _personDetails.value = Async.Loading
            try {
                val result = peopleApi.getPersonDetails(id)
                _personDetails.value = Async.Success(result)
            } catch (e: Exception) {
                _personDetails.value = Async.Error(e)
            }
        }
    }
}
