package com.imdb_compose

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.AddBox
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import com.imdb_compose.ui.theme.Imdb_composeTheme
import kotlinx.serialization.Serializable

interface Navigator {
    @Serializable
    data object HomeScreen: Navigator
    @Serializable
    data class CategoryPage(val catagory: String): Navigator
    @Serializable
    data class MovieDetailsPage(val movie: String): Navigator
    @Serializable
    data class PersonDetailsPage(val person: String, val id: Int): Navigator
    @Serializable
    data class TvDetailsPage(val show: String): Navigator
}

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Imdb_composeTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    val navController = rememberNavController()
                    val viewModel = HomeScreenViewModel()

                    NavHost(navController = navController, startDestination = Navigator.HomeScreen ) {
                        composable<Navigator.HomeScreen> {
                            HomeScreen(top = { TopBarNoNav() }, bottom = { BottomBar(navController) }, viewModel = viewModel, navController = navController)
                        }
                        composable<Navigator.CategoryPage> {
                            val args =  it.toRoute<Navigator.CategoryPage>()
                            CategoryPage(args.catagory, viewModel = viewModel, navController = navController, { navController.popBackStack() })
                        }
                        composable<Navigator.MovieDetailsPage> {
                            val args =  it.toRoute<Navigator.MovieDetailsPage>()
                            MovieDetailsPage(args.movie, viewModel = viewModel, navController = navController, { navController.popBackStack() })
                        }
                        composable<Navigator.PersonDetailsPage> {
                            val args =  it.toRoute<Navigator.PersonDetailsPage>()
                            PersonDetailsPage(args.person, args.id, viewModel = viewModel, navController = navController, { navController.popBackStack() })
                        }
                        composable<Navigator.TvDetailsPage> {
                            val args =  it.toRoute<Navigator.TvDetailsPage>()
                            TvDetailsPage(args.show, viewModel = viewModel, navController = navController, { navController.popBackStack() })
                        }
                    }
                }
            }
        }
    }
}

@Composable
@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
fun HomeScreen(
    top: @Composable () -> Unit,
    bottom: @Composable () -> Unit,
    viewModel: HomeScreenViewModel,
    navController: NavController
) {
    Scaffold (
        modifier = Modifier.fillMaxSize(),
        topBar = {
            top()
        },
        bottomBar = {
            bottom()
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            LazyColumn {
                item {
                    GenerateLazyRows(viewModel, navController)
                }
            }
        }
    }
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun TopBarNoNav() {
    TopAppBar(
        title = {
            Text(text = "What to watch")
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopBarWithBackBtn(
    title: String,
    backBtn: () -> Unit
) {
    TopAppBar(
        modifier = Modifier,
        title = {
            Text(text = title)
        },
        navigationIcon = {
            IconButton(onClick = { backBtn() }) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "back")
            }
        }
    )
}

@Composable
fun BottomBar(navController: NavController) {
    BottomAppBar(
        modifier = Modifier.fillMaxWidth(),
        actions = {
            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                    IconButton(onClick = { navController.navigate(Navigator.HomeScreen) }) {
                        Icon(imageVector = Icons.Default.Home, contentDescription = "home")
                    }
                    IconButton(onClick = {}) {
                        Icon(imageVector = Icons.Default.Search, contentDescription = "search")
                    }
                    IconButton(onClick = {}) {
                        Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "home")
                    }
                    IconButton(onClick = {}) {
                        Icon(imageVector = Icons.Default.Person, contentDescription = "profile")
                    }
                }
            }
        }
    )
}

