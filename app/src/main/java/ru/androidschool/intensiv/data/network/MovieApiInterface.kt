package ru.androidschool.intensiv.data.network

import io.reactivex.Observable
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query
import ru.androidschool.intensiv.BuildConfig.THE_MOVIE_DATABASE_API
import ru.androidschool.intensiv.data.repository.ActorResponse
import ru.androidschool.intensiv.data.dto.MovieDetails
import ru.androidschool.intensiv.data.dto.MovieResponse
import ru.androidschool.intensiv.data.dto.SearchResult

interface MovieApiInterface {

    @GET("movie/now_playing/")
    fun getMovieNowPlaying(
        @Query("api_key") apiKey: String = THE_MOVIE_DATABASE_API,
        @Query("language") language: String = LANGUAGE,
        @Query("page") page: String = PAGE
    ): Single<MovieResponse>

    // реализовать остальыне гетыры
    @GET("movie/popular/")
    fun getMoviePopular(
        @Query("api_key") apiKey: String = THE_MOVIE_DATABASE_API,
        @Query("language") language: String = LANGUAGE,
        @Query("page") page: String = PAGE
    ): Single<MovieResponse>

    @GET("movie/upcoming/")
    fun getUpcoming(
        @Query("api_key") apiKey: String = THE_MOVIE_DATABASE_API,
        @Query("language") language: String = LANGUAGE,
        @Query("page") page: String = PAGE
    ): Single<MovieResponse>

    @GET("tv/popular/")
    fun getTvPopular(
        @Query("api_key") apiKey: String = THE_MOVIE_DATABASE_API,
        @Query("language") language: String = LANGUAGE,
        @Query("page") page: String = PAGE
    ): Single<MovieResponse>

    @GET("movie/{movie_id}")
    fun getMovieDetails(
        @Path("movie_id") id: Int,
        @Query("api_key") apiKey: String = THE_MOVIE_DATABASE_API,
        @Query("language") language: String = LANGUAGE
    ): Single<MovieDetails>
    @GET("movie/{movie_id}/credits")
    fun getCastActor(
        @Path("movie_id") id: Int,
        @Query("api_key") apiKey: String = THE_MOVIE_DATABASE_API,
        @Query("language") language: String = LANGUAGE
    ): Single<ActorResponse>
    @GET("search/movie")
    fun getSearchMovie(
        @Query("api_key") apiKey: String,
        @Query("language") language: String,
        @Query("query") query: String
    ): Observable<SearchResult>

    companion object {
        const val LANGUAGE = "ru"
        const val API_KEY = THE_MOVIE_DATABASE_API
        const val PAGE = "1"
    }
}
