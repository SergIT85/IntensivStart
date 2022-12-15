package ru.androidschool.intensiv.data

import com.google.gson.annotations.SerializedName

data class MovieResponse(
    var page: Int,
    var results: List<Movie>,
    @SerializedName("total_results")
    var totalResult: Int,
    @SerializedName("total_pages")
    var totalPages: Int
)
