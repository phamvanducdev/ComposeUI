package com.ducpv.composeui.navigation

import androidx.annotation.StringRes
import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import com.ducpv.composeui.R
import com.ducpv.composeui.feature.analogclock.AnalogClockScreen
import com.ducpv.composeui.feature.runningtracker.RunningTrackerScreen
import com.ducpv.composeui.feature.switchlocker.SwitchLockerScreen
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
    val startDestination = TopLevelDestination.AnalogClock.graphRoute
    AnimatedNavHost(
        navController = appState.navController,
        startDestination = startDestination,
    ) {
        navigation(
            route = TopLevelDestination.AnalogClock.graphRoute,
            startDestination = NavDestinations.AnalogClock.route,
        ) {
            composable(NavDestinations.AnalogClock.route) {
                AnalogClockScreen(appState = appState)
            }
        }
        navigation(
            route = TopLevelDestination.SwitchLocker.graphRoute,
            startDestination = NavDestinations.SwitchLocker.route,
        ) {
            composable(route = NavDestinations.SwitchLocker.route) {
                SwitchLockerScreen(appState = appState)
            }
        }
        navigation(
            route = TopLevelDestination.TicTacToeGame.graphRoute,
            startDestination = NavDestinations.TicTacToeGame.route,
        ) {
            composable(route = NavDestinations.TicTacToeGame.route) {
                TicTacToeGameScreen(appState = appState)
            }
        }
        navigation(
            route = TopLevelDestination.RunningTracker.graphRoute,
            startDestination = NavDestinations.RunningTracker.route,
        ) {
            composable(route = NavDestinations.RunningTracker.route) {
                RunningTrackerScreen(appState = appState)
            }
        }
    }
}

enum class NavDestinations(val route: String, @StringRes val label: Int) {
    AnalogClock("analog_clock_route", R.string.analog_clock),
    SwitchLocker("switch_locker_route", R.string.switch_locker),
    TicTacToeGame("tic_tac_toe_game_route", R.string.tic_tac_toe_game),
    RunningTracker("running_tracker_route", R.string.running_tracker);

    companion object {
        fun findByRoute(route: String?): NavDestinations? {
            return values().find { it.route == route }
        }
    }
}