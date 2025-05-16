package com.example.moviedb.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class VideoResponse(
    @SerialName("id")
    val id: Int,
    @SerialName("results")
    val videos: List<Video>
)
