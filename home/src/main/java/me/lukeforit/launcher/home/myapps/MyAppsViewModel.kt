package me.lukeforit.launcher.home.myapps

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toImmutableList
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import me.lukeforit.launcher.domain.AppLauncher
import me.lukeforit.launcher.domain.AppRepository
import me.lukeforit.launcher.domain.ShortcutRepository
import me.lukeforit.launcher.domain.model.AppInfo
import javax.inject.Inject

@HiltViewModel
class MyAppsViewModel @Inject constructor(
    private val appRepository: AppRepository,
    private val appLauncher: AppLauncher,
    private val shortcutRepository: ShortcutRepository
) : ViewModel() {

    private val _myAppsState = MutableStateFlow(MyAppsState(persistentListOf()))
    val myAppsState: StateFlow<MyAppsState> = _myAppsState

    init {
        loadApplications()
    }

    fun onEvent(event: MyAppsEvents) {
        when (event) {
            is MyAppsEvents.LaunchApp -> appLauncher.launchApp(event.app.packageName)
            is MyAppsEvents.AddShortcut -> {
                viewModelScope.launch {
                    shortcutRepository.addShortcut(event.app.packageName)
                }
            }
        }
    }

    private fun loadApplications() {
        viewModelScope.launch {
            try {
                if (appRepository.installedApps.value.isEmpty()) {
                    appRepository.refreshApps()
                }
                appRepository.installedApps.collect { apps ->
                    _myAppsState.value = MyAppsState(apps.toImmutableList())
                }
            } catch (e: Exception) {
                // swallow
            }
        }
    }
}

data class MyAppsState(val apps: ImmutableList<AppInfo>)

sealed interface MyAppsEvents {
    class LaunchApp(val app: AppInfo) : MyAppsEvents
    class AddShortcut(val app: AppInfo) : MyAppsEvents
}