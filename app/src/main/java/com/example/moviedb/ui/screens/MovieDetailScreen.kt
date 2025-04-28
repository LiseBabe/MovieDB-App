package com.example.moviedb.ui.screens

import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.gestures.snapping.snapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.moviedb.models.Review
import com.example.moviedb.models.Video
import com.example.moviedb.network.MovieDBApiService
import com.example.moviedb.utils.Constants
import com.example.moviedb.utils.MovieDetailsDisplayType
import com.example.moviedb.viewmodel.MovieDBViewModel
import com.example.moviedb.viewmodel.MovieListUiState
import com.example.moviedb.viewmodel.MovieReviewsUiState
import com.example.moviedb.viewmodel.MovieVideosUiState
import com.example.moviedb.viewmodel.SelectedMovieUiState
import android.content.Context
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.media3.common.MediaItem
import androidx.media3.exoplayer.ExoPlayer
import androidx.media3.ui.PlayerView

@Composable
fun MovieDetailScreen(
    selectedMovieUiState: SelectedMovieUiState,
    movieReviewsUiState: MovieReviewsUiState,
    movieVideosUiState: MovieVideosUiState,
    movieDetailsDisplayType: MovieDetailsDisplayType,
    modifier: Modifier = Modifier
) {
    val videos : List<Video> = listOf(Video("test", "test", "test", 1, "test", true, "10/10/2021"))

    when (selectedMovieUiState) {
        is SelectedMovieUiState.Success -> {
            when (movieDetailsDisplayType) {
                MovieDetailsDisplayType.TALL -> {
                    Column (modifier){
                        MovieImageElement(selectedMovieUiState, modifier)
                        MovieDescriptionElement(selectedMovieUiState, modifier)
                        when (movieReviewsUiState) {
                            is MovieReviewsUiState.Success -> {
                                ReviewAndVideoRow(
                                    movieDetailsDisplayType,
                                    videos,
                                    movieReviewsUiState.reviews,
                                    modifier
                                )
                            }
                            is MovieReviewsUiState.Loading -> {
                                Card (modifier = Modifier.fillMaxWidth(0.35f)) {
                                    Text(
                                        text = "Loading reviews...",
                                        style = MaterialTheme.typography.bodySmall,
                                        modifier = Modifier.padding(16.dp)
                                    )
                                }
                            }
                            is MovieReviewsUiState.Error -> {
                                Card (modifier = Modifier.fillMaxWidth(0.35f)) {
                                    Text(
                                        text = "Error: Something went wrong!",
                                        style = MaterialTheme.typography.bodySmall,
                                        modifier = Modifier.padding(16.dp)
                                    )
                                }
                            }
                        }
                        // ReviewAndVideoRow(movieDetailsDisplayType, videos, reviews, modifier)
                    }
                }
                MovieDetailsDisplayType.WIDE -> {
                    LazyColumn {
                        item {
                            Row (modifier = Modifier.fillMaxWidth().height(250.dp)){
                                MovieImageElement(selectedMovieUiState, modifier)
                                Spacer(modifier = Modifier.size(8.dp))
                                MovieDescriptionElement(
                                    selectedMovieUiState,
                                    modifier
                                )
                            }
                        }

                        item {
                            when (movieReviewsUiState) {
                                is MovieReviewsUiState.Success -> {
                                    ReviewAndVideoRow(
                                        movieDetailsDisplayType,
                                        videos,
                                        movieReviewsUiState.reviews,
                                        modifier
                                    )
                                }
                                is MovieReviewsUiState.Loading -> {
                                    // Show a loading spinner or a placeholder
                                    Text("to be done")
                                }
                                is MovieReviewsUiState.Error -> {
                                    // Show an error message
                                    Text("to be done")
                                }
                            }
                        }
                    }
                }
            }
        }
        is SelectedMovieUiState.Loading -> {
            Text(
                text = "Loading...",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(16.dp)
            )
        }
        is SelectedMovieUiState.Error -> {
            Text(
                text = "Error...",
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier.padding(16.dp)
            )
        }
    }
}

@Composable
fun MovieImageElement(selectedMovieUiState : SelectedMovieUiState.Success, modifier : Modifier = Modifier) {
    Box(
        modifier.height(250.dp).padding(0.dp)
    ) {
        AsyncImage(
            model = Constants.BACKDROP_IMAGE_BASE_URL + Constants.BACKDROP_IMAGE_BASE_WIDTH + selectedMovieUiState.movie.backdropPath,
            contentDescription = selectedMovieUiState.movie.title,
            modifier = modifier,
            contentScale = ContentScale.Crop
        )
    }
}

@Composable
fun MovieDescriptionElement(selectedMovieUiState: SelectedMovieUiState.Success, modifier : Modifier = Modifier) {
    Column {
        Text(
            text = selectedMovieUiState.movie.title,
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = selectedMovieUiState.movie.releaseDate,
            style = MaterialTheme.typography.bodySmall
        )
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = selectedMovieUiState.movie.overview,
            style = MaterialTheme.typography.bodySmall,
            overflow = TextOverflow.Ellipsis,
        )
        Spacer(modifier = Modifier.size(8.dp))
    }
}

@Composable
fun ReviewAndVideoRow(movieDetailsDisplayType: MovieDetailsDisplayType,
                      videos: List<Video>,
                      reviews: List<Review>,
                      modifier: Modifier = Modifier) {
    Column {
        val videoRowState = rememberLazyListState()
        LazyRow(
            state = videoRowState,
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            flingBehavior = rememberSnapFlingBehavior(lazyListState = videoRowState),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(videos.size) {
                MovieDetailVideoCard(
                    videos[it],
                    fillMaxWidth = movieDetailsDisplayType == MovieDetailsDisplayType.TALL,
                    modifier.fillParentMaxWidth(),
                )
            }
        }
        //Reviews Row
        val reviewRowState = rememberLazyListState()
        LazyRow(
            state = reviewRowState,
            contentPadding = PaddingValues(8.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            flingBehavior = rememberSnapFlingBehavior(lazyListState = reviewRowState),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(reviews.size) {
                MovieDetailReviewCard(
                    reviews[it],
                    fillMaxWidth = movieDetailsDisplayType == MovieDetailsDisplayType.TALL,
                    modifier.fillParentMaxWidth(),
                )
            }
        }
    }
}

@Composable
fun MovieDetailReviewCard(review: Review, fillMaxWidth: Boolean, modifier: Modifier = Modifier) {
    Card (modifier = if (fillMaxWidth) modifier else Modifier.fillMaxWidth(0.35f)) {
        Column (modifier = Modifier.padding(16.dp)){
            Text(text = review.author, style = MaterialTheme.typography.headlineMedium)

            Spacer(modifier = Modifier.size(8.dp))

            Text(text = review.createdAt, style = MaterialTheme.typography.labelMedium)

            Spacer(modifier = Modifier.size(8.dp))

            Text(text = review.content, style = MaterialTheme.typography.bodyMedium)
        }
    }
}

@Composable
fun MovieDetailVideoCard(video: Video, fillMaxWidth: Boolean, modifier: Modifier = Modifier) {
    Card (modifier = if (fillMaxWidth) modifier .fillMaxSize()
        .aspectRatio(16/9f)
        else Modifier
        .fillMaxWidth(0.35f)
        .aspectRatio(16/9f)) {
        val context = LocalContext.current

        val exoPlayer = remember {
            ExoPlayer.Builder(context).build().apply {
                // Fix video for the moment
                val mediaItem = MediaItem.fromUri("https://commondatastorage.googleapis.com/gtv-videos-bucket/sample/BigBuckBunny.mp4")
                setMediaItem(mediaItem)
                prepare()
                playWhenReady = false
            }
        }

        DisposableEffect(Unit) {
            onDispose {
                exoPlayer.release()
            }
        }

        AndroidView(
            factory = {
                PlayerView(it).apply {
                    player = exoPlayer
                    useController = true
                    layoutParams = android.view.ViewGroup.LayoutParams(
                        android.view.ViewGroup.LayoutParams.MATCH_PARENT,
                        android.view.ViewGroup.LayoutParams.MATCH_PARENT
                    )
                }
            },
            modifier = if (fillMaxWidth) modifier.fillMaxSize() else Modifier.fillMaxWidth(0.35f)
        )
    }
}