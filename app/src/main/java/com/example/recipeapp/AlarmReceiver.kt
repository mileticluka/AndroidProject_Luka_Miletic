package com.example.recipeapp

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import androidx.work.ExistingWorkPolicy
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        WorkManager.getInstance(context!!).apply {
            enqueueUniqueWork(
                WORKER,
                ExistingWorkPolicy.KEEP,
                OneTimeWorkRequest.Companion.from(NotificationWorker::class.java)
            )
        }
    }
}