package com.ducpv.composeui.feature.chat.detail

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ducpv.composeui.domain.usecase.chat.MessageItem
import com.ducpv.composeui.feature.chat.detail.components.InputMessage
import com.ducpv.composeui.feature.chat.detail.components.ListMessage
import com.ducpv.composeui.navigation.AppState
import com.ducpv.composeui.navigation.NavigationIcon
import kotlinx.coroutines.launch

/**
 * Created by pvduc9773 on 17/06/2023.
 */
@Composable
fun ChatDetailScreen(
    appState: AppState,
    viewModel: ChatDetailViewModel = hiltViewModel(),
) {
    val messages: List<MessageItem> by viewModel.messageList.collectAsStateWithLifecycle(initialValue = emptyList())

    val messagesScrollState = rememberLazyListState()

    LaunchedEffect(Unit) {
        appState.navigationIcon = NavigationIcon.Back
        viewModel.roomInfo.collect { roomInfo ->
            appState.topBarTitle = roomInfo?.name
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
    ) {
        ListMessage(
            messages = messages,
            onScrollState = messagesScrollState,
            modifier = Modifier
                .fillMaxSize()
                .weight(1f),
        )
        InputMessage(
            onSendMessage = {
                viewModel.sendTextMessage(it)
            },
            onResetScrollState = {
                appState.coroutineScope.launch {
                    messagesScrollState.animateScrollToItem(0)
                }
            },
            // Let this element handle the padding so that the elevation is shown behind the navigation bar
            modifier = Modifier
                .navigationBarsPadding()
                .imePadding(),
        )
    }
}
