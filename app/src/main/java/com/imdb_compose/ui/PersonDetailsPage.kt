package com.imdb_compose.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.compose.ripeMango
import com.imdb_compose.ImageAsync
import com.imdb_compose.domain.ActorDetail
import com.imdb_compose.domain.Async
import com.imdb_compose.isError
import com.imdb_compose.isLoading

@Composable
fun PersonDetailsPage(
    personDetails: Async<ActorDetail>,
    name: String,
    navController: NavController,
    clickHandlerBackButton: () -> Unit
) {
    when(personDetails) {
        is Async.Init, is Async.Loading -> {
            Box(
                modifier = Modifier
                    .fillMaxSize(),
                contentAlignment = Alignment.Center
            ) {
                Column {
                    isLoading()
                    Text("Loading...")
                }
            }
        }
        is Async.Success -> {
            personDetails.data.let { details ->
                Column(
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .fillMaxSize()
                        .shadow(8.dp)
                ) {
                    // Name & Known For
                    Column(modifier = Modifier) {
                        Text(
                            text = details.name,
                            fontStyle = MaterialTheme.typography.headlineLarge.fontStyle
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        details.known_for_department?.let {
                            println(it);
                            Text(
                                text = it,
                                fontStyle = MaterialTheme.typography.titleMedium.fontStyle
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                    }
                    // Profile Pic & Bio
                    Row(modifier = Modifier.fillMaxWidth()) {
                        Column(modifier = Modifier.width(170.dp)) {
                            ImageAsync(
                                backDropPath = details.profile_path,
                                contentDescription = "",
                                clip = true,
                                crop = false
                            )
                        }

                        Column(modifier = Modifier.padding(start = 8.dp)) {
                            Text(
                                text = details.biography,
                                maxLines = 6,
                                overflow = TextOverflow.Ellipsis
                            )
                            Text(text = "Born: ${ details.birthday }")
                        }
                    }
                    // Add to favorits
                    Row(modifier = Modifier) {
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp)
                                .padding(top = 16.dp),
                            shape = RoundedCornerShape(4.dp),
                            onClick = { /*TODO*/ },
                            colors = ButtonColors(
                                containerColor = ripeMango,
                                contentColor = Color.White,
                                disabledContainerColor = Color.Red,
                                disabledContentColor = Color.White,
                            )
                        ) {
                            Icon(
                                imageVector = Icons.Outlined.Add,
                                contentDescription = "",
                                tint = Color.Black
                            )
                            Text(text = "  Add to favorites", color = Color.Black)
                        }
                    }
                }
            }
        }
        else -> {
            if (personDetails is Async.Error) {
                isError()
            }
        }
    }
}
