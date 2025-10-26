package me.lukeforit.launcher.home

import me.lukeforit.launcher.domain.model.AppInfo

sealed class HomeState {
    data object Loading : HomeState()
    data class Success(val apps: List<AppInfo>) : HomeState()
    data class Error(val message: String) : HomeState()
}
