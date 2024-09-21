package com.imdb_compose

import android.annotation.SuppressLint
import android.graphics.Paint.Align
import androidx.compose.foundation.Image
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.HorizontalUncontainedCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import coil.Coil
import coil.ImageLoader
import coil.compose.AsyncImage
import coil.compose.AsyncImagePainter
import coil.compose.rememberAsyncImagePainter
import coil.compose.rememberImagePainter
import coil.request.ImageRequest
import coil.size.Size
import coil.transform.CircleCropTransformation
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import org.jetbrains.annotations.Async

@Composable
fun CategoryPage(
    category: String,
    viewModel: HomeScreenViewModel,
    navController: NavController,
    clickHandlerBackBtn: () -> Unit
) {
    val catagoryPageViewModel = CatagoryPageViewModel(category)

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        topBar = {
            TopBarWithBackBtn(category, clickHandlerBackBtn)
        },
        bottomBar = {
            BottomBar(navController)
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues = paddingValues),
        ) {
            val movies = viewModel.movieListOfWeek.collectAsState()
            Row (
                modifier = Modifier
                    .height(420.dp)
                    .fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                TvCarousel(category, viewModel, catagoryPageViewModel)
            }
            Spacer(modifier = Modifier.height(8.dp))
//            Column (
//                modifier = Modifier.fillMaxWidth()
//            ) {
//                LazyColumn(
//                    modifier = Modifier
//                ) {
//                    movies.value?.results?.forEach { movie ->
//                        item {
//                            Row(
//                                modifier = Modifier
//                                    .height(100.dp)
//                                    .fillMaxWidth()
//                                    .border(width = 1.dp, color = Color.Black)
//                                    .clickable {
//                                        navController.navigate(
//                                            Navigator.MovieDetailsPage(
//                                                movie.title
//                                            )
//                                        )
//                                    },
//                                horizontalArrangement = Arrangement.Start,
//                            ) {
//                                Box(
//                                    modifier = Modifier.padding(start = 4.dp, end = 4.dp)
//                                ) {
//                                    Column (
//                                        modifier = Modifier.fillMaxHeight(),
//                                        verticalArrangement = Arrangement.SpaceBetween
//                                    ) {
//                                        Text(text = "Catagory Page")
//                                        Text(text = movie.title)
//                                        Text(text = movie.overview)
//                                    }
//                                }
//                            }
//                        }
//                    }
//                }
//            }
        }
    }
}

@SuppressLint("CoroutineCreationDuringComposition", "StateFlowValueCalledInComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TvCarousel(category: String, viewModel: HomeScreenViewModel, catagoryPageViewModel: CatagoryPageViewModel) {
    val tvShows = viewModel.trendingTv.collectAsState().value?.results?.toList()!!



    val carouselState = rememberCarouselState { 3 }
    Box(modifier = Modifier.fillMaxSize()) {
        HorizontalUncontainedCarousel(
            state = carouselState,
            itemWidth = 400.dp,
            itemSpacing = 8.dp
        ) {page ->
            Box(
                modifier = Modifier
                    .size(250.dp)
                    .border(width = 2.dp, color = MaterialTheme.colorScheme.outline)
            ) {
                Image(
                    painter =  when (page) {
                        0 -> painterResource(R.drawable.geodude_74)
                        1 -> painterResource(R.drawable.graveler_75)
                        else -> painterResource(R.drawable.golem_76)
                    },
                    contentDescription = "",
                    modifier = Modifier
                        .padding(all = 16.dp)
                        .size(200.dp)
                )
            }
        }
    }
}

