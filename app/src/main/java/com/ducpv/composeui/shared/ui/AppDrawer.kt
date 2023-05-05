package com.ducpv.composeui.shared.ui

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import com.ducpv.composeui.navigation.TopLevelDestination
import com.ducpv.composeui.shared.theme.ThemeColor
import com.ducpv.composeui.shared.theme.alpha10
import com.ducpv.composeui.shared.theme.color

/**
 * Created by pvduc9773 on 24/04/2023.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppDrawer(
    destinations: List<TopLevelDestination>,
    currentDestination: NavDestination?,
    navigateToTopLevelDestination: (TopLevelDestination) -> Unit
) {
    DismissibleDrawerSheet(
        modifier = Modifier.fillMaxWidth(0.75f),
    ) {
        destinations.forEach { destination ->
            val selected = currentDestination.isTopLevelDestinationInHierarchy(destination)
            DrawerItem(
                icon = destination.icon,
                label = destination.label,
                selected = selected,
                onItemClick = {
                    navigateToTopLevelDestination(destination)
                },
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DrawerItem(
    icon: ImageVector,
    label: Int,
    selected: Boolean = false,
    onItemClick: () -> Unit = {}
) {
    NavigationDrawerItem(
        modifier = Modifier.padding(0.dp),
        selected = selected,
        icon = {
            Icon(
                imageVector = icon,
                contentDescription = null,
            )
        },
        label = {
            Text(text = stringResource(id = label))
        },
        shape = RoundedCornerShape(0),
        colors = NavigationDrawerItemDefaults.colors(
            selectedContainerColor = ThemeColor.Blue.color.alpha10,
            unselectedContainerColor = ThemeColor.Transparent.color,
            selectedIconColor = ThemeColor.Black.color,
            unselectedIconColor = ThemeColor.Brown.color,
            selectedTextColor = ThemeColor.Black.color,
            unselectedTextColor = ThemeColor.Brown.color,
        ),
        onClick = onItemClick,
    )
}

private fun NavDestination?.isTopLevelDestinationInHierarchy(destination: TopLevelDestination) =
    this?.hierarchy?.any {
        it.route?.contains(destination.graphRoute, true) ?: false
    } ?: false
