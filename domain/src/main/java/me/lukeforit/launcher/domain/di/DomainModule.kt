package me.lukeforit.launcher.domain.di

import android.content.Context
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import me.lukeforit.launcher.domain.AppInfoProvider
import me.lukeforit.launcher.domain.AppInfoProviderImpl

@Module
@InstallIn(SingletonComponent::class)
object DomainModule {

    @Provides
    fun appInfoProvider(
        @ApplicationContext context: Context,
    ): AppInfoProvider = AppInfoProviderImpl(context)
}