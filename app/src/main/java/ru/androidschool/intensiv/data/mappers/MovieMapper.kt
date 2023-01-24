package ru.androidschool.intensiv.data.mappers

import ru.androidschool.intensiv.data.dto.Movie
import ru.androidschool.intensiv.data.dto.MovieResponse
import ru.androidschool.intensiv.data.vo.MovieVo

object MovieMapper {
    fun toValueObject(dto: MovieResponse): List<MovieVo> {
        return dto.results.map { toValueObject(it) }
    }

    private fun toValueObject(dto: Movie): MovieVo {
        return MovieVo(
            id = dto.id,
            name = dto.name,
            originalTitle = dto.originalTitle,
            overview = dto.overview,
            popularity = dto.popularity,
            posterPath = dto.posterPath,
            releaseDate = dto.releaseDate,
            title = dto.title,
            voteAverage = dto.voteAverage

        )
    }
}
