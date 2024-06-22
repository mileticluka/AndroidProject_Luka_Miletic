package com.example.recipeapp

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Intent
import android.content.pm.PackageManager
import android.icu.util.Calendar
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.TimePicker
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat


class TimerActivity : AppCompatActivity() {
    private lateinit var timePicker: TimePicker
    private lateinit var buttonSetAlarm: Button
    private lateinit var alarm: AlarmManager

    private var pendingIntent: PendingIntent? = null

    @RequiresApi(Build.VERSION_CODES.S)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_timer)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        alarm = getSystemService(AlarmManager::class.java)
        timePicker = findViewById(R.id.timePicker)
        buttonSetAlarm = findViewById(R.id.button_submit)

        buttonSetAlarm.setOnClickListener {
            val calendar = Calendar.getInstance()

            calendar.set(Calendar.HOUR_OF_DAY, timePicker.hour);
            calendar.set(Calendar.MINUTE, timePicker.minute);

            var time = (calendar.getTimeInMillis() - (calendar.getTimeInMillis() % 60000));
            if (System.currentTimeMillis() > time) {
                time += (1000 * 60 * 60 * 24);
            }


            val intent = Intent(this, AlarmReceiver::class.java)
            pendingIntent = PendingIntent.getBroadcast(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

            if (!alarm.canScheduleExactAlarms()) {
                Toast.makeText(this, "No permission to set exact alarm", Toast.LENGTH_LONG).show()
                return@setOnClickListener;
            }


            if (ActivityCompat.checkSelfPermission(
                    this,
                    android.Manifest.permission.POST_NOTIFICATIONS
                ) != PackageManager.PERMISSION_GRANTED
            ) {
                Toast.makeText(this, "No permission to post notifications", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }

            alarm.setExact(AlarmManager.RTC_WAKEUP, time, pendingIntent!!);

            Toast.makeText(this, "Alarm registered", Toast.LENGTH_LONG).show()
        }
    }
}