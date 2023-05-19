package com.ducpv.composeui.feature.tictactoegame

import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ducpv.composeui.shared.theme.ComposeUITheme
import com.ducpv.composeui.shared.theme.ThemeColor
import com.ducpv.composeui.shared.theme.color

/**
 * Created by pvduc9773 on 30/03/2023.
 */
@Preview(showBackground = true)
@Composable
fun GameBoardPreview() {
    val cellItems = mutableMapOf(
        1 to Cell.CIRCLE,
        2 to Cell.CROSS,
        3 to Cell.CROSS,
        4 to Cell.CIRCLE,
        5 to Cell.CIRCLE,
        6 to Cell.CROSS,
        7 to Cell.CIRCLE,
        8 to Cell.NONE,
        9 to Cell.NONE,
    )

    ComposeUITheme {
        Box {
            GameBoard()
            LazyVerticalGrid(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .padding(20.dp),
                columns = GridCells.Fixed(3),
            ) {
                cellItems.forEach { (cellNo, _) ->
                    item {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(1f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center,
                        ) {
                            when (cellItems[cellNo]) {
                                Cell.CIRCLE -> CircleCell()
                                Cell.CROSS -> CrossCell()
                                else -> Unit
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
                DrawVictoryLine(
                    victoryType = VictoryType.VERTICAL1,
                    victoryPlayer = Player.CIRCLE,
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GameActionCircleTurnPreview() {
    ComposeUITheme {
        GameAction(
            state = GameState.CIRCLE_TURN,
            action = {
                // @null
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun GameActionCrossTurnPreview() {
    ComposeUITheme {
        GameAction(
            state = GameState.CROSS_TURN,
            action = {
                // @null
            },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CircleCellPreview() {
    ComposeUITheme {
        CircleCell()
    }
}

@Preview(showBackground = true)
@Composable
fun ItemCrossPreview() {
    ComposeUITheme {
        CrossCell()
    }
}

@Composable
fun GameBoard() {
    val drawLineColor = ThemeColor.Gray.color
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .padding(20.dp),
    ) {
        drawLine(
            color = drawLineColor,
            strokeWidth = 4f,
            cap = StrokeCap.Round,
            start = Offset(x = size.width / 3, y = 0f),
            end = Offset(x = size.width / 3, y = size.height),
        )
        drawLine(
            color = drawLineColor,
            strokeWidth = 4f,
            cap = StrokeCap.Round,
            start = Offset(x = size.width * 2 / 3, y = 0f),
            end = Offset(x = size.width * 2 / 3, y = size.height),
        )
        drawLine(
            color = drawLineColor,
            strokeWidth = 4f,
            cap = StrokeCap.Round,
            start = Offset(x = 0f, y = size.height / 3),
            end = Offset(x = size.width, y = size.height / 3),
        )
        drawLine(
            color = drawLineColor,
            strokeWidth = 4f,
            cap = StrokeCap.Round,
            start = Offset(x = 0f, y = size.height * 2 / 3),
            end = Offset(x = size.width, y = size.height * 2 / 3),
        )
    }
}

@Composable
fun CircleCell(
    size: Dp = 54.dp,
    padding: Dp = 4.dp,
    stroke: Float = 8f
) {
    val drawCircleColor = Player.CIRCLE.displayColor
    Canvas(
        modifier = Modifier
            .size(size)
            .padding(padding),
    ) {
        drawCircle(
            color = drawCircleColor,
            style = Stroke(stroke),
        )
    }
}

@Composable
fun CrossCell(
    size: Dp = 54.dp,
    padding: Dp = 4.dp,
    stroke: Float = 8f
) {
    val drawLineColor = Player.CROSS.displayColor
    Canvas(
        modifier = Modifier
            .size(size)
            .padding(padding),
    ) {
        drawLine(
            color = drawLineColor,
            strokeWidth = stroke,
            cap = StrokeCap.Round,
            start = Offset(x = 0f, y = 0f),
            end = Offset(x = this.size.width, y = this.size.height),
        )
        drawLine(
            color = drawLineColor,
            strokeWidth = stroke,
            cap = StrokeCap.Round,
            start = Offset(x = 0f, y = this.size.height),
            end = Offset(x = this.size.width, y = 0f),
        )
    }
}

@Composable
fun DrawVictoryLine(
    victoryPlayer: Player? = null,
    victoryType: VictoryType = VictoryType.NONE
) {
    if (victoryPlayer == null) return
    when (victoryType) {
        VictoryType.HORIZONTAL1 -> WinHorizontalLine1(victoryPlayer.displayColor)
        VictoryType.HORIZONTAL2 -> WinHorizontalLine2(victoryPlayer.displayColor)
        VictoryType.HORIZONTAL3 -> WinHorizontalLine3(victoryPlayer.displayColor)
        VictoryType.VERTICAL1 -> WinVerticalLine1(victoryPlayer.displayColor)
        VictoryType.VERTICAL2 -> WinVerticalLine2(victoryPlayer.displayColor)
        VictoryType.VERTICAL3 -> WinVerticalLine3(victoryPlayer.displayColor)
        VictoryType.DIAGONAL1 -> WinDiagonalLine1(victoryPlayer.displayColor)
        VictoryType.DIAGONAL2 -> WinDiagonalLine2(victoryPlayer.displayColor)
        else -> Unit
    }
}

@Composable
fun WinHorizontalLine1(color: Color = ThemeColor.Red.color) {
    val transitionStrokeWidth by rememberInfiniteTransition().animateFloat(
        initialValue = 0f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(600),
            repeatMode = RepeatMode.Reverse,
        ),
    )
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawLine(
            color = color,
            strokeWidth = transitionStrokeWidth,
            cap = StrokeCap.Round,
            start = Offset(x = 0f, y = size.height * 1 / 6),
            end = Offset(x = size.width, y = size.height * 1 / 6),
        )
    }
}

@Composable
fun WinHorizontalLine2(color: Color = ThemeColor.Red.color) {
    val transitionStrokeWidth by rememberInfiniteTransition().animateFloat(
        initialValue = 0f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(600),
            repeatMode = RepeatMode.Reverse,
        ),
    )
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawLine(
            color = color,
            strokeWidth = transitionStrokeWidth,
            cap = StrokeCap.Round,
            start = Offset(x = 0f, y = size.height * 3 / 6),
            end = Offset(x = size.width, y = size.height * 3 / 6),
        )
    }
}

@Composable
fun WinHorizontalLine3(color: Color = ThemeColor.Red.color) {
    val transitionStrokeWidth by rememberInfiniteTransition().animateFloat(
        initialValue = 0f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(600),
            repeatMode = RepeatMode.Reverse,
        ),
    )
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawLine(
            color = color,
            strokeWidth = transitionStrokeWidth,
            cap = StrokeCap.Round,
            start = Offset(x = 0f, y = size.height * 5 / 6),
            end = Offset(x = size.width, y = size.height * 5 / 6),
        )
    }
}

@Composable
fun WinVerticalLine1(color: Color = ThemeColor.Red.color) {
    val transitionStrokeWidth by rememberInfiniteTransition().animateFloat(
        initialValue = 0f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(600),
            repeatMode = RepeatMode.Reverse,
        ),
    )
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawLine(
            color = color,
            strokeWidth = transitionStrokeWidth,
            cap = StrokeCap.Round,
            start = Offset(x = size.width * 1 / 6, y = 0f),
            end = Offset(x = size.width * 1 / 6, y = size.height),
        )
    }
}

@Composable
fun WinVerticalLine2(color: Color = ThemeColor.Red.color) {
    val transitionStrokeWidth by rememberInfiniteTransition().animateFloat(
        initialValue = 0f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(600),
            repeatMode = RepeatMode.Reverse,
        ),
    )
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawLine(
            color = color,
            strokeWidth = transitionStrokeWidth,
            cap = StrokeCap.Round,
            start = Offset(x = size.width * 3 / 6, y = 0f),
            end = Offset(x = size.width * 3 / 6, y = size.height),
        )
    }
}

@Composable
fun WinVerticalLine3(color: Color = ThemeColor.Red.color) {
    val transitionStrokeWidth by rememberInfiniteTransition().animateFloat(
        initialValue = 0f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(600),
            repeatMode = RepeatMode.Reverse,
        ),
    )
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawLine(
            color = color,
            strokeWidth = transitionStrokeWidth,
            cap = StrokeCap.Round,
            start = Offset(x = size.width * 5 / 6, y = 0f),
            end = Offset(x = size.width * 5 / 6, y = size.height),
        )
    }
}

@Composable
fun WinDiagonalLine1(color: Color = ThemeColor.Red.color) {
    val transitionStrokeWidth by rememberInfiniteTransition().animateFloat(
        initialValue = 0f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(600),
            repeatMode = RepeatMode.Reverse,
        ),
    )
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawLine(
            color = color,
            strokeWidth = transitionStrokeWidth,
            cap = StrokeCap.Round,
            start = Offset(x = 0f, y = 0f),
            end = Offset(x = size.width, y = size.height),
        )
    }
}

@Composable
fun WinDiagonalLine2(color: Color = ThemeColor.Red.color) {
    val transitionStrokeWidth by rememberInfiniteTransition().animateFloat(
        initialValue = 0f,
        targetValue = 8f,
        animationSpec = infiniteRepeatable(
            animation = tween(600),
            repeatMode = RepeatMode.Reverse,
        ),
    )
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawLine(
            color = color,
            strokeWidth = transitionStrokeWidth,
            cap = StrokeCap.Round,
            start = Offset(x = 0f, y = size.height),
            end = Offset(x = size.width, y = 0f),
        )
    }
}

