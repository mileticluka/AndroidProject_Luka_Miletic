package com.example.recipeapp

import android.R
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.content.pm.PackageManager
import android.media.RingtoneManager
import androidx.core.app.ActivityCompat
import androidx.core.app.NotificationCompat
import androidx.core.content.ContextCompat
import androidx.work.Worker
import androidx.work.WorkerParameters

const val WORKER = "com.example.recipeapp"
class NotificationWorker(
    private val context: Context,
    workerParams: WorkerParameters
) : Worker(context, workerParams) {
    override fun doWork(): Result {
        if (ActivityCompat.checkSelfPermission(
                context!!,
                android.Manifest.permission.POST_NOTIFICATIONS
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            println("no permission")
            return Result.failure()
        }

        createNotificationChannel(context!!)

        val alarmUri = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_ALARM)

        val builder: NotificationCompat.Builder =
            NotificationCompat.Builder(context, "MEALS_CHANNEL")
                .setSmallIcon(R.drawable.ic_lock_idle_alarm)
                .setContentTitle("Timer is up")
                .setContentText("The timer you set has passed and you're being notified")
                .setPriority(NotificationCompat.PRIORITY_DEFAULT)
                .setSound(alarmUri)

        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.notify(0, builder.build())

        return Result.success()
    }

    private fun createNotificationChannel(context: Context) {
        val name: CharSequence = "MealsChannel"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel("MEALS_CHANNEL", name, importance)

        val notificationManager =
            ContextCompat.getSystemService(context, NotificationManager::class.java)
        notificationManager!!.createNotificationChannel(channel)
    }
}