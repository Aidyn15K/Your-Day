package com.example.yourday

import android.app.*
import android.appwidget.AppWidgetManager
import android.content.ComponentName
import android.content.Intent
import android.os.*
import android.widget.RemoteViews
import java.util.*

class SecondService : Service() {

    private val handler = Handler(Looper.getMainLooper())

    private val runnable = object : Runnable {
        override fun run() {
            updateWidgets()
            handler.postDelayed(this, 1000)
        }
    }

    override fun onCreate() {
        super.onCreate()
        startForegroundService()
        handler.post(runnable)
    }

    override fun onDestroy() {
        handler.removeCallbacks(runnable)
        super.onDestroy()
    }

    override fun onBind(intent: Intent?): IBinder? = null

    private fun startForegroundService() {
        val channelId = "yourday_channel"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                channelId,
                "Your Day",
                NotificationManager.IMPORTANCE_LOW
            )
            getSystemService(NotificationManager::class.java)
                .createNotificationChannel(channel)
        }

        val notification = Notification.Builder(this, channelId)
            .setContentTitle("Your Day Widget")
            .setContentText("Running...")
            .setSmallIcon(android.R.drawable.ic_menu_recent_history)
            .build()

        startForeground(1, notification)
    }

    private fun updateWidgets() {
        val manager = AppWidgetManager.getInstance(this)
        val component = ComponentName(this, SecondOfDayWidget::class.java)
        val ids = manager.getAppWidgetIds(component)

        val cal = Calendar.getInstance()
        val seconds = cal.get(Calendar.HOUR_OF_DAY) * 3600 +
                cal.get(Calendar.MINUTE) * 60 +
                cal.get(Calendar.SECOND)

        for (id in ids) {
            val views = RemoteViews(packageName, R.layout.widget_layout)
            views.setTextViewText(R.id.textView, seconds.toString())
            manager.updateAppWidget(id, views)
        }
    }
}
