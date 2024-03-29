package com.example.book_madness.ui.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.navArgument
import com.example.book_madness.ui.bookItem.BookDetailsScreen
import com.example.book_madness.ui.bookItem.BookEditScreen
import com.example.book_madness.ui.bookItem.BookEntryScreen
import com.example.book_madness.ui.home.HomeScreen
import com.example.book_madness.ui.stats.StatisticsScreen
import com.example.book_madness.ui.timer.CountDownTimerScreen

@Composable
fun BookMadnessNavHost(
    navController: NavHostController,
    startDestination: String,
    modifier: Modifier = Modifier
) {
    NavHost(
        navController = navController,
        startDestination = startDestination,
        modifier = modifier
    ) {
        composable(route = BookMadnessScreenRoutes.HOME_SCREEN) {
            HomeScreen(
                navigateToBookEntry = { navController.navigate(BookMadnessScreenRoutes.BOOK_ADD_SCREEN) },
                navigateToBookDetails = {
                    navController.navigate("${BookMadnessScreenRoutes.BOOK_DETAIL_SCREEN}/${it}")
                },
                bottomNavigationBar = { BottomNavigationBar(navController) }
            )
        }
        composable(route = BookMadnessScreenRoutes.STATISTICS_SCREEN) {
            StatisticsScreen(
                navigateToBookEntry = { navController.navigate(BookMadnessScreenRoutes.BOOK_ADD_SCREEN) },
                bottomNavigationBar = { BottomNavigationBar(navController) }
            )
        }
        composable(route = BookMadnessScreenRoutes.BOOK_ADD_SCREEN) {
            BookEntryScreen(
                navigateBack = { navController.popBackStack() }
            )
        }
        composable(
            route = BookMadnessDestinations.BOOK_EDIT_ROUTE,
            arguments = listOf(navArgument(BookMadnessDestinationsArgs.BOOK_ID_ARG) {
                type = NavType.IntType
            })
        ) {
            BookEditScreen(
                navigateBack = { navController.popBackStack() }
            )
        }
        composable(
            route = BookMadnessDestinations.BOOK_DETAIL_ROUTE,
            arguments = listOf(navArgument(BookMadnessDestinationsArgs.BOOK_ID_ARG) {
                type = NavType.IntType
            })
        ) {
            BookDetailsScreen(
                navigateBack = { navController.popBackStack() },
                navigateToEditBook = { navController.navigate("${BookMadnessScreenRoutes.BOOK_EDIT_SCREEN}/$it") },
                bottomNavigationBar = { BottomNavigationBar(navController) },
            )
        }
        composable(route = BookMadnessScreenRoutes.COUNT_DOWN_TIMER_SCREEN) {
            CountDownTimerScreen(
                bottomNavigationBar = { BottomNavigationBar(navController) }
            )
        }
    }
}
