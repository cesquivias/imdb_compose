package com.imdb_compose

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
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
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import coil.compose.AsyncImage
import coil.compose.SubcomposeAsyncImage
import com.example.compose.AppTheme
//import com.imdb_compose.ui.theme.Imdb_composeTheme
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
//            window.statusBarColor = getColor(R.color.black)
            window.navigationBarColor = getColor(R.color.black)
            AppTheme () {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.secondary
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
        containerColor = Color.Transparent,
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
            Text(
                text = "What to watch",
                color = MaterialTheme.colorScheme.onSecondary,
                fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                fontStyle = MaterialTheme.typography.headlineLarge.fontStyle,
                fontWeight = MaterialTheme.typography.headlineLarge.fontWeight
            )
        },
        colors = TopAppBarColors(
            containerColor = Color.Transparent,
            titleContentColor = TopAppBarDefaults.topAppBarColors().titleContentColor,
            actionIconContentColor = TopAppBarDefaults.topAppBarColors().actionIconContentColor,
            scrolledContainerColor = TopAppBarDefaults.topAppBarColors().scrolledContainerColor,
            navigationIconContentColor = TopAppBarDefaults.topAppBarColors().navigationIconContentColor
        )
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
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSecondary,
                fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                fontStyle = MaterialTheme.typography.headlineLarge.fontStyle,
                fontWeight = MaterialTheme.typography.headlineLarge.fontWeight
            )
        },
        navigationIcon = {
            IconButton(onClick = { backBtn() }) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "back")
            }
        },
        colors = TopAppBarColors(
            containerColor = Color.Transparent,
            titleContentColor = TopAppBarDefaults.topAppBarColors().titleContentColor,
            actionIconContentColor = TopAppBarDefaults.topAppBarColors().actionIconContentColor,
            scrolledContainerColor = TopAppBarDefaults.topAppBarColors().scrolledContainerColor,
            navigationIconContentColor = TopAppBarDefaults.topAppBarColors().navigationIconContentColor
        )
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
        },
        containerColor = Color.Transparent
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
                    Row (verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .padding(start = 8.dp)
                                .clip(RoundedCornerShape(100f))
                                .border(
                                    2.dp,
                                    color = Color.Yellow
                                )
                                .height(28.dp)
                                .width(8.dp)
                                .background(Color.Yellow)
                        )
                        Text(
                            text = catagory,
                            modifier = Modifier.padding(start = 8.dp),
                            color = MaterialTheme.colorScheme.onSecondary,
                            fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                            fontStyle = MaterialTheme.typography.headlineMedium.fontStyle,
                            fontWeight = MaterialTheme.typography.headlineMedium.fontWeight
                        )
                    }
                    Row (verticalAlignment = Alignment.CenterVertically) {
                        TextButton(
                            modifier = Modifier.padding(start = 8.dp),
                            onClick = { navController.navigate(Navigator.CategoryPage(catagory = catagory)) }
                        ) {
                            Text(
                                text = "see all",
                                modifier = Modifier.padding(start = 8.dp),
                                color = Color.Blue,
                                fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                                fontStyle = MaterialTheme.typography.headlineSmall.fontStyle,
                                fontWeight = MaterialTheme.typography.headlineSmall.fontWeight
                            )
                        }
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
            item {
                Spacer(modifier = Modifier.width(8.dp))
                Column(modifier = Modifier.padding(bottom = 16.dp)) {
                    Box (
                        modifier = Modifier
                            .width(155.dp)
                            .height(210.dp)
                            .padding(end = 8.dp)
                            .clickable { navController.navigate(Navigator.MovieDetailsPage(movie.title)) },
                        contentAlignment = Alignment.TopStart
                    ) {
                        val imageUrl = "${ Retrofit.BASE_IMAGE_URL }${ Retrofit.IMAGE_PATH }${ movie.poster_path }"
                        SubcomposeAsyncImage(
                            model = imageUrl,
                            modifier = Modifier
                                .fillMaxSize()
                                .aspectRatio(2f / 3f, true),
                            contentDescription = "${ catagory } ${ movie.title }",
                            loading = { isLoading() }
                        )
                        Box(modifier = Modifier.padding(top = 4.dp, start = 4.dp)) {
                            Icon(imageVector = Icons.Outlined.AddBox, contentDescription = "add")
                        }
                    }
                    Box (
                        modifier = Modifier
                            .width(155.dp)
                            .height(210.dp)
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
                        val imageUrl = "${ Retrofit.BASE_IMAGE_URL }${ Retrofit.IMAGE_PATH }${ person.profile_path }"
                        SubcomposeAsyncImage(
                            model = imageUrl,
                            modifier = Modifier
                                .fillMaxSize()
                                .aspectRatio(5f / 8f, matchHeightConstraintsFirst = true),
                            contentScale = ContentScale.Fit ,
                            contentDescription = "${ catagory } ${ person.name }",
                            loading = { isLoading() }
                        )
                        Box(modifier = Modifier.padding(bottom = 4.dp, start = 4.dp)) {
                            Icon(imageVector = Icons.Outlined.FavoriteBorder, contentDescription = "favorite")
                        }
                    }
                    Box (
                        modifier = Modifier
                            .width(175.dp)
                            .height(125.dp)
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
                    Box (modifier = Modifier
                        .width(155.dp)
                        .height(210.dp)
                        .padding(end = 8.dp)
                        .clickable { navController.navigate(Navigator.TvDetailsPage(show.name)) },
                        contentAlignment = Alignment.TopStart
                    ) {
                        val imageUrl = "${ Retrofit.BASE_IMAGE_URL }${ Retrofit.IMAGE_PATH }${ if (show.backdrop_path?.isEmpty() == true) show.backdrop_path else { show.poster_path } }"
                        SubcomposeAsyncImage(
                            modifier = Modifier
                                .fillMaxSize()
                                .aspectRatio(ratio = 2f / 3f, matchHeightConstraintsFirst = true),
                            contentScale = ContentScale.Fit,
                            model = imageUrl,
                            contentDescription = "${ catagory } ${ show.name }",
                            loading = { isLoading() }
                        )
                        Box(modifier = Modifier.padding(top = 4.dp, start = 4.dp)) {
                            Icon(imageVector = Icons.Outlined.AddBox, contentDescription = "add")
                        }
                    }
                    Box (
                        modifier = Modifier
                            .width(150.dp)
                            .height(100.dp)
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
    CircularProgressIndicator(
        modifier = Modifier
            .fillMaxSize()
            .padding(128.dp),
        color = MaterialTheme.colorScheme.outline
    )
}


@SuppressLint("UnusedMaterial3ScaffoldPaddingParameter")
@Composable
//@Preview(showBackground = true, showSystemUi = true)
fun SurfacePreview() {
    AppTheme () {
        Surface (
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.scrim
        ) {
            Scaffold (
                modifier = Modifier.fillMaxSize(),
                topBar = {
                    TopBarNoNav()
                },
                bottomBar = {
                    BottomAppBar(
                        modifier = Modifier.fillMaxWidth(),
                        actions = {
                            Box(modifier = Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) {
                                Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                                    IconButton(onClick = {}) {
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
                },
                containerColor = Color.Transparent
            ) { paddingValues ->
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(paddingValues)
                        .border(width = 1.dp, color = MaterialTheme.colorScheme.outline)
                ) {
                    Column {
                        Row (
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row {
                                Box(
                                    modifier = Modifier
                                        .padding(start = 8.dp)
                                        .clip(RoundedCornerShape(100f))
                                        .border(2.dp, color = Color.Yellow)
                                        .height(32.dp)
                                        .width(8.dp)
                                        .background(Color.Yellow)
                                )
                                Text(
                                    text = "catagory",
                                    modifier = Modifier.padding(start = 8.dp),
                                    color = MaterialTheme.colorScheme.onSecondary,
                                    fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                                    fontStyle = MaterialTheme.typography.headlineMedium.fontStyle,
                                    fontWeight = MaterialTheme.typography.headlineMedium.fontWeight
                                )
                            }
                            TextButton(
                                modifier = Modifier.padding(start = 8.dp),
                                onClick = {}
                            ) {
                                Text(
                                    text = "see all",
                                    modifier = Modifier.padding(start = 8.dp),
                                    color = Color.Blue,
                                    fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                                    fontStyle = MaterialTheme.typography.headlineSmall.fontStyle,
                                    fontWeight = MaterialTheme.typography.headlineSmall.fontWeight
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
