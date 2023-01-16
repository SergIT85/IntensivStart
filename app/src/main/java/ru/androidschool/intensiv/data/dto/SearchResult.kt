package ru.androidschool.intensiv.data.dto

import com.google.gson.annotations.SerializedName

data class SearchResult(
    val page: Int,
    val results: List<Movie>,
    @SerializedName("total_results")
    val totalResults: Int,
    @SerializedName("total_pages")
    val totalPages: Int
)
