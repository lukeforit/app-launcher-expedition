package me.lukeforit.launcher.home

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import me.lukeforit.launcher.domain.AppInfoProvider
import me.lukeforit.launcher.domain.model.AppInfo
import javax.inject.Inject

@HiltViewModel
class HomeViewModel @Inject constructor(private val appInfoProvider: AppInfoProvider) : ViewModel() {

    private val _apps = MutableStateFlow<List<AppInfo>>(emptyList())
    val apps: StateFlow<List<AppInfo>> = _apps

    init {
        loadApplications()
    }

    private fun loadApplications() {
        viewModelScope.launch {
            _apps.value = appInfoProvider.getInstalledApps()
        }
    }
}
