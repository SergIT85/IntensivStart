package ru.androidschool.intensiv.data.repository

import android.annotation.SuppressLint
import io.reactivex.Observable
import ru.androidschool.intensiv.data.roomdata.MovieDatabase
import ru.androidschool.intensiv.data.roomdata.MovieEntity

class WatchlistRepository(private val database: MovieDatabase) {

    @SuppressLint("CheckResult")
    fun getWatchlist(): Observable<List<MovieEntity>> {
        return database.movieDao().getListViewModelMovieEntity()
    }
}