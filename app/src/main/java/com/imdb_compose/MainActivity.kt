@file:OptIn(ExperimentalMaterial3Api::class)
package com.imdb_compose

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.SystemBarStyle
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
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
import androidx.compose.material.icons.outlined.Add
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
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarColors
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.carousel.HorizontalMultiBrowseCarousel
import androidx.compose.material3.carousel.rememberCarouselState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.translate
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.toSize
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import coil.compose.SubcomposeAsyncImage
import com.example.compose.AppTheme
import com.example.compose.gray100
import com.example.compose.gray200
import com.example.compose.gray300
import com.example.compose.gray600
import com.imdb_compose.domain.Images
import com.imdb_compose.domain.Resources
import com.imdb_compose.ui.CategoryPage
import com.imdb_compose.ui.Destination
import com.imdb_compose.ui.HomePage
import com.imdb_compose.ui.MovieDetailsPage
import com.imdb_compose.ui.PersonDetailsPage
import com.imdb_compose.ui.PlayClipPage
import com.imdb_compose.ui.SearchPage
import com.imdb_compose.ui.TvDetailsPage
import com.imdb_compose.ui.viewmodel.CategoryPageViewModel
import com.imdb_compose.ui.viewmodel.HomeScreenViewModel
import com.imdb_compose.ui.viewmodel.MovieDetailsPageViewModel
import com.imdb_compose.ui.viewmodel.PersonDetailsPageViewModel
import com.imdb_compose.ui.viewmodel.PlayClipViewModel
import com.imdb_compose.ui.viewmodel.TvDetailsPageViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            enableEdgeToEdge(
                statusBarStyle = SystemBarStyle.auto(
                    MaterialTheme.colorScheme.primary.toArgb(),
                    MaterialTheme.colorScheme.primary.toArgb()
                ),
                navigationBarStyle = SystemBarStyle.auto(
                    MaterialTheme.colorScheme.primary.toArgb(),
                    MaterialTheme.colorScheme.primary.toArgb()
                )
            )
            AppTheme {
                Theme()
            }
        }
    }
}

@Composable
fun Theme() {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.secondary
    ) {
        val whatToWatch = stringResource(id = R.string.what_to_watch)
        var titleScreen by remember {
            mutableStateOf(whatToWatch)
        }

        val navController = rememberNavController()
        val viewModel: HomeScreenViewModel = hiltViewModel()

        Scaffold (
            modifier = Modifier.fillMaxSize(),
            containerColor = Color.Transparent,
            topBar = {
                TopBar(
                    titleScreen,
                    showUpButton = navController.previousBackStackEntry != null,
                    onUpClicked = { navController.navigateUp() }
                )
            },
            bottomBar = {
                BottomBar(
                    onClick = { destination ->
                        if (navController.graph.findStartDestination().hasRoute(destination::class)) {
                            navController.popBackStack(destination, false)
                        } else {
                            navController.navigate(destination) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                // on the back stack as users select items
                                popUpTo(navController.graph.startDestinationId) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination when
                                // reselecting the same item
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        }
                    }
                )
            }
        ) { paddingValues ->
            Box(modifier = Modifier
                .padding(paddingValues)
                .fillMaxSize()
            ) {
                NavHost(navController = navController, startDestination = Destination.HomePage ) {
                    composable<Destination.HomePage> {
                        titleScreen = stringResource(id = R.string.what_to_watch)
                        HomePage(
                            viewModel.catagories,
                            viewModel.noMovies.collectAsState(),
                            viewModel.movieListOfWeek.collectAsState(),
                            viewModel.trendingMovies.collectAsState(),
                            viewModel.airingTodayTv.collectAsState(),
                            viewModel.trendingTv.collectAsState(),
                            viewModel.upcomingMovies.collectAsState(),
                            viewModel.popularPersons.collectAsState(),
                            viewModel.trendingPersons.collectAsState(),
                            viewModel.boxOffice,
                            navController = navController
                        )
                    }
                    composable<Destination.SearchPage> {
                        titleScreen = stringResource(R.string.search)
                        SearchPage()
                    }
                    composable<Destination.PlayClipPage> {
                        val playClipViewModel: PlayClipViewModel = hiltViewModel()
                        LaunchedEffect(558449) {
                            viewModel.viewModelScope.launch {
                                playClipViewModel.getMovieClip(558449)
                            }
                        }
                        val movieClip by playClipViewModel.movieClip.collectAsState()
                        titleScreen = "Preview"
                        PlayClipPage(movieClip)
                    }
                    composable<Destination.CategoryPage> {
                        val args = it.toRoute<Destination.CategoryPage>()
                        val categoryPageViewModel: CategoryPageViewModel = hiltViewModel()
                        titleScreen = args.catagory
                        CategoryPage(args.catagory, navController = navController, { navController.popBackStack() })
                    }
                    composable<Destination.MovieDetailsPage> {
                        val args = it.toRoute<Destination.MovieDetailsPage>()
                        val movieDetailsPageViewModel: MovieDetailsPageViewModel = hiltViewModel()
                        LaunchedEffect(args.id) {
                            movieDetailsPageViewModel.getMovieDetails(args.id)
                            movieDetailsPageViewModel.getMovieImages(args.id)
                        }
                        val movieDetails by movieDetailsPageViewModel.movieDetails.collectAsState()
                        val movieImages by movieDetailsPageViewModel.movieImages.collectAsState()
                        titleScreen = args.title
                        MovieDetailsPage(movieDetails, movieImages, args.title, navController = navController, { navController.popBackStack() })
                    }
                    composable<Destination.PersonDetailsPage> {
                        val args = it.toRoute<Destination.PersonDetailsPage>()
                        val personDetailsPageViewModel: PersonDetailsPageViewModel = hiltViewModel()
                        LaunchedEffect(args.id) {
                            personDetailsPageViewModel.getPersonDetails(args.id)
                        }
                        val personDetails by personDetailsPageViewModel.personDetails.collectAsState()
                        titleScreen = args.name
                        PersonDetailsPage(personDetails, args.name, navController = navController, { navController.popBackStack() })
                    }
                    composable<Destination.TvDetailsPage> {
                        val args = it.toRoute<Destination.TvDetailsPage>()
                        val tvDetailsPageViewModel: TvDetailsPageViewModel = hiltViewModel()
                        LaunchedEffect(args.id) {
                            tvDetailsPageViewModel.getTvSeriesDetails(args.id)
                            tvDetailsPageViewModel.getTvSeriesImages(args.id)
                        }
                        val tvDetails by tvDetailsPageViewModel.tvDetails.collectAsState()
                        val tvImages by tvDetailsPageViewModel.tvImages.collectAsState()
                        titleScreen = args.show
                        TvDetailsPage(tvDetails, tvImages, navController = navController, { navController.popBackStack() })
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
fun InformationCircle(
    paddingStart: Dp = 0.dp,
    paddingTop: Dp = 0.dp,
    paddingEnd: Dp = 0.dp,
    paddingBottom: Dp = 0.dp,
) {
    Row (
        modifier = Modifier
            .fillMaxWidth()
            .padding(
                start = paddingStart,
                top = paddingTop,
                end = paddingEnd,
                bottom = paddingBottom
            ),
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
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun ImageAsync(
    clip: Boolean = false,
    crop: Boolean = false,
    contentDescription: String,
    aspectRatio: Float = 2f / 3f,
    imgPath: String = Resources.IMAGE_PATH,
    backDropPath: String
) {
    SubcomposeAsyncImage(
        model = "${ Resources.BASE_IMAGE_URL }${ imgPath }${ backDropPath }",
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(aspectRatio)
            .clip(
                if (clip) {
                    RoundedCornerShape(16.dp)
                } else {
                    RoundedCornerShape(0.dp)
                }
            ),
        contentScale = if (crop) {
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
    Box(modifier = Modifier.shadow(8.dp)) {
        Box(
            modifier = Modifier
                .shadow(8.dp)
                .background(color = gray300)
        ) {
            Text(
                modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp),
                text = txt,
                color = Color.White
            )
        }
    }
}

@Composable
fun Pager(images: Images?) {
    val pagerState = rememberPagerState { images?.backdrops?.size!! }
    HorizontalPager(
        state = pagerState,
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 16.dp)
    ) { i ->
        Card(modifier = Modifier.fillMaxWidth()) {
            ImageAsync(
                backDropPath = when (i) {
                    0 -> images?.backdrops!![0].file_path
                    1 -> images?.backdrops!![1].file_path
                    2 -> images?.backdrops!![2].file_path
                    3 -> images?.backdrops!![3].file_path
                    else -> images?.backdrops!![4].file_path
                },
                aspectRatio = when (i) {
                    0 -> images.backdrops[0].aspect_ratio
                    1 -> images.backdrops[1].aspect_ratio
                    2 -> images.backdrops[2].aspect_ratio
                    3 -> images.backdrops[3].aspect_ratio
                    else -> images.backdrops[4].aspect_ratio
                },
                contentDescription = ""
            )
        }
    }
}

@Composable
fun Carousel(images: State<Images?>) {
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
fun TopBar(
    title: String,
    showUpButton: Boolean,
    onUpClicked: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSecondary,
                style = MaterialTheme.typography.headlineLarge
            )
        },
        navigationIcon = {
            if (showUpButton) {
                IconButton(onClick = onUpClicked) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back),
                        tint = Color.White
                    )
                }
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
fun BottomBar(onClick: (Destination) -> Unit) {
    BottomAppBar(
        modifier = Modifier.fillMaxWidth(),
        actions = {
            Row (modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceEvenly) {
                IconButton(onClick = { onClick(Destination.HomePage) }) {
                    Icon(imageVector = Icons.Default.Home, contentDescription = "home")
                }
                IconButton(onClick = { onClick(Destination.SearchPage) }) {
                    Icon(imageVector = Icons.Default.Search, contentDescription = "search")
                }
                IconButton(onClick = { onClick(Destination.PlayClipPage) }) {
                    Icon(imageVector = Icons.Default.PlayArrow, contentDescription = "home")
                }
                IconButton(onClick = {}) {
                    Icon(imageVector = Icons.Default.Person, contentDescription = "profile")
                }
            }
        },
        containerColor = Color.Transparent
    )
}

@Composable
fun isLoading() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        CircularProgressIndicator(
            modifier = Modifier.fillMaxSize(fraction = 0.5f),
            color = MaterialTheme.colorScheme.outline
        )
    }
}

@Composable
fun isError() {
    Box(
        modifier = Modifier
            .padding()
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Column {
            isLoading()
            Text("errr... opps")
        }
    }
}
