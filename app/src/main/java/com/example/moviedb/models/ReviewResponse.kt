package com.example.moviedb.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class ReviewResponse(
    @SerialName(value = "id")
    var id: Long = 0L,

    @SerialName(value = "page")
    var page: Int = 0,

    @SerialName(value = "results")
    var results: List<Review> = listOf(),

    @SerialName(value = "total_pages")
    var total_pages: Int = 0,

    @SerialName(value = "total_results")
    var total_results: Int = 0
)
