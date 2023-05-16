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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ducpv.composeui.shared.theme.ThemeColor
import com.ducpv.composeui.shared.theme.color

/**
 * Created by pvduc9773 on 30/03/2023.
 */
@OptIn(ExperimentalAnimationApi::class)
@Composable
fun TicTacToeGameScreen(viewModel: GameViewModel = hiltViewModel()) {
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(42.dp),
                verticalAlignment = Alignment.CenterVertically,
            ) {
                AnimatedVisibility(
                    visible = viewModel.gameUiState.isGameOver,
                    enter = fadeIn(tween(600)),
                    exit = fadeOut(tween(0)),
                ) {
                    val winnerDisplayName = when (viewModel.gameUiState.victoryType) {
                        VictoryType.DRAW -> "Game draw!"
                        else -> "${viewModel.gameUiState.currentPlayer?.displayName} winner!"
                    }
                    val winnerDisplayColor = when (viewModel.gameUiState.victoryType) {
                        VictoryType.DRAW -> ThemeColor.Pink.color
                        else -> viewModel.gameUiState.currentPlayer?.displayColor ?: ThemeColor.Pink.color
                    }
                    Text(
                        modifier = Modifier.fillMaxSize(),
                        textAlign = TextAlign.Center,
                        text = winnerDisplayName,
                        color = winnerDisplayColor,
                        fontSize = 24.sp,
                        fontFamily = FontFamily.Monospace,
                    )
                }
            }
            Spacer(modifier = Modifier.height(12.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .shadow(
                        elevation = 8.dp,
                        shape = RoundedCornerShape(8.dp),
                    )
                    .clip(RoundedCornerShape(8.dp))
                    .background(ThemeColor.White.color),
                contentAlignment = Alignment.Center,
            ) {
                GameBoard()
                LazyVerticalGrid(
                    modifier = Modifier
                        .fillMaxWidth()
                        .aspectRatio(1f)
                        .padding(20.dp),
                    columns = GridCells.Fixed(3),
                ) {
                    viewModel.cells.forEach { (cellNo, cell) ->
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .aspectRatio(1f)
                                    .clickable(
                                        indication = null,
                                        interactionSource = MutableInteractionSource(),
                                        onClickLabel = cellNo.toString(),
                                        onClick = { viewModel.onCellSelected(cellNo) },
                                    ),
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxSize(),
                                    horizontalAlignment = Alignment.CenterHorizontally,
                                    verticalArrangement = Arrangement.Center,
                                ) {
                                    AnimatedVisibility(
                                        visible = cell != Cell.NONE,
                                        enter = scaleIn(tween(600)),
                                        exit = scaleOut(tween(600)),
                                    ) {
                                        when (cell) {
                                            Cell.CIRCLE -> CircleCell()
                                            Cell.CROSS -> CrossCell()
                                            else -> Unit
                                        }
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
                        visible = viewModel.gameUiState.isGameOver,
                        enter = fadeIn(tween(600)),
                        exit = fadeOut(tween(600)),
                    ) {
                        DrawVictoryLine(
                            victoryType = viewModel.gameUiState.victoryType,
                            victoryPlayer = viewModel.gameUiState.currentPlayer,
                        )
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
            GameAction(
                state = viewModel.gameUiState.gameState,
                action = {
                    viewModel.onActionClickListener()
                },
            )
        }
    }
}
