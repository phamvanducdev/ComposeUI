package com.ducpv.composeui.feature.chat.detail.components

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.LastBaseline
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.ducpv.composeui.domain.model.chat.User
import com.ducpv.composeui.domain.usecase.chat.MessageItem
import com.ducpv.composeui.shared.theme.*
import com.ducpv.composeui.shared.utility.toFormattedDurationDayTime
import java.util.*

/**
 * Created by pvduc9773 on 19/06/2023.
 */

@Composable
fun ItemMessage(
    message: MessageItem,
    isFirstMessageByAuthor: Boolean,
    isLastMessageByAuthor: Boolean,
    onAuthorClickListener: (User) -> Unit = {},
    onItemClickListener: (MessageItem) -> Unit = {},
) {
    val borderColor = if (message.isMine) {
        ThemeColor.Blue.color.alpha50
    } else {
        ThemeColor.Brown.color.alpha50
    }
    val spaceBetweenAuthors = if (isLastMessageByAuthor) {
        Modifier.padding(top = 8.dp)
    } else {
        Modifier
    }
    Row(modifier = spaceBetweenAuthors) {
        if (isLastMessageByAuthor) {
            // Avatar
            AsyncImage(
                modifier = Modifier
                    .padding(start = 12.dp, end = 8.dp)
                    .size(32.dp)
                    .border(1.dp, borderColor, CircleShape)
                    .border(2.dp, MaterialTheme.colorScheme.surface, CircleShape)
                    .clip(CircleShape)
                    .align(Alignment.Top)
                    .clickable {
                        onAuthorClickListener.invoke(message.author)
                    },
                model = message.author.avatar,
                contentScale = ContentScale.Crop,
                contentDescription = null,
            )
        } else {
            // Space under avatar
            Spacer(modifier = Modifier.width(52.dp)) // [12dp] [32dp] [8dp]
        }
        AuthorAndTextMessage(
            message = message,
            isFirstMessageByAuthor = isFirstMessageByAuthor,
            isLastMessageByAuthor = isLastMessageByAuthor,
            onAuthorClickListener = onAuthorClickListener,
            modifier = Modifier
                .padding(end = 16.dp)
                .weight(1f),
        )
    }
}

@Composable
fun AuthorAndTextMessage(
    modifier: Modifier,
    message: MessageItem,
    isFirstMessageByAuthor: Boolean,
    isLastMessageByAuthor: Boolean,
    onAuthorClickListener: (User) -> Unit = {},
    onItemClickListener: (MessageItem) -> Unit = {},
) {
    Column(modifier = modifier) {
        if (isLastMessageByAuthor) {
            AuthorNameAndTime(
                auth = message.author,
                createdAt = message.createdAt,
                onAuthorClickListener = onAuthorClickListener,
            )
        }
        ChatItemBubble(
            message = message,
            onItemClickListener = onItemClickListener,
        )
        if (isFirstMessageByAuthor) {
            // Last bubble before next author
            Spacer(modifier = Modifier.height(8.dp))
        } else {
            // Between bubbles
            Spacer(modifier = Modifier.height(4.dp))
        }
    }
}

@Composable
fun AuthorNameAndTime(
    auth: User,
    createdAt: Date,
    onAuthorClickListener: (User) -> Unit = {},
) {
    // Combine author and timestamp for a11y.
    Row(
        modifier = Modifier.semantics(mergeDescendants = true) {},
    ) {
        Text(
            text = auth.name.orEmpty(),
            style = AppTypography.titleMedium,
            color = com.ducpv.composeui.shared.theme.ThemeColor.Black.color,
            modifier = Modifier
                .alignBy(LastBaseline)
                .paddingFrom(LastBaseline, after = 8.dp), // Space to 1st bubble
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = createdAt.toFormattedDurationDayTime(LocalContext.current),
            style = AppTypography.bodySmall,
            color = com.ducpv.composeui.shared.theme.ThemeColor.Black.color.alpha50,
            modifier = Modifier.alignBy(LastBaseline),
        )
    }
}

private val ChatBubbleShape = RoundedCornerShape(4.dp, 16.dp, 16.dp, 16.dp)

@Composable
fun ChatItemBubble(
    message: MessageItem,
    onItemClickListener: (MessageItem) -> Unit = {},
) {
    val backgroundBubbleColor = if (message.isMine) {
        ThemeColor.Blue.color.alpha20
    } else {
        ThemeColor.Gray.color.alpha20
    }

    Column {
        Surface(
            color = backgroundBubbleColor,
            shape = ChatBubbleShape,
        ) {
            // TODO detect tag, url, image...
            Text(
                text = message.content,
                modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
            )
        }
    }
}
