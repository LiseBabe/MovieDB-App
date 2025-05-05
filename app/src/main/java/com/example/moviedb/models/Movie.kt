package com.example.moviedb.models

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverter
import com.example.moviedb.utils.MovieCacheType
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable



@Serializable
@Entity(tableName = "favorite_movies")
open class Movie(
    @PrimaryKey
    @SerialName(value = "id")
    open var id: Long = 0L,
    @SerialName(value = "title")
    open var title: String,
    @SerialName(value = "poster_path")
    open var posterPath: String,
    @SerialName(value = "backdrop_path")
    open var backdropPath: String?,
    @SerialName(value = "release_date")
    open var releaseDate: String,
    @SerialName(value = "overview")
    open var overview: String,
    @SerialName(value = "genre_ids")
    open var genreIds: List<Int>
)

@Entity(tableName = "cache_movies")
class CacheMovie (@PrimaryKey
                      @SerialName(value = "id")
                  override var id: Long = 0L,
                  @SerialName(value = "title")
                  override var title: String,
                  @SerialName(value = "poster_path")
                  override var posterPath: String,
                  @SerialName(value = "backdrop_path")
                  override var backdropPath: String?,
                  @SerialName(value = "release_date")
                  override var releaseDate: String,
                  @SerialName(value = "overview")
                  override var overview: String,
                  @SerialName(value = "genre_ids")
                  override var genreIds: List<Int>) : Movie (
    id = id,
    title = title,
    posterPath = posterPath,
    backdropPath = backdropPath,
    releaseDate = releaseDate,
    overview = overview,
    genreIds = genreIds
)
