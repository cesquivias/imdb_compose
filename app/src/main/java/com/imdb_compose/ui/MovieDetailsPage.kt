package com.imdb_compose.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.imdb_compose.BottomBar
import com.imdb_compose.Pager
import com.imdb_compose.domain.Retrofit
import com.imdb_compose.Tags
import com.imdb_compose.TopBarWithBackBtn
import com.imdb_compose.isLoading
import com.imdb_compose.ripeMango
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
            viewModel.getMovieImages(id)
        }

        if (viewModel.movieDetails.value?.id == id && viewModel.movieImages.value?.id == id) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues = paddingValues),
                contentAlignment = Alignment.TopStart
            ) {
                val movieDetails = viewModel.movieDetails.collectAsState()
                val movieImages = viewModel.movieImages.collectAsState()

                movieDetails.value?.let { details ->
                    Column {
                        LazyColumn {
                            item {
                                // Title
                                Text(
                                    modifier = Modifier.padding(start = 6.dp),
                                    text = details.title,
                                    lineHeight = 50.sp,
                                    fontSize = MaterialTheme.typography.displayMedium.fontSize,
                                    fontWeight = MaterialTheme.typography.displayMedium.fontWeight,
                                    fontStyle = MaterialTheme.typography.displayMedium.fontStyle,
                                    fontFamily = MaterialTheme.typography.displayMedium.fontFamily
                                )
                                // Year Rating Duration
                                Row (
                                    modifier = Modifier
                                        .fillMaxWidth(0.4f)
                                        .padding(start = 16.dp, bottom = 8.dp),
                                    horizontalArrangement = Arrangement.SpaceBetween
                                ) {
                                    Text(details.release_date.substring(0, 4))
                                    Text("${ if (details.adult) "R" else "PG-13" }")
                                    Text("${ details.runtime / 60 }h ${ details.runtime % 60 }m")
                                }

                                Pager(images = movieImages)
                            }
                            // Image & description
                            item {
                                Row (modifier = Modifier.padding(start = 8.dp)) {
                                    AsyncImage(
                                        modifier = Modifier.width(170.dp),
                                        model = "${Retrofit.BASE_IMAGE_URL}${Retrofit.IMAGE_PATH}${ details.poster_path }",
                                        contentDescription = ""
                                    )
                                    Text(
                                        modifier = Modifier.padding(horizontal = 8.dp),
                                        text = details.overview,
                                        overflow = TextOverflow.Ellipsis,
                                        maxLines = 10
                                    )
                                }
                            }
                            // tags
                            item {
                                LazyRow {
                                    details.genres.forEachIndexed { i, genre ->
                                        item {
                                            Box(
                                                modifier = Modifier
                                                    .padding(start = 16.dp)
                                                    .padding(vertical = 8.dp)
                                            ) {
                                                Tags(txt = genre.name)
                                                Spacer(modifier = Modifier.width(8.dp))
                                            }
                                        }
                                    }
                                }
                            }
                            // Add to watchlist
                            item {
                                Button(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .height(40.dp)
                                        .padding(horizontal = 8.dp),
                                    shape = RoundedCornerShape(4.dp),
                                    onClick = { /*TODO*/ },
                                    colors = ButtonColors(
                                        containerColor = ripeMango,
                                        contentColor = Color.White,
                                        disabledContainerColor = Color.Red,
                                        disabledContentColor = Color.White,
                                    )
                                ) {
                                    Icon(imageVector = Icons.Outlined.Add, contentDescription = "", tint = Color.Black)
                                    Text(text = "  Add to Watchlist", color = Color.Black)
                                }
                            }
                        }
                    }
                }
            }
        } else {
            isLoading()
            Text(text = "Loading...")
        }
    }
}

