package com.example.moviedb.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.moviedb.MovieDBApplication
import com.example.moviedb.database.MoviesRepository
import com.example.moviedb.models.Movie
import com.example.moviedb.models.Review
import kotlinx.coroutines.launch
import java.io.IOException
import retrofit2.HttpException

sealed interface MovieListUiState {
    data class Success(val movies: List<Movie>) : MovieListUiState
    object Error : MovieListUiState
    object Loading : MovieListUiState
}

sealed interface SelectedMovieUiState {
    data class Success(val movie: Movie) : SelectedMovieUiState
    object Error : SelectedMovieUiState
    object Loading : SelectedMovieUiState
}

sealed interface MovieReviewsUiState {
    data class Success(val reviews: List<Review>) : MovieReviewsUiState
    object Error : MovieReviewsUiState
    object Loading : MovieReviewsUiState
}

class MovieDBViewModel(private val moviesRepository: MoviesRepository) : ViewModel() {
    var movieListUiState: MovieListUiState by
    mutableStateOf(MovieListUiState.Loading)
        private set

    var selectedMovieUiState: SelectedMovieUiState by
            mutableStateOf(SelectedMovieUiState.Loading)
    private set

    var movieReviewsUIState: MovieReviewsUiState by
    mutableStateOf(MovieReviewsUiState.Loading)
        private set

    init {
        getPopularMovies()
    }

    private fun getTopRatedMovies() {
        viewModelScope.launch {
            movieListUiState = MovieListUiState.Loading
            movieListUiState = try {
                MovieListUiState.Success(moviesRepository.getTopRatedMovies().results)
            } catch (e: IOException) {
                MovieListUiState.Error
            } catch (e: HttpException) {
                MovieListUiState.Error
            }
        }
    }

    fun getPopularMovies() {
        viewModelScope.launch {
            movieListUiState = MovieListUiState.Loading
            movieListUiState = try {
                MovieListUiState.Success(moviesRepository.getPopularMovies().results)
            } catch (e: IOException) {
                MovieListUiState.Error
            } catch (e: HttpException) {
                MovieListUiState.Error
            }
        }
    }

    fun getMoviesReview(movieId : Long) {
        println("Fetching reviews for movieId = $movieId")
        viewModelScope.launch {
            movieReviewsUIState = MovieReviewsUiState.Loading
            movieReviewsUIState = try {
                val result = moviesRepository.getMovieReviews(movieId)
                println("Fetched reviews successfully: ${result.results.size} reviews")
                MovieReviewsUiState.Success(result.results)
            } catch (e: IOException) {
                println("IOException: ${e.message}")
                MovieReviewsUiState.Error
            } catch (e: HttpException) {
                println("HttpException: ${e.message}")
                MovieReviewsUiState.Error
            }
        }
    }

    fun setSelectedMovie(movie: Movie) {
        viewModelScope.launch {
            selectedMovieUiState = SelectedMovieUiState.Loading
            selectedMovieUiState = try {
                SelectedMovieUiState.Success(movie)
            } catch (e: IOException) {
                SelectedMovieUiState.Error
            } catch (e: HttpException) {
                SelectedMovieUiState.Error
            }
        }
    }


    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
                val application = (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MovieDBApplication)
                val moviesRepository = application.container.moviesRepository
                MovieDBViewModel(moviesRepository = moviesRepository)
            }
        }
    }

}