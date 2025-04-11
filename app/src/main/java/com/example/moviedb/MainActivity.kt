package com.example.moviedb

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.moviedb.database.Movies
import com.example.moviedb.ui.theme.MovieDBTheme
import com.example.moviedb.models.Movie
import com.example.moviedb.utils.Constans
import coil.compose.AsyncImage

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
    MovieList(movieList = Movies().getMovies(), )
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
                Text(text = movie.title,
                    style = MaterialTheme.typography.headlineSmall)
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