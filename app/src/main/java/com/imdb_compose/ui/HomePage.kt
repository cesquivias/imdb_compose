@file:OptIn(ExperimentalMaterial3Api::class)
package com.imdb_compose.ui

import android.annotation.SuppressLint
import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.BookOnline
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.compose.blue400
import com.example.compose.gray200
import com.example.compose.gray400
import com.example.compose.ripeMango
import com.imdb_compose.ImageAsync
import com.imdb_compose.InformationCircle
import com.imdb_compose.R
import com.imdb_compose.Ribbon
import com.imdb_compose.domain.ActorList
import com.imdb_compose.domain.MovieList
import com.imdb_compose.domain.TvList
import java.text.SimpleDateFormat
import java.util.Date

@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun HomePage(
    catagories: List<String>,
    noMovies: State<MovieList?>,
    movieListOfWeek: State<MovieList?>,
    trendingMovies: State<MovieList?>,
    airingTodayTv: State<TvList?>,
    trendingTv: State<TvList?>,
    upcomingMovies: State<MovieList?>,
    popularPersons: State<ActorList?>,
    trendingPersons: State<ActorList?>,
    boxOffice: List<Map<String, String>>,
    navController: NavController
) {
    LazyColumn {
        item {
            catagories.forEachIndexed { i, catagory ->
                Row (
                    modifier = Modifier
                        .fillMaxWidth(fraction = 0.95f)
                        .fillMaxHeight(fraction = 0.95f)
                        .background(color = gray400)
                        .padding(bottom = 16.dp)
                ) {
                    Column (modifier = Modifier.padding(start = 8.dp, top = 8.dp)) {
                        Row (
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            // Orange Accent <Catagory>
                            Row (verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .padding(start = 8.dp)
                                        .clip(RoundedCornerShape(24.dp))
                                        .height(36.dp)
                                        .width(6.dp)
                                        .background(ripeMango)
                                )
                                Text(
                                    text = catagory,
                                    modifier = Modifier.padding(start = 8.dp),
                                    color = MaterialTheme.colorScheme.onSecondary,
                                    style = MaterialTheme.typography.headlineMedium
                                )
                            }
                            // See All Btn
                            Row (verticalAlignment = Alignment.CenterVertically) {
                                TextButton(
                                    modifier = Modifier.padding(start = 8.dp),
                                    onClick = { navController.navigate(Destination.CategoryPage(catagory = catagory)) }
                                ) {
                                    Text(
                                        text = stringResource(R.string.see_all),
                                        modifier = Modifier.padding(start = 8.dp),
                                        color = blue400,
                                        fontStyle = MaterialTheme.typography.headlineLarge.fontStyle,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        when(catagory) {
                            "Popular actors" -> PersonBox(catagory, popularPersons, navController)
                            "Trending movies" -> MovieBox(catagory, trendingMovies, navController)
                            "Movies of the week" -> MovieBox(catagory, movieListOfWeek, navController)
                            "Upcoming movies" -> UpcommingBox(catagory, upcomingMovies, navController)
                            "Tv airing today" -> TvBox(catagory, airingTodayTv, navController)
                            "Trending tv" -> TvBox(catagory, trendingTv, navController)
                            "Trending people" -> PersonBox(catagory, trendingPersons, navController)
                            "Top box office" -> BoxOfficeBox(catagory, boxOffice, navController)
                            else -> MovieBox(catagory, noMovies, navController)
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun MovieBox(
    catagory: String,
    movies: State<MovieList?>,
    navController: NavController
) {
    Box(modifier = Modifier.padding(start = 8.dp)) {
        LazyRow {
            movies.value?.results?.filter {
                it.poster_path != null
            }?.forEachIndexed { i, movie ->
                item {
                    BoxA(
                        catagory = catagory,
                        id = movie.id,
                        name = movie.title,
                        posterPath = movie.poster_path,
                        details = movie.overview,
                        score = movie.vote_average,
                        onClick = { id ->
                            navController.navigate(
                                Destination.MovieDetailsPage(
                                    title = movie.title,
                                    id = id,
                                    catagory = catagory
                                )
                            )
                        },
                        rank = "${ i + 1 }"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }
    }
}

@Composable
fun TvBox(
    catagory: String,
    tvShows: State<TvList?>,
    navController: NavController
) {
    Box(modifier = Modifier.padding(start = 8.dp)) {
        LazyRow {
            tvShows.value?.results?.filter {
                it.poster_path != null
            }?.forEachIndexed { i, show ->
                item {
                    BoxA(
                        catagory = catagory,
                        id = show.id,
                        name = show.name,
                        posterPath = show.poster_path,
                        details = show.overview,
                        score = show.vote_average,
                        onClick = { id ->
                            navController.navigate(
                                Destination.TvDetailsPage(
                                    show = show.name,
                                    id = id,
                                    catagory = catagory
                                )
                            )
                        },
                        rank = "${ i + 1 }"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }
    }
}

@Composable
fun PersonBox(
    catagory: String,
    persons: State<ActorList?>,
    navController: NavController
) {
    Box(modifier = Modifier.padding(start = 8.dp)) {
        LazyRow {
            persons.value?.results?.filter {
                it.profile_path != null
            }?.forEachIndexed { i, person ->
                item {
                    BoxClipped(
                        catagory = catagory,
                        rank = "${ i + 1 }",
                        id = person.id,
                        name = person.name,
                        bestKnownFor = person.known_for_department,
                        popularity = person.popularity,
                        profilePath = person.profile_path,
                        onClick = { id ->
                            navController.navigate(
                                Destination.PersonDetailsPage(
                                    name = person.name,
                                    id = id,
                                    catagory = catagory
                                )
                            )
                        }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }
    }
}

@Composable
fun BoxClipped(
    catagory: String,
    id: Int,
    rank: String,
    name: String,
    bestKnownFor: String?,
    popularity: String,
    profilePath: String,
    onClick: (Int) -> Unit
) {
    Column(
        modifier = Modifier
            .height(336.dp)
            .width(192.dp)
            .shadow(elevation = 8.dp)
    ) {
        Box(
            modifier = Modifier
                .width(192.dp)
                .clip(
                    RoundedCornerShape(
                        topStart = 16.dp,
                        topEnd = 16.dp
                    )
                )
                .shadow(elevation = 8.dp)
                .clickable { onClick(id) },
            contentAlignment = Alignment.Center
        ) {
            Box(
                modifier = Modifier
                    .height(256.dp)
                    .width(176.dp)
                    .shadow(8.dp),
                contentAlignment = Alignment.BottomStart
            ) {
                ImageAsync(
                    contentDescription = "${ catagory } ${ name }",
                    clip = true,
                    aspectRatio = 5f / 8f,
                    backDropPath = profilePath
                )
                Heart(name = name, paddingStart = 8.dp, paddingBottom = 8.dp)
            }
        }
        Details(catagory = catagory, name = name, score = popularity, rank = rank, details = "", width = 192.dp, trending = false, showCircleI = false)
    }
}

@Composable
fun Heart(
    name: String,
    paddingStart: Dp,
    paddingBottom: Dp
) {
    // Favorite
    Box(
        modifier = Modifier
            .padding(start = paddingStart, bottom = paddingBottom)
            .size(40.dp)
            .clip(CircleShape)
            .border(
                width = 2.dp,
                color = Color.Magenta,
                shape = CircleShape
            )
            .background(gray200.copy(alpha = 0.8f)),
        contentAlignment = Alignment.Center
    ) {
        Icon(
            modifier = Modifier,
            imageVector = Icons.Outlined.FavoriteBorder,
            contentDescription = "Favorite ${ name }"
        )
    }
}

@Composable
fun UpcommingBox(
    catagory: String,
    movies: State<MovieList?>,
    navController: NavController
) {
    Box(modifier = Modifier.padding(start = 8.dp)) {
        LazyRow {
            movies.value?.results?.filter {
                it.overview != null
            }?.forEachIndexed { i, movie ->
                item {
                    BoxB(
                        catagory = catagory,
                        id = movie.id,
                        name = movie.title,
                        details = movie.overview,
                        date = movie.release_date,
                        score = movie.vote_average,
                        posterPath = movie.poster_path,
                        onClick = { id ->
                            navController.navigate(
                                Destination.MovieDetailsPage(
                                    title = movie.title,
                                    id = id,
                                    catagory = catagory
                                )
                            )
                        }
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }
    }
}

@Composable
fun BoxOfficeBox(
    catagory: String,
    boxOfficeNumbers: List<Map<String, String>>,
    navController: NavController
) {
    // date range
    Row (Modifier.padding(start = 8.dp)) {
        Box(
            modifier = Modifier.height(30.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 16.dp),
                text = "${ boxOfficeNumbers.first()["date range"] }"
            )
        }
    }

    Box(
        modifier = Modifier
            .height(680.dp)
            .fillMaxWidth(0.99f)
            .padding(all = 8.dp)
            .shadow(8.dp)
    ) {
        Column (
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Spacer(modifier = Modifier.height(16.dp))
            boxOfficeNumbers.forEachIndexed { i, movieMap ->
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .heightIn(min = 64.dp, max = 80.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        modifier = Modifier
                            .width(72.dp)
                            .padding(start = 8.dp, end = 8.dp),
                        text = "${ i + 1 }",
                        fontSize = MaterialTheme.typography.displaySmall.fontSize,
                        textAlign = TextAlign.Center
                    )

                    Ribbon(paddingEnd = 16.dp)

                    Column (
                        modifier = Modifier
                    ) {
                        Text(text = "${ movieMap["Release"] }")
                        Text(text = "${ movieMap["Gross"] }", textAlign = TextAlign.Center)
                    }

                    Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.CenterEnd) {
                        Icon(
                            modifier = Modifier.padding(end = 16.dp),
                            imageVector = Icons.Outlined.BookOnline,
                            contentDescription = "book online"
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BoxA(
    catagory: String,
    id: Int,
    name: String,
    posterPath: String,
    details: String,
    score: String,
    onClick: (Int) -> Unit,
    rank: String
) {
    Column(
        modifier = Modifier
            .width(225.dp)
            .height(525.dp)
            .shadow(elevation = 8.dp)
    ) {
        Box (
            modifier = Modifier
                .width(220.dp)
                .clickable { onClick(id) },
            contentAlignment = Alignment.TopStart
        ) {
            Column (modifier = Modifier.padding(5.dp)) {
                Box(modifier = Modifier.width(210.dp)) {
                    ImageAsync(
                        contentDescription = "${ catagory } ${ name }",
                        backDropPath = posterPath
                    )
                    Ribbon()
                }
            }
        }
        val trending = Regex("trending").containsMatchIn(catagory.lowercase())
        Details(catagory = catagory, rank = rank, name = name, details = details, score = score, width = 225.dp, trending = trending, showCircleI = !trending)
    }
}

@SuppressLint("SimpleDateFormat")
@Composable
fun BoxB(
    catagory: String,
    id: Int,
    name: String,
    details: String,
    score: String,
    date: Date,
    posterPath: String,
    onClick: (Int) -> Unit,
) {
    val releaseDateFormat = SimpleDateFormat("yyyy MMM")
    Column {
        Row(
            modifier = Modifier
                .width(323.dp)
        ) {
            // upcoming date
            Box(
                modifier = Modifier
                    .padding(start = 18.dp, top = 4.dp, bottom = 12.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = releaseDateFormat.format(date),
                    fontStyle = MaterialTheme.typography.labelLarge.fontStyle,
                    color = ripeMango,
                    textAlign = TextAlign.Center
                )
            }
        }

        Column(
            modifier = Modifier
                .width(323.dp)
                .shadow(elevation = 8.dp)
        ) {
            // movie box
            Box(
                modifier = Modifier
                    .width(320.dp)
                    .clickable { onClick(id) },
                contentAlignment = Alignment.TopStart
            ) {
                Column(modifier = Modifier.padding(5.dp)) {
                    Box(modifier = Modifier.width(310.dp)) {
                        ImageAsync(
                            contentDescription = "${ catagory } ${ name }",
                            aspectRatio = 5f / 8f,
                            backDropPath = posterPath,
                        )
                        // ribbon
                        Ribbon(paddingStart = 12.dp)
                    }
                }
            }
            Details(catagory = catagory, name = name, rank = "", details = details, score = score, width = 323.dp, trending = false, showCircleI = false)
        }
    }
}

@Composable
fun Details(
    catagory: String,
    rank: String,
    name: String,
    details: String,
    score: String,
    width: Dp,
    showCircleI: Boolean,
    trending: Boolean
) {
    Column (
        modifier = Modifier
            .width(width)
            .fillMaxHeight()
            .padding(start = 8.dp, bottom = 8.dp),
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        // Trending
        if (trending) {
            Box(
                modifier = Modifier.padding(start = 4.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                Text(
                    text = rank,
                    fontStyle = MaterialTheme.typography.displaySmall.fontStyle
                )
            }
        }
        // movie || show || person name
        Box(
            modifier = Modifier.padding(start = 8.dp, top = 8.dp, bottom = 8.dp),
            contentAlignment = Alignment.CenterStart
        ) {
            Text(text = name, minLines = 1, maxLines = 1, overflow = TextOverflow.Ellipsis)
        }
        // details
        if (details.isNotBlank()) {
            Text(
                text = details,
                modifier = Modifier.padding(all = 12.dp),
                minLines = 1,
                maxLines = 3,
                overflow = TextOverflow.Ellipsis
            )
        }
        // rating
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp)
        ) {
            Icon(
                modifier = Modifier.padding(start = 8.dp),
                imageVector = Icons.Filled.Star,
                contentDescription = "rating",
                tint = ripeMango
            )
            Text(modifier = Modifier.padding(start = 8.dp), text = score)
        }
        // Cirlce i
        if (showCircleI) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.BottomEnd
            ) {
                InformationCircle(paddingEnd = 16.dp)
            }
        }
    }
}
