package com.demo.workmanagerproofofconcept

import javax.inject.Inject

interface LocationRepository {
    fun getLocation(): Location
}

class DefaultLocationRepository @Inject constructor(
    private val gpsDataSource: LocationGpsDataSource
) : LocationRepository {
    override fun getLocation(): Location = gpsDataSource.getLocation()
}

data class Location(
    val latitude: Int,
    val longitude: Int
)