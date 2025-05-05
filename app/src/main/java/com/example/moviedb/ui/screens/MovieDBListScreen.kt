package com.example.moviedb.ui.screens

import android.content.Context
import android.content.Intent
import android.graphics.Paint
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.lazy.staggeredgrid.LazyVerticalStaggeredGrid
import androidx.compose.foundation.lazy.staggeredgrid.StaggeredGridCells
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import coil.compose.AsyncImage
import com.example.moviedb.models.Movie
import com.example.moviedb.utils.Constants
import androidx.compose.ui.tooling.preview.Preview
import com.example.moviedb.database.Genres
import com.example.moviedb.ui.theme.MovieDBTheme
import com.example.moviedb.utils.MovieCacheType
import com.example.moviedb.utils.MovieScreenDisplayType
import com.example.moviedb.viewmodel.MovieDBViewModel
import com.example.moviedb.viewmodel.MovieListUiState
import org.intellij.lang.annotations.JdkConstants

@Composable
fun MovieListScreen(
    movieDBViewModel: MovieDBViewModel,
    movieScreenDisplayType: MovieScreenDisplayType,
    onMovieListItemClicked: (Movie) -> Unit,
    modifier: Modifier = Modifier) {

    LazyVerticalStaggeredGrid (columns = if (movieScreenDisplayType == MovieScreenDisplayType.LIST)
        StaggeredGridCells.Fixed(1) else if (movieScreenDisplayType == MovieScreenDisplayType.NARROW_GRID) StaggeredGridCells.Fixed(2)
        else StaggeredGridCells.Fixed(3),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalItemSpacing = 8.dp,
    modifier = modifier) {
        val movieListUiState = movieDBViewModel.movieListUiState
        val savedMoviesRepository = movieDBViewModel.savedMoviesRepository
        when(movieListUiState) {
            is MovieListUiState.Success -> {
                item {
                    LaunchedEffect(true) {
                        if(movieDBViewModel.networkManager.hasInternet) {
                            savedMoviesRepository.deleteAllMovies(MovieCacheType.REGULAR)
                            savedMoviesRepository.insertMovies(movieListUiState.movies)
                        }
                    }
                }

                items(movieListUiState.movies.size) {
                    MovieListItemCard(
                        movie = movieListUiState.movies[it],
                        onMovieListItemClicked,
                        modifier = Modifier
                            .padding(8.dp)
                    )
                }
            }

            is MovieListUiState.Loading -> {
                item {
                    Text(
                        text = "Loading...",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }

            is MovieListUiState.Error -> {
                item {
                    Text(
                        text = "Error: Something went wrong!",
                        style = MaterialTheme.typography.bodySmall,
                        modifier = Modifier.padding(16.dp)
                    )
                }
            }
        }
    }
}


@Composable
@OptIn(ExperimentalLayoutApi::class)
fun MovieListItemCard(movie: Movie,
                      onMovieListItemClicked: (Movie) -> Unit,
                      modifier: Modifier = Modifier) {
    Card(
        onClick = {
            onMovieListItemClicked(movie)
        } )
    {
        Row {
            Box {
                AsyncImage(
                    model = Constants.POSTER_IMAGE_BASE_URL + Constants.POSTER_IMAGE_BASE_WIDTH + movie.posterPath,
                    contentDescription = movie.title,
                    modifier = modifier
                        .width(92.dp)
                        .height(138.dp),
                    contentScale = ContentScale.Crop
                )
            }
            Column {
                Text(
                    text = movie.title,
                    style = MaterialTheme.typography.headlineSmall,
                )

                Spacer(modifier = Modifier.size(4.dp))

                FlowRow(horizontalArrangement = Arrangement.Start) {
                    val genres = Genres.getGenresFromIds(movie.genreIds)
                    genres.forEach { g ->
                        ElevatedCard (modifier = Modifier.padding(2.dp)) {
                            Text(text = g.name, style = MaterialTheme.typography.bodySmall,
                                modifier = Modifier.padding(horizontal = 4.dp)
                                    .align(Alignment.CenterHorizontally))
                        }
                    }
                }

                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    text = movie.releaseDate,
                    style = MaterialTheme.typography.bodySmall
                )
                Spacer(modifier = Modifier.size(8.dp))

                Text(
                    text = movie.overview,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )
                Spacer(modifier = Modifier.size(8.dp))
            }
        }
    }
}

private fun openIMDB(context: Context, id: String) {
    //https://developer.android.com/guide/components/intents-filters
    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = ("https://imdb.com/title/$id").toUri()
    }
    context.startActivity(Intent.createChooser(intent, "Open in IMDB"))

}

@Preview(showBackground = true)
@Composable
fun MovieItemPreview(){
    MovieDBTheme {
        MovieListItemCard(movie = Movie(
            2,
            "Captain America: Brave New World",
            "/pzIddUEMWhWzfvLI3TwxUG2wGoi.jpg",
            "/gsQJOfeW45KLiQeEIsom94QPQwb.jpg",
            "2025-02-12",
            "When a group of radical activists take over an energy company's annual gala, seizing 300 hostages, an ex-soldier turned window cleaner suspended 50 storeys up on the outside of the building must save those trapped inside, including her younger brother.",
            listOf(28, 53, 878)
        ),  {})
    }
}