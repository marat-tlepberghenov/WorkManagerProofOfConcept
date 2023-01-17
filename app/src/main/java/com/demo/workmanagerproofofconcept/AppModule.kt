package com.demo.workmanagerproofofconcept

import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class AppModule {

    @Singleton
    @Binds
    abstract fun bindsLocationGpsDataSource(
        defaultLocationGpsDataSource: DefaultLocationGpsDataSource
    ): LocationGpsDataSource

    @Singleton
    @Binds
    abstract fun bindsLocationRepository(
        defaultLocationRepository: DefaultLocationRepository
    ): LocationRepository
}