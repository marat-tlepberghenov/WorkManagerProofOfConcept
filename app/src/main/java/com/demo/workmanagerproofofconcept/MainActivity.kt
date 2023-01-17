package com.demo.workmanagerproofofconcept

import android.os.Bundle
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.WorkManager
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    private val locationPermissionRequest = registerForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted) {
            startSendLocationWork()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        locationPermissionRequest.launch(android.Manifest.permission.ACCESS_FINE_LOCATION)
    }

    private fun startSendLocationWork() {
        //Need verify if a work with name [ SendLocationWorker.NAME] is running.
        //If true
        WorkManager.getInstance(this).enqueueUniqueWork(
            SendLocationWorker.NAME,
            ExistingWorkPolicy.REPLACE,
            OneTimeWorkRequestBuilder<SendLocationWorker>().build()
        )
    }
}