package ru.androidschool.intensiv.data.dto

import android.content.Context
import androidx.room.*
import ru.androidschool.intensiv.Domain.MovieDao

@Database(entities = [MovieEntity::class], version = 1)
abstract class MovieDatabase : RoomDatabase() {
    abstract fun movieDao(): MovieDao

    companion object {
        private var instance: MovieDatabase? = null

        @Synchronized
        fun get(context: Context): MovieDatabase {
            if (instance == null) {
                instance = Room.databaseBuilder(
                    context.applicationContext,
                    MovieDatabase::class.java, "MovieDatabase"
                ).build()
            }
            return instance!!
        }
    }
}
