package com.example.moviedb.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Review(
    @SerialName(value = "author")
    var author : String,
    @SerialName(value = "content")
    var content : String,
    @SerialName(value = "created_at")
    var createdAt : String
)
