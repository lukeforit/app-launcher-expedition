package me.lukeforit.launcher.home.main

import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberUpdatedState
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import me.lukeforit.launcher.domain.receiver.TimeReceiver

@Composable
fun ObservableTimeEffect(onTimeChanged: () -> Unit) {
    val context = LocalContext.current
    val currentOnTimeChanged by rememberUpdatedState(onTimeChanged)

    val timeReceiver = remember {
        TimeReceiver { currentOnTimeChanged() }
    }

    DisposableEffect(context, timeReceiver) {
        ContextCompat.registerReceiver(
            /* context = */ context,
            /* receiver = */ timeReceiver,
            /* filter = */ TimeReceiver.intentFilter,
            /* flags = */ ContextCompat.RECEIVER_NOT_EXPORTED
        )
        onDispose {
            context.unregisterReceiver(timeReceiver)
        }
    }
}
