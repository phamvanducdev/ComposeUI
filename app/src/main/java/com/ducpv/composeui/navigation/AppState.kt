package com.ducpv.composeui.navigation

import androidx.compose.animation.ExperimentalAnimationApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavDestination
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.navOptions
import com.ducpv.composeui.R
import com.google.accompanist.navigation.animation.rememberAnimatedNavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Created by pvduc9773 on 03/04/2023.
 */
@OptIn(ExperimentalMaterial3Api::class)
class AppState(
    val drawerState: DrawerState,
    val navController: NavHostController,
    val coroutineScope: CoroutineScope,
    val snackHostState: SnackbarHostState
) {
    init {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            drawerGesturesEnabled = if (multipleTopDestination) {
                val destinations = TopLevelDestination.values().toList() - TopLevelDestination.RunTracker
                destination.route in destinations.map { it.startRoute }
            } else {
                false
            }
        }
    }

    var drawerGesturesEnabled by mutableStateOf(true)

    val topLevelDestinations = TopLevelDestination.values().asList()

    val currentDestination: NavDestination?
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination

    private val currentTopLevelDestination: TopLevelDestination?
        get() = topLevelDestinations.find {
            it.startRoute == navController.currentDestination?.route
        }

    private val multipleTopDestination: Boolean
        get() = topLevelDestinations.size > 1

    val topBarTitle: Int?
        @Composable get() = NavDestinations.findByRoute(currentDestination?.route)?.label

    val navigationIcon: ImageVector?
        get() {
            if (topLevelDestinations.size <= 1) return null
            return if (currentTopLevelDestination != null) {
                Icons.Default.Menu
            } else {
                Icons.Default.ArrowBack
            }
        }

    fun onNavigationClick() {
        if (currentTopLevelDestination != null) {
            onChangeDrawerState()
        } else {
            navController.popBackStack()
        }
    }

    fun navigateToTopLevelDestination(destination: TopLevelDestination) {
        val topLevelNavOptions = navOptions {
            // Pop up to the start destination of the graph to
            // avoid building up a large stack of destinations
            // on the back stack as users select items
            popUpTo(navController.graph.findStartDestination().id) {
                saveState = true
            }
            // Avoid multiple copies of the same destination when
            // re-selecting the same item
            launchSingleTop = true
            // Restore state when re-selecting a previously selected item
            restoreState = true
        }
        when (destination) {
            TopLevelDestination.TicTacToeGame,
            TopLevelDestination.RunTracker -> {
                navController.navigate(destination.graphRoute, topLevelNavOptions)
                onChangeDrawerState()
            }
        }
    }

    private fun onChangeDrawerState() {
        coroutineScope.launch {
            if (drawerState.isAnimationRunning) return@launch
            if (drawerState.isClosed) {
                drawerState.open()
            } else {
                drawerState.close()
            }
        }
    }
}

enum class TopLevelDestination(
    val icon: ImageVector,
    val label: Int,
    val startRoute: String,
    val graphRoute: String
) {
    TicTacToeGame(
        icon = Icons.Default.Gamepad,
        label = R.string.tic_tac_toe_game,
        startRoute = NavDestinations.TicTacToeGame.route,
        graphRoute = "tic_tac_toe_game_graph",
    ),
    RunTracker(
        icon = Icons.Default.DirectionsRun,
        label = R.string.run_tracker,
        startRoute = NavDestinations.RunTracker.route,
        graphRoute = "running_route_graph",
    )
}

@OptIn(
    ExperimentalMaterial3Api::class,
    ExperimentalAnimationApi::class,
)
@Composable
fun rememberAppState(
    drawerState: DrawerState = rememberDrawerState(
        initialValue = DrawerValue.Closed,
    ),
    navController: NavHostController = rememberAnimatedNavController(),
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    snackHostState: SnackbarHostState = remember { SnackbarHostState() },
): AppState {
    return remember(drawerState, navController, coroutineScope, snackHostState) {
        AppState(
            drawerState,
            navController,
            coroutineScope,
            snackHostState,
        )
    }
}
