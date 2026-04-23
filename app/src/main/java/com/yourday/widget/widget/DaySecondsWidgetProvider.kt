package com.yourday.widget.widget

import android.app.AlarmManager
import android.app.PendingIntent
import android.appwidget.AppWidgetManager
import android.appwidget.AppWidgetProvider
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import android.widget.RemoteViews
import com.yourday.widget.R
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import kotlin.math.roundToInt

class DaySecondsWidgetProvider : AppWidgetProvider() {

    override fun onEnabled(context: Context) {
        super.onEnabled(context)
        refreshAll(context)
    }

    override fun onDisabled(context: Context) {
        super.onDisabled(context)
        cancelTick(context)
    }

    override fun onReceive(context: Context, intent: Intent) {
        super.onReceive(context, intent)
        if (intent.action == ACTION_TICK || intent.action == AppWidgetManager.ACTION_APPWIDGET_UPDATE) {
            refreshAll(context)
        }
    }

    override fun onUpdate(
        context: Context,
        appWidgetManager: AppWidgetManager,
        appWidgetIds: IntArray,
    ) {
        refreshAll(context)
    }

    companion object {
        private const val ACTION_TICK = "com.yourday.widget.ACTION_TICK"
        private val clockFormatter: DateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss")

        fun refreshAll(context: Context) {
            val manager = AppWidgetManager.getInstance(context)
            val component = ComponentName(context, DaySecondsWidgetProvider::class.java)
            val ids = manager.getAppWidgetIds(component)

            if (ids.isEmpty()) {
                cancelTick(context)
                return
            }

            val now = LocalTime.now()
            val elapsedSeconds = now.toSecondOfDay()
            val remainingSeconds = 86_400 - elapsedSeconds
            val percent = ((elapsedSeconds / 86_400f) * 100).roundToInt()

            ids.forEach { appWidgetId ->
                val views = RemoteViews(context.packageName, R.layout.widget_day_seconds)
                views.setTextViewText(R.id.secondsValue, elapsedSeconds.toString())
                views.setTextViewText(
                    R.id.progressValue,
                    context.getString(R.string.progress_format, percent),
                )
                views.setTextViewText(
                    R.id.remainingValue,
                    context.getString(R.string.remaining_format, remainingSeconds),
                )
                views.setTextViewText(
                    R.id.timeValue,
                    context.getString(R.string.time_format, now.format(clockFormatter)),
                )
                manager.updateAppWidget(appWidgetId, views)
            }

            scheduleNextTick(context)
        }

        private fun scheduleNextTick(context: Context) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            val triggerAtMillis = SystemClock.elapsedRealtime() + 1_000L
            alarmManager.setExactAndAllowWhileIdle(
                AlarmManager.ELAPSED_REALTIME,
                triggerAtMillis,
                pendingIntent(context),
            )
        }

        private fun cancelTick(context: Context) {
            val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
            alarmManager.cancel(pendingIntent(context))
        }

        private fun pendingIntent(context: Context): PendingIntent {
            val intent = Intent(context, DaySecondsWidgetProvider::class.java).apply {
                action = ACTION_TICK
            }
            return PendingIntent.getBroadcast(
                context,
                86400,
                intent,
                PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE,
            )
        }
    }
}
