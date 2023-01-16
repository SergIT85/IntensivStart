package ru.androidschool.intensiv.data.dto

import androidx.room.*

@Entity(tableName = "MovieEntity")
data class MovieEntity(
    @PrimaryKey
    @ColumnInfo(name = "movieId")
    val id: Long,

    @ColumnInfo(name = "title")
    val title: String?,

    @ColumnInfo(name = "posterPath")
    val posterPath: String?,

    @ColumnInfo(name = "overview")
    val overview: String?,

    @ColumnInfo(name = "rating")
    val rating: Float?,

)
