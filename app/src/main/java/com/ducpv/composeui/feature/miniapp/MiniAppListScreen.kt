package com.ducpv.composeui.feature.miniapp

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ducpv.composeui.feature.CatalogItem
import com.ducpv.composeui.navigation.AppState
import com.ducpv.composeui.navigation.NavDestinations

/**
 * Created by pvduc9773 on 03/04/2023.
 */
@Composable
fun MiniAppListScreen(
    appState: AppState
) {
    val miniApps = listOf(
        NavDestinations.AnalogClock,
        NavDestinations.TicTacToe,
        NavDestinations.SwitchButton,
        NavDestinations.GoogleMap,
    )
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        items(miniApps) {
            CatalogItem(
                name = it.label,
                description = it.route,
                onItemClick = {
                    appState.navController.navigate(it.route)
                },
            )
        }
    }
}
