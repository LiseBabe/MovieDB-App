package com.example.moviedb.database

import com.example.moviedb.models.Genre

object Genres {
    val genreMap = mapOf(
        28 to Genre(28, "Action"),
        12 to Genre(12, "Adventure"),
        16 to Genre(16, "Animation"),
        35 to Genre(35, "Comedy"),
        80 to Genre(80, "Crime"),
        99 to Genre(99, "Documentary"),
        18 to Genre(18, "Drama"),
        10751 to Genre(10751, "Family"),
        14 to Genre(14, "Fantasy"),
        36 to Genre(36, "History"),
        27 to Genre(27, "Horror"),
        10402 to Genre(10402, "Music"),
        9648 to Genre(9648, "Mystery"),
        10749 to Genre(10749, "Romance"),
        878 to Genre(878, "Science Fiction"),
        10770 to Genre(10770, "TV Movie"),
        53 to Genre(53, "Thriller"),
        10752 to Genre(10752, "War"),
        37 to Genre(37, "Western")
    )
    fun idToGenre(id: Int): Genre? {
        return genreMap[id]
    }

    fun getGenresFromIds(ids: List<Int>): List<Genre> {
        val out = ArrayList<Genre>()
        for (id in ids) {
            val g = idToGenre(id)
            if (g != null) {
                out.add(g)
            }
        }
        return out
    }
}