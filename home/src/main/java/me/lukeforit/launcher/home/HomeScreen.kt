package me.lukeforit.launcher.home

import android.content.Context
import android.content.Intent
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.drawable.toBitmap
import me.lukeforit.launcher.domain.model.AppInfo

@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val appList by viewModel.apps.collectAsState()
    val isLoading = appList.isEmpty()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        if (isLoading) {
            // Show a loading indicator while fetching the list
            CircularProgressIndicator(Modifier.align(Alignment.Center))
        } else {
            // Display the list of applications
            AppList(apps = appList)
        }
    }
}

/**
 * Composable for the list view of all applications.
 */
@Composable
fun AppList(apps: List<AppInfo>) {
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        item {
            // Simple header
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = "App Drawer",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
                Spacer(modifier = Modifier.height(16.dp))
            }
        }
        items(apps) { app ->
            AppItem(app = app)
        }
    }
}

/**
 * Composable for a single app item in the list.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppItem(app: AppInfo) {
    val context = LocalContext.current

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
            .clip(RoundedCornerShape(12.dp))
            .clickable { launchApp(context, app.packageName) },
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Row(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // App Icon
            // Convert Android Drawable to Compose ImageBitmap
            val imageBitmap = remember(app.icon) {
                app.icon.toBitmap(width = 96, height = 96).asImageBitmap()
            }
            Image(
                bitmap = imageBitmap,
                contentDescription = "Icon for ${app.label}",
                modifier = Modifier
                    .size(48.dp)
                    .clip(RoundedCornerShape(8.dp))
                    .background(Color.White) // Provide a solid background for transparent icons
            )
            Spacer(modifier = Modifier.size(16.dp))

            // App Label
            Text(
                text = app.label,
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 18.sp,
                fontWeight = FontWeight.Medium,
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
            )
        }
    }
}

/**
 * Helper function to launch an application by its package name.
 */
fun launchApp(context: Context, packageName: String) {
    val launchIntent: Intent? = context.packageManager
        .getLaunchIntentForPackage(packageName.toString())

    if (launchIntent != null) {
        // Essential flag for a launcher to start the new activity in a fresh task
        launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
        context.startActivity(launchIntent)
    } else {
        Toast.makeText(context, "Cannot launch app: $packageName", Toast.LENGTH_SHORT).show()
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAppList() {
    MaterialTheme(colorScheme = darkColorScheme()) {
//        val dummyApps = listOf(
//            AppInfo("Gmail", "com.google.android.gm",
//                painterResource(R.drawable.sym_def_app_icon).toBitmap().toDrawable(LocalContext.current.resources)
//            ),
//            AppInfo("Maps", "com.google.android.apps.maps",
//                painterResource(R.drawable.sym_def_app_icon).toBitmap().toDrawable(LocalContext.current.resources)
//            ),
//            AppInfo("YouTube", "com.google.android.youtube",
//                painterResource(R.drawable.sym_def_app_icon).toBitmap().toDrawable(LocalContext.current.resources)
//            ),
//        )
//        // Note: For a real preview, you can't easily load real icons, so we use dummy drawables.
//        AppList(apps = dummyApps)
    }
}
