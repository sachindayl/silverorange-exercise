package com.silverorange.videoplayer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel = VideoViewModel()
            val viewState: MainViewState = viewModel.mainViewState.collectAsState().value
            TopBar()
            TextCard(viewState)
        }
    }
}

@Composable
fun TopBar() {
    TopAppBar(title = { Text(text = "Video Player") })
}

@Composable
fun TextCard(
    state: MainViewState
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {

        val description =
            if (state.videosList.isNotEmpty()) state.videosList.first().description else ""
        Text(text = description)
    }
}
