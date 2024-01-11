package com.ducpv.composeui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import androidx.core.view.WindowCompat
import com.ducpv.composeui.navigation.AppNavHost
import com.ducpv.composeui.navigation.AppState
import com.ducpv.composeui.navigation.rememberAppState
import com.ducpv.composeui.shared.theme.ComposeUITheme
import com.ducpv.composeui.shared.ui.AppDrawer
import com.ducpv.composeui.shared.ui.AppScaffold
import com.google.accompanist.navigation.material.ExperimentalMaterialNavigationApi
import com.google.accompanist.navigation.material.ModalBottomSheetLayout
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        val splashScreen = installSplashScreen()
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        splashScreen.setKeepOnScreenCondition {
            viewModel.state.value != MainViewModel.MainAppState.Completed
        }
        viewModel.initialize()
        setContent {
            ComposeUITheme {
                ComposeUiApp()
            }
        }
    }
}

@OptIn(ExperimentalMaterialNavigationApi::class)
@Composable
fun ComposeUiApp(
    appState: AppState = rememberAppState(appContext = LocalContext.current)
) {
    ModalNavigationDrawer(
        modifier = Modifier.fillMaxSize(),
        drawerState = appState.drawerState,
        gesturesEnabled = appState.drawerGesturesEnabled,
        drawerContent = {
            AppDrawer(
                currentNavDestination = appState.currentDestination,
                navGraphDestinations = appState.navGraphDestinations,
                navigateToNavGraphDestination = appState::navigateToNavGraphDestination,
            )
        },
    ) {
        ModalBottomSheetLayout(
            sheetShape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp),
            bottomSheetNavigator = appState.bottomSheetNavigator,
        ) {
            AppScaffold(
                topBarTitle = appState.topBarTitle,
                navigationIcon = appState.navigationIcon?.icon,
                snackHostState = appState.snackHostState,
                onNavigationClick = appState::onNavigationClick,
            ) { paddingValues ->
                Box(modifier = Modifier.padding(paddingValues)) {
                    AppNavHost(appState = appState)
                }
            }
        }
    }
}
