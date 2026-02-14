package me.lukeforit.launcher.home

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
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
import androidx.compose.ui.draw.paint
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toDrawable
import me.lukeforit.launcher.domain.model.AppInfo
import me.lukeforit.launcher.domain.model.HomePage

private const val SWIPE_THRESHOLD = 50
private const val BACKGROUND_OFFSET = 200f

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun HomeScreen(
    onSwipeFromBottom: () -> Unit,
    viewModel: HomeViewModel
) {
    val homeState by viewModel.homeState.collectAsState()
    val pagerState = rememberPagerState(initialPage = HomePage.Main.ordinal) { HomePage.entries.size }

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .pointerInput(Unit) {
                var verticalDragAmount = 0f
                var hasSwiped = false
                detectDragGestures(
                    onDragStart = {
                        verticalDragAmount = 0f
                        hasSwiped = false
                    },
                    onDrag = { change, dragAmount ->
                        verticalDragAmount += dragAmount.y
                        if (dragAmount.y < 0) {
                            change.consume()
                        }

                        if (!hasSwiped && verticalDragAmount < -SWIPE_THRESHOLD.dp.toPx()) {
                            hasSwiped = true
                            onSwipeFromBottom()
                        }
                    },
                    onDragEnd = {}
                )
            }
    ) {
        Layout(
            Modifier
                .fillMaxSize()
                .graphicsLayer {
                    val offset = (pagerState.currentPage - HomePage.Main.ordinal) + pagerState.currentPageOffsetFraction
                    translationX = -offset * BACKGROUND_OFFSET
                }
                .paint(
                    painterResource(id = R.drawable.test_image),
                    alignment = Alignment.Center,
                    contentScale = ContentScale.Crop,
                    alpha = 0.8f,
                    colorFilter = null,
                ),
        ) { _, constraints ->
            layout(constraints.minWidth, constraints.minHeight) {}
        }
        Scaffold(
            containerColor = Color.Transparent
        ) { paddingValues ->
            when (val state = homeState) {
                is HomeState.Loading -> {
                    CircularProgressIndicator(Modifier.padding(paddingValues))
                }

                is HomeState.Success -> HorizontalPager(
                    state = pagerState
                ) {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.BottomCenter) {
                        Text("PAGE $it")
                    }
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