@Composable
fun GameAction(
    state: GameState,
    action: () -> Unit
) {
    val animatedPaddingStart by animateDpAsState(
        targetValue = when (state) {
            GameState.CIRCLE_TURN -> 8.dp + 64.dp
            else -> 8.dp
        },
        animationSpec = tween(
            durationMillis = 600,
            easing = FastOutSlowInEasing,
        ),
    )

    val animatedPaddingEnd by animateDpAsState(
        targetValue = when (state) {
            GameState.CROSS_TURN -> 8.dp + 64.dp
            else -> 8.dp
        },
        animationSpec = tween(
            durationMillis = 600,
            easing = FastOutSlowInEasing,
        ),
    )

    val animatedThumbnailColor by animateColorAsState(
        targetValue = state.thumbnailColor,
        animationSpec = tween(
            durationMillis = 600,
            easing = FastOutSlowInEasing,
        ),
    )

    val animatedToggleColor by animateColorAsState(
        targetValue = state.toggleColor,
        animationSpec = tween(
            durationMillis = 600,
            easing = FastOutSlowInEasing,
        ),
    )

    val animatedTitleColor by animateColorAsState(
        targetValue = state.titleColor,
        animationSpec = tween(
            durationMillis = 600,
            easing = FastOutSlowInEasing,
        ),
    )

    val transitionScale by rememberInfiniteTransition().animateFloat(
        initialValue = 1f,
        targetValue = when (state) {
            GameState.CIRCLE_TURN,
            GameState.CROSS_TURN -> 1f
            else -> 0.9f
        },
        animationSpec = infiniteRepeatable(
            animation = tween(600),
            repeatMode = RepeatMode.Reverse,
        ),
    )

    Column(
        modifier = Modifier
            .height(64.dp)
            .fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .shapeBackground(color = animatedThumbnailColor),
        ) {
            // thumbnail
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically,
            ) {
                CircleCell(size = 24.dp, padding = 2.dp)
                CrossCell(size = 24.dp, padding = 4.dp)
            }
            // toggle
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(
                        start = animatedPaddingStart,
                        end = animatedPaddingEnd,
                        top = 8.dp,
                        bottom = 8.dp,
                    )
                    .shapeBackground(color = animatedToggleColor)
                    .clickable {
                        action.invoke()
                    },
            ) {
                Row(
                    modifier = Modifier.fillMaxSize(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                ) {
                    Text(
                        modifier = Modifier
                            .padding(8.dp)
                            .scale(transitionScale),
                        text = state.title,
                        color = animatedTitleColor,
                        fontSize = 16.sp,
                    )
                }
            }
        }
    }
}

@Composable
fun Modifier.shapeBackground(
    color: Color,
    percent: Int = 50,
    elevation: Dp = 0.dp,
): Modifier {
    return this
        .background(
            color = color,
            shape = RoundedCornerShape(percent),
        )
        .shadow(
            elevation = elevation,
            shape = RoundedCornerShape(percent),
        )
        .clip(
            shape = RoundedCornerShape(percent),
        )
}
