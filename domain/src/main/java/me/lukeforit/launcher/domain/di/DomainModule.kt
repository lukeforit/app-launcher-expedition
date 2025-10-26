package me.lukeforit.launcher.domain.di

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import me.lukeforit.launcher.domain.AppInfoProvider
import me.lukeforit.launcher.domain.AppInfoProviderImpl
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DomainModule {

    @Binds
    @Singleton
    abstract fun appInfoProvider(impl: AppInfoProviderImpl): AppInfoProvider
}