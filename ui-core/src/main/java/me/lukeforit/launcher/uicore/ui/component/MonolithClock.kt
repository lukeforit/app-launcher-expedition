package me.lukeforit.launcher.uicore.ui.component

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

@Composable
fun MonolithClock(
    hours: String,
    minutes: String,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = hours,
            style = MaterialTheme.typography.displayLarge.copy(
                fontSize = 100.sp,
                fontWeight = FontWeight.Thin,
                lineHeight = 100.sp
            ),
            color = MaterialTheme.colorScheme.onBackground
        )
        Text(
            text = minutes,
            style = MaterialTheme.typography.displayLarge.copy(
                fontSize = 100.sp,
                fontWeight = FontWeight.Thin,
                lineHeight = 100.sp
            ),
            color = MaterialTheme.colorScheme.onBackground
        )
    }
}