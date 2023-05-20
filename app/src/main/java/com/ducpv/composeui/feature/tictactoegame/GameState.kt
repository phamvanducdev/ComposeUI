package com.ducpv.composeui.feature.tictactoegame

import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import com.ducpv.composeui.shared.theme.ThemeColor
import com.ducpv.composeui.shared.theme.alpha20
import com.ducpv.composeui.shared.theme.color

/**
 * Created by pvduc9773 on 30/03/2023.
 */
enum class GameState {
    START, // Bắt đầu game
    CIRCLE_TURN, // Lượt chơi 〇
    CROSS_TURN, // Lượt chơi X
    CIRCLE_WON, // 〇 thắng
    CROSS_WON, // X thắng
    DRAW; // Hoà

    val title: String
        get() = when (this) {
            START -> "Start game"
            CIRCLE_TURN -> "Circle turn"
            CROSS_TURN -> "Cross turn"
            CIRCLE_WON -> "Reset game"
            CROSS_WON -> "Reset game"
            DRAW -> "Reset game"
        }

    val titleColor: Color
        @Composable get() = when (this) {
            START -> ThemeColor.White.color
            CIRCLE_TURN -> ThemeColor.White.color
            CROSS_TURN -> ThemeColor.White.color
            CIRCLE_WON -> ThemeColor.White.color
            CROSS_WON -> ThemeColor.White.color
            DRAW -> ThemeColor.White.color
        }

    val thumbnailColor: Color
        @Composable get() = when (this) {
            START -> ThemeColor.Gray.color.alpha20
            CIRCLE_TURN -> ThemeColor.Green.color.alpha20
            CROSS_TURN -> ThemeColor.Red.color.alpha20
            CIRCLE_WON -> ThemeColor.Green.color.alpha20
            CROSS_WON -> ThemeColor.Red.color.alpha20
            DRAW -> ThemeColor.Pink.color.alpha20
        }

    val toggleColor: Color
        @Composable get() = when (this) {
            START -> ThemeColor.Gray.color
            CIRCLE_TURN -> ThemeColor.Green.color
            CROSS_TURN -> ThemeColor.Red.color
            CIRCLE_WON -> ThemeColor.Green.color
            CROSS_WON -> ThemeColor.Red.color
            DRAW -> ThemeColor.Pink.color
        }
}

enum class Player {
    CIRCLE,
    CROSS;

    val displayName: String
        get() = if (this == CIRCLE) "Circle" else "Cross"

    val displayColor: Color
        @Composable get() = if (this == CIRCLE) ThemeColor.Green.color else ThemeColor.Red.color

    fun toCell(): Cell {
        return when (this) {
            CIRCLE -> Cell.CIRCLE
            CROSS -> Cell.CROSS
        }
    }

    fun toNextPlayer(): Player {
        return (Player.values().toList() - this).random()
    }
}

enum class Cell {
    CIRCLE,
    CROSS,
    NONE
}

enum class WinnerType {
    HORIZONTAL1,
    HORIZONTAL2,
    HORIZONTAL3,
    VERTICAL1,
    VERTICAL2,
    VERTICAL3,
    DIAGONAL1,
    DIAGONAL2,
    DRAW,
    NONE
}
