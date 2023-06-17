package com.ducpv.composeui.shared.ui

import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow

/**
 * Created by pvduc9773 on 21/04/2023.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppTopBar(
    title: Int? = null,
    navigationIcon: ImageVector? = null,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    onNavigationClick: () -> Unit = {},
) {
    TopAppBar(
        title = {
            if (title != null) {
                Text(
                    text = stringResource(id = title),
                    overflow = TextOverflow.Ellipsis,
                    maxLines = 1,
                )
            }
        },
        navigationIcon = {
            if (navigationIcon != null) {
                IconButton(onClick = onNavigationClick) {
                    Icon(
                        imageVector = navigationIcon,
                        contentDescription = navigationIcon.name,
                    )
                }
            }
        },
        scrollBehavior = scrollBehavior,
    )
}
