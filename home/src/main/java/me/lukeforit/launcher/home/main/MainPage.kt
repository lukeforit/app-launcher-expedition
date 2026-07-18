package me.lukeforit.launcher.home.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import me.lukeforit.launcher.domain.model.AppInfo
import me.lukeforit.launcher.home.myapps.AppIconImage
import me.lukeforit.launcher.uicore.ui.theme.Spacing
import me.lukeforit.launcher.uicore.ui.theme.Shapes
import me.lukeforit.launcher.uicore.ui.theme.Size

private val TileSize = 80.dp

@Composable
fun MainPage(
    shortcuts: List<AppInfo>,
    onLaunchApp: (AppInfo) -> Unit,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.BottomCenter
    ) {
        ShortcutGrid(
            shortcuts = shortcuts,
            onLaunchApp = onLaunchApp,
            modifier = Modifier.padding(bottom = 32.dp)
        )
    }
}

@Composable
private fun ShortcutGrid(
    shortcuts: List<AppInfo>,
    onLaunchApp: (AppInfo) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(4),
        modifier = modifier
            .fillMaxWidth()
            .padding(Spacing.Medium),
        verticalArrangement = Arrangement.spacedBy(Spacing.Medium),
        horizontalArrangement = Arrangement.spacedBy(Spacing.Medium)
    ) {
        items(shortcuts) { app ->
            ShortcutItem(app = app, onLaunchApp = onLaunchApp)
        }
    }
}

@Composable
private fun ShortcutItem(app: AppInfo, onLaunchApp: (AppInfo) -> Unit) {
    Box(
        modifier = Modifier
            .clickable { onLaunchApp(app) }
            .padding(Spacing.Medium),
        contentAlignment = Alignment.BottomCenter
    ) {
        Box(
            modifier = Modifier
                .size(TileSize)
                .border(Size.BorderMedium, MaterialTheme.colorScheme.outline, Shapes.RotatedSquare)
                .clip(Shapes.RotatedSquare)
                .background(MaterialTheme.colorScheme.surface),
            contentAlignment = Alignment.Center
        ) {
            AppIconImage(
                icon = app.icon,
                modifier = Modifier.size(Size.Medium),
            )
        }

        Box(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .background(
                    color = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.7f),
                    shape = RoundedCornerShape(percent = 50)
                )
                .padding(horizontal = Spacing.Medium, vertical = 2.dp)
        ) {
            Text(
                text = app.label,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                textAlign = TextAlign.Center,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                style = MaterialTheme.typography.labelSmall
            )
        }
    }
}