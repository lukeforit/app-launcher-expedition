package me.lukeforit.launcher.expedition

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import dagger.hilt.android.AndroidEntryPoint
import me.lukeforit.launcher.expedition.nav.NavMainScreen
import me.lukeforit.launcher.uicore.ui.theme.Expedition33LauncherTheme

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        enableEdgeToEdge()
        setContent {
            Expedition33LauncherTheme(dynamicColor = false) {
                NavMainScreen()
            }
        }
    }
}