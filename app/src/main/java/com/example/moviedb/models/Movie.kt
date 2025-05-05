package com.example.moviedb.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.example.moviedb.utils.MovieCacheType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable



@Serializable
@Entity(tableName = "movies")
data class Movie(
    @PrimaryKey
    @SerialName(value = "id")
    var id: Long = 0L,
    @SerialName(value = "title")
    var title: String,
    @SerialName(value = "poster_path")
    var posterPath: String,
    @SerialName(value = "backdrop_path")
    var backdropPath: String?,
    @SerialName(value = "release_date")
    var releaseDate: String,
    @SerialName(value = "overview")
    var overview: String,
    @SerialName(value = "genre_ids")
    var genreIds: List<Int>,
    var cacheType: MovieCacheType = MovieCacheType.REGULAR
)
