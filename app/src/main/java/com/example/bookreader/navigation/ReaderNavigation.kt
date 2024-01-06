package com.example.bookreader.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.example.bookreader.ui.screens.details.DetailsScreen
import com.example.bookreader.ui.screens.home.HomeScreen
import com.example.bookreader.ui.screens.home.HomeScreenViewModel
import com.example.bookreader.ui.screens.login.LoginScreen
import com.example.bookreader.ui.screens.search.SearchScreen
import com.example.bookreader.ui.screens.search.SearchViewModel
import com.example.bookreader.ui.screens.splash.SplashScreen
import com.example.bookreader.ui.screens.stats.StatsScreen
import com.example.bookreader.ui.screens.update.UpdateScreen

@Composable
fun ReaderNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = ReaderScreens.SplashScreen.name) {

        val detailName = ReaderScreens.DetailsScreen.name
        composable(
            route = "$detailName/{bookId}",
            arguments = listOf(navArgument("bookId") {
                type = NavType.StringType
                defaultValue = ""
            })
        ) {
            DetailsScreen(
                navController = navController,
                bookId = it.arguments?.getString("bookId") ?: ""
            )
        }

        composable(route = ReaderScreens.HomeScreen.name) {
            val viewModel = hiltViewModel<HomeScreenViewModel>()
            HomeScreen(navController = navController, viewModel = viewModel)
        }
        composable(route = ReaderScreens.LoginScreen.name) {
            LoginScreen(navController = navController)
        }
        composable(route = ReaderScreens.SearchScreen.name) {
            val viewModel = hiltViewModel<SearchViewModel>()
            SearchScreen(navController = navController, viewModel = viewModel)
        }
        composable(route = ReaderScreens.SplashScreen.name) {
            SplashScreen(navController = navController)
        }
        composable(route = ReaderScreens.StatsScreen.name) {
            StatsScreen(navController = navController)
        }

        val updateName = ReaderScreens.UpdateScreen.name
        composable(
            route = "$updateName/{bookItemId}",
            arguments = listOf(navArgument("bookItemId") {
                type = NavType.StringType
                defaultValue = ""
            })
        ) {
            UpdateScreen(
                navController = navController,
                bookItemId = it.arguments?.getString("bookItemId") ?: ""
            )
        }

    }
}