package com.ducpv.demo.shared.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

enum class ThemeColor {
    Transparent,
    White,
    Black,
    Gray,
    Blue,
    Brown,
    Green,
    Indigo,
    Orange,
    Pink,
    Purple,
    Red,
    Teal,
    Yellow
}

val isLightTheme: Boolean
    @Composable get() = !isSystemInDarkTheme()

val ThemeColor.color: Color
    @Composable get() = when (this) {
        ThemeColor.Transparent -> Color(0x00FFFFFF)
        ThemeColor.White -> if (isLightTheme) Color(0xFFFFFFFF) else Color(0xFF000000)
        ThemeColor.Black -> if (isLightTheme) Color(0xFF000000) else Color(0xFFFFFFFF)
        ThemeColor.Gray -> if (isLightTheme) Color(0xFFC9C9C9) else Color(0xFFC5C5C5)
        ThemeColor.Blue -> if (isLightTheme) Color(0xFF2196F3) else Color(0xFF90CAF9)
        ThemeColor.Brown -> if (isLightTheme) Color(0xFF795548) else Color(0xFFBCAAA4)
        ThemeColor.Green -> if (isLightTheme) Color(0xFF43A047) else Color(0xFFA5D6A7)
        ThemeColor.Indigo -> if (isLightTheme) Color(0xFF3F51B5) else Color(0xFFC5CAE9)
        ThemeColor.Orange -> if (isLightTheme) Color(0xFFE65100) else Color(0xFFFFB74D)
        ThemeColor.Pink -> if (isLightTheme) Color(0xFFE91E63) else Color(0xFFF48FB1)
        ThemeColor.Purple -> if (isLightTheme) Color(0xFF6200EE) else Color(0xFFBB86FC)
        ThemeColor.Red -> if (isLightTheme) Color(0xFFB00020) else Color(0xFFCF6679)
        ThemeColor.Teal -> if (isLightTheme) Color(0xFF03DAC6) else Color(0xFF03DAC6)
        ThemeColor.Yellow -> if (isLightTheme) Color(0xFFFFEB3B) else Color(0xFFFFF59D)
    }

val Color.alpha10: Color get() = this.copy(alpha = 0.1f)
val Color.alpha20: Color get() = this.copy(alpha = 0.2f)
val Color.alpha30: Color get() = this.copy(alpha = 0.3f)
val Color.alpha40: Color get() = this.copy(alpha = 0.4f)
val Color.alpha50: Color get() = this.copy(alpha = 0.5f)
val Color.alpha60: Color get() = this.copy(alpha = 0.6f)
val Color.alpha70: Color get() = this.copy(alpha = 0.7f)
val Color.alpha80: Color get() = this.copy(alpha = 0.8f)
val Color.alpha90: Color get() = this.copy(alpha = 0.9f)