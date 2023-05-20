package com.ducpv.composeui.feature.tictactoegame

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

/**
 * Created by pvduc9773 on 30/03/2023.
 */
data class GameUiState(
    val currentPlayer: Player? = null,
    val winnerType: WinnerType = WinnerType.NONE,
) {
    val gameState: GameState
        get() {
            if (currentPlayer == null) {
                return GameState.START
            }
            return when (winnerType) {
                WinnerType.DRAW -> { // game draw
                    GameState.DRAW
                }
                WinnerType.NONE -> { // game running
                    if (currentPlayer == Player.CIRCLE) {
                        GameState.CIRCLE_TURN
                    } else {
                        GameState.CROSS_TURN
                    }
                }
                else -> { // game over
                    if (currentPlayer == Player.CIRCLE) {
                        GameState.CIRCLE_WON
                    } else {
                        GameState.CROSS_WON
                    }
                }
            }
        }

    val isGameStart: Boolean
        get() = gameState == GameState.START

    val isGameOver: Boolean
        get() = winnerType != WinnerType.NONE
}

@HiltViewModel
class GameViewModel @Inject constructor() : ViewModel() {
    val cells = mutableMapOf(
        1 to Cell.NONE,
        2 to Cell.NONE,
        3 to Cell.NONE,
        4 to Cell.NONE,
        5 to Cell.NONE,
        6 to Cell.NONE,
        7 to Cell.NONE,
        8 to Cell.NONE,
        9 to Cell.NONE,
    )
    var gameUiState by mutableStateOf(GameUiState())

    fun onActionClickListener() {
        if (gameUiState.isGameStart || gameUiState.isGameOver) {
            onResetGame()
            return
        }
        if (onCheckBoardEmpty()) { // action change current player is start
            gameUiState = gameUiState.copy(
                currentPlayer = gameUiState.currentPlayer?.toNextPlayer(),
            )
            return
        }
    }

    private fun onResetGame() {
        cells.forEach { (i, _) ->
            cells[i] = Cell.NONE
        }
        gameUiState = gameUiState.copy(
            currentPlayer = Player.values().random(),
            winnerType = WinnerType.NONE,
        )
    }

    fun onCellSelected(cellNo: Int) {
        if (gameUiState.isGameOver) return
        if (cells[cellNo] != Cell.NONE) return // cell is selected
        val currentPlayer = gameUiState.currentPlayer ?: return
        cells[cellNo] = currentPlayer.toCell() // set data to cell selected
        gameUiState = gameUiState.copy(
            winnerType = getVictoryType(), // check game victory
        )
        if (!gameUiState.isGameOver) {
            gameUiState = gameUiState.copy(
                currentPlayer = gameUiState.currentPlayer?.toNextPlayer(),
            )
        }
    }

    private fun onCheckBoardFull(): Boolean {
        return cells.all { it.value != Cell.NONE }
    }

    private fun onCheckBoardEmpty(): Boolean {
        return cells.all { it.value == Cell.NONE }
    }

    private fun getVictoryType(): WinnerType {
        val currentPlayer = gameUiState.currentPlayer ?: return WinnerType.NONE
        val currentCells = cells.filter { it.value == currentPlayer.toCell() }
        return when {
            currentCells.containsKey(1) && currentCells.containsKey(2) && currentCells.containsKey(3) -> {
                WinnerType.HORIZONTAL1
            }
            currentCells.containsKey(4) && currentCells.containsKey(5) && currentCells.containsKey(6) -> {
                WinnerType.HORIZONTAL2
            }
            currentCells.containsKey(7) && currentCells.containsKey(8) && currentCells.containsKey(9) -> {
                WinnerType.HORIZONTAL3
            }
            currentCells.containsKey(1) && currentCells.containsKey(4) && currentCells.containsKey(7) -> {
                WinnerType.VERTICAL1
            }
            currentCells.containsKey(2) && currentCells.containsKey(5) && currentCells.containsKey(8) -> {
                WinnerType.VERTICAL2
            }
            currentCells.containsKey(3) && currentCells.containsKey(6) && currentCells.containsKey(9) -> {
                WinnerType.VERTICAL3
            }
            currentCells.containsKey(1) && currentCells.containsKey(5) && currentCells.containsKey(9) -> {
                WinnerType.DIAGONAL1
            }
            currentCells.containsKey(3) && currentCells.containsKey(5) && currentCells.containsKey(7) -> {
                WinnerType.DIAGONAL2
            }
            else -> {
                if (onCheckBoardFull()) {
                    WinnerType.DRAW
                } else {
                    WinnerType.NONE
                }
            }
        }
    }
}
