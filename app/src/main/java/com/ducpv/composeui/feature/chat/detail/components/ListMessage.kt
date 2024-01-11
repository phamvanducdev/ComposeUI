package com.ducpv.composeui.feature.chat.detail.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import com.ducpv.composeui.domain.usecase.chat.MessageItem
import com.ducpv.composeui.shared.utility.toFormattedDurationDateTime
import kotlinx.coroutines.launch

/**
 * Created by pvduc9773 on 19/06/2023.
 */
@Composable
fun ListMessage(
    modifier: Modifier = Modifier,
    messages: List<MessageItem>,
    onScrollState: LazyListState,
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()

    Box(modifier = modifier) {
        LazyColumn(
            state = onScrollState,
            reverseLayout = true,
            modifier = Modifier.fillMaxSize(),
        ) {
            for (index in messages.indices) {
                val message = messages[index]
                val preMessage = messages.getOrNull(index - 1)
                val nextMessage = messages.getOrNull(index + 1)

                val isFirstMessageByTimeCreated =
                    nextMessage?.createdAt?.toFormattedDurationDateTime(context) !=
                        message.createdAt.toFormattedDurationDateTime(context)

                val isFirstMessageByAuthor = preMessage?.author?.uid != message.author.uid
                val isLastMessageByAuthor =
                    nextMessage?.author?.uid != message.author.uid || isFirstMessageByTimeCreated

                item {
                    ItemMessage(
                        message = message,
                        isFirstMessageByAuthor = isFirstMessageByAuthor,
                        isLastMessageByAuthor = isLastMessageByAuthor,
                        onAuthorClickListener = {
                            // TODO
                        },
                        onItemClickListener = {
                            // TODO
                        },
                    )
                }

                if (index == messages.size - 1 || isFirstMessageByTimeCreated) { // First message or first message by date
                    item {
                        ItemDateTimeHeader(
                            dateTimeString = message.createdAt.toFormattedDurationDateTime(LocalContext.current),
                        )
                    }
                }
            }
        }

        // Jump to bottom button shows up when user scrolls past a threshold.
        // Convert to pixels:
        val jumpToBottomThreshold = 56.dp
        val jumpThreshold = with(LocalDensity.current) {
            jumpToBottomThreshold.toPx()
        }
        // Show the button if the first visible item is not the first one or if the offset is
        // greater than the threshold.
        val jumpToBottomButtonEnabled by remember {
            derivedStateOf {
                onScrollState.firstVisibleItemIndex != 0 ||
                    onScrollState.firstVisibleItemScrollOffset > jumpThreshold
            }
        }
        ButtonJumpToBottom(
            // Only show if the scroller is not at the bottom
            enabled = jumpToBottomButtonEnabled,
            onClicked = {
                coroutineScope.launch {
                    onScrollState.animateScrollToItem(0)
                }
            },
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(end = 16.dp),
        )
    }
}
