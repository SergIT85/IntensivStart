package ru.androidschool.intensiv.Domain.usecase

import io.reactivex.Single
import ru.androidschool.intensiv.Domain.repository.MovieDomainRepository
import ru.androidschool.intensiv.data.vo.MovieVo
import ru.androidschool.intensiv.extension.extSingle

class TvShowsUseCase(private val repository: MovieDomainRepository) {

    fun getMovies(): Single<List<MovieVo>> {
        return repository.getMovies()
            .extSingle()
    }
}
