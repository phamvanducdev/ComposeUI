package com.ducpv.composeui.feature.chat.rooms

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyListScope
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.ducpv.composeui.R
import com.ducpv.composeui.domain.model.UserInfo
import com.ducpv.composeui.navigation.AppState
import com.ducpv.composeui.shared.theme.ThemeColor
import com.ducpv.composeui.shared.theme.alpha20
import com.ducpv.composeui.shared.theme.color

/**
 * Created by pvduc9773 on 23/05/2023.
 */
@Composable
fun ChatRoomsScreen(
    appState: AppState,
    viewModel: ChatRoomsViewModel = hiltViewModel()
) {
    val uiState = viewModel.uiState.collectAsStateWithLifecycle()

    LazyColumn(modifier = Modifier.fillMaxSize()) {
        itemUserInfo(
            userInfo = uiState.value.userInfo,
            onLogout = viewModel::onLogout,
        )
        for (chatRoom in uiState.value.chatRooms) {
            item {
                Text(text = chatRoom.name)
            }
        }
    }

    if (uiState.value.userInfo == null) {
        Column(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Button(onClick = { appState.navController.navigate("authentication/signIn") }) {
                Text(text = stringResource(R.string.sign_in))
            }
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { appState.navController.navigate("authentication/signUp") }) {
                Text(text = stringResource(R.string.sign_up))
            }
        }
    }
}

fun LazyListScope.itemUserInfo(
    userInfo: UserInfo?,
    onLogout: () -> Unit,
) {
    if (userInfo == null) {
        item {

        }
    }
    if (userInfo != null) {
        item {
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
                        .background(ThemeColor.Gray.color.alpha20)
                ) {
                    // TODO Image Avatar
                }
                Spacer(modifier = Modifier.width(8.dp))
                Column(
                    modifier = Modifier.weight(1f),
                ) {
                    Text(
                        text = userInfo.name.orEmpty(),
                        color = ThemeColor.Black.color,
                        fontSize = 14.sp,
                    )
                    Text(
                        text = userInfo.email,
                        color = ThemeColor.Gray.color,
                        fontSize = 10.sp
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
                        contentDescription = null
                    )
                }
            }
        }
    }
}
