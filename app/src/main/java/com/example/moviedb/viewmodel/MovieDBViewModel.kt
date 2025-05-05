package com.example.moviedb.viewmodel

import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import android.net.NetworkRequest
import androidx.compose.runtime.getValue
import androidx.compose.runtime.setValue
import androidx.compose.runtime.mutableStateOf
import androidx.core.content.ContextCompat.getSystemService

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import com.example.moviedb.MovieDBApplication
import com.example.moviedb.database.MoviesRepository
import com.example.moviedb.database.SavedMoviesRepository
import com.example.moviedb.models.Movie
import com.example.moviedb.models.Review
import com.example.moviedb.models.Video
import com.example.moviedb.network.NetworkManager
import com.example.moviedb.utils.MovieCacheType
import com.example.moviedb.utils.MovieListScreens
import kotlinx.coroutines.launch
import java.io.IOException
import retrofit2.HttpException

sealed interface MovieListUiState {
    data class Success(val movies: List<Movie>) : MovieListUiState
    object Error : MovieListUiState
    object Loading : MovieListUiState
}

sealed interface SelectedMovieUiState {
    data class Success(val movie: Movie, val isFavorite: Boolean) : SelectedMovieUiState
    object Error : SelectedMovieUiState
    object Loading : SelectedMovieUiState
}

sealed interface MovieReviewsUiState {
    data class Success(val reviews: List<Review>) : MovieReviewsUiState
    object Error : MovieReviewsUiState
    object Loading : MovieReviewsUiState
}

sealed interface MovieVideosUiState {
    data class Success(val videos: List<Video>) : MovieVideosUiState
    object Error : MovieVideosUiState
    object Loading : MovieVideosUiState
}

class MovieDBViewModel(private val moviesRepository: MoviesRepository,
    val savedMoviesRepository: SavedMoviesRepository,
    val networkManager: NetworkManager) : ViewModel() {
    var movieListUiState: MovieListUiState by
    mutableStateOf(MovieListUiState.Loading)
        private set

    var selectedMovieUiState: SelectedMovieUiState by
            mutableStateOf(SelectedMovieUiState.Loading)
    private set

    var movieReviewsUIState: MovieReviewsUiState by
    mutableStateOf(MovieReviewsUiState.Loading)
        private set

    var movieVideosUiState: MovieVideosUiState by
    mutableStateOf(MovieVideosUiState.Loading)
        private set

    var currentMovieList : MovieListScreens = MovieListScreens.POPULAR
    var lastCacheList : MovieListScreens = MovieListScreens.POPULAR

    init {
        getPopularMovies()
    }

    fun getTopRatedMovies() {
            viewModelScope.launch {
                movieListUiState = MovieListUiState.Loading
                movieListUiState = try {
                    if( networkManager.hasInternet) {
                        MovieListUiState.Success(moviesRepository.getTopRatedMovies().results)
                    } else if(lastCacheList == MovieListScreens.TOP_RATED){
                        MovieListUiState.Success(savedMoviesRepository.getCacheMovies())
                    } else {
                        MovieListUiState.Error
                    }
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
                if( networkManager.hasInternet) {
                    MovieListUiState.Success(moviesRepository.getPopularMovies().results)
                } else if(lastCacheList == MovieListScreens.POPULAR){
                    MovieListUiState.Success(savedMoviesRepository.getCacheMovies())
                } else {
                    MovieListUiState.Error
                }
            } catch (e: IOException) {
                MovieListUiState.Error
            } catch (e: HttpException) {
                MovieListUiState.Error
            }
        }
    }

    fun getMoviesReview(movieId : Long) {
        viewModelScope.launch {
            movieReviewsUIState = MovieReviewsUiState.Loading
            movieReviewsUIState = try {
                val result = moviesRepository.getMovieReviews(movieId)
                MovieReviewsUiState.Success(result.results)
            } catch (e: IOException) {
                MovieReviewsUiState.Error
            } catch (e: HttpException) {
                MovieReviewsUiState.Error
            }
        }
    }

    fun getMovieVideos(movieId: Long) {
        println("Fetching videos for movieId = $movieId")
        viewModelScope.launch {
            movieVideosUiState = MovieVideosUiState.Loading
            movieVideosUiState = try {
                val result = moviesRepository.getMovieVideos(movieId)
                println("Fetched videos successfully: ${result.videos.size} videos")
                MovieVideosUiState.Success(result.videos)
            } catch (e: IOException) {
                println("IOException: ${e.message}")
                MovieVideosUiState.Error
            } catch (e: HttpException) {
                println("HttpException: ${e.message}")
                MovieVideosUiState.Error
            }
        }
    }


    fun setSelectedMovie(movie: Movie) {
        viewModelScope.launch {
            selectedMovieUiState = SelectedMovieUiState.Loading
            selectedMovieUiState = try {
                SelectedMovieUiState.Success(movie, savedMoviesRepository.getMovie(movie.id) != null)
            } catch (e: IOException) {
                SelectedMovieUiState.Error
            } catch (e: HttpException) {
                SelectedMovieUiState.Error
            }
        }
    }

    fun getSavedMovies() {
        viewModelScope.launch {
            movieListUiState = MovieListUiState.Loading
            movieListUiState = try {
                MovieListUiState.Success(savedMoviesRepository.getSavedMovies())
            } catch (e: IOException) {
                MovieListUiState.Error
            } catch (e: HttpException) {
                MovieListUiState.Error
            }
        }
    }

    fun saveMovie(movie: Movie) {
        viewModelScope.launch {
            savedMoviesRepository.insertMovie(movie)
            selectedMovieUiState = SelectedMovieUiState.Success(movie, true)
        }
    }

    fun deleteMovie(movie: Movie) {
        viewModelScope.launch {
            savedMoviesRepository.deleteMovie(movie)
            selectedMovieUiState = SelectedMovieUiState.Success(movie, false)
            if (currentMovieList == MovieListScreens.SAVED) getSavedMovies()
        }
    }

    fun onInternetReconnect() {
        when (currentMovieList) {
            MovieListScreens.POPULAR -> getPopularMovies()
            MovieListScreens.TOP_RATED -> getTopRatedMovies()
            MovieListScreens.SAVED -> getSavedMovies()
        }
    }


    companion object {
        val Factory = { networkManager: NetworkManager ->
            viewModelFactory {
                initializer {
                    val application =
                        (this[ViewModelProvider.AndroidViewModelFactory.APPLICATION_KEY] as MovieDBApplication)
                    val moviesRepository = application.container.moviesRepository
                    val savedMoviesRepository = application.container.savedMoviesRepository
                    MovieDBViewModel(
                        moviesRepository = moviesRepository,
                        savedMoviesRepository = savedMoviesRepository,
                        networkManager = networkManager
                    )
                }
            }
        }
    }

}