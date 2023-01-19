package ru.androidschool.intensiv.Domain

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.room.*
import io.reactivex.Completable
import io.reactivex.Single
import ru.androidschool.intensiv.data.dto.MovieEntity

@Dao
interface MovieDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun save(movieEntity: MovieEntity): Completable

    @Delete
    fun delete(movieEntity: MovieEntity): Completable

    @Query("SELECT * FROM MovieEntity")
    fun getMovieEntity(): Single<List<MovieEntity>>

    @Query("SELECT * FROM MovieEntity")
    fun getListViewModelMovieEntity(): LiveData<List<MovieEntity>>

    @Query("DELETE FROM MovieEntity WHERE movieId = :movieId")
    fun deleteById(movieId: Long): Completable

    @Query("SELECT EXISTS (SELECT 1 FROM MovieEntity WHERE movieId = :movieId)")
    fun exists(movieId: Long): Single<Boolean>
}
