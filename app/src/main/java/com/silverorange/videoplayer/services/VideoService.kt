package com.silverorange.videoplayer.services

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

interface IVideoService {
    fun retrofitSetup(): Retrofit
    fun videoServiceAPI(retrofit: Retrofit): VideoServiceAPI.IVideoServiceAPI
}

class VideoService : IVideoService {
    private val mUrl = "http://10.0.2.2:4000/"
    var retrofit: Retrofit
    init {
        retrofit = retrofitSetup()
    }
    override fun retrofitSetup(): Retrofit {
        val gSon = GsonBuilder()
            .setLenient()
            .create()
        val interceptor = HttpLoggingInterceptor()
        interceptor.level = HttpLoggingInterceptor.Level.BODY
        val client = OkHttpClient.Builder().addInterceptor(interceptor).build()
        return Retrofit.Builder()
            .baseUrl(mUrl)
            .client(client)
            .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gSon))
            .build()
    }

    override fun videoServiceAPI(retrofit: Retrofit): VideoServiceAPI.IVideoServiceAPI {
        return retrofit.create(VideoServiceAPI.IVideoServiceAPI::class.java)
    }

}

