package com.silverorange.videoplayer

import android.os.Bundle
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.FrameLayout
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Scaffold
import androidx.compose.material.Text
import androidx.compose.material.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.C
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.datasource.DataSource
import androidx.media3.datasource.DefaultDataSource
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.exoplayer.source.ProgressiveMediaSource
import androidx.media3.ui.AspectRatioFrameLayout

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val viewModel = VideoViewModel()
            val viewState: MainViewState = viewModel.mainViewState.collectAsState().value
            Scaffold(topBar = { TopBar() }) {
                Column(Modifier.fillMaxSize()) {
                    VideoPlayerBox(viewState)
                    Column(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .verticalScroll(rememberScrollState())
                    ) {

                        TextCard(viewState)
                    }
                }

            }


        }
    }
}

@Composable
fun TopBar() {
    TopAppBar(title = { Text(text = "Video Player") })
}


@Composable
fun VideoPlayerBox(state: MainViewState) {
    val url =
        if (state.videosList.isNotEmpty()) state.videosList.first().fullURL else ""
    Box(modifier = Modifier.height(250.dp)) {
        VideoPlayer(url)
    }
}

@Composable
fun TextCard(
    state: MainViewState
) {
    val title =
        if (state.videosList.isNotEmpty()) state.videosList.first().title else ""
    val author =
        if (state.videosList.isNotEmpty()) state.videosList.first().author.name else ""
    val description =
        if (state.videosList.isNotEmpty()) state.videosList.first().description else ""
    Column(
        modifier = Modifier
            .fillMaxHeight()
            .padding(bottom = 8.dp),
    ) {

        Text(
            text = title, fontSize = 32.sp,
            fontWeight = FontWeight.Bold, modifier = Modifier.padding(vertical = 8.dp)
        )
        Text(
            text = author,
            fontSize = 22.sp,
            fontWeight = FontWeight.SemiBold,
            modifier = Modifier.padding(bottom = 8.dp)
        )
        Text(text = description)
    }
}


@Composable
fun VideoPlayer(url: String) {
    val context = LocalContext.current

    val exoPlayer = remember {
        ExoPlayer.Builder(context)
            .build()
            .apply {
                val defaultDataSourceFactory = DefaultDataSource.Factory(context)
                val dataSourceFactory: DataSource.Factory = DefaultDataSource.Factory(
                    context,
                    defaultDataSourceFactory
                )

                val source = ProgressiveMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(MediaItem.fromUri(url))

                setMediaSource(source)
                prepare()
            }
    }

    exoPlayer.playWhenReady = url != ""
    exoPlayer.videoScalingMode = C.VIDEO_SCALING_MODE_SCALE_TO_FIT
    exoPlayer.repeatMode = Player.REPEAT_MODE_ONE

    DisposableEffect(
        AndroidView(factory = {
            androidx.media3.ui.PlayerView(context).apply {
                hideController()
                useController = false
                resizeMode = AspectRatioFrameLayout.RESIZE_MODE_FIT

                player = exoPlayer
                layoutParams = FrameLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
            }
        })
    ) {
        onDispose { exoPlayer.release() }
    }
}