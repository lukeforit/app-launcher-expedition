package me.lukeforit.launcher.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.darkColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.core.graphics.drawable.toDrawable
import me.lukeforit.launcher.domain.model.AppInfo

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(viewModel: HomeViewModel) {
    val homeState by viewModel.homeState.collectAsState()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Image(
            painter = painterResource(id = R.drawable.test_image),
            contentDescription = "background",
            modifier = Modifier
                .fillMaxSize()
                .alpha(0.8f),
            contentScale = ContentScale.Crop
        )
        Scaffold(
            containerColor = Color.Transparent
        ) { paddingValues ->
            when (val state = homeState) {
                is HomeState.Loading -> {
                    CircularProgressIndicator(Modifier.padding(paddingValues))
                }

                is HomeState.Success -> {
                }

                is HomeState.Error -> {
                    Text(
                        text = state.message,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAppList() {
    MaterialTheme(colorScheme = darkColorScheme()) {
        val dummyApps = listOf(
            AppInfo(
                label = "Gmail", packageName = "com.google.android.gm", icon = 0xFFF44336.toInt().toDrawable()
            ),
            AppInfo(
                label = "Maps", packageName = "com.google.android.apps.maps",
                icon = 0xFF4CAF50.toInt().toDrawable()
            ),
            AppInfo(
                label = "YouTube", packageName = "com.google.android.youtube",
                icon = 0xFF2196F3.toInt().toDrawable()
            ),
        )
    }
}
