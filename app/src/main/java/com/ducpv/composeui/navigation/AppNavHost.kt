package com.ducpv.composeui.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.runtime.Composable
import androidx.navigation.NavType
import androidx.navigation.navArgument
import com.ducpv.composeui.feature.chat.authentication.AuthenticationBottomSheet
import com.ducpv.composeui.feature.chat.rooms.ChatRoomsScreen
import com.ducpv.composeui.feature.runtracker.RunTrackerScreen
import com.ducpv.composeui.feature.tictactoegame.TicTacToeGameScreen
import com.google.accompanist.navigation.animation.AnimatedNavHost
import com.google.accompanist.navigation.animation.composable
import com.google.accompanist.navigation.animation.navigation
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.bottomSheet

/**
 * Created by pvduc9773 on 03/04/2023.
 */
@OptIn(
    ExperimentalAnimationApi::class,
    ExperimentalMaterialNavigationApi::class
)
@Composable
fun AppNavHost(appState: AppState) {
    AnimatedNavHost(
        navController = appState.navController,
        startDestination = NavGraphDestination.Chat.graphRoute,
    ) {
        navigation(
            route = NavGraphDestination.TicTacToeGame.graphRoute,
            startDestination = NavGraphDestination.TicTacToeGame.startRoute,
        ) {
            composable(route = "ticTacToe") {
                TicTacToeGameScreen(appState = appState)
            }
        }
        navigation(
            route = NavGraphDestination.RunTracker.graphRoute,
            startDestination = NavGraphDestination.RunTracker.startRoute,
        ) {
            composable(route = "runTracker") {
                RunTrackerScreen(appState = appState)
            }
        }
        navigation(
            route = NavGraphDestination.Chat.graphRoute,
            startDestination = NavGraphDestination.Chat.startRoute,
        ) {
            composable(route = "chatRooms") {
                ChatRoomsScreen(appState = appState)
            }
        }
        bottomSheet(
            route = "authentication/{typeId}",
            arguments = listOf(
                navArgument("typeId") {
                    type = NavType.StringType
                }
            ),
        ) {
            AuthenticationBottomSheet(appState = appState)
        }
    }
}