@Composable
fun GenerateLazyRows(viewModel: HomeScreenViewModel, navController: NavController) {
    viewModel.catagories.forEachIndexed { i, catagory ->
        Row (
            modifier = Modifier
                .fillMaxWidth()
                .border(width = 1.dp, color = MaterialTheme.colorScheme.outline)
        ) {
            Column {
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = catagory,
                        modifier = Modifier.padding(start = 8.dp)
                    )
                    TextButton(
                        modifier = Modifier.padding(start = 8.dp),
                        onClick = { navController.navigate(Navigator.CategoryPage(catagory = catagory)) }
                    ) {
                        Text(text = "see all")
                    }
                }

                when(catagory) {
                    "Movies of the week" -> CreateMovieDetailsBox(catagory, viewModel.movieListOfWeek.collectAsState(), viewModel, navController)
                    "Trending movies" -> CreateMovieDetailsBox(catagory, viewModel.trendingMovies.collectAsState(), viewModel, navController)
                    "Upcoming movies" -> CreateMovieDetailsBox(catagory, viewModel.upcomingMovies.collectAsState(), viewModel, navController)
                    "Trending tv" -> CreateTvDetailsBox(catagory, viewModel.trendingTv.collectAsState(), navController)
                    "Tv airing today" -> CreateTvDetailsBox(catagory, viewModel.airingTodayTv.collectAsState(), navController)
                    "Popular actors" -> CreatePersonDetailsBox(catagory, viewModel.popularPersons.collectAsState(), navController)
                    "Trending people" -> CreatePersonDetailsBox(catagory, viewModel.trendingPersons.collectAsState(), navController)
                    else -> CreateMovieDetailsBox(catagory, viewModel.noMovies.collectAsState(), viewModel, navController)
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun CreateMovieDetailsBox(catagory: String, movies: State<MovieList?>, viewModel: HomeScreenViewModel, navController: NavController) {
    LazyRow {
        movies.value?.results?.forEachIndexed { i, movie ->
            if (i <= 3) {
                item {
                    Spacer(modifier = Modifier.width(8.dp))
                    Column(modifier = Modifier.padding(bottom = 16.dp)) {
                        Box (
                            modifier = Modifier
                                .width(125.dp)
                                .height(175.dp)
                                .border(
                                    width = 2.dp,
                                    color = MaterialTheme.colorScheme.outline
                                )
                                .padding(end = 8.dp)
                                .clickable { navController.navigate(Navigator.MovieDetailsPage(movie.title)) },
                            contentAlignment = Alignment.TopStart
                        ) {
                            Box(modifier = Modifier.padding(top = 4.dp, start = 4.dp)) {
                                Icon(imageVector = Icons.Outlined.AddBox, contentDescription = "add")
                            }
                            val imageUrl = "${ Retrofit.BASE_IMAGE_URL }${ Retrofit.IMAGE_PATH }${ movie.poster_path }"
                            SubcomposeAsyncImage(model = imageUrl, contentDescription = "${ catagory } ${ movie.title }", loading = { isLoading() })
                        }
                        Box (
                            modifier = Modifier
                                .width(125.dp)
                                .height(100.dp)
                                .border(
                                    width = 2.dp,
                                    color = MaterialTheme.colorScheme.outline
                                )
                                .padding(end = 8.dp),
                            contentAlignment = Alignment.TopStart
                        ) {
                            Box(
                                modifier = Modifier.padding(4.dp)
                            ) {
                                Column (
                                    modifier = Modifier.fillMaxHeight(),
                                    verticalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    Text(
                                        text = "${ i + 1 }",
                                        modifier = Modifier,
                                        fontFamily = MaterialTheme.typography.titleLarge.fontFamily,
                                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                                        fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
                                    )
                                    Row(modifier = Modifier.fillMaxWidth()) {
                                        Icon(imageVector = Icons.Filled.Star, contentDescription = "rating")
                                        Text(modifier = Modifier.padding(start = 8.dp), text = movie.vote_average)
                                    }
                                    Text(text = movie.title)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CreatePersonDetailsBox(catagory: String, persons: State<ActorList?>, navController: NavController) {
    LazyRow {
        persons.value?.results?.forEachIndexed { i, person ->
            item {
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.padding(bottom = 16.dp)) {
                    Box (
                        modifier = Modifier
                            .width(175.dp)
                            .height(225.dp)
                            .border(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.outline
                            )
                            .padding(end = 8.dp)
                            .clickable {
                                navController.navigate(
                                    Navigator.PersonDetailsPage(
                                        person.name,
                                        person.id
                                    )
                                )
                            },
                        contentAlignment = Alignment.BottomStart
                    ) {
                        Box(modifier = Modifier.padding(bottom = 4.dp, start = 4.dp)) {
                            Icon(imageVector = Icons.Outlined.FavoriteBorder, contentDescription = "favorite")
                        }
                        val imageUrl = "${ Retrofit.BASE_IMAGE_URL }${ Retrofit.IMAGE_PATH }${ person.profile_path }"
                        SubcomposeAsyncImage(model = imageUrl, contentDescription = "${ catagory } ${ person.name }", loading = { isLoading() })
                    }
                    Box (
                        modifier = Modifier
                            .width(175.dp)
                            .height(125.dp)
                            .border(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.outline
                            )
                            .padding(end = 8.dp),
                        contentAlignment = Alignment.TopStart
                    ) {
                        Box(
                            modifier = Modifier.padding(4.dp)
                        ) {
                            Column (
                                modifier = Modifier.fillMaxHeight(),
                                verticalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Text(
                                    text = person.original_name,
                                    modifier = Modifier,
                                    fontFamily = MaterialTheme.typography.titleLarge.fontFamily,
                                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                                    fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
                                )
                                Text(text = person.known_for_department)
                                Row(modifier = Modifier.fillMaxWidth()) {
                                    Icon(imageVector = Icons.Filled.Star, contentDescription = "rating")
                                    Text(modifier = Modifier.padding(start = 8.dp), text = person.popularity)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CreateTvDetailsBox(catagory: String, tvShows: State<TvList?>, navController: NavController) {
    LazyRow {
        tvShows.value?.results?.forEachIndexed { i, show ->
            item {
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.padding(bottom = 16.dp)) {
                    Box (
                        modifier = Modifier
                            .width(125.dp)
                            .height(175.dp)
                            .border(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.outline
                            )
                            .padding(end = 8.dp)
                            .clickable { navController.navigate(Navigator.TvDetailsPage(show.name)) },
                        contentAlignment = Alignment.TopStart
                    ) {
                        Box(modifier = Modifier.padding(top = 4.dp, start = 4.dp)) {
                            Icon(imageVector = Icons.Outlined.AddBox, contentDescription = "add")
                        }
                        val imageUrl = "${ Retrofit.BASE_IMAGE_URL }${ Retrofit.IMAGE_PATH }${ show.backdrop_path }"
                        SubcomposeAsyncImage(model = imageUrl, contentDescription = "${ catagory } ${ show.name }", loading = { isLoading() })
                    }
                    Box (
                        modifier = Modifier
                            .width(125.dp)
                            .height(100.dp)
                            .border(
                                width = 2.dp,
                                color = MaterialTheme.colorScheme.outline
                            )
                            .padding(end = 8.dp),
                        contentAlignment = Alignment.TopStart
                    ) {
                        Box(
                            modifier = Modifier.padding(4.dp)
                        ) {
                            Column (
                                modifier = Modifier.fillMaxHeight(),
                                verticalArrangement = Arrangement.SpaceEvenly
                            ) {
                                Text(
                                    text = "${ i + 1 }",
                                    modifier = Modifier,
                                    fontFamily = MaterialTheme.typography.titleLarge.fontFamily,
                                    fontSize = MaterialTheme.typography.titleLarge.fontSize,
                                    fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
                                )
                                Row(modifier = Modifier.fillMaxWidth()) {
                                    Icon(imageVector = Icons.Filled.Star, contentDescription = "rating")
                                    Text(modifier = Modifier.padding(start = 8.dp), text = show.popularity)
                                }
                                Text(text = show.original_name)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun isLoading() {
    CircularProgressIndicator(modifier = Modifier
        .fillMaxSize()
        .padding(128.dp), color = MaterialTheme.colorScheme.outline)
}
