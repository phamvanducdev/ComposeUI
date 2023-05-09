package com.ducpv.composeui.shared.ui

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import com.ducpv.composeui.R

/**
 * Created by pvduc9773 on 21/04/2023.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TopAppBar(
    title: Int? = null,
    navigationIcon: ImageVector? = null,
    scrollBehavior: TopAppBarScrollBehavior? = null,
    onNavigationClick: () -> Unit = {},
    onOpenSourceClick: () -> Unit = {},
) {
    var moreMenuExpanded by remember { mutableStateOf(false) }

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
        actions = {
            Box {
                Row {
                    IconButton(onClick = { moreMenuExpanded = true }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            contentDescription = null,
                        )
                    }
                }
                MoreMenu(
                    expanded = moreMenuExpanded,
                    onDismissRequest = {
                        moreMenuExpanded = false
                    },
                    onClick = {
                        onOpenSourceClick.invoke()
                        moreMenuExpanded = false
                    },
                )
            }
        },
        navigationIcon = {
            if (navigationIcon != null) {
                IconButton(onClick = onNavigationClick) {
                    Icon(
                        imageVector = navigationIcon,
                        contentDescription = null,
                    )
                }
            }
        },
        scrollBehavior = scrollBehavior,
    )
}

@Composable
private fun MoreMenu(
    expanded: Boolean,
    onDismissRequest: () -> Unit,
    onClick: () -> Unit = {},
) {
    DropdownMenu(
        expanded = expanded,
        onDismissRequest = onDismissRequest,
    ) {
        DropdownMenuItem(onClick = onClick, text = {
            Text(stringResource(R.string.open_source))
        })
    }
}
