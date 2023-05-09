package com.ducpv.composeui.feature.tictactoegame

/**
 * Created by pvduc9773 on 30/03/2023.
 */
data class GameState(
    val currentTurn: BoardCellValue = BoardCellValue.CIRCLE,
    val victoryType: VictoryType = VictoryType.NONE,
)

enum class BoardCellValue {
    CIRCLE,
    CROSS,
    NONE;

    val nextTurn: BoardCellValue
        get() = when (this) {
            CIRCLE -> CROSS
            CROSS -> CIRCLE
            NONE -> listOf(CIRCLE, CROSS).random()
        }
}

enum class VictoryType {
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
