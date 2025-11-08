package me.lukeforit.launcher.expedition.nav

import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.hilt.lifecycle.viewmodel.compose.hiltViewModel
import androidx.navigation3.runtime.NavKey
import androidx.navigation3.runtime.entryProvider
import androidx.navigation3.runtime.rememberNavBackStack
import androidx.navigation3.ui.NavDisplay
import kotlinx.serialization.Serializable
import me.lukeforit.launcher.home.HomeScreen
import me.lukeforit.launcher.home.HomeViewModel
import me.lukeforit.launcher.home.myapps.MyAppsScreen
import me.lukeforit.launcher.home.myapps.MyAppsViewModel

sealed class NavScreen : NavKey {
    @Serializable
    object Home : NavScreen()

    @Serializable
    object MyApps : NavScreen()
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun NavMainScreen() {
    val backStack = rememberNavBackStack(NavScreen.Home)
    val bottomSheetStrategy = remember { TransparentBottomSheetSceneStrategy<NavKey>() }
    NavDisplay(
        backStack = backStack,
        sceneStrategy = bottomSheetStrategy,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<NavScreen.Home> {
                val viewModel = hiltViewModel<HomeViewModel>()
                HomeScreen(
                    onSwipeFromBottom = { backStack.add(NavScreen.MyApps) },
                    viewModel = viewModel,
                )
            }
            entry<NavScreen.MyApps>(
                metadata = TransparentBottomSheetSceneStrategy.bottomSheet()
            ) {
                val viewModel = hiltViewModel<MyAppsViewModel>()
                MyAppsScreen(viewModel = viewModel)
            }
        }
    )
}

