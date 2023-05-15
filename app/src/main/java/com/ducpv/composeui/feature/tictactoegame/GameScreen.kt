package com.ducpv.composeui.feature.tictactoegame

import androidx.compose.animation.*
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ducpv.composeui.navigation.AppState
import com.ducpv.composeui.shared.theme.ThemeColor
import com.ducpv.composeui.shared.theme.alpha10
import com.ducpv.composeui.shared.theme.color

/**
 * Created by pvduc9773 on 30/03/2023.
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TicTacToeGameScreen(
    appState: AppState,
    viewModel: GameViewModel = hiltViewModel()
) {
    val boardItems = viewModel.boardItems
    val currentTurn = viewModel.gameState.currentTurn
    val victoryType = viewModel.gameState.victoryType

    if (victoryType != VictoryType.NONE) {
        val message = when (victoryType) {
            VictoryType.DRAW -> "Game draw!"
            else -> "Player ${currentTurn.title} winner!"
        }
        LaunchedEffect(Unit) {
            appState.snackHostState.showSnackbar(message)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1f)
                .shadow(
                    elevation = 10.dp,
                    shape = RoundedCornerShape(10.dp),
                )
                .clip(RoundedCornerShape(10.dp))
                .background(ThemeColor.White.color),
            contentAlignment = Alignment.Center,
        ) {
            BoardGame()
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .padding(20.dp),
                columns = GridCells.Fixed(3),
            ) {
                boardItems.forEach { (cellNo, _) ->
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f)
                                .clickable(
                                    interactionSource = MutableInteractionSource(),
                                    indication = null,
                                ) {
                                    viewModel.onSelectCell(cellNo)
                                },
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            AnimatedVisibility(
                                visible = boardItems[cellNo] != BoardCellValue.NONE,
                                enter = scaleIn(tween(600)),
                                exit = scaleOut(tween(600)),
                            ) {
                                when (boardItems[cellNo]) {
                                    BoardCellValue.CIRCLE -> CircleCell()
                                    BoardCellValue.CROSS -> CrossCell()
                                    else -> Unit
                                }
                            }
                        }
                    }
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
            ) {
                AnimatedVisibility(
                    visible = victoryType != VictoryType.NONE,
                    enter = fadeIn(tween(600)),
                    exit = fadeOut(tween(600)),
                ) {
                    DrawVictoryLine(victoryType = victoryType)
                }
            }
        }
        Spacer(modifier = Modifier.height(24.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween,
        ) {
            Button(
                colors = ButtonDefaults.buttonColors(
                    contentColor = ThemeColor.White.color,
                    containerColor = ThemeColor.Blue.color.alpha10,
                ),
                contentPadding = PaddingValues(horizontal = 16.dp, vertical = 0.dp),
                onClick = {},
            ) {
                Text(
                    text = "Current turn ->",
                    color = ThemeColor.Blue.color,
                )
                AnimatedContent(targetState = currentTurn) { currentTurn ->
                    when (currentTurn) {
                        BoardCellValue.CIRCLE -> CircleCell(
                            size = 32.dp,
                            padding = 8.dp,
                            stroke = 5f,
                        )
                        BoardCellValue.CROSS -> CrossCell(
                            size = 32.dp,
                            padding = 8.dp,
                            stroke = 5f,
                        )
                        else -> Unit
                    }
                }
            }
            Button(
                colors = ButtonDefaults.buttonColors(
                    contentColor = ThemeColor.White.color,
                    containerColor = ThemeColor.Blue.color,
                ),
                onClick = { viewModel.onResetGame() },
            ) {
                Text(text = "Reset game!")
            }
        }
    }
}
