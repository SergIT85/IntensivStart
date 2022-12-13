package ru.androidschool.intensiv.data

import com.google.gson.annotations.SerializedName

data class MovieDetails(
    val id: String?,
    val genres: List<String>,
    @SerializedName("release_date")
    val releaseDate: String,
    @SerializedName("production_companies")
    val productionCompanies: List<String>
)
