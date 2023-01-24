package ru.androidschool.intensiv.data.repository

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import ru.androidschool.intensiv.Presentation.watchlist.MoviePreviewItem
import ru.androidschool.intensiv.data.dto.MovieDatabase
import ru.androidschool.intensiv.data.dto.MovieEntity
import ru.androidschool.intensiv.extension.extSingle
import timber.log.Timber

class WatchlistRepository(private val database: MovieDatabase) {

    @SuppressLint("CheckResult")
    fun getWatchlist(): LiveData<List<MovieEntity>> {
        return database.movieDao().getListViewModelMovieEntity()
    }
    private fun openMovie(action: MovieEntity) {
    }
}