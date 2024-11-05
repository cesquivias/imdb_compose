package com.imdb_compose

import android.annotation.SuppressLint
import android.graphics.BlurMaskFilter
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.annotation.FloatRange
import androidx.compose.animation.core.spring
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
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Add
import androidx.compose.material.icons.outlined.AddBox
import androidx.compose.material.icons.outlined.BookOnline
import androidx.compose.material.icons.outlined.FavoriteBorder
import androidx.compose.material.icons.outlined.StarOutline
import androidx.compose.material3.BottomAppBar
import androidx.compose.material3.Card
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
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.currentCompositionLocalContext
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.BlurredEdgeTreatment
import androidx.compose.ui.draw.blur
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.drawWithCache
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Paint
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.drawIntoCanvas
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.max
import androidx.compose.ui.unit.toSize
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
    data class MovieDetailsPage(val title: String, val id: Int): Navigator
    @Serializable
    data class PersonDetailsPage(val person: String, val id: Int): Navigator
    @Serializable
    data class TvDetailsPage(val show: String, val id: Int): Navigator
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
                            MovieDetailsPage(args.title, args.id, viewModel = viewModel, navController = navController, { navController.popBackStack() })
                        }
                        composable<Navigator.PersonDetailsPage> {
                            val args =  it.toRoute<Navigator.PersonDetailsPage>()
                            PersonDetailsPage(args.person, args.id, viewModel = viewModel, navController = navController, { navController.popBackStack() })
                        }
                        composable<Navigator.TvDetailsPage> {
                            val args =  it.toRoute<Navigator.TvDetailsPage>()
                            TvDetailsPage(args.show, args.id, viewModel = viewModel, navController = navController, { navController.popBackStack() })
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
                    LazyRows(viewModel, navController)
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
                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                fontStyle = MaterialTheme.typography.titleLarge.fontStyle,
                fontWeight = MaterialTheme.typography.titleLarge.fontWeight
            )
        },
        navigationIcon = {
            IconButton(onClick = { backBtn() }) {
                Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "back", tint = Color.White)
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
val blue100 = Color(0xff1eeeff)
val blue200 = Color(0xff3D76E0)
val blue300 = Color(0xff1e90ff)
val blue400 = Color(0xff1e32ff)
val bananaYellow = Color(0xffffe134)
val ripeMango = Color(0xffffbf2e)
val constuctionOrange = Color(0xffffc300)
val deepSaffron= Color(0xffff9e28)
val princetonOrange = Color(0xfffe7c22)
val giantsOrange = Color(0xfffe5a1c)

@Composable
fun LazyRows(
    viewModel: HomeScreenViewModel,
    navController: NavController
) {
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
                                    text = "See all",
                                    modifier = Modifier.padding(start = 8.dp),
                                    color = blue400,
                                    fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                                    fontStyle = MaterialTheme.typography.headlineSmall.fontStyle,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    when(catagory) {
                        "Tv airing today" -> TvBox(catagory, viewModel.airingTodayTv.collectAsState(), navController)
                        "Trending tv" -> TvBox(catagory, viewModel.trendingTv.collectAsState(), navController)
                        "Top box office" -> BoxOfficeBox(catagory, viewModel.boxOffice, navController)
                        "Upcoming movies" -> UpcommingBox(catagory, viewModel.upcomingMovies.collectAsState(), viewModel, navController)
                        "Popular actors" -> PersonBox(catagory, viewModel.popularPersons.collectAsState(), navController)
                        "Trending people" -> PersonBox(catagory, viewModel.trendingPersons.collectAsState(), navController)
                        "Movies of the week" -> MovieBox(catagory, viewModel.movieListOfWeek.collectAsState(), viewModel, navController)
                        "Trending movies" -> MovieBox(catagory, viewModel.trendingMovies.collectAsState(), viewModel, navController)
                        else -> MovieBox(catagory, viewModel.noMovies.collectAsState(), viewModel, navController)
                    }
                }
            }
        }
        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
