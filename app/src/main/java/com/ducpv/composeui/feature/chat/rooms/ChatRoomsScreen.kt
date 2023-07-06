package com.ducpv.composeui.feature.chat.rooms

import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.PickVisualMediaRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import coil.compose.AsyncImage
import com.ducpv.composeui.R
import com.ducpv.composeui.domain.model.chat.Room
import com.ducpv.composeui.domain.model.chat.User
import com.ducpv.composeui.navigation.AppState
import com.ducpv.composeui.navigation.NavigationIcon
import com.ducpv.composeui.shared.compose.ScreenStateOverlay
import com.ducpv.composeui.shared.theme.ThemeColor
import com.ducpv.composeui.shared.theme.alpha10
import com.ducpv.composeui.shared.theme.color
import com.ducpv.composeui.shared.utility.toFormattedDurationTime

/**
 * Created by pvduc9773 on 23/05/2023.
 */
@Composable
fun ChatRoomsScreen(
    appState: AppState,
    viewModel: ChatRoomsViewModel = hiltViewModel()
) {
    val user: User? by viewModel.userInfo.collectAsStateWithLifecycle(initialValue = null)
    val roomList: List<Room> by viewModel.roomList.collectAsStateWithLifecycle(initialValue = emptyList())

    val singlePhotoPickerLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.PickVisualMedia(),
        onResult = { imageUri ->
            viewModel.onChangeImage(
                imageUri = imageUri,
                onSuccess = {
                    viewModel.clearScreenState()
                    appState.showSnackBarMessage(R.string.update_image_avatar_success)
                },
            )
        },
    )

    LaunchedEffect(Unit) {
        appState.topBarTitle = appState.appContext.getString(R.string.chat_rooms)
        appState.navigationIcon = NavigationIcon.Menu
    }

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        authorItem(
            user = user,
            onSignIn = {
                appState.navController.navigate("app/authentication/signIn")
            },
            onSignUp = {
                appState.navController.navigate("app/authentication/signUp")
            },
            onLogout = {
                viewModel.onLogout {
                    appState.navController.navigate("app/authentication/signIn")
                }
            },
            onChangeImage = {
                singlePhotoPickerLauncher.launch(
                    PickVisualMediaRequest(ActivityResultContracts.PickVisualMedia.ImageAndVideo),
                )
            },
        )
        roomItems(
            roomList = roomList,
            onJoinRoom = { rid ->
                appState.navController.navigate("app/chat/detail/$rid")
            },
        )
    }

    ScreenStateOverlay(
        screenState = viewModel.screenState,
        onDismiss = {
            viewModel.clearScreenState()
        },
    )
}

fun LazyListScope.authorItem(
    user: User? = null,
    onSignIn: () -> Unit,
    onSignUp: () -> Unit,
    onLogout: () -> Unit,
    onChangeImage: () -> Unit,
) {
    if (user != null) {
        item {
            ItemAuthor(
                user = user,
                onLogout = onLogout,
                onChangeImage = onChangeImage,
            )
        }
    } else {
        item {
            ItemAuthentication(
                onSignIn = onSignIn,
                onSignUp = onSignUp,
            )
        }
    }
}

fun LazyListScope.roomItems(
    roomList: List<Room>,
    onJoinRoom: (rid: String) -> Unit,
) {
    for (room in roomList) {
        item {
            ItemRoom(
                room = room,
                onItemClickListener = onJoinRoom,
            )
        }
    }
}

@Composable
fun ItemAuthor(
    user: User,
    onLogout: () -> Unit,
    onChangeImage: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Box(
            modifier = Modifier
                .size(42.dp)
                .clip(CircleShape)
                .background(ThemeColor.Gray.color.alpha10)
                .clickable { onChangeImage.invoke() },
        ) {
            AsyncImage(
                modifier = Modifier
                    .border(1.dp, ThemeColor.Blue.color, CircleShape)
                    .border(2.dp, MaterialTheme.colorScheme.surface, CircleShape)
                    .clip(CircleShape),
                model = user.avatar,
                contentScale = ContentScale.Crop,
                contentDescription = null,
            )
        }
        Spacer(modifier = Modifier.width(8.dp))
        Column(
            modifier = Modifier.weight(1f),
        ) {
            Text(
                text = user.name.orEmpty(),
                color = ThemeColor.Black.color,
                fontSize = 14.sp,
            )
            Text(
                text = user.email,
                color = ThemeColor.Gray.color,
                fontSize = 10.sp,
            )
        }
        Button(
            onClick = onLogout,
            modifier = Modifier.size(32.dp),
            contentPadding = PaddingValues(horizontal = 0.dp),
        ) {
            Icon(
                modifier = Modifier.size(16.dp),
                imageVector = Icons.Default.Logout,
                contentDescription = null,
            )
        }
    }
}

@Composable
fun ItemAuthentication(
    onSignIn: () -> Unit,
    onSignUp: () -> Unit,
) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Button(onClick = onSignIn) {
            Text(text = stringResource(R.string.sign_in))
        }
        Button(onClick = onSignUp) {
            Text(text = stringResource(R.string.sign_up))
        }
    }
}

@Composable
fun ItemRoom(
    room: Room,
    onItemClickListener: (String) -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onItemClickListener.invoke(room.rid) },
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(ThemeColor.Gray.color.alpha10),
            ) {
                AsyncImage(
                    model = room.avatar,
                    contentScale = ContentScale.Crop,
                    contentDescription = null,
                )
            }
            Spacer(modifier = Modifier.width(8.dp))
            Column(
                modifier = Modifier.weight(1f),
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        text = room.name,
                        color = ThemeColor.Black.color,
                        fontSize = 14.sp,
                    )
                    room.newestMessage?.createdAt?.let { createdAt ->
                        Text(
                            text = createdAt.toFormattedDurationTime(LocalContext.current),
                            color = ThemeColor.Gray.color,
                            fontSize = 8.sp,
                        )
                    }
                }
                Text(
                    text = room.newestMessage?.content
                        ?: room.createdAt.toFormattedDurationTime(LocalContext.current),
                    color = ThemeColor.Gray.color,
                    fontSize = 10.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                )
            }
        }
    }
}
