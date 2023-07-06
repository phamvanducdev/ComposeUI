package com.ducpv.composeui.feature.chat.detail.components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.RowScope
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.ducpv.composeui.shared.theme.AppTypography
import com.ducpv.composeui.shared.theme.alpha10
import com.ducpv.composeui.shared.theme.color

/**
 * Created by pvduc9773 on 19/06/2023.
 */
@Composable
fun ItemDateTimeHeader(
    dateTimeString: String,
) {
    Row(
        modifier = Modifier
            .padding(vertical = 4.dp, horizontal = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        DateTimeHeaderLine()
        Text(
            text = dateTimeString,
            modifier = Modifier.padding(horizontal = 16.dp),
            style = AppTypography.labelSmall,
            color = com.ducpv.composeui.shared.theme.ThemeColor.Gray.color,
        )
        DateTimeHeaderLine()
    }
}

@Composable
private fun RowScope.DateTimeHeaderLine() {
    Divider(
        modifier = Modifier
            .weight(1f)
            .align(Alignment.CenterVertically),
        color = com.ducpv.composeui.shared.theme.ThemeColor.Gray.color.alpha10,
    )
}