fun MovieBox(
    catagory: String,
    movies: State<MovieList?>,
    viewModel: HomeScreenViewModel,
    navController: NavController
) {
    Box(modifier = Modifier.padding(start = 8.dp)) {
        LazyRow {
            movies.value?.results?.filter {
                it.poster_path != null
            }?.forEachIndexed { i, movie ->
                item {
                    PosterBoxA(
                        catagory = catagory,
                        movie = movie,
                        show = null,
                        navController = navController,
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
                    PosterBoxA(
                        catagory = catagory,
                        show = show,
                        movie = null,
                        navController = navController,
                        rank = "${i + 1}"
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
                    PosterBoxClip(catagory = catagory, rank = "${ i + 1 }", person = person, navController = navController)
                    Spacer(modifier = Modifier.width(8.dp))
                }
            }
        }
    }
}

@Composable
fun PosterBoxClip(
    catagory: String,
    rank: String,
    person: ActorResult,
    navController: NavController,
) {
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
                ImageAsync(
                    contentDescription = "${ catagory } ${ person.name }",
                    clip = true,
                    aspectRatio = 5f / 8f,
                    backDropPath = person.profile_path
                )
                Heart(name = person.name, paddingStart = 8.dp, paddingBottom = 8.dp)
            }
        }
        // Bottom Half Details
        Details(catagory = catagory, rank = rank, movie = null, show = null, person = person, showCircleI = false)
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
    viewModel: HomeScreenViewModel,
    navController: NavController
) {
    Box(modifier = Modifier.padding(start = 8.dp)) {
        LazyRow {
            movies.value?.results?.forEachIndexed { i, movie ->
                item {
                    PosterBoxB(catagory = catagory, movie = movie, navController = navController)
                }
            }
        }
    }
}

@Composable
fun BoxOfficeBox(
    catagory: String,
    boxOfficeNumbers:  List<Map<String, String>>,
    navController: NavController
) {
    // date range
    Row (
        Modifier
            .padding(start = 16.dp)
            .fillMaxWidth(0.975f)
    ) {
        Box(
            modifier = Modifier
                .height(30.dp)
                .fillMaxWidth()
                .shadow(
                    color = gray100,
                    offsetX = 0.dp,
                    offsetY = 0.dp,
                    blurRadius = 0.dp,
                    blurRadiusFilter = "SOLID"
                ),
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

    Box (
        Modifier
            .padding(
                start = 16.dp,
                top = 8.dp,
                bottom = 8.dp
            )
            .fillMaxWidth(0.98f)) {
        Box(
            modifier = Modifier
                .height(650.dp)
                .fillMaxWidth(0.99f)
                .shadow(
                    color = gray500,
                    offsetX = 0.dp,
                    offsetY = 0.dp,
                    blurRadius = 0.dp,
                    blurRadiusFilter = "SOLID"
                )
        ) {
            Column (
                modifier = Modifier.fillMaxSize(),
                verticalArrangement = Arrangement.SpaceBetween
            ) {
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

                        Box(
                            modifier = Modifier
                                .padding(end = 16.dp)
                                .size(36.dp)
                                .background(gray200.copy(alpha = 0.6f))
                                .drawBehind {
                                    val path = Path().apply {
                                        moveTo(75f, 126f)
                                        lineTo(0f, 166f)
                                        lineTo(0f, 126f)
                                        moveTo(45f, 126f)
                                        lineTo(125f, 126f)
                                        lineTo(125f, 166f)
                                        close()
                                    }
                                    drawPath(path, color = gray200.copy(alpha = 0.6f))
                                },
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Add,
                                contentDescription = "add favorite"
                            )
                        }

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
}

@Composable
fun PosterBoxA(
    catagory: String,
    movie: MovieResult?,
    show: TvResult?,
    navController: NavController,
    rank: String
) {
    Column(
        modifier = Modifier
            .shadow(
                color = gray600.copy(alpha = 0.6f),
                offsetX = 0.dp,
                offsetY = 0.dp,
                blurRadius = 4.dp
            ),
    ) {
        Box (
            modifier = Modifier
                .width(180.dp)
                .height(270.dp)
                .padding(vertical = 2.dp)
                .shadow(
                    color = gray500,
                    offsetX = 0.dp,
                    offsetY = 0.dp,
                    blurRadius = 4.dp
                )
                .clickable {
                    if (movie != null) {
                        navController.navigate(Navigator.MovieDetailsPage(title = movie.title, id = movie.id))
                    }
                    if (show != null) {
                        navController.navigate(Navigator.TvDetailsPage(show = show.name, id = show.id))
                    }
                },
            contentAlignment = Alignment.TopStart
        ) {
            Column {
                if (movie != null) {
                    Box(modifier = Modifier) {
                        ImageAsync(
                            contentDescription = "${ catagory } ${ movie.title }",
                            clip = false,
                            backDropPath = movie.poster_path
                        )
                        Ribbon(paddingStart = 2.dp)
                    }
                }
                if (show != null) {
                    Box(modifier = Modifier) {
                        ImageAsync(
                            contentDescription = "${ catagory } ${ show.name }",
                            clip = false,
                            backDropPath = show.poster_path
                        )
                        Ribbon(paddingStart = 2.dp)
                    }
                }
            }
        }
        if (movie != null) {
            Details(catagory = catagory, rank = rank, movie = movie, show = null, person = null, showCircleI = true)
        }
        if (show != null) {
            Details(catagory = catagory, rank = rank, show = show, movie = null, person = null, showCircleI = true)
        }
    }
}

@Composable
fun PosterBoxB(
    catagory: String,
    movie: MovieResult,
    navController: NavController
) {
    Column (modifier = Modifier.height(574.dp)) {
        Row {
            // upcoming date
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 18.dp, top = 4.dp, bottom = 12.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                //"2024-07-31"
                val date = movie.release_date.split("-")
                val monthMap = mapOf("01" to "Jan", "02" to "Feb", "03" to "Mar", "04" to "Apr", "05" to "May", "06" to "Jun", "07" to "Jul", "08" to "Aug", "09" to "Sep", "10" to "Oct", "11" to "Nov", "12" to "Dec",)

                Text(
                    text = "${ monthMap[date[1]] }-${ date[2] }",
                    fontFamily = MaterialTheme.typography.labelLarge.fontFamily,
                    fontSize = MaterialTheme.typography.labelLarge.fontSize,
                    fontStyle = MaterialTheme.typography.labelLarge.fontStyle,
                    fontWeight = MaterialTheme.typography.labelLarge.fontWeight,
                    color = ripeMango,
                    textAlign = TextAlign.Center
                )
            }
        }
        // movie box
        Box(
            modifier = Modifier
                .width(300.dp)
                .height(440.dp)
                .clickable {
                    navController.navigate(
                        Navigator.MovieDetailsPage(title = movie.title, id = movie.id)
                    )
                },
            contentAlignment = Alignment.TopStart
        ) {
            ImageAsync(
                contentDescription = "${ catagory } ${ movie.title }",
                aspectRatio = 5f / 8f,
                backDropPath = if (movie.backdrop_path.isEmpty()) {
                    movie.backdrop_path
                } else {
                    movie.poster_path
                }
            )
            // ribbon
            Ribbon(paddingStart = 12.dp)
        }
        // details box
        Column (
            modifier = Modifier
                .width(288.dp)
                .shadow(
                    color = gray500,
                    offsetX = 12.dp,
                    offsetY = 0.dp,
                    blurRadius = 4.dp,
                    blurRadiusFilter = "SOLID"
                )
        ) {
            Box(modifier = Modifier.padding(start = 16.dp, top = 8.dp), contentAlignment = Alignment.CenterStart) {
                Details(
                    catagory = catagory,
                    rank = "",
                    movie = movie,
                    show = null,
                    person = null,
                    showCircleI = false
                )
            }
        }
    }
}

@Composable
fun Details(
    catagory: String,
    rank: String,
    movie: MovieResult?,
    show: TvResult?,
    person: ActorResult?,
    showCircleI: Boolean
) {
    Box (modifier = Modifier.width(180.dp)) {
        Column (
            modifier = Modifier
                .fillMaxWidth()
                .padding(start = 4.dp, bottom = 8.dp),
            verticalArrangement = Arrangement.SpaceEvenly
        ) {
            // Trending
            if (Regex("trending").containsMatchIn(catagory.lowercase())) {
                Box(
                    modifier = Modifier.padding(start = 4.dp),
                    contentAlignment = Alignment.CenterStart
                ) {
                    Text(
                        text = rank,
                        fontSize = MaterialTheme.typography.displaySmall.fontSize,
                        fontFamily = MaterialTheme.typography.displaySmall.fontFamily,
                        fontStyle = MaterialTheme.typography.displaySmall.fontStyle,
                        fontWeight = MaterialTheme.typography.displaySmall.fontWeight
                    )
                }
            }
            // person name
            if (person != null) {
                Text(
                    text = person.original_name,
                    modifier = Modifier.padding(start = 8.dp),
                    overflow = TextOverflow.Ellipsis,
                    softWrap = false
                )
                Text(
                    text = person.known_for_department,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
            // rating
            Row(modifier = Modifier.fillMaxWidth().padding(bottom = 8.dp)) {
                Icon(
                    modifier = Modifier.padding(start = 8.dp),
                    imageVector = Icons.Filled.Star,
                    contentDescription = "rating",
                    tint = ripeMango
                )
                if (movie != null) {
                    Text(modifier = Modifier.padding(start = 8.dp), text = movie.vote_average)
                }
                if (show != null) {
                    Text(modifier = Modifier.padding(start = 8.dp), text = show.popularity)
                }
                if (person != null) {
                    Text(modifier = Modifier.padding(start = 8.dp), text = person.popularity)
                }
            }
            // Movie & Show names
            Box(
                modifier = Modifier.padding(start = 4.dp, bottom = 8.dp),
                contentAlignment = Alignment.CenterStart
            ) {
                if (movie != null) {
                    Text(text = movie.title, minLines = 1, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
                if (show != null) {
                    Text(text = show.name, minLines = 1, maxLines = 1, overflow = TextOverflow.Ellipsis)
                }
            }
            // Cirlce i
            if (showCircleI) {
                Row (
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(end = 8.dp),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.End
                ) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(color = gray200),
                        contentAlignment = Alignment.Center
                    ) {
                        Box(
                            modifier = Modifier
                                .size(25.dp)
                                .clip(CircleShape)
                                .background(color = gray600),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                modifier = Modifier.fillMaxSize(),
                                text = "i",
                                fontWeight = FontWeight.Bold,
                                fontSize = MaterialTheme.typography.titleLarge.fontSize,
                                color = gray100,
                                textAlign = TextAlign.Center
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Ribbon(
    paddingStart: Dp = 0.dp,
    paddingTop: Dp = 0.dp,
    paddingEnd: Dp = 0.dp,
    paddingBottom: Dp = 0.dp,
    left: Float = 0.0f,
    top: Float = 0.0f
) {
    Box(
        modifier = Modifier
            .padding(
                start = paddingStart,
                top = paddingTop,
                end = paddingEnd,
                bottom = paddingBottom
            )
            .size(36.dp)
            .background(gray200.copy(alpha = 0.6f))
            .drawBehind {
                val path = Path().apply {
                    moveTo(75f, 126f)
                    lineTo(0f, 166f)
                    lineTo(0f, 126f)
                    moveTo(45f, 126f)
                    lineTo(125f, 126f)
                    lineTo(125f, 166f)
                    close()
                }
                this.translate(left = left, top = top, {})
                drawPath(path, color = gray200.copy(alpha = 0.6f))
            },
        contentAlignment = Alignment.Center
    ) {
        Icon(
            imageVector = Icons.Outlined.Add,
            contentDescription = "add favorite"
        )
    }
}

@Composable
fun ImageAsync(
    clip: Boolean = false,
    contentDescription: String,
    aspectRatio: Float = 2f / 3f,
    imgPath: String = Retrofit.IMAGE_PATH,
    backDropPath: String
) {
    SubcomposeAsyncImage(
        model = "${ Retrofit.BASE_IMAGE_URL }${ imgPath }${ backDropPath }",
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(aspectRatio, true)
            .clip(
                if (clip) {
                    RoundedCornerShape(16.dp)
                } else {
                    RoundedCornerShape(0.dp)
                }
            ),
        contentScale = if (clip) {
            ContentScale.Crop
        } else {
            ContentScale.FillBounds
        },
        contentDescription = contentDescription,
        loading = { isLoading() }
    )
}

@Composable
fun Tags(txt: String) {
    Box(
        modifier = Modifier
            .shadow(
                color = gray600,
                offsetX = 0.dp,
                offsetY = 0.dp,
                blurRadius = 2.dp,
                blurRadiusFilter = "NORMAL"
            )
    ) {
        Box(
            modifier = Modifier
                .shadow(
                    color = gray300,
                    offsetX = 0.dp,
                    offsetY = 0.dp,
                    blurRadius = 2.dp,
                    blurRadiusFilter = "INNER"
                )
        ) {
            Text(
                modifier = Modifier.padding(all = 8.dp),
                text = txt,
                color = Color.White
            )
        }
    }
}

@Composable
fun Pager(images: State<Images?>) {
    val pagerState = rememberPagerState { images.value?.backdrops?.size!! }

    Row (modifier = Modifier
        .fillMaxWidth()
        .padding(bottom = 16.dp)) {
        Column {
            HorizontalPager(
                state = pagerState,
                modifier = Modifier.fillMaxWidth()
            ) { i ->
                Card(modifier = Modifier.fillMaxWidth()) {
                    ImageAsync(
                        backDropPath = when (i) {
                            0 -> images.value?.backdrops!![0].file_path
                            1 -> images.value?.backdrops!![1].file_path
                            2 -> images.value?.backdrops!![2].file_path
                            3 -> images.value?.backdrops!![3].file_path
                            else -> images.value?.backdrops!![4].file_path
                        },
                        aspectRatio = when (i) {
                            0 -> images.value?.backdrops!![0].aspect_ratio
                            1 -> images.value?.backdrops!![1].aspect_ratio
                            2 -> images.value?.backdrops!![2].aspect_ratio
                            3 -> images.value?.backdrops!![3].aspect_ratio
                            else -> images.value?.backdrops!![4].aspect_ratio
                        },
                        contentDescription = ""
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun Carosuel(images: State<Images?>) {
    var rowSize by remember {
        mutableStateOf(Size.Zero)
    }
    val state = rememberCarouselState { 5 }

    Row (
        modifier = Modifier
            .height(400.dp)
            .fillMaxWidth()
            .onGloballyPositioned { layoutCoordinates ->
                rowSize = layoutCoordinates.size.toSize()
            }
    ) {
        val width = LocalDensity.current.run { rowSize.width.toDp() }

        Column {
            HorizontalMultiBrowseCarousel(
                state = state,
                preferredItemWidth = width,
                itemSpacing = 0.dp
            ) {i ->
                Box(modifier = Modifier.fillMaxSize()) {
                    ImageAsync(
                        backDropPath = when (i) {
                            0 -> images.value?.backdrops!![0].file_path
                            1 -> images.value?.backdrops!![1].file_path
                            2 -> images.value?.backdrops!![2].file_path
                            3 -> images.value?.backdrops!![3].file_path
                            else -> images.value?.backdrops!![4].file_path
                        },
                        aspectRatio = when (i) {
                            0 -> images.value?.backdrops!![0].aspect_ratio
                            1 -> images.value?.backdrops!![1].aspect_ratio
                            2 -> images.value?.backdrops!![2].aspect_ratio
                            3 -> images.value?.backdrops!![3].aspect_ratio
                            else -> images.value?.backdrops!![4].aspect_ratio
                        },
                        contentDescription = ""
                    )
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

// why the need to create a entirly new domain module?
// why to make from api() -> implementation() to not expose that stuff to app
