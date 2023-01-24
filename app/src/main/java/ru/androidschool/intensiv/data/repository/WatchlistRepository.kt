package ru.androidschool.intensiv.data.repository

import android.annotation.SuppressLint
import androidx.lifecycle.LiveData

import ru.androidschool.intensiv.data.roomdata.MovieDatabase
import ru.androidschool.intensiv.data.roomdata.MovieEntity
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