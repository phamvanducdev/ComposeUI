package com.ducpv.demo.feature.miniapp.switchbutton

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ducpv.demo.R
import com.ducpv.demo.navigation.AppState

/**
 * Created by pvduc9773 on 28/04/2023.
 */
enum class ButtonState {
    NONE,
    RETRY,
    SCANNING,
    LOCKED,
    UNLOCKED;

    val thumbnailColor: Color
        get() = when (this) {
            NONE -> Color(0xFFF5F6F7)
            RETRY -> Color(0x0DD1D1D1)
            SCANNING -> Color(0x0D4C7390)
            LOCKED -> Color(0x0DFF6941)
            UNLOCKED -> Color(0x0D00ACC1)
        }

    val toggleColor: Color
        get() = when (this) {
            NONE -> Color(0xFF283C4B)
            RETRY -> Color(0xFFE4E5E6)
            SCANNING -> Color(0xFF4C7390)
            LOCKED -> Color(0xFFFF6941)
            UNLOCKED -> Color(0xFF00ACC1)
        }

    val icon: Int?
        get() = when (this) {
            NONE -> R.drawable.ic_refresh
            RETRY -> R.drawable.ic_refresh
            SCANNING -> null
            LOCKED -> R.drawable.ic_locker
            UNLOCKED -> R.drawable.ic_unlocker
        }

    val title: String
        get() = when (this) {
            NONE -> "スキャンする"
            RETRY -> "もう一度さがす"
            SCANNING -> "Bluetooth接続中..."
            LOCKED -> "閉まっています"
            UNLOCKED -> "開いています"
        }

    val titleColor: Color
        get() = when (this) {
            NONE -> Color(0xFFFFFFFF)
            RETRY -> Color(0x80131313)
            SCANNING -> Color(0xFFFFFFFF)
            LOCKED -> Color(0xFFFFFFFF)
            UNLOCKED -> Color(0xFFFFFFFF)
        }
}

@Composable
fun SwitchButtonScreen(appState: AppState) {
    var buttonState by remember { mutableStateOf(ButtonState.UNLOCKED) }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.height(16.dp))
        SwitchButtonView(
            state = buttonState,
            onChange = {
                buttonState = it
            }
        )
        Spacer(modifier = Modifier.height(32.dp))
        CheckBox(
            checked = buttonState == ButtonState.NONE,
            description = ButtonState.NONE.name,
            onCheckedChange = {
                buttonState = ButtonState.NONE
            }
        )
        CheckBox(
            checked = buttonState == ButtonState.RETRY,
            description = ButtonState.RETRY.name,
            onCheckedChange = {
                buttonState = ButtonState.RETRY
            }
        )
        CheckBox(
            checked = buttonState == ButtonState.SCANNING,
            description = ButtonState.SCANNING.name,
            onCheckedChange = {
                buttonState = ButtonState.SCANNING
            }
        )
        CheckBox(
            checked = buttonState == ButtonState.LOCKED,
            description = ButtonState.LOCKED.name,
            onCheckedChange = {
                buttonState = ButtonState.LOCKED
            }
        )
        CheckBox(
            checked = buttonState == ButtonState.UNLOCKED,
            description = ButtonState.UNLOCKED.name,
            onCheckedChange = {
                buttonState = ButtonState.UNLOCKED
            }
        )
    }
}

@Composable
fun CheckBox(
    checked: Boolean,
    description: String,
    onCheckedChange: (Boolean) -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Checkbox(
            checked = checked,
            onCheckedChange = onCheckedChange
        )
        Text(
            modifier = Modifier.padding(start = 2.dp),
            text = description
        )
    }
}

@Composable
fun SwitchButtonView(
    state: ButtonState,
    onChange: ((ButtonState) -> Unit)?,
) {
    val width = 256.dp
    val height = 64.dp
    val padding = 8.dp
    val iconSize = 24.dp

    val animatedPaddingStart by animateDpAsState(
        targetValue = if (state == ButtonState.UNLOCKED) iconSize * 3 else padding,
        animationSpec = tween(
            durationMillis = 600,
            easing = FastOutSlowInEasing
        )
    )

    val animatedPaddingEnd by animateDpAsState(
        targetValue = if (state == ButtonState.LOCKED) iconSize * 3 else padding,
        animationSpec = tween(
            durationMillis = 600,
            easing = FastOutSlowInEasing
        )
    )

    val animatedThumbnailColor by animateColorAsState(
        targetValue = state.thumbnailColor,
        animationSpec = tween(
            durationMillis = 600,
            easing = FastOutSlowInEasing
        )
    )

    val animatedToggleColor by animateColorAsState(
        targetValue = state.toggleColor,
        animationSpec = tween(
            durationMillis = 600,
            easing = FastOutSlowInEasing
        )
    )

    val animatedTitleColor by animateColorAsState(
        targetValue = state.titleColor,
        animationSpec = tween(
            durationMillis = 600,
            easing = FastOutSlowInEasing
        )
    )

    Box(
        modifier = Modifier
            .size(width, height)
            .shapeBackground(color = animatedThumbnailColor)
    ) {
        // thumbnail
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                modifier = Modifier.size(iconSize),
                painter = painterResource(id = R.drawable.ic_locker),
                tint = Color(0xFFD1D1D1),
                contentDescription = null
            )
            Icon(
                modifier = Modifier.size(iconSize),
                painter = painterResource(id = R.drawable.ic_unlocker),
                tint = Color(0xFFD1D1D1),
                contentDescription = null
            )
        }
        // toggle
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(
                    start = animatedPaddingStart,
                    end = animatedPaddingEnd,
                    top = padding,
                    bottom = padding
                )
                .shapeBackground(color = animatedToggleColor)
                .clickable {
                    val newState = (ButtonState.values().toList() - state).random()
                    onChange?.invoke(newState)
                }
        ) {
            Row(
                modifier = Modifier.fillMaxSize(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                if (state.icon != null) {
                    Icon(
                        painter = painterResource(id = state.icon!!),
                        contentDescription = null,
                        tint = animatedTitleColor
                    )
                }
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = state.title,
                    color = animatedTitleColor,
                    fontSize = 16.sp
                )
            }
        }
    }
}

@Composable
fun Modifier.shapeBackground(
    color: Color,
    percent: Int = 50,
    elevation: Dp = 0.dp,
): Modifier {
    return this
        .background(
            color = color,
            shape = RoundedCornerShape(percent)
        )
        .shadow(
            elevation = elevation,
            shape = RoundedCornerShape(percent)
        )
        .clip(
            shape = RoundedCornerShape(percent)
        )
}
