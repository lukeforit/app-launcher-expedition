package me.lukeforit.launcher.home.myapps

import android.content.res.Configuration
import android.graphics.Color.BLUE
import android.graphics.Color.GREEN
import android.graphics.Color.RED
import android.graphics.Color.YELLOW
import android.graphics.drawable.AdaptiveIconDrawable
import android.graphics.drawable.Drawable
import android.os.Build
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toDrawable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.google.accompanist.drawablepainter.rememberDrawablePainter
import me.lukeforit.launcher.domain.model.AppInfo
import me.lukeforit.launcher.uicore.ui.theme.Spacing
import me.lukeforit.launcher.uicore.ui.theme.Expedition33LauncherTheme
import me.lukeforit.launcher.uicore.ui.theme.Shapes
import me.lukeforit.launcher.uicore.ui.theme.Size

private val TileSize = 80.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyAppsScreen(viewModel: MyAppsViewModel) {
    val myAppsState by viewModel.myAppsState.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = Color.Transparent,
    ) { paddingValues ->
        AppGrid(
            apps = myAppsState.apps, 
            paddingValues = paddingValues,
            onEvent = viewModel::onEvent
        )
    }
}

@Composable
private fun AppGrid(
    apps: List<AppInfo>,
    paddingValues: PaddingValues = PaddingValues(),
    onEvent: (MyAppsEvents) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = Size.Large),
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(Spacing.Medium),
        horizontalArrangement = Arrangement.spacedBy(Spacing.Medium),
        contentPadding = paddingValues
    ) {
        items(apps) { app ->
            AppGridItem(app = app, onEvent = onEvent)
        }
    }
}

@Composable
private fun AppGridItem(app: AppInfo, onEvent: (MyAppsEvents) -> Unit) {
    Column(
        modifier = Modifier
            .clickable { onEvent(MyAppsEvents.LaunchApp(app)) }
            .padding(Spacing.Medium),
        horizontalAlignment = Alignment.CenterHorizontally
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

        Text(
            text = app.label,
            color = MaterialTheme.colorScheme.onBackground,
            textAlign = TextAlign.Center,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis,
        )
    }
}

@Composable
fun AppIconImage(
    icon: Drawable,
    modifier: Modifier = Modifier,
) {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU && icon is AdaptiveIconDrawable && icon.monochrome != null) {
        Image(
            painter = rememberDrawablePainter(icon.monochrome),
            contentDescription = null,
            modifier = modifier,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary, BlendMode.SrcIn),
        )
    } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O && icon is AdaptiveIconDrawable) {
        Image(
            painter = rememberDrawablePainter(icon.background),
            contentDescription = null,
            modifier = modifier,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.background, BlendMode.SrcIn),
        )
        Image(
            painter = rememberDrawablePainter(icon.foreground),
            contentDescription = null,
            modifier = modifier,
            colorFilter = ColorFilter.tint(MaterialTheme.colorScheme.primary, BlendMode.SrcIn),
        )
    } else {
        Image(
            painter = rememberDrawablePainter(icon),
            contentDescription = null,
            modifier = modifier
        )
    }
}

@Preview(uiMode = Configuration.UI_MODE_NIGHT_YES)
@Composable
fun MyAppsScreenPreview() {
    Expedition33LauncherTheme(dynamicColor = false) {
        Scaffold {
            AppGrid(
                listOf(
                    AppInfo("App 1", "com.app1", RED.toDrawable()),
                    AppInfo("App 2", "com.app2", GREEN.toDrawable()),
                    AppInfo("App 3", "com.app3", BLUE.toDrawable()),
                    AppInfo("Very long app name that should be ellipsized", "com.app4", YELLOW.toDrawable()),
                ),
                paddingValues = it,
                onEvent = {}
            )
        }
    }
}
