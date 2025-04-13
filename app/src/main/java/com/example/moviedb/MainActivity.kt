package com.example.moviedb

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.pointer.motionEventSpy
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.moviedb.database.Movies
import com.example.moviedb.ui.theme.MovieDBTheme
import com.example.moviedb.models.Movie
import com.example.moviedb.models.Genre
import com.example.moviedb.database.Genres
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
                        modifier = Modifier.padding(innerPadding),
                        screen = "movie"
                    )
                }
            }
        }
    }
}

@Composable
fun MovieDBApp(name: String, modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    NavHost(navController, startDestination = "home") {
        composable("home") { HomeScreen(navController) }
        composable("second") { SecondScreen(navController) }
        composable("third") { ThirdScreen(navController) }
    }
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

@Composable
fun HomeScreen(navController: NavHostController) {
    Scaffold { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentAlignment = Alignment.Center
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Text(
                    text = "Welcome to MovieDB",
                    style = MaterialTheme.typography.headlineLarge
                )

                Button(
                    onClick = { navController.navigate("second") },
                    modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    Text("List of Movies")
                }

                Button(
                    onClick = { navController.navigate("third") },
                    modifier = Modifier.fillMaxWidth(0.8f)
                ) {
                    Text("Movie Links")
                }
            }
            
fun GenreList(genreList: List<Genre>, modifier: Modifier = Modifier){
    LazyColumn(modifier = modifier) {
        items(genreList){ genre ->
            GenreListItemCard(genre, modifier.padding(8.dp))
        }
    }
}

@Composable
fun SecondScreen(navController: NavHostController) {
    Scaffold(
        bottomBar = {
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Home")
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text(
                    text = "Movie List",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            MovieList(movieList = Movies().getMovies())
        }
    }
}

@Composable
fun ThirdScreen(navController: NavHostController) {
    Scaffold(
        bottomBar = {
            Button(
                onClick = { navController.popBackStack() },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Text("Home")
            }
        }
    ) { innerPadding ->
        Column(modifier = Modifier
            .fillMaxSize()
            .padding(innerPadding)) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 16.dp)
            ) {
                Text(
                    text = "Movie Links",
                    style = MaterialTheme.typography.headlineMedium,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

fun GenreListItemCard(genre: Genre, modifier: Modifier = Modifier){
    Card(modifier = modifier) {
        Spacer(modifier = Modifier.size(8.dp))
        Text(
            text = genre.name,
            style = MaterialTheme.typography.headlineSmall
        )
        Spacer(modifier = Modifier.size(8.dp))
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    MovieDBTheme {
        val navController = rememberNavController()
        HomeScreen(navController = navController)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview2() {
    MovieDBTheme {
        val navController = rememberNavController()
        SecondScreen(navController = navController)
    }
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview3() {
    MovieDBTheme {
        val navController = rememberNavController()
        ThirdScreen(navController = navController)
    }
}