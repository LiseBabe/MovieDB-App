package com.example.moviedb.database

import com.example.moviedb.models.MovieResponse
import com.example.moviedb.models.ReviewResponse
import com.example.moviedb.models.VideoResponse
import com.example.moviedb.network.MovieDBApiService

interface MoviesRepository {
    suspend fun getPopularMovies(): MovieResponse
    suspend fun getTopRatedMovies(): MovieResponse
    suspend fun getMovieReviews(movieId: Long): ReviewResponse
    suspend fun getMovieVideos(movieId: Long): VideoResponse
}

class NetworkMoviesRepository(private val apiService: MovieDBApiService) : MoviesRepository {
    override suspend fun getPopularMovies(): MovieResponse {
        return apiService.getPopularMovies()
    }

    override suspend fun getTopRatedMovies(): MovieResponse {
        return apiService.getTopRatedMovies()
    }

    override suspend fun getMovieReviews(movieId: Long): ReviewResponse {
        return apiService.getMovieReviews(movieId)
    }

    override suspend fun getMovieVideos(movieId: Long): VideoResponse {
        return apiService.getMovieVideos(movieId)
    }

}