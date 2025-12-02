package me.lukeforit.launcher.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import me.lukeforit.launcher.domain.AppInfoProvider
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val appInfoProvider: AppInfoProvider) : ViewModel() {

    private val _homeState = MutableStateFlow<HomeState>(HomeState.Loading)
    val homeState: StateFlow<HomeState> = _homeState.asStateFlow()

    init {
        loadApplications()
    }

    private fun loadApplications() {
        viewModelScope.launch {
            _homeState.value = HomeState.Loading
            try {
                val apps = appInfoProvider.getInstalledApps()
                _homeState.value = HomeState.Success(apps)
            } catch (e: Exception) {
                _homeState.value = HomeState.Error("Failed to load apps")
            }
        }
    }
}
