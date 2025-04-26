package com.example.moviedb.models

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Video(
    @SerialName(value = "name")
    var name : String,
    @SerialName(value = "key")
    var key : String,
    @SerialName(value = "site")
    var site : String,
    @SerialName(value = "size")
    var size : Int = 0,
    @SerialName(value = "type")
    var type : String,
    @SerialName(value = "official")
    var official : Boolean = true,
    @SerialName(value = "published_at")
    var publishedAt : String,
)
