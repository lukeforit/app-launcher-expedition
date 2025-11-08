package me.lukeforit.launcher.expedition.nav

import androidx.compose.runtime.Composable
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

@Composable
fun NavMainScreen() {
    val backStack = rememberNavBackStack(NavScreen.Home)
    NavDisplay(
        backStack = backStack,
        onBack = { backStack.removeLastOrNull() },
        entryProvider = entryProvider {
            entry<NavScreen.Home> {
                val viewModel = hiltViewModel<HomeViewModel>()
                HomeScreen(
                    onSwipeFromBottom = { backStack.add(NavScreen.MyApps) },
                    viewModel = viewModel,
                )
            }
            entry<NavScreen.MyApps> {
                val viewModel = hiltViewModel<MyAppsViewModel>()
                MyAppsScreen(viewModel = viewModel)
            }
        }
    )
}

