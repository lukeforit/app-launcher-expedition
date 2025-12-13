package me.lukeforit.launcher.uicore.ui.component

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import me.lukeforit.launcher.uicore.R

@Composable
fun DragHandle(
    modifier: Modifier = Modifier
) {
    Image(
        painter = painterResource(id = R.drawable.drag_handle),
        contentDescription = "Drag handle",
        modifier = modifier
            .fillMaxWidth()
            .height(128.dp)
            .padding(vertical = 4.dp),
        contentScale = ContentScale.Fit,
        alpha = 0.7f,
    )
}
