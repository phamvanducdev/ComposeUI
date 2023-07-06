package com.ducpv.composeui.feature.chat.authentication

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ducpv.composeui.R
import com.ducpv.composeui.navigation.AppState
import com.ducpv.composeui.shared.compose.ScreenStateOverlay
import com.ducpv.composeui.shared.theme.AppTypography
import com.ducpv.composeui.shared.theme.ThemeColor
import com.ducpv.composeui.shared.theme.color

/**
 * Created by pvduc9773 on 24/05/2023.
 */
@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun AuthenticationBottomSheet(
    appState: AppState,
    viewModel: AuthenticationViewModel = hiltViewModel(),
) {
    val keyboardController = LocalSoftwareKeyboardController.current

    var email by remember {
        mutableStateOf("phamvanduc.dev@gmail.com")
    }
    var password by remember {
        mutableStateOf("P@ssword")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp, vertical = 12.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .width(32.dp)
                .height(4.dp)
                .clip(RoundedCornerShape(50))
                .background(ThemeColor.Gray.color),
        )
        Spacer(modifier = Modifier.height(24.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = email,
            onValueChange = { email = it },
            label = { Text(stringResource(R.string.email)) },
            singleLine = true,
            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Next),
            textStyle = TextStyle(color = ThemeColor.Black.color),
        )
        Spacer(modifier = Modifier.height(16.dp))
        TextField(
            modifier = Modifier.fillMaxWidth(),
            value = password,
            onValueChange = { password = it },
            label = { Text(stringResource(R.string.password)) },
            singleLine = true,
            textStyle = TextStyle(color = ThemeColor.Black.color),
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Password,
                imeAction = ImeAction.Done,
            ),
            keyboardActions = KeyboardActions(onDone = { keyboardController?.hide() }),
            visualTransformation = PasswordVisualTransformation(),
        )
        Spacer(modifier = Modifier.height(24.dp))
        Button(
            onClick = {
                viewModel.onButtonClick(
                    email = email,
                    password = password,
                    onSuccess = {
                        viewModel.clearScreenState()
                        appState.navController.popBackStack()
                        when (viewModel.authenticationType) {
                            AuthenticationType.SIGN_IN -> appState.showSnackBarMessage(R.string.sign_in_success)
                            AuthenticationType.SIGN_UP -> appState.showSnackBarMessage(R.string.sign_up_success)
                        }
                    },
                )
            },
            contentPadding = PaddingValues(horizontal = 12.dp),
        ) {
            Text(
                text = stringResource(id = viewModel.authenticationType.displayName),
                textAlign = TextAlign.Center,
                style = AppTypography.bodyLarge,
            )
        }
    }

    ScreenStateOverlay(
        screenState = viewModel.screenState,
        onDismiss = {
            viewModel.clearScreenState()
        },
    )
}
