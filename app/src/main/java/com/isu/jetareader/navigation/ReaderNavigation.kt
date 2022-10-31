package com.isu.jetareader.navigation

import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.isu.jetareader.screens.ReaderSplashScreen
import com.isu.jetareader.screens.details.BookDetailsScreen
import com.isu.jetareader.screens.home.Home
import com.isu.jetareader.screens.home.HomeScreenViewModel
import com.isu.jetareader.screens.login.ReaderLoginScreen
import com.isu.jetareader.screens.search.BooksSearchViewModel
import com.isu.jetareader.screens.search.ReaderBookSearchSearchScreen
import com.isu.jetareader.screens.stats.ReaderStatsScreen
import com.isu.jetareader.screens.update.BookUpdateScreen


@Composable
fun ReaderNavigation() {
    val navController = rememberNavController()
    NavHost(navController = navController, startDestination = ReaderScreens.SplashScreen.name){
        composable(route = ReaderScreens.SplashScreen.name){
            ReaderSplashScreen(navController = navController)
        }

        composable(route = ReaderScreens.ReaderHomeScreen.name){
            val homeViewModel = hiltViewModel<HomeScreenViewModel>()
            Home(navController = navController, viewModel= homeViewModel)
        }
        composable(route = ReaderScreens.SearchScreen.name){
            val viewModel = hiltViewModel<BooksSearchViewModel>()
            ReaderBookSearchSearchScreen(navController = navController, viewModel = viewModel)
        }
        val detailName = ReaderScreens.DetailScreen.name
        composable(route = "$detailName/{bookId}", arguments = listOf(navArgument("bookId"){
            type = NavType.StringType
        })){backStackEntry->
            backStackEntry.arguments?.getString("bookId").let {
                BookDetailsScreen(navController = navController, bookId = it.toString())
            }
        }
        composable(route = ReaderScreens.LoginScreen.name){
            ReaderLoginScreen(navController = navController)
        }
        composable(route = ReaderScreens.ReaderStatsScreen.name){
            val homeViewModel = hiltViewModel<HomeScreenViewModel>()
            ReaderStatsScreen(navController= navController,viewModel = homeViewModel)
        }
        val updateName = ReaderScreens.UpdateScreen.name
        composable("$updateName/{bookItemId}",
            arguments = listOf(navArgument("bookItemId") {
                type = NavType.StringType
            })) { navBackStackEntry ->

            navBackStackEntry.arguments?.getString("bookItemId").let {
                BookUpdateScreen(navController = navController, bookItemId = it.toString())
            }

        }
    }
}