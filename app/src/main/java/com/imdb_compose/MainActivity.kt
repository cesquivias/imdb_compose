package com.imdb_compose

import android.annotation.SuppressLint
import android.graphics.BlurMaskFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.FloatRange
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import androidx.compose.material.icons.outlined.AddBox
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.StarOutline
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
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
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

@SuppressLint("SuspiciousModifierThen")
fun Modifier.shadow(
    color: Color = Color.Black,
    offsetX: Dp = 0.dp,
    offsetY: Dp = 0.dp,
    blurRadius: Dp = 0.dp,
    blurRadiusFilter: String = "NORMAL"
) = then(
    drawBehind {
        drawIntoCanvas { canvas ->
            val paint = Paint()
            val frameworkPaint = paint.asFrameworkPaint()

            if (blurRadius != 0.dp) {
                when (blurRadiusFilter) {
                    "SOLID" -> frameworkPaint.maskFilter = (BlurMaskFilter(blurRadius.toPx(), BlurMaskFilter.Blur.SOLID))
                    "OUTER" -> frameworkPaint.maskFilter = (BlurMaskFilter(blurRadius.toPx(), BlurMaskFilter.Blur.OUTER))
                    "INNER" -> frameworkPaint.maskFilter = (BlurMaskFilter(blurRadius.toPx(), BlurMaskFilter.Blur.INNER))
                    else -> frameworkPaint.maskFilter = (BlurMaskFilter(blurRadius.toPx(), BlurMaskFilter.Blur.NORMAL))
                }
            }

            frameworkPaint.color = color.toArgb()
            val leftPixel = offsetX.toPx()
            val topPixel = offsetY.toPx()

            canvas.drawRect(
                left = leftPixel,
                top = topPixel,
                right = size.width + topPixel,
                bottom = size.height + leftPixel,
                paint = paint,
            )
        }
    }
)

val gray100 = Color(0xff555555)
val gray200 = Color(0xff444444)
val gray300 = Color(0xff333333)
val gray400 = Color(0xff222222)
val gray500 = Color(0xff111111)
val gray600 = Color(0xff000000)

