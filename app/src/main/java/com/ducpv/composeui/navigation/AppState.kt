package com.ducpv.composeui.navigation

import android.content.Context
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
import com.google.accompanist.navigation.material.BottomSheetNavigator
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.rememberBottomSheetNavigator
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

/**
 * Created by pvduc9773 on 03/04/2023.
 */
@OptIn(ExperimentalMaterialNavigationApi::class)
class AppState(
    val appContext: Context,
    val coroutineScope: CoroutineScope,
    val drawerState: DrawerState,
    val snackHostState: SnackbarHostState,
    val navController: NavHostController,
    val bottomSheetNavigator: BottomSheetNavigator
) {
    var topBarTitle: String? by mutableStateOf(null)

    var navigationIcon: NavigationIcon? by mutableStateOf(null)

    var drawerGesturesEnabled: Boolean by mutableStateOf(true)

    init {
        navController.addOnDestinationChangedListener { _, currentDestination, _ ->
            drawerGesturesEnabled = if (navGraphDestinations.size > 1) {
                val navGraphDestinations = NavGraphDestination.values().toList() - NavGraphDestination.RunTracker
                currentDestination.route in navGraphDestinations.map { it.startRoute }
            } else {
                false
            }
        }
    }

    val navGraphDestinations = NavGraphDestination.values().asList()

    val currentDestination: NavDestination?
        @Composable get() = navController.currentBackStackEntryAsState().value?.destination

    fun onNavigationClick() {
        when (navigationIcon) {
            NavigationIcon.Menu -> onChangeDrawerState()
            NavigationIcon.Back -> navController.popBackStack()
            else -> Unit
        }
    }

    fun navigateToNavGraphDestination(navGraph: NavGraphDestination) {
        val navOptions = navOptions {
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
        when (navGraph) {
            NavGraphDestination.TicTacToeGame,
            NavGraphDestination.RunTracker,
            NavGraphDestination.Chat -> {
                navController.navigate(navGraph.graphRoute, navOptions)
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

    fun showSnackBarMessage(messageRes: Int) {
        coroutineScope.launch {
            snackHostState.showSnackbar(appContext.getString(messageRes))
        }
    }

    fun showSnackBarMessage(message: String) {
        coroutineScope.launch {
            snackHostState.showSnackbar(message)
        }
    }
}

enum class NavGraphDestination(
    val icon: ImageVector,
    val title: Int,
    val graphRoute: String,
    val startRoute: String,
) {
    TicTacToeGame(
        icon = Icons.Default.Gamepad,
        title = R.string.tic_tac_toe_game,
        graphRoute = "ticTacToeGraph",
        startRoute = "game/ticTacToe",
    ),
    RunTracker(
        icon = Icons.Default.DirectionsRun,
        title = R.string.run_tracker,
        graphRoute = "runTrackerGraph",
        startRoute = "app/runTracker",
    ),
    Chat(
        icon = Icons.Default.Inbox,
        title = R.string.chat,
        graphRoute = "chatGraph",
        startRoute = "app/chat/rooms",
    )
}

enum class NavigationIcon(val icon: ImageVector) {
    Menu(Icons.Default.Menu),
    Back(Icons.Default.ArrowBack),
}

@OptIn(
    ExperimentalAnimationApi::class,
    ExperimentalMaterialNavigationApi::class,
)
@Composable
fun rememberAppState(
    appContext: Context,
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    drawerState: DrawerState = rememberDrawerState(initialValue = DrawerValue.Closed),
    snackHostState: SnackbarHostState = remember { SnackbarHostState() },
    bottomSheetNavigator: BottomSheetNavigator = rememberBottomSheetNavigator(),
    navController: NavHostController = rememberAnimatedNavController(bottomSheetNavigator),
): AppState {
    return remember(appContext, coroutineScope, drawerState, snackHostState, navController, bottomSheetNavigator) {
        AppState(
            appContext,
            coroutineScope,
            drawerState,
            snackHostState,
            navController,
            bottomSheetNavigator,
        )
    }
}
