package com.ducpv.demo.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import com.ducpv.demo.feature.component.ComponentListScreen
import com.ducpv.demo.feature.miniapp.MiniAppListScreen
import com.ducpv.demo.feature.miniapp.clock.AnalogClockScreen
import com.ducpv.demo.feature.miniapp.switchbutton.SwitchButtonScreen
import com.ducpv.demo.feature.miniapp.tictactoe.TicTacToeScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation

/**
 * Created by pvduc9773 on 03/04/2023.
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun AppNavHost(appState: AppState) {
    val startDestination = TopLevelDestination.MiniApp.graphRoute
    AnimatedNavHost(
        navController = appState.navController,
        startDestination = startDestination
    ) {
        navigation(
            route = TopLevelDestination.MiniApp.graphRoute,
            startDestination = NavDestinations.MiniAppList.route,
        ) {
            composable(NavDestinations.MiniAppList.route) {
                MiniAppListScreen(appState = appState)
            }
            composable(NavDestinations.AnalogClock.route) {
                AnalogClockScreen(appState = appState)
            }
            composable(NavDestinations.TicTacToe.route) {
                TicTacToeScreen(appState = appState)
            }
            composable(NavDestinations.SwitchButton.route) {
                SwitchButtonScreen(appState = appState)
            }
        }
        navigation(
            route = TopLevelDestination.Component.graphRoute,
            startDestination = NavDestinations.ComponentList.route
        ) {
            composable(route = NavDestinations.ComponentList.route) {
                ComponentListScreen(appState = appState)
            }
        }
    }
}

enum class NavDestinations(val route: String, val label: String) {
    ComponentList("component_route", "Component"),

    MiniAppList("mini_app_route", "MiniApp"),
    SwitchButton("switch_button", "SwitchButton"),
    AnalogClock("analog_clock_route", "AnalogClock"),
    TicTacToe("tic_tac_toe_route", "TicTacToe");

    companion object {
        fun findByRoute(route: String?): NavDestinations? {
            return values().find { it.route == route }
        }
    }
}
