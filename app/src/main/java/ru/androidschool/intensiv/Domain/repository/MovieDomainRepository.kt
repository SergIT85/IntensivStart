package ru.androidschool.intensiv.Domain.repository

import io.reactivex.Single

import ru.androidschool.intensiv.data.vo.MovieVo

interface MovieDomainRepository {
    fun getMovies(): Single<List<MovieVo>>
}
