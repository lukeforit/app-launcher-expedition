package me.lukeforit.launcher.home.myapps

import android.content.Context
import android.content.Intent
import android.content.res.Configuration
import android.graphics.Color.BLUE
import android.graphics.Color.GREEN
import android.graphics.Color.RED
import android.graphics.Color.YELLOW
import android.widget.Toast
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.drawable.toDrawable
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import me.lukeforit.launcher.domain.model.AppInfo
import me.lukeforit.launcher.uicore.ui.theme.Expedition33LauncherTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MyAppsScreen(viewModel: MyAppsViewModel) {
    val myAppsState by viewModel.myAppsState.collectAsStateWithLifecycle()

    Scaffold(
        containerColor = Color.Transparent,
    ) { paddingValues ->
        AppGrid(apps = myAppsState.apps, paddingValues = paddingValues)
    }
}

@Composable
fun AppGrid(
    apps: List<AppInfo>,
    paddingValues: PaddingValues = PaddingValues(),
) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 128.dp),
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = paddingValues
    ) {
        items(apps) { app ->
            AppGridItem(app = app)
        }
    }
}

@Composable
fun AppGridItem(app: AppInfo) {
    val context = LocalContext.current
    Column(
        modifier = Modifier
            .clickable { launchApp(context, app.packageName) }
            .padding(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        val imageBitmap = remember(app.icon) {
            app.icon.toBitmap(width = 96, height = 96).asImageBitmap()
        }
        Box(
            modifier = Modifier
                .size(80.dp)
                .border(4.dp, MaterialTheme.colorScheme.outline, RotatedSquare)
                .clip(RotatedSquare)
                .background(MaterialTheme.colorScheme.surface),
            contentAlignment = Alignment.Center
        ) {
            Image(
                bitmap = imageBitmap,
                contentDescription = app.label,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
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

val RotatedSquare = object : Shape {
    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        return Outline.Generic(
            path = Path().apply {
                // Move to the top center
                moveTo(size.width / 2f, 0f)
                // Line to the right center
                lineTo(size.width, size.height / 2f)
                // Line to the bottom center
                lineTo(size.width / 2f, size.height)
                // Line to the left center
                lineTo(0f, size.height / 2f)
                // Close the path to complete the diamond
                close()
            }
        )
    }
}

fun launchApp(context: Context, packageName: String) {
    val launchIntent: Intent? = context.packageManager
        .getLaunchIntentForPackage(packageName.toString())

    if (launchIntent != null) {
        launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(launchIntent)
    } else {
        Toast.makeText(context, "Cannot launch app: $packageName", Toast.LENGTH_SHORT).show()
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
            )
        }
    }
}