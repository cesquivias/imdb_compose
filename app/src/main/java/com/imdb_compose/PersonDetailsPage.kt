package com.imdb_compose

import android.annotation.SuppressLint
import android.graphics.drawable.shapes.RoundRectShape
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
import coil.compose.AsyncImage
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition", "StateFlowValueCalledInComposition")
@Composable
fun PersonDetailsPage(
    person: String,
    id: Int,
    viewModel: HomeScreenViewModel,
    navController: NavController,
    clickHandlerBackBtn: () -> Unit
) {
    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = Color.Transparent,
        topBar = {
            TopBarWithBackBtn(person, clickHandlerBackBtn)
        },
        bottomBar = {
            BottomBar(navController)
        }
    ) { paddingValues ->
        viewModel.viewModelScope.launch {
            viewModel.getPersonDetails(id)
        }

        if (viewModel.personDetails.value?.id == id) {
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                val personDetails = viewModel.personDetails.collectAsState()
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(horizontal = 16.dp)
                ) {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .shadow(
                                color = gray500,
                                offsetX = 0.dp,
                                offsetY = 0.dp,
                                blurRadiusFilter = "SOLID"
                            ),
                    ) {
                        personDetails.value?.let {
                            Box(modifier = Modifier.padding(start = 8.dp, top = 16.dp, end = 8.dp)) {
                                Column {
                                    // Name & Known For
                                    Column {
                                        Text(
                                            text = it.name,
                                            fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                                            fontWeight = MaterialTheme.typography.headlineLarge.fontWeight,
                                            fontStyle = MaterialTheme.typography.headlineLarge.fontStyle,
                                            fontFamily = MaterialTheme.typography.headlineLarge.fontFamily
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                        Text(
                                            text = it.known_for_department,
                                            fontSize = MaterialTheme.typography.titleMedium.fontSize,
                                            fontWeight = MaterialTheme.typography.titleMedium.fontWeight,
                                            fontStyle = MaterialTheme.typography.titleMedium.fontStyle,
                                            fontFamily = MaterialTheme.typography.titleMedium.fontFamily
                                        )
                                        Spacer(modifier = Modifier.height(8.dp))
                                    }
                                    // Profile Pic & Bio
                                    Row (
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(240.dp)
                                    ) {
                                        Box(modifier = Modifier.width(125.dp)) {
                                            AsyncImage(
                                                model = "${Retrofit.BASE_IMAGE_URL}${Retrofit.IMAGE_PATH}${it.profile_path}",
                                                contentDescription = null,
                                                contentScale = ContentScale.FillWidth
                                            )
                                        }

                                        Box(modifier = Modifier.padding(start = 8.dp)) {
                                            Column {
                                                if (it.biography != null) {
                                                    Text(
                                                        text = it.biography,
                                                        maxLines = 6,
                                                        overflow = TextOverflow.Ellipsis
                                                    )
                                                }
                                                if (it.birthday != null) {
                                                    Text(text = "Born: ${ it.birthday }")
                                                }
                                            }
                                        }
                                    }
                                    // Add to favorits
                                    Button(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(40.dp),
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
                                        Text(text = "  Add to favorites", color = Color.Black)
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } else {
            Box(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column {
                    isLoading()
                    Text("Loading...")
                }
            }
        }
    }
}
