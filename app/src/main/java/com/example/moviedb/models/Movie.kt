package com.example.moviedb.models

data class Movie(
    var id: Long = 0L,
    var imdb_id: String,
    var title: String,
    var posterPath: String,
    var backdropPath: String,
    var releaseDate: String,
    var overview: String
)
