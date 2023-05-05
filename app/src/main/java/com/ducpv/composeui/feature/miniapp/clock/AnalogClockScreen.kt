package com.ducpv.composeui.feature.miniapp.clock

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.rotate
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.ducpv.composeui.navigation.AppState
import com.ducpv.composeui.shared.theme.ThemeColor
import com.ducpv.composeui.shared.theme.color
import java.util.*
import kotlin.math.min
import kotlin.time.Duration.Companion.seconds
import kotlinx.coroutines.delay

/**
 * Created by pvduc9773 on 25/04/2023.
 */
@Composable
fun AnalogClockScreen(appState: AppState) {
    var hour by remember { mutableStateOf(0) }
    var minute by remember { mutableStateOf(0) }
    var second by remember { mutableStateOf(0) }

    LaunchedEffect(Unit) {
        while (true) {
            val currentCalendar = Calendar.getInstance()
            hour = currentCalendar.get(Calendar.HOUR)
            minute = currentCalendar.get(Calendar.MINUTE)
            second = currentCalendar.get(Calendar.SECOND)
            delay(1.seconds)
        }
    }
    Box(modifier = Modifier.fillMaxSize()) {
        AnalogClockView(
            hour = hour,
            minute = minute,
            second = second,
        )
    }
}

@Composable
fun AnalogClockView(
    hour: Int,
    minute: Int,
    second: Int,
) {
    val primaryColor = ThemeColor.Black.color
    val secondaryColor = ThemeColor.Gray.color
    val drawSecondColor = ThemeColor.Red.color
    val drawMinuteColor = ThemeColor.Blue.color
    val drawHourColor = ThemeColor.Black.color

    Canvas(modifier = Modifier.fillMaxSize()) {
        val diameter = min(size.width, size.height) * 0.8f
        val radius = diameter / 2

        drawCircle(
            color = secondaryColor.copy(alpha = 0.05f),
            radius = radius * 1.1f,
        )

        repeat(60) {
            val size = if (it % 5 == 0) 20f else 10f
            val color = if (it % 5 == 0) primaryColor else secondaryColor
            val start = center - Offset(0f, radius)
            val end = start + Offset(0f, size)
            rotate(it / 60f * 360) {
                drawLine(
                    color = color,
                    start = start,
                    end = end,
                    strokeWidth = 2.dp.toPx(),
                    cap = StrokeCap.Round,
                )
            }
        }

        val secondRatio = second / 60f
        val minuteRatio = minute / 60f
        val hourRatio = hour / 12f

        rotate(hourRatio * 360, center) {
            drawLine(
                color = drawHourColor,
                start = center - Offset(0f, radius * 0.4f),
                end = center + Offset(0f, radius * 0.1f),
                strokeWidth = 4.dp.toPx(),
                cap = StrokeCap.Round,
            )
        }

        rotate(minuteRatio * 360, center) {
            drawLine(
                color = drawMinuteColor,
                start = center - Offset(0f, radius * 0.6f),
                end = center + Offset(0f, radius * 0.1f),
                strokeWidth = 3.dp.toPx(),
                cap = StrokeCap.Round,
            )
        }

        rotate(secondRatio * 360, center) {
            drawLine(
                color = drawSecondColor,
                start = center - Offset(0f, radius * 0.8f),
                end = center + Offset(0f, radius * 0.1f),
                strokeWidth = 2.dp.toPx(),
                cap = StrokeCap.Round,
            )
        }

        drawCircle(
            color = drawSecondColor,
            radius = 4.dp.toPx(),
            center = center,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun LockViewPreview() {
    AnalogClockView(
        hour = 2,
        minute = 15,
        second = 45,
    )
}
