package com.silverorange.videoplayer.services

import com.silverorange.videoplayer.models.VideoModel
import io.reactivex.Single
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Headers

interface VideoServiceAPI {

    interface IVideoServiceAPI {
        @Headers("Content-Type: application/json")
        @GET("videos")
        fun getVideos(): Single<Response<List<VideoModel>>>
    }

}