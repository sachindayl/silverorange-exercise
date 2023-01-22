package com.silverorange.videoplayer.models

data class VideoModel (
    val id: String,
    val title: String,
    val hlsURL: String,
    val fullURL: String,
    val description: String,
    val publishedAt: String,
    val author: Author
)
