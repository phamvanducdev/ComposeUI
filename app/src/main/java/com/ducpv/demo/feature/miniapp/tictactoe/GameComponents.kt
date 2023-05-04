package com.ducpv.demo.feature.miniapp.tictactoe

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.ducpv.demo.shared.theme.ComposeUITheme
import com.ducpv.demo.shared.theme.ThemeColor
import com.ducpv.demo.shared.theme.color

/**
 * Created by pvduc9773 on 30/03/2023.
 */
@Preview(showBackground = true)
@Composable
fun BoardGamePreview() {
    val boardItems = mutableMapOf(
        1 to BoardCellValue.CIRCLE,
        2 to BoardCellValue.CROSS,
        3 to BoardCellValue.CROSS,
        4 to BoardCellValue.CIRCLE,
        5 to BoardCellValue.CIRCLE,
        6 to BoardCellValue.CROSS,
        7 to BoardCellValue.CIRCLE,
        8 to BoardCellValue.NONE,
        9 to BoardCellValue.NONE,
    )

    ComposeUITheme {
        Box {
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
                                .aspectRatio(1f),
                            horizontalAlignment = Alignment.CenterHorizontally,
                            verticalArrangement = Arrangement.Center
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
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                DrawVictoryLine(victoryType = VictoryType.VERTICAL1)
            }
        }
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
fun CrossCellPreview() {
    ComposeUITheme {
        CrossCell()
    }
}

@Composable
fun BoardGame() {
    val drawLineColor = ThemeColor.Gray.color
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f)
            .padding(20.dp)
    ) {
        drawLine(
            color = drawLineColor,
            strokeWidth = 4f,
            cap = StrokeCap.Round,
            start = Offset(x = size.width / 3, y = 0f),
            end = Offset(x = size.width / 3, y = size.height)
        )
        drawLine(
            color = drawLineColor,
            strokeWidth = 4f,
            cap = StrokeCap.Round,
            start = Offset(x = size.width * 2 / 3, y = 0f),
            end = Offset(x = size.width * 2 / 3, y = size.height)
        )
        drawLine(
            color = drawLineColor,
            strokeWidth = 4f,
            cap = StrokeCap.Round,
            start = Offset(x = 0f, y = size.height / 3),
            end = Offset(x = size.width, y = size.height / 3)
        )
        drawLine(
            color = drawLineColor,
            strokeWidth = 4f,
            cap = StrokeCap.Round,
            start = Offset(x = 0f, y = size.height * 2 / 3),
            end = Offset(x = size.width, y = size.height * 2 / 3)
        )
    }
}

@Composable
fun CircleCell(
    size: Dp = 60.dp,
    padding: Dp = 5.dp,
    stroke: Float = 10f
) {
    val drawCircleColor = ThemeColor.Teal.color
    Canvas(
        modifier = Modifier
            .size(size)
            .padding(padding)
    ) {
        drawCircle(
            color = drawCircleColor,
            style = Stroke(stroke)
        )
    }
}

@Composable
fun CrossCell(
    size: Dp = 60.dp,
    padding: Dp = 5.dp,
    stroke: Float = 10f
) {
    val drawLineColor = ThemeColor.Orange.color
    Canvas(
        modifier = Modifier
            .size(size)
            .padding(padding)
    ) {
        drawLine(
            color = drawLineColor,
            strokeWidth = stroke,
            cap = StrokeCap.Round,
            start = Offset(x = 0f, y = 0f),
            end = Offset(x = this.size.width, y = this.size.height)
        )
        drawLine(
            color = drawLineColor,
            strokeWidth = stroke,
            cap = StrokeCap.Round,
            start = Offset(x = 0f, y = this.size.height),
            end = Offset(x = this.size.width, y = 0f)
        )
    }
}

@Composable
fun DrawVictoryLine(victoryType: VictoryType = VictoryType.NONE) {
    when (victoryType) {
        VictoryType.HORIZONTAL1 -> WinHorizontalLine1()
        VictoryType.HORIZONTAL2 -> WinHorizontalLine2()
        VictoryType.HORIZONTAL3 -> WinHorizontalLine3()
        VictoryType.VERTICAL1 -> WinVerticalLine1()
        VictoryType.VERTICAL2 -> WinVerticalLine2()
        VictoryType.VERTICAL3 -> WinVerticalLine3()
        VictoryType.DIAGONAL1 -> WinDiagonalLine1()
        VictoryType.DIAGONAL2 -> WinDiagonalLine2()
        else -> Unit
    }
}


@Composable
fun WinHorizontalLine1() {
    val drawLineColor = ThemeColor.Red.color
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawLine(
            color = drawLineColor,
            strokeWidth = 5f,
            cap = StrokeCap.Round,
            start = Offset(x = 0f, y = size.height * 1 / 6),
            end = Offset(x = size.width, y = size.height * 1 / 6)
        )
    }
}

@Composable
fun WinHorizontalLine2() {
    val drawLineColor = ThemeColor.Red.color
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawLine(
            color = drawLineColor,
            strokeWidth = 5f,
            cap = StrokeCap.Round,
            start = Offset(x = 0f, y = size.height * 3 / 6),
            end = Offset(x = size.width, y = size.height * 3 / 6)
        )
    }
}

@Composable
fun WinHorizontalLine3() {
    val drawLineColor = ThemeColor.Red.color
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawLine(
            color = drawLineColor,
            strokeWidth = 5f,
            cap = StrokeCap.Round,
            start = Offset(x = 0f, y = size.height * 5 / 6),
            end = Offset(x = size.width, y = size.height * 5 / 6)
        )
    }
}

@Composable
fun WinVerticalLine1() {
    val drawLineColor = ThemeColor.Red.color
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawLine(
            color = drawLineColor,
            strokeWidth = 5f,
            cap = StrokeCap.Round,
            start = Offset(x = size.width * 1 / 6, y = 0f),
            end = Offset(x = size.width * 1 / 6, y = size.height)
        )
    }
}

@Composable
fun WinVerticalLine2() {
    val drawLineColor = ThemeColor.Red.color
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawLine(
            color = drawLineColor,
            strokeWidth = 5f,
            cap = StrokeCap.Round,
            start = Offset(x = size.width * 3 / 6, y = 0f),
            end = Offset(x = size.width * 3 / 6, y = size.height)
        )
    }
}

@Composable
fun WinVerticalLine3() {
    val drawLineColor = ThemeColor.Red.color
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawLine(
            color = drawLineColor,
            strokeWidth = 5f,
            cap = StrokeCap.Round,
            start = Offset(x = size.width * 5 / 6, y = 0f),
            end = Offset(x = size.width * 5 / 6, y = size.height)
        )
    }
}

@Composable
fun WinDiagonalLine1() {
    val drawLineColor = ThemeColor.Red.color
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawLine(
            color = drawLineColor,
            strokeWidth = 5f,
            cap = StrokeCap.Round,
            start = Offset(x = 0f, y = 0f),
            end = Offset(x = size.width, y = size.height)
        )
    }
}

@Composable
fun WinDiagonalLine2() {
    val drawLineColor = ThemeColor.Red.color
    Canvas(modifier = Modifier.fillMaxSize()) {
        drawLine(
            color = drawLineColor,
            strokeWidth = 5f,
            cap = StrokeCap.Round,
            start = Offset(x = 0f, y = size.height),
            end = Offset(x = size.width, y = 0f)
        )
    }
}
