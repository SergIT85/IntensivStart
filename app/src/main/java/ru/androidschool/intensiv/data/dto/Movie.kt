package ru.androidschool.intensiv.data.dto

import com.google.gson.annotations.SerializedName

data class Movie(
    val name: String,
    @SerializedName("adult")
    val isAdult: Boolean,
    val overview: String?,
    @SerializedName("release_date")
    val releaseDate: String?,
    @SerializedName("genre_ids")
    val genreIds: List<Int>,
    val id: Int,
    @SerializedName("original_title")
    val originalTitle: String?,
    @SerializedName("original_language")
    val originalLanguage: String?,
    val title: String?,
    @SerializedName("backdrop_path")
    val backdropPath: String?,
    val popularity: Double,
    @SerializedName("vote_count")
    val voteCount: Int,
    val video: Boolean,
    @SerializedName("vote_average")
    val voteAverage: Double,
    @SerializedName("dates")
    val dates: List<String>
) {
    val rating: Float
        get() = voteAverage.div(2).toFloat()
    @SerializedName("poster_path")
    var posterPath: String? = null
        get() = "https://image.tmdb.org/t/p/w500$field"
}
