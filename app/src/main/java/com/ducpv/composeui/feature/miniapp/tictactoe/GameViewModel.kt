package com.ducpv.composeui.feature.miniapp.tictactoe

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by pvduc9773 on 30/03/2023.
 */
@HiltViewModel
class GameViewModel @Inject constructor() : ViewModel() {
    val boardItems = mutableMapOf(
        1 to BoardCellValue.NONE,
        2 to BoardCellValue.NONE,
        3 to BoardCellValue.NONE,
        4 to BoardCellValue.NONE,
        5 to BoardCellValue.NONE,
        6 to BoardCellValue.NONE,
        7 to BoardCellValue.NONE,
        8 to BoardCellValue.NONE,
        9 to BoardCellValue.NONE,
    )
    var gameState by mutableStateOf(GameState())

    fun onResetGame() {
        boardItems.forEach { (i, _) ->
            boardItems[i] = BoardCellValue.NONE
        }
        gameState = gameState.copy(
            currentTurn = BoardCellValue.values().filter { it != BoardCellValue.NONE }.random(),
            victoryType = VictoryType.NONE,
        )
    }

    fun onSelectCell(cellNo: Int) {
        if (boardItems[cellNo] != BoardCellValue.NONE) return
        if (gameState.victoryType != VictoryType.NONE) return
        boardItems[cellNo] = gameState.currentTurn
        if (onCheckVictory(gameState.currentTurn)) {
            return
        }
        if (onCheckFullBoard()) {
            gameState = gameState.copy(
                victoryType = VictoryType.DRAW,
            )
            return
        }
        gameState = gameState.copy(
            currentTurn = gameState.currentTurn.nextTurn,
        )
    }

    private fun onCheckFullBoard(): Boolean {
        return boardItems.all { it.value != BoardCellValue.NONE }
    }

    private fun onCheckVictory(boardValue: BoardCellValue): Boolean {
        when {
            boardItems[1] == boardValue && boardItems[2] == boardValue && boardItems[3] == boardValue -> {
                gameState = gameState.copy(victoryType = VictoryType.HORIZONTAL1)
                return true
            }
            boardItems[4] == boardValue && boardItems[5] == boardValue && boardItems[6] == boardValue -> {
                gameState = gameState.copy(victoryType = VictoryType.HORIZONTAL2)
                return true
            }
            boardItems[7] == boardValue && boardItems[8] == boardValue && boardItems[9] == boardValue -> {
                gameState = gameState.copy(victoryType = VictoryType.HORIZONTAL3)
                return true
            }
            boardItems[1] == boardValue && boardItems[4] == boardValue && boardItems[7] == boardValue -> {
                gameState = gameState.copy(victoryType = VictoryType.VERTICAL1)
                return true
            }
            boardItems[2] == boardValue && boardItems[5] == boardValue && boardItems[8] == boardValue -> {
                gameState = gameState.copy(victoryType = VictoryType.VERTICAL2)
                return true
            }
            boardItems[3] == boardValue && boardItems[6] == boardValue && boardItems[9] == boardValue -> {
                gameState = gameState.copy(victoryType = VictoryType.VERTICAL3)
                return true
            }
            boardItems[1] == boardValue && boardItems[5] == boardValue && boardItems[9] == boardValue -> {
                gameState = gameState.copy(victoryType = VictoryType.DIAGONAL1)
                return true
            }
            boardItems[3] == boardValue && boardItems[5] == boardValue && boardItems[7] == boardValue -> {
                gameState = gameState.copy(victoryType = VictoryType.DIAGONAL2)
                return true
            }
            else -> return false
        }
    }
}
