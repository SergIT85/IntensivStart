package ru.androidschool.intensiv.data.repository

import io.reactivex.Single
import ru.androidschool.intensiv.Domain.repository.MovieDomainRepository
import ru.androidschool.intensiv.data.mappers.MovieMapper
import ru.androidschool.intensiv.data.network.MovieApiClient
import ru.androidschool.intensiv.data.vo.MovieVo

class TvShowRemoteRepository : MovieDomainRepository {

    override fun getMovies(): Single<List<MovieVo>> {
        return MovieApiClient.apiClient.getTvPopular()
            .map { MovieMapper.toValueObject(it) }
    }
}
