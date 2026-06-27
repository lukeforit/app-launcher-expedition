package me.lukeforit.launcher.domain

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import me.lukeforit.launcher.domain.model.AppInfo
import javax.inject.Inject
import javax.inject.Singleton

interface AppRepository {
    val installedApps: StateFlow<List<AppInfo>>
    suspend fun refreshApps()
}

@Singleton
class AppRepositoryImpl @Inject constructor(
    private val appInfoProvider: AppInfoProvider
) : AppRepository {

    private val repositoryScope = CoroutineScope(Dispatchers.Default)
    private val _installedApps = MutableStateFlow<List<AppInfo>>(emptyList())
    override val installedApps: StateFlow<List<AppInfo>> = _installedApps.asStateFlow()

    init {
        appInfoProvider.registerPackageCallback {
            repositoryScope.launch {
                refreshApps()
            }
        }
    }

    override suspend fun refreshApps() {
        val apps = appInfoProvider.getInstalledApps()
        _installedApps.value = apps
    }
}
