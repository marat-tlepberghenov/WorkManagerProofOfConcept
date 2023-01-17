package com.demo.workmanagerproofofconcept

import android.content.Context
import android.content.pm.PackageManager
import androidx.core.content.ContextCompat
import javax.inject.Inject

interface LocationGpsDataSource {
    fun getLocation(): Location
}

class DefaultLocationGpsDataSource @Inject constructor() : LocationGpsDataSource {
    private var x = 0
    private var y = 0
    override fun getLocation(): Location {
        return Location(y++, x++)
    }
}

val Context.isLocationGranted
    get() = ContextCompat.checkSelfPermission(
        this,
        android.Manifest.permission.ACCESS_FINE_LOCATION
    ) == PackageManager.PERMISSION_GRANTED