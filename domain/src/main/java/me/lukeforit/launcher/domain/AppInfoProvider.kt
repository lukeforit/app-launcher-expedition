package me.lukeforit.launcher.domain

import android.content.Context
import android.content.pm.LauncherActivityInfo
import android.content.pm.LauncherApps
import android.os.UserHandle
import android.os.UserManager
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import me.lukeforit.launcher.domain.di.IoDispatcher
import me.lukeforit.launcher.domain.model.AppInfo
import javax.inject.Inject

interface AppInfoProvider {
    suspend fun getInstalledApps(): List<AppInfo>
    fun registerPackageCallback(onChanged: () -> Unit)
}

class AppInfoProviderImpl @Inject constructor(
    @param:ApplicationContext private val context: Context,
    @param:IoDispatcher private val ioDispatcher: CoroutineDispatcher
) : AppInfoProvider {

    private val launcherApps = context.getSystemService(Context.LAUNCHER_APPS_SERVICE) as LauncherApps
    private val userManager = context.getSystemService(Context.USER_SERVICE) as UserManager

    override suspend fun getInstalledApps(): List<AppInfo> = withContext(ioDispatcher) {
        val appList = mutableListOf<AppInfo>()
        val userProfiles: List<UserHandle> = userManager.userProfiles

        for (userHandle in userProfiles) {
            val activityList: List<LauncherActivityInfo> = launcherApps.getActivityList(null, userHandle)
            for (activityInfo in activityList) {
                val app = AppInfo(
                    label = activityInfo.label.toString(),
                    packageName = activityInfo.applicationInfo.packageName,
                    icon = activityInfo.getBadgedIcon(0)
                )
                appList.add(app)
            }
        }
        appList.sortedBy { it.label.lowercase() }
    }

    override fun registerPackageCallback(onChanged: () -> Unit) {
        launcherApps.registerCallback(object : LauncherApps.Callback() {
            override fun onPackageRemoved(packageName: String, user: UserHandle) { onChanged() }
            override fun onPackageAdded(packageName: String, user: UserHandle) { onChanged() }
            override fun onPackageChanged(packageName: String, user: UserHandle) { onChanged() }
            override fun onPackagesAvailable(userNames: Array<out String>, user: UserHandle, replacing: Boolean) { onChanged() }
            override fun onPackagesUnavailable(userNames: Array<out String>, user: UserHandle, replacing: Boolean) { onChanged() }
        })
    }
}