@Composable
fun GenerateLazyRows(viewModel: HomeScreenViewModel, navController: NavController) {
    viewModel.catagories.forEachIndexed { i, catagory ->
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .shadow(
                    color = gray600.copy(alpha = 0.6f),
                    offsetX = 0.dp,
                    offsetY = 0.dp,
                    blurRadius = 4.dp
                )
        ) {
            Row (
                modifier = Modifier
                    .fillMaxWidth(fraction = 0.95f)
                    .fillMaxHeight(fraction = 0.95f)
                    .shadow(
                        color = gray400,
                        offsetX = 8.dp,
                        offsetY = 8.dp,
                        blurRadius = 4.dp,
                        blurRadiusFilter = "SOLID"
                    )
            ) {
                Column (modifier = Modifier.padding(start = 8.dp, top = 8.dp)) {
                    Row (
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Yellow Accent <Catagory>
                        Row (verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .padding(start = 8.dp)
                                    .clip(RoundedCornerShape(24.dp))
                                    .height(36.dp)
                                    .width(6.dp)
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
                        // See All Btn
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

                    Spacer(modifier = Modifier.height(8.dp))

                    when(catagory) {
//                    "Movies of the week" -> CreateMovieDetailsBox(catagory, viewModel.movieListOfWeek.collectAsState(), viewModel, navController)
                        "Popular actors" -> CreatePersonDetailsBox(catagory, viewModel.popularPersons.collectAsState(), navController)
                        "Trending movies" -> CreateMovieDetailsBox(catagory, viewModel.trendingMovies.collectAsState(), viewModel, navController)
//                    "Upcoming movies" -> CreateMovieDetailsBox(catagory, viewModel.upcomingMovies.collectAsState(), viewModel, navController)
//                    "Trending tv" -> CreateTvDetailsBox(catagory, viewModel.trendingTv.collectAsState(), navController)
//                    "Tv airing today" -> CreateTvDetailsBox(catagory, viewModel.airingTodayTv.collectAsState(), navController)
//                    "Trending people" -> CreatePersonDetailsBox(catagory, viewModel.trendingPersons.collectAsState(), navController)
                        else -> CreateMovieDetailsBox(catagory, viewModel.noMovies.collectAsState(), viewModel, navController)
                    }
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
    Box(modifier = Modifier.padding(start = 8.dp)) {
        LazyRow {
            persons.value?.results?.forEachIndexed { i, person ->
                item {
                    Column(
                        modifier = Modifier
                            .height(376.dp)
                            .fillMaxWidth()
                            .shadow(
                                color = gray600.copy(alpha = 0.6f),
                                offsetX = 0.dp,
                                offsetY = 0.dp,
                                blurRadius = 4.dp
                            )
                    ) {
                        Box(
                            modifier = Modifier
                                .height(272.dp)
                                .width(176.dp)
                                .clip(
                                    RoundedCornerShape(
                                        topStart = 16.dp,
                                        topEnd = 16.dp,
                                        bottomStart = 0.dp,
                                        bottomEnd = 0.dp
                                    )
                                )
                                .shadow(
                                    color = gray500,
                                    offsetX = 0.dp,
                                    offsetY = 0.dp,
                                    blurRadius = 4.dp
                                )
                                .clickable {
                                    navController.navigate(
                                        Navigator.PersonDetailsPage(
                                            person.name,
                                            person.id
                                        )
                                    )
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Box(
                                modifier = Modifier
                                    .height(256.dp)
                                    .width(160.dp)
                                    .shadow(
                                        color = gray600,
                                        offsetX = 0.dp,
                                        offsetY = 0.dp,
                                        blurRadius = 4.dp
                                    ),
                                contentAlignment = Alignment.BottomStart
                            ) {
                                // Image
                                val imageUrl =
                                    "${Retrofit.BASE_IMAGE_URL}${Retrofit.IMAGE_PATH}${person.profile_path}"
                                SubcomposeAsyncImage(
                                    model = imageUrl,
                                    modifier = Modifier
                                        .fillMaxSize()
                                        .aspectRatio(5f / 8f, matchHeightConstraintsFirst = true)
                                        .clip(RoundedCornerShape(16.dp)),
                                    contentScale = ContentScale.Crop,
                                    contentDescription = "${catagory} ${person.name}",
                                    loading = { isLoading() }
                                )
                                // Favorite
                                Box(
                                    modifier = Modifier
                                        .padding(start = 4.dp, bottom = 4.dp)
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
                                        contentDescription = "Favorite ${person.name}"
                                    )
                                }
                            }
                        }
                        // Bottom Half - Descrition
                        Box(
                            modifier = Modifier.fillMaxSize(),
                            contentAlignment = Alignment.TopStart
                        ) {
                            Box(
                                modifier = Modifier.padding(start = 4.dp, top = 0.dp, bottom = 4.dp)
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxHeight().width(160.dp),
                                    verticalArrangement = Arrangement.SpaceEvenly
                                ) {
                                    Text(
                                        text = person.original_name,
                                        modifier = Modifier,
                                        fontFamily = MaterialTheme.typography.titleLarge.fontFamily,
                                        fontSize = MaterialTheme.typography.titleLarge.fontSize,
                                        fontWeight = MaterialTheme.typography.titleLarge.fontWeight,
                                        overflow = TextOverflow.Ellipsis,
                                        softWrap = false
                                    )
                                    Text(text = person.known_for_department)
                                    Row(modifier = Modifier.fillMaxWidth()) {
                                        Icon(
                                            imageVector = Icons.Filled.Star,
                                            contentDescription = "rating"
                                        )
                                        Text(
                                            modifier = Modifier.padding(start = 8.dp),
                                            text = person.popularity
                                        )
                                    }
                                }
                            }
                        }
                    }
                    Spacer(modifier = Modifier.width(8.dp))
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
