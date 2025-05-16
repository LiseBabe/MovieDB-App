package com.example.moviedb.database

import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.moviedb.models.CacheMovie
import com.example.moviedb.models.Movie
import com.example.moviedb.models.MovieResponse
import com.example.moviedb.models.ReviewResponse
import com.example.moviedb.models.VideoResponse
import com.example.moviedb.network.MovieDBApiService
import com.example.moviedb.utils.MovieCacheType

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

interface SavedMoviesRepository{
    suspend fun getSavedMovies(): List<Movie>
    suspend fun insertMovie(movie: Movie)
    suspend fun getMovie(id: Long): Movie
    suspend fun deleteMovie(movie: Movie)
    suspend fun deleteAllMovies()
    suspend fun getCacheMovies(): List<CacheMovie>
    suspend fun insertCacheMovie(cacheMovie: List<Movie>)
}

class CachedMoviesRepository(private val movieDAO: MovieDataAccessObject, private val cacheMovieDAO: CacheMovieDataAccessObject): SavedMoviesRepository{
    override suspend fun getSavedMovies(): List<Movie> {
        return movieDAO.getSavedMovies()
    }

    override suspend fun insertMovie(movie: Movie) {
        movieDAO.insertMovie(movie)
    }

    override suspend fun getMovie(id: Long): Movie {
        return movieDAO.getMovie(id)
    }

    override suspend fun deleteMovie(movie: Movie) {
        movieDAO.deleteMovie(movie.id)
    }

    override suspend fun getCacheMovies() : List<CacheMovie>{
        return cacheMovieDAO.getCacheMovies()
    }


    override suspend fun insertCacheMovie(cacheMovie : List<Movie>){
        var cacheMovieList : List<CacheMovie> = cacheMovie.map { m ->
            CacheMovie(id = m.id,
                title = m.title,
                posterPath = m.posterPath,
                backdropPath = m.backdropPath,
                releaseDate = m.releaseDate,
                overview = m.overview,
                genreIds = m.genreIds
                )
        }
        cacheMovieDAO.insertCacheMovie(cacheMovieList)
    }

    override suspend fun deleteAllMovies() {
        cacheMovieDAO.deleteAllMovies()
    }

}