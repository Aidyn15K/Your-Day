package com.example.yourday

import android.appwidget.AppWidgetProvider
import android.content.Context
import android.content.Intent
import android.os.Build

class SecondOfDayWidget : AppWidgetProvider() {
    override fun onEnabled(context: Context) {
        val intent = Intent(context, SecondService::class.java)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            context.startForegroundService(intent)
        } else {
            context.startService(intent)
        }
    }

    override fun onDisabled(context: Context) {
        context.stopService(Intent(context, SecondService::class.java))
    }
}
