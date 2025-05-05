package com.example.moviedb.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.moviedb.models.Movie

@Dao
interface MovieDataAccessObject {
    @Query("SELECT * FROM movies WHERE cacheType = :type")
    suspend fun getSavedMovies(type: Int): List<Movie>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMovie(movie: Movie)

    @Query("SELECT * FROM movies WHERE id = :id AND cacheType = 1")
    suspend fun getMovie(id: Long): Movie

    @Query("DELETE FROM movies WHERE id = :id AND cacheType = 1")
    suspend fun deleteMovie(id: Long)

    @Query("DELETE FROM movies WHERE cacheType = :type")
    suspend fun deleteAllMovies(type: Int)
}