package com.ducpv.demo.shared.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowInsetsControllerCompat

val LightColorScheme = lightColorScheme(
    primary = Color(0xFF984816),
    onPrimary = Color(0xFFFFFFFF),
    primaryContainer = Color(0xFFFFDBC9),
    onPrimaryContainer = Color(0xFF341000),
    inversePrimary = Color(0xFFFFB68F),
    secondary = Color(0xFF765849),
    onSecondary = Color(0xFFFFFFFF),
    secondaryContainer = Color(0xFFFFDBC9),
    onSecondaryContainer = Color(0xFF2B160B),
    tertiary = Color(0xFF656032),
    onTertiary = Color(0xFFFFFFFF),
    tertiaryContainer = Color(0xFFEBE4AA),
    onTertiaryContainer = Color(0xFF1F1C00),
    background = Color(0xFFFCFCFC),
    onBackground = Color(0xFF201A17),
    surface = Color(0xFFFCFCFC),
    onSurface = Color(0xFF201A17),
    surfaceVariant = Color(0xFFF4DED5),
    onSurfaceVariant = Color(0xFF53443D),
    inverseSurface = Color(0xFF362F2C),
    inverseOnSurface = Color(0xFFFBEEE9),
    error = Color(0xFFBA1B1B),
    onError = Color(0xFFFFFFFF),
    errorContainer = Color(0xFFFFDAD4),
    onErrorContainer = Color(0xFF410001),
    outline = Color(0xFF85736B),
)

val DarkColorScheme = darkColorScheme(
    primary = Color(0xFFFFB68F),
    onPrimary = Color(0xFF562000),
    primaryContainer = Color(0xFF793100),
    onPrimaryContainer = Color(0xFFFFDBC9),
    inversePrimary = Color(0xFF984816),
    secondary = Color(0xFFE6BEAC),
    onSecondary = Color(0xFF432B1E),
    secondaryContainer = Color(0xFF5C4032),
    onSecondaryContainer = Color(0xFFFFDBC9),
    tertiary = Color(0xFFCFC890),
    onTertiary = Color(0xFF353107),
    tertiaryContainer = Color(0xFF4C481C),
    onTertiaryContainer = Color(0xFFEBE4AA),
    background = Color(0xFF201A17),
    onBackground = Color(0xFFEDE0DB),
    surface = Color(0xFF201A17),
    onSurface = Color(0xFFEDE0DB),
    surfaceVariant = Color(0xFF53443D),
    onSurfaceVariant = Color(0xFFD7C2B9),
    inverseSurface = Color(0xFFEDE0DB),
    inverseOnSurface = Color(0xFF362F2C),
    error = Color(0xFFFFB4A9),
    onError = Color(0xFF680003),
    errorContainer = Color(0xFF930006),
    onErrorContainer = Color(0xFFFFDAD4),
    outline = Color(0xFFA08D85),
)

@Composable
fun ComposeUITheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) {
                dynamicDarkColorScheme(context)
            } else {
                dynamicLightColorScheme(context)
            }
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = Color.Transparent.toArgb()
            window.navigationBarColor = Color.Transparent.toArgb()
            WindowInsetsControllerCompat(window, view).isAppearanceLightStatusBars = !darkTheme
            WindowInsetsControllerCompat(window, view).isAppearanceLightNavigationBars = !darkTheme
        }
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = AppTypography,
        content = content,
    )
}
