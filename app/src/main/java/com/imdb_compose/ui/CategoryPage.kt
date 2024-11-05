package com.imdb_compose.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.imdb_compose.BottomBar
import com.imdb_compose.TopBarWithBackBtn

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
//                TvCarousel(category, viewModel, catagoryPageViewModel)
                Text("TODO")
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

//@SuppressLint("CoroutineCreationDuringComposition", "StateFlowValueCalledInComposition")
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun TvCarousel(category: String, viewModel: HomeScreenViewModel, catagoryPageViewModel: CatagoryPageViewModel) {
//    val tvShows = viewModel.trendingTv.collectAsState().value?.results?.toList()!!
//    val carouselState = rememberCarouselState { 3 }
//    Box(modifier = Modifier.fillMaxSize()) {
//        HorizontalUncontainedCarousel(
//            state = carouselState,
//            itemWidth = 400.dp,
//            itemSpacing = 8.dp
//        ) {page ->
//            Box(
//                modifier = Modifier
//                    .size(250.dp)
//                    .border(width = 2.dp, color = MaterialTheme.colorScheme.outline)
//            ) {
//
//            }
//        }
//    }
//}

