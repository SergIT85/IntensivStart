package ru.androidschool.intensiv.data.repository

import ru.androidschool.intensiv.data.dto.Movie

object MockRepository {

    fun getMovies(): List<Movie> {

        val moviesList = mutableListOf<Movie>()
        for (x in 0..10) {
            val movie = Movie(
                title = "Spider-Man $x",
                voteAverage = 10.0 - x,
                // posterPath = "posterPath",
                isAdult = true,
                overview = "overview",
                releaseDate = "1999",
                genreIds = listOf(1 + x, 2 + x, 3 + x),
                id = 1 + x,
                name = "name",
                originalTitle = "originalTitle",
                backdropPath = "https://m.media-amazon.com/images/M/MV5BYTk3MDljOWQtNGI2My00OTEzLTlhY" +
                        "jQtOTQ4ODM2MzUwY2IwXkEyXkFqcGdeQXVyNTIzOTk5ODM@._V1_.jpg",
                popularity = 10.0 + x,
                voteCount = 1 + x,
                video = true,
                originalLanguage = "language",
                dates = listOf("1999", "2000")

            )
            moviesList.add(movie)
        }

        return moviesList
    }
}
