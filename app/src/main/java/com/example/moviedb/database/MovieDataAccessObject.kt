package com.example.moviedb.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.moviedb.models.Movie

@Dao
interface MovieDataAccessObject {
    @Query("SELECT * FROM favorite_movies")
    suspend fun getSavedMovies(): List<Movie>

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertMovie(movie: Movie)

    @Query("SELECT * FROM favorite_movies WHERE id = :id")
    suspend fun getMovie(id: Long): Movie

    @Query("DELETE FROM favorite_movies WHERE id = :id")
    suspend fun deleteMovie(id: Long)
}