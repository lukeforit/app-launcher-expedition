package me.lukeforit.launcher.domain

import android.content.Context
import dagger.hilt.android.qualifiers.ApplicationContext

interface AppInfoProvider {
}

class AppInfoProviderImpl(
    @ApplicationContext context: Context
) : AppInfoProvider