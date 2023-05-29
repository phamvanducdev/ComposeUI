package com.ducpv.composeui.feature.tictactoegame

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ducpv.composeui.R
import com.ducpv.composeui.navigation.AppState
import com.ducpv.composeui.navigation.NavigationIcon
import com.ducpv.composeui.shared.theme.ThemeColor
import com.ducpv.composeui.shared.theme.color

/**
 * Created by pvduc9773 on 30/03/2023.
 */
@Composable
fun TicTacToeGameScreen(
    appState: AppState,
    viewModel: GameViewModel = hiltViewModel()
) {
    LaunchedEffect(Unit) {
        appState.topBarTitle = R.string.tic_tac_toe_game
        appState.navigationIcon = NavigationIcon.Menu
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(24.dp),
            verticalArrangement = Arrangement.spacedBy(space = 24.dp),
        ) {
            GameHeaderView(
                isGameOver = viewModel.gameUiState.isGameOver,
                winnerType = viewModel.gameUiState.winnerType,
                currentPlayer = viewModel.gameUiState.currentPlayer,
            )
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
                GameBoardView()
                GameCellsView(
                    cells = viewModel.cells,
                    onCellSelected = viewModel::onCellSelected,
                )
                GameWinnerView(
                    isGameOver = viewModel.gameUiState.isGameOver,
                    winnerType = viewModel.gameUiState.winnerType,
                    winnerPlayer = viewModel.gameUiState.currentPlayer,
                )
            }
            GameActionView(
                state = viewModel.gameUiState.gameState,
                action = {
                    viewModel.onActionClickListener()
                },
            )
        }
    }
}
