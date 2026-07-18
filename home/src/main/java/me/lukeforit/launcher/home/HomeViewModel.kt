package me.lukeforit.launcher.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import me.lukeforit.launcher.domain.AppLauncher
import me.lukeforit.launcher.domain.AppRepository
import me.lukeforit.launcher.domain.ShortcutRepository
import me.lukeforit.launcher.domain.model.AppInfo
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(
    private val appRepository: AppRepository,
    private val shortcutRepository: ShortcutRepository,
    private val appLauncher: AppLauncher
) : ViewModel() {

    private val _homeState = MutableStateFlow<HomeState>(HomeState.Loading)
    val homeState: StateFlow<HomeState> = _homeState.asStateFlow()

    init {
        loadApplications()
    }

    fun onEvent(event: HomeEvents) {
        when (event) {
            is HomeEvents.LaunchApp -> appLauncher.launchApp(event.app.packageName)
        }
    }

    private fun loadApplications() {
        viewModelScope.launch {
            _homeState.value = HomeState.Loading
            try {
                if (appRepository.installedApps.value.isEmpty()) {
                    appRepository.refreshApps()
                }
                combine(
                    appRepository.installedApps,
                    shortcutRepository.shortcuts
                ) { apps, shortcuts ->
                    val shortcutApps = apps.filter { it.packageName in shortcuts }
                    HomeState.Success(apps = apps, shortcuts = shortcutApps)
                }.collect { state ->
                    _homeState.value = state
                }
            } catch (e: Exception) {
                _homeState.value = HomeState.Error("Failed to load apps")
            }
        }
    }
}

sealed interface HomeEvents {
    class LaunchApp(val app: AppInfo) : HomeEvents
}
