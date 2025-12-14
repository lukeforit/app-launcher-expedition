package me.lukeforit.launcher.domain

import android.content.Context
import android.content.Intent
import android.widget.Toast
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject

interface AppLauncher {
    fun launchApp(packageName: String)
}

class AppLauncherImpl @Inject constructor(@ApplicationContext private val context: Context) : AppLauncher {
    override fun launchApp(packageName: String) {
        val launchIntent: Intent? = context.packageManager.getLaunchIntentForPackage(packageName)

        if (launchIntent != null) {
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
            context.startActivity(launchIntent)
        } else {
            Toast.makeText(context, "Cannot launch app: $packageName", Toast.LENGTH_SHORT).show()
        }
    }
}