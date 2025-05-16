@file:kotlin.OptIn(ExperimentalMaterial3Api::class)
@file:Suppress("INFERRED_TYPE_VARIABLE_INTO_EMPTY_INTERSECTION_WARNING")

package com.example.moviedb.ui.screens

import android.annotation.SuppressLint
import androidx.annotation.OptIn
import androidx.annotation.StringRes
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationRail
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.windowsizeclass.WindowHeightSizeClass
import androidx.compose.material3.windowsizeclass.WindowSizeClass
import androidx.compose.material3.windowsizeclass.WindowWidthSizeClass
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.example.moviedb.R
import com.example.moviedb.database.Movies
import com.example.moviedb.models.Movie
import com.example.moviedb.network.NetworkManager
import com.example.moviedb.ui.screens.MovieListItemCard
import com.example.moviedb.ui.screens.MovieListScreen
import com.example.moviedb.viewmodel.MovieDBViewModel
import com.example.moviedb.ui.theme.MovieDBTheme
import com.example.moviedb.utils.AppBarType
import com.example.moviedb.utils.MovieDetailsDisplayType
import com.example.moviedb.utils.MovieListScreens
import com.example.moviedb.utils.MovieScreenDisplayType
import com.example.moviedb.viewmodel.MovieVideosUiState
import com.example.moviedb.viewmodel.SelectedMovieUiState


enum class MovieDBScreen(@StringRes val title: Int){
    List(title = R.string.app_name),
    Detail(title = R.string.movie_detail)
}

@SuppressLint("UnrememberedMutableState")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDBAppBar(
    currentScreen: MovieDBScreen,
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier,
    movieDBViewModel: MovieDBViewModel
) {
    var menuExpanded by remember { mutableStateOf(false) }
    var showMenuButton = mutableStateOf(true)
    TopAppBar(
        title = {Text(stringResource(currentScreen.title))},
        colors = TopAppBarDefaults.mediumTopAppBarColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        ),
        actions = {
            if (showMenuButton.value) {
                IconButton(onClick = {
                    menuExpanded = !menuExpanded
                }) {
                    Icon(
                        Icons.Filled.MoreVert,
                        contentDescription = "Open menu to select movie list"
                    )
                }
                DropdownMenu(
                    expanded = menuExpanded, onDismissRequest = {
                        menuExpanded = false
                    }) {
                    DropdownMenuItem(
                        onClick = {
                            movieDBViewModel.getPopularMovies()
                            menuExpanded = false
                            movieDBViewModel.currentMovieList = MovieListScreens.POPULAR
                        },
                        text = {
                            Text(stringResource(R.string.popular_movies))
                        }
                    )
                    DropdownMenuItem(
                        onClick = {
                            movieDBViewModel.getTopRatedMovies()
                            menuExpanded = false
                            movieDBViewModel.currentMovieList = MovieListScreens.TOP_RATED
                        },
                        text = {
                            Text(stringResource(R.string.top_rated_movies))
                        }
                    )
                    DropdownMenuItem(
                        onClick = {
                            movieDBViewModel.getSavedMovies()
                            menuExpanded = false
                            movieDBViewModel.currentMovieList = MovieListScreens.SAVED
                        },
                        text = {
                            Text(stringResource(R.string.saved))
                        }
                    )
                }
            }
        },
        modifier = modifier,
        navigationIcon = {
            if (canNavigateBack) {
                IconButton(onClick = navigateUp) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = stringResource(R.string.back_button)
                    )
                }
                showMenuButton.value = false
            }
        }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MovieDBNavRail(
    canNavigateBack: Boolean,
    navigateUp: () -> Unit,
    modifier: Modifier = Modifier
) {
    NavigationRail(
        containerColor = MaterialTheme.colorScheme.primaryContainer,
        modifier = modifier
    ) {
        if (canNavigateBack) {
            IconButton(onClick = navigateUp) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                    contentDescription = stringResource(R.string.back_button)
                )
            }
        }
    }
}


@Composable
fun MovieDBApp(
               navController: NavHostController = rememberNavController(),
               windowSize: WindowSizeClass
) {
    val backStackEntry by navController.currentBackStackEntryAsState()

    val currentScreen = MovieDBScreen.valueOf(
        backStackEntry?.destination?.route ?: MovieDBScreen.List.name
    )

    val appBarType: AppBarType = when (windowSize.heightSizeClass) {
        WindowHeightSizeClass.Compact -> {
            AppBarType.RAIL
        }
        else -> {
            AppBarType.REGULAR
        }
    }

    val movieDetailsDisplayType: MovieDetailsDisplayType
    val movieScreenDisplayType: MovieScreenDisplayType
    when (windowSize.widthSizeClass) {
        WindowWidthSizeClass.Compact -> {
            movieDetailsDisplayType = MovieDetailsDisplayType.TALL
            movieScreenDisplayType = MovieScreenDisplayType.LIST
        }
        WindowWidthSizeClass.Medium -> {
            movieDetailsDisplayType = MovieDetailsDisplayType.TALL
            movieScreenDisplayType = MovieScreenDisplayType.NARROW_GRID
        }
        WindowWidthSizeClass.Expanded -> {
            movieDetailsDisplayType = MovieDetailsDisplayType.WIDE
            movieScreenDisplayType = MovieScreenDisplayType.LARGE_GRID
        }
        else -> {
            movieDetailsDisplayType = MovieDetailsDisplayType.TALL
            movieScreenDisplayType = MovieScreenDisplayType.NARROW_GRID
        }
    }
    val context = LocalContext.current
    val networkManager = remember { NetworkManager(context) }
    val movieDBViewModel: MovieDBViewModel = viewModel(factory =
        MovieDBViewModel.Factory(networkManager))
    networkManager.onInternetReconnect = { movieDBViewModel.onInternetReconnect() }

    Scaffold(
        topBar = {
            if (appBarType == AppBarType.REGULAR) {
                MovieDBAppBar(
                    currentScreen = currentScreen,
                    canNavigateBack = navController.previousBackStackEntry != null,
                    navigateUp = { navController.navigateUp() },
                    movieDBViewModel = movieDBViewModel
                )
            }
        }
    ) { innerPadding ->
        Row {
            if (appBarType == AppBarType.RAIL) {
                MovieDBNavRail(
                    canNavigateBack = navController.previousBackStackEntry != null,
                    navigateUp = { navController.navigateUp() }
                )
            }
            NavHost(
                navController = navController,
                startDestination = MovieDBScreen.List.name,
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding)
            ) {
                composable(route = MovieDBScreen.List.name) {
                    MovieListScreen(
                        movieDBViewModel = movieDBViewModel,
                        movieScreenDisplayType = movieScreenDisplayType,
                        onMovieListItemClicked = {
                            movieDBViewModel.setSelectedMovie(it)
                            movieDBViewModel.getMoviesReview(it.id)
                            navController.navigate(MovieDBScreen.Detail.name)
                        }, modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp)
                    )
                }
                composable(route = MovieDBScreen.Detail.name) {
                    MovieDetailScreen(
                        movieDBViewModel = movieDBViewModel,
                        movieDetailsDisplayType = movieDetailsDisplayType,
                        modifier = Modifier
                    )
                }
            }

        }
    }
}