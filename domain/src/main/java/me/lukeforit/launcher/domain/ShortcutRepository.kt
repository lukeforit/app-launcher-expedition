package me.lukeforit.launcher.domain

import android.content.Context
import android.content.SharedPreferences
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import javax.inject.Inject
import javax.inject.Singleton
import androidx.core.content.edit

interface ShortcutRepository {
    val shortcuts: Flow<Set<String>>
    suspend fun addShortcut(packageName: String)
    suspend fun removeShortcut(packageName: String)
}

@Singleton
class ShortcutRepositoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : ShortcutRepository {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("launcher_shortcuts", Context.MODE_PRIVATE)

    private val _shortcuts = MutableStateFlow<Set<String>>(readShortcuts())
    override val shortcuts: Flow<Set<String>> = _shortcuts.asStateFlow()

    override suspend fun addShortcut(packageName: String) {
        val current = readShortcuts().toMutableSet()
        if (current.add(packageName)) {
            writeShortcuts(current)
            _shortcuts.value = current
        }
    }

    override suspend fun removeShortcut(packageName: String) {
        val current = readShortcuts().toMutableSet()
        if (current.remove(packageName)) {
            writeShortcuts(current)
            _shortcuts.value = current
        }
    }

    private fun readShortcuts(): Set<String> {
        return sharedPreferences.getStringSet("shortcuts", emptySet()) ?: emptySet()
    }

    private fun writeShortcuts(shortcuts: Set<String>) {
        sharedPreferences.edit { putStringSet("shortcuts", shortcuts) }
    }
}
