package ru.androidschool.intensiv.extension

import android.annotation.SuppressLint
import io.reactivex.Completable
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

@SuppressLint("CheckResult")
fun <T> Observable<T>.extObservable(): Observable<T> {
    return this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

@SuppressLint("CheckResult")
fun <T> Single<T>.extSingle(): Single<T> {
    return this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

fun Completable.extCompletable(): Completable {
    return this.subscribeOn(Schedulers.io())
        .observeOn(AndroidSchedulers.mainThread())
}

// не получается создать эту логику в extension функции т.к. не могу реализовать логику
// открытия openMovieDetails(movies) из вынесенной функции. Буду думать дальше.

/*fun<T> List<T>.createMainCardContainer(movie: List<Movie>): MainCardContainer {
    val listMovie = listOf(movie.map {
        MovieItem(it) { movies ->
            FeedFragment.openMovieDetails(movies)
        }
    }. let {
        return MainCardContainer(
            R.string.popular,
            it.toList())
    })
}*/
