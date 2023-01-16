package ru.androidschool.intensiv.Presentation.tvshows

import android.annotation.SuppressLint
import ru.androidschool.intensiv.Domain.usecase.TvShowsUseCase
import ru.androidschool.intensiv.Presentation.base.BasePresenter
import ru.androidschool.intensiv.data.vo.MovieVo
import timber.log.Timber

class TvShowsPresenter(private val useCase: TvShowsUseCase) :
    BasePresenter<TvShowsPresenter.TvShowsView>() {

    @SuppressLint("CheckResult")
    fun getTvPopular() {
        useCase.getMovies()
            .subscribe({
                var tvShowsList: ArrayList<TvShowsItem> = ArrayList()
                tvShowsList.clear()

                tvShowsList = it.map {
                    TvShowsItem(it) { movie ->
                        view?.openMovieDetails(movie)
                    }
                }.toList() as ArrayList<TvShowsItem>
                view?.showMovies(tvShowsList)
            },
                { t ->
                    Timber.e(t, t.toString())
                    view?.showEmptyMovies()
                })
    }

        interface TvShowsView {
            fun showMovies(movies: ArrayList<TvShowsItem>)
            fun showEmptyMovies()
            fun openMovieDetails(movie: MovieVo)
        }
}
