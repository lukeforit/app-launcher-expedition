package me.lukeforit.launcher.domain

import android.content.Context
import android.content.Intent
import android.content.pm.ResolveInfo
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import me.lukeforit.launcher.domain.di.IoDispatcher
import me.lukeforit.launcher.domain.model.AppInfo
import javax.inject.Inject

interface AppInfoProvider {
    suspend fun getInstalledApps(): List<AppInfo>
}

class AppInfoProviderImpl @Inject constructor(
    @param:ApplicationContext private val context: Context,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : AppInfoProvider {
    override suspend fun getInstalledApps(): List<AppInfo> = withContext(ioDispatcher) {
        val packageManager = context.packageManager

        val mainIntent = Intent(Intent.ACTION_MAIN, null)
        mainIntent.addCategory(Intent.CATEGORY_LAUNCHER)

        val allApps: List<ResolveInfo> = packageManager.queryIntentActivities(mainIntent, 0)

        val appList = mutableListOf<AppInfo>()

        for (ri in allApps) {
            val app = AppInfo(
                label = ri.loadLabel(packageManager).toString(),
                packageName = ri.activityInfo.packageName,
                icon = ri.activityInfo.loadIcon(packageManager)
            )
            appList.add(app)
        }
        appList.sortedBy { it.label.lowercase() }
    }
}
