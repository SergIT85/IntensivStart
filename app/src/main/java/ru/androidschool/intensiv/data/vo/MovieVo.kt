package ru.androidschool.intensiv.data.vo

data class MovieVo(
    val name: String,
    val id: Int,
    val originalTitle: String?,
    val overview: String?,
    val popularity: Double,
    val posterPath: String?,
    val releaseDate: String?,
    val voteAverage: Double,
    val title: String?,
) {
    val rating: Float
        get() = voteAverage.div(2).toFloat()
}
