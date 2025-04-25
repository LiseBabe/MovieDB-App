package com.example.moviedb.ui.screens

import android.content.Context
import android.content.Intent
import androidx.compose.foundation.gestures.snapping.rememberSnapFlingBehavior
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Card
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
import com.example.moviedb.utils.Constans

@Composable
fun MovieListScreen(
    movieList: List<Movie>,
    onMovieListItemClicked: (Movie) -> Unit,
    modifier: Modifier = Modifier) {
    LazyColumn(
        modifier = modifier) {
        items(movieList) { movie ->
            MovieListItemCard(movie = movie, onMovieListItemClicked, modifier = Modifier.padding(8.dp))
        }
    }
}


@Composable
fun MovieListItemCard(movie: Movie,
                      onMovieListItemClicked: (Movie) -> Unit,
                      modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val lazyRowState = rememberLazyListState(
        initialFirstVisibleItemIndex = 1
    )
    LazyRow (
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(16.dp),
        state = lazyRowState,
        flingBehavior = rememberSnapFlingBehavior(lazyRowState)
    ) {
        item {
            Card(modifier = modifier.fillParentMaxWidth(),
                onClick = {
                    onMovieListItemClicked(movie)
                } )
            {
                Text ("Trailer Card", fontSize = 30.sp, modifier = Modifier.padding(16.dp))
            }
        }
        item {
            Card(modifier = modifier.fillParentMaxWidth(),
                onClick = {
                    onMovieListItemClicked(movie)
                } )
            {
                Row {
                    Box {
                        AsyncImage(
                            model = Constans.POSTER_IMAGE_BASE_URL + Constans.POSTER_IMAGE_BASE_WIDTH + movie.posterPath,
                            contentDescription = movie.title,
                            modifier = modifier
                                .width(92.dp)
                                .height(138.dp),
                            contentScale = ContentScale.Crop
                        )
                    }
                    Column {
                        Row (
                            verticalAlignment = Alignment.Top,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ){
                            Text(
                                text = movie.title,
                                style = MaterialTheme.typography.headlineSmall,
                                modifier = Modifier.weight(1f)
                            )

                            Spacer(modifier = Modifier.size(8.dp))

                            IconButton(
                                onClick = {
                                    openIMDB(context, movie.imdb_id)
                                }) {
                                Icon(
                                    imageVector = Icons.Filled.Info,
                                    contentDescription = "IMDB Link"
                                )
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
        item {
            Card(modifier = modifier.fillParentMaxWidth(),
                onClick = {
                    onMovieListItemClicked(movie)
                } )
            {
                Text ("Reviews Card", fontSize = 30.sp, modifier = Modifier.padding(16.dp))
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