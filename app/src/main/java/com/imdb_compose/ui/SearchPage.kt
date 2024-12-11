package com.imdb_compose.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.TextFieldState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Delete
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.imdb_compose.R
import com.imdb_compose.domain.Async
import com.imdb_compose.isError
import com.imdb_compose.isLoading
import com.imdb_compose.ui.viewmodel.SearchPageViewModel

@Composable
fun SearchPage() {
    val viewModel: SearchPageViewModel = hiltViewModel()

    val textFieldSearch by viewModel.textFieldSearchState.collectAsState()
    val searchResult by viewModel.searchResult.collectAsState()

    var searchString by remember { mutableStateOf(textFieldSearch) }
    var result by remember { mutableStateOf(searchResult) }

    val interactionSource = remember { MutableInteractionSource() }

    Box(modifier = Modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            Row (
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 8.dp)
                    .height(40.dp)
                    .background(
                        color = MaterialTheme.colorScheme.surface,
                        shape = RoundedCornerShape(4.dp)
                    ),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                IconButton({ viewModel.getSearchResults(searchString.text.toString()) }) {
                    Icon(
                        imageVector = Icons.Outlined.Search,
                        modifier = Modifier.padding(start = 4.dp),
                        contentDescription = stringResource(R.string.search_tmdb),
                        tint = MaterialTheme.colorScheme.onSurface
                    )
                }
                BasicTextField(
                    state = searchString,
                    modifier = Modifier.fillMaxWidth(0.80f),
                    textStyle = MaterialTheme.typography.bodyLarge,
                    interactionSource = interactionSource,
                    lineLimits = TextFieldLineLimits.SingleLine,
                    keyboardOptions = KeyboardOptions(autoCorrectEnabled = false)
                )
                AnimatedVisibility(visible = searchString.text.isNotBlank()) {
                    IconButton({ searchString = TextFieldState(); }) {
                        Icon(
                            imageVector = Icons.Outlined.Delete,
                            modifier = Modifier.padding(start = 8.dp),
                            contentDescription = stringResource(R.string.delete_search),
                            tint = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }

            when(searchResult) {
                is Async.Init -> {
                    Text(
                        text = searchString.text.toString(),
                        modifier = Modifier,
                        color = MaterialTheme.colorScheme.surface,
                        textAlign = TextAlign.Center,
                        style = MaterialTheme.typography.displayMedium
                    )
                }
                is Async.Loading -> {
                    isLoading()
                }
                is Async.Success -> {
                    LazyColumn {
                        item {
                            Text(
                                text = result.data.toString(),
                                modifier = Modifier,
                                color = MaterialTheme.colorScheme.surface,
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.displaySmall
                            )
                            Text(
                                text = searchResult.data.toString(),
                                modifier = Modifier,
                                color = MaterialTheme.colorScheme.surface,
                                textAlign = TextAlign.Center,
                                style = MaterialTheme.typography.displaySmall
                            )
                        }
                    }
                }
                else -> {
                    if (searchResult is Async.Error) {
                        isError()
                    }
                }
            }
        }
    }
}
