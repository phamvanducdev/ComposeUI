package com.ducpv.composeui

import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.core.view.WindowCompat
import com.ducpv.composeui.navigation.*
import com.ducpv.composeui.shared.theme.ComposeUITheme
import com.ducpv.composeui.shared.ui.AppDrawer
import com.ducpv.composeui.shared.ui.AppScaffold
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val viewModel: MainViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        val contentView: View = findViewById(android.R.id.content)
        contentView.viewTreeObserver.addOnPreDrawListener(
            object : ViewTreeObserver.OnPreDrawListener {
                override fun onPreDraw(): Boolean {
                    return if (viewModel.state.value == MainViewModel.MainAppState.Completed) {
                        contentView.viewTreeObserver.removeOnPreDrawListener(this)
                        setContent {
                            ComposeUITheme {
                                ComposeUiApp()
                            }
                        }
                        true
                    } else {
                        false
                    }
                }
            },
        )
        viewModel.initialize()
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComposeUiApp(
    appState: AppState = rememberAppState()
) {
    ModalNavigationDrawer(
        modifier = Modifier.fillMaxSize(),
        drawerState = appState.drawerState,
        drawerContent = {
            AppDrawer(
                destinations = appState.topLevelDestinations,
                currentDestination = appState.currentDestination,
                navigateToTopLevelDestination = appState::navigateToTopLevelDestination,
            )
        },
    ) {
        AppScaffold(
            topBarTitle = appState.topBarTitle,
            navigationIcon = appState.navigationIcon,
            snackHostState = appState.snackHostState,
            onNavigationClick = appState::onNavigationClick,
        ) { paddingValues ->
            Box(modifier = Modifier.padding(paddingValues)) {
                AppNavHost(appState = appState)
            }
        }
    }
}
