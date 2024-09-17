package com.imdb_compose

import android.annotation.SuppressLint
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavController
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
                Column {
                    Text("Person Details")
                    personDetails.value?.let { Text(it.name) }
                    personDetails.value?.let { Text(it.biography) }
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
                    Text("Loading...")
                }
            }
        }
    }
}
