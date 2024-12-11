package com.imdb_compose.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.imdb_compose.domain.Async
import com.imdb_compose.domain.VideoClipList
import com.imdb_compose.isError
import com.imdb_compose.isLoading

@Composable
fun PlayClipPage(movieClip: Async<VideoClipList>) {
    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {

        if (movieClip is Async.Init || movieClip is Async.Loading) {
            isLoading()
        } else if (movieClip is Async.Success) {
            LazyColumn {
                item {
                    Text(text = "${ movieClip.data.toString() }")
                }
            }
        } else {
            isError()
        }
    }
}
