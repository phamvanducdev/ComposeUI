package com.ducpv.composeui.navigation

import androidx.annotation.StringRes
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import com.ducpv.composeui.R
import com.ducpv.composeui.feature.runtracker.RunTrackerScreen
import com.ducpv.composeui.feature.tictactoegame.TicTacToeGameScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation

/**
 * Created by pvduc9773 on 03/04/2023.
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavHost(appState: AppState) {
    val startDestination = TopLevelDestination.TicTacToeGame.graphRoute
    AnimatedNavHost(
        navController = appState.navController,
        startDestination = startDestination,
    ) {
        navigation(
            route = TopLevelDestination.TicTacToeGame.graphRoute,
            startDestination = NavDestinations.TicTacToeGame.route,
        ) {
            composable(route = NavDestinations.TicTacToeGame.route) {
                TicTacToeGameScreen()
            }
        }
        navigation(
            route = TopLevelDestination.RunTracker.graphRoute,
            startDestination = NavDestinations.RunTracker.route,
        ) {
            composable(route = NavDestinations.RunTracker.route) {
                RunTrackerScreen()
            }
        }
    }
}

enum class NavDestinations(val route: String, @StringRes val label: Int) {
    TicTacToeGame("tic_tac_toe_game_route", R.string.tic_tac_toe_game),
    RunTracker("run_tracker_route", R.string.run_tracker);

    companion object {
        fun findByRoute(route: String?): NavDestinations? {
            return values().find { it.route == route }
        }
    }
}
