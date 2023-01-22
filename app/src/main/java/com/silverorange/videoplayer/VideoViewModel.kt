package com.silverorange.videoplayer

import android.util.Log
import androidx.lifecycle.ViewModel
import com.silverorange.videoplayer.models.VideoModel
import com.silverorange.videoplayer.services.VideoService
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.schedulers.Schedulers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow


class VideoViewModel : ViewModel() {

    private val _mainViewState: MutableStateFlow<MainViewState> =
        MutableStateFlow(MainViewState(videosList = emptyList()))
    val mainViewState = _mainViewState.asStateFlow()
    private val videoService = VideoService()
    private val _compositeDisposable = CompositeDisposable()

    init {
        val videosList: MutableList<VideoModel> = mutableListOf()
        val videos = videoService.videoServiceAPI(videoService.retrofit).getVideos().map { it ->
            if (it.isSuccessful) {
                it.body()?.forEach { video ->
                    videosList.add(video)
                }
                _mainViewState.value.videosList = videosList
            }
        }.subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeBy(
                onError = {
                    Log.d("retrieve Videos", "${it.stackTrace}")
                })

        _compositeDisposable.add(videos)

    }

    override fun onCleared() {
        super.onCleared()
        if (!_compositeDisposable.isDisposed) _compositeDisposable.dispose()
    }

}