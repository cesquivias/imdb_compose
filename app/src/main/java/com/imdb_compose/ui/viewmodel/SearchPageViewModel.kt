package com.imdb_compose.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.imdb_compose.domain.Async
import com.imdb_compose.domain.MovieApi
import com.imdb_compose.domain.MultiSearch
import com.imdb_compose.domain.PeopleApi
import com.imdb_compose.domain.SearchApi
import com.imdb_compose.domain.TvApi
import androidx.compose.foundation.text.input.TextFieldState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchPageViewModel @Inject constructor(
    private val movieApi: MovieApi,
    private val tvApi: TvApi,
    private val peopleApi: PeopleApi,
    private val searchApi: SearchApi
): ViewModel() {
    private val _textFieldSearchState: MutableStateFlow<TextFieldState> = MutableStateFlow(TextFieldState(""))
    var textFieldSearchState: StateFlow<TextFieldState> = _textFieldSearchState.asStateFlow()

    private val _searchResult: MutableStateFlow<Async<MultiSearch>> = MutableStateFlow(Async.Init)
    val searchResult: StateFlow<Async<MultiSearch>> = _searchResult.asStateFlow()

    fun getSearchResults(query: String) {
        viewModelScope.launch {
            _searchResult.value = Async.Loading
            try {
                val result = searchApi.queryMultiSearch(query)
                _searchResult.value = Async.Success(result)
            } catch (e: Exception) {
                _searchResult.value = Async.Error(e)
            }
        }
    }

}
