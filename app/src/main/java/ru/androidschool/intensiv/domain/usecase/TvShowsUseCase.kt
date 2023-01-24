package ru.androidschool.intensiv.domain.usecase

import io.reactivex.Single
import ru.androidschool.intensiv.domain.repository.MovieDomainRepository
import ru.androidschool.intensiv.data.vo.MovieVo
import ru.androidschool.intensiv.extension.extSingle

class TvShowsUseCase(private val repository: MovieDomainRepository) {

    fun getMovies(): Single<List<MovieVo>> {
        return repository.getMovies()
            .extSingle()
    }
}
