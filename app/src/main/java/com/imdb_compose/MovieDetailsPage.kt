package com.imdb_compose

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import kotlinx.coroutines.launch


@SuppressLint("CoroutineCreationDuringComposition", "StateFlowValueCalledInComposition")
@Composable
fun MovieDetailsPage(
    title: String,
    id: Int,
    viewModel: HomeScreenViewModel,
    navController: NavController,
    clickHandlerBackBtn: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Transparent,
        topBar = {
            TopBarWithBackBtn(title, clickHandlerBackBtn)
        },
        bottomBar = {
            BottomBar(navController)
        }
    ) { paddingValues ->

        viewModel.viewModelScope.launch {
            viewModel.getMovieDetails(id)
        }

        if (viewModel.movieDetails.value?.id == id) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues = paddingValues),
                contentAlignment = Alignment.Center
            ) {
                val movieDetails = viewModel.movieDetails.collectAsState()

                movieDetails.value?.let { details ->
                    Column {
                        Text("${details.id}")
                        Text(details.imdb_id)
                        Text(details.title)
                        Text(details.original_title)
                        Text("${details.adult}")
                        Text(details.backdrop_path)
//                        Text(details.genres)
//                        Text(details.belongs_to_collection)
                        Text("${details.budget}")
                        Text(details.overview)
                        Text(details.popularity)
                        Text(details.poster_path)
                        Text(details.release_date)
                        Text("${ details.revenue }")
                        Text("${ details.runtime }")
                        Text(details.status)
                        Text(details.tagline)
                        Text(details.vote_average)
                        Text("${ details.vote_count }")
                    }
                }
            }
        } else {
            isLoading()
            Text(text = "Loading...")
        }
    }
}
