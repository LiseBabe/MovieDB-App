package com.example.moviedb

import android.content.Context
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.animation.expandHorizontally
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Info
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.moviedb.database.Movies
import com.example.moviedb.ui.theme.MovieDBTheme
import com.example.moviedb.models.Movie
import com.example.moviedb.utils.Constans
import coil.compose.AsyncImage
import androidx.core.net.toUri

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MovieDBTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MovieDBApp(
                        name = "Android",
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun MovieDBApp(name: String, modifier: Modifier = Modifier) {
    MovieList(movieList = Movies().getMovies())
}

@Composable
fun MovieList(movieList: List<Movie>, modifier: Modifier = Modifier){
    LazyColumn(modifier = modifier) {
        items(movieList){ movie ->
            MovieListItemCard(movie = movie, modifier = Modifier.padding(8.dp))
        }

    }

}

@Composable
fun MovieListItemCard(movie: Movie, modifier: Modifier = Modifier){
    val context = LocalContext.current;
    Card (modifier = modifier){
        Row {
            Box {
                AsyncImage(
                    model = Constans.POSTER_IMAGE_BASE_URL+Constans.POSTER_IMAGE_BASE_WIDTH+movie.posterPath,
                    contentDescription = movie.title,
                    modifier = modifier.width(92.dp).height(138.dp),
                    contentScale = ContentScale.Crop
                )
            }
            Column {
                Row (
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.Top,
                    horizontalArrangement = Arrangement.SpaceBetween
                ){
                    Text(text = movie.title,
                        style = MaterialTheme.typography.headlineSmall,
                        modifier = Modifier.weight(1f))
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

                Text(text = movie.releaseDate,
                    style = MaterialTheme.typography.bodySmall)
                Spacer(modifier = Modifier.size(8.dp))

                Text(text = movie.overviews,
                    style = MaterialTheme.typography.bodySmall,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis)
                Spacer(modifier = Modifier.size(8.dp))

            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MovieDBTheme {
        MovieDBApp("Android")
    }
}

private fun openIMDB(context: Context, id: String) {
    //https://developer.android.com/guide/components/intents-filters
    val intent = Intent(Intent.ACTION_VIEW).apply {
        data = ("https://imdb.com/title/$id").toUri()
    }
    context.startActivity(Intent.createChooser(intent, "Open in IMDB"))

}