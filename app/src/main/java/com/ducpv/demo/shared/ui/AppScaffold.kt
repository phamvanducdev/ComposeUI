package com.ducpv.demo.shared.ui

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.input.nestedscroll.nestedScroll
import com.ducpv.demo.navigation.AppState

/**
 * Created by pvduc9773 on 21/04/2023.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppScaffold(
    topBarTitle: String?,
    navigationIcon: ImageVector? = null,
    snackHostState: SnackbarHostState,
    onNavigationClick: () -> Unit = {},
    content: @Composable (PaddingValues) -> Unit
) {
    val scrollBehavior = TopAppBarDefaults.pinnedScrollBehavior()
    Scaffold(
        topBar = {
            TopAppBar(
                title = topBarTitle,
                navigationIcon = navigationIcon,
                scrollBehavior = scrollBehavior,
                onNavigationClick = onNavigationClick,
            )
        },
        snackbarHost = { SnackbarHost(snackHostState) },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection),
        content = content
    )
}