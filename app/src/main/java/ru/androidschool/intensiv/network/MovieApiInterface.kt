package ru.androidschool.intensiv.network

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.androidschool.intensiv.BuildConfig.THE_MOVIE_DATABASE_API
import ru.androidschool.intensiv.data.ActorResponse
import ru.androidschool.intensiv.data.MovieDetails
import ru.androidschool.intensiv.data.MovieResponse

const val LANGUAGE = "ru"

interface MovieApiInterface {

    @GET("movie/now_playing/")
    fun getMovieNowPlaying(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: String
    ): Call<MovieResponse>

    // реализовать остальыне гетыры
    @GET("movie/popular/")
    fun getMoviePopular(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: String
    ): Call<MovieResponse>

    @GET("tv/popular/")
    fun getTvPopular(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("page") page: String
    ): Call<MovieResponse>

    @GET("movie/{movie_id}")
    fun getMovieDetails(
        @Path("movie_id") id: Int,
        @Query("api_key") apiKey: String = THE_MOVIE_DATABASE_API,
        @Query("language") language: String = LANGUAGE
    ): Call<MovieDetails>
    @GET("movie/{movie_id}/credits")
    fun getCastActor(
        @Path("movie_id") id: Int,
        @Query("api_key") apiKey: String = THE_MOVIE_DATABASE_API,
        @Query("language") language: String = LANGUAGE
    ): Call<ActorResponse>
}
