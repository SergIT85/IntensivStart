package ru.androidschool.intensiv.data.dto

import com.google.gson.annotations.SerializedName

data class MovieDetails(
    val id: String?,
    val genres: List<Genre>,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("production_companies")
    val productionCompanies: List<Studio>
)
