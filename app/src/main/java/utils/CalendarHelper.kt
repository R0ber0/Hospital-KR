package com.kotov.hospital.utils

import android.content.Context
import android.content.Intent
import android.provider.CalendarContract
import java.util.Calendar

object CalendarHelper {

    fun addEventToCalendar(
        context: Context,
        title: String,
        description: String
    ) {
        try {
            val startTime = Calendar.getInstance().timeInMillis + 86400000L // завтра

            val intent = Intent(Intent.ACTION_INSERT).apply {
                data = CalendarContract.Events.CONTENT_URI
                putExtra(CalendarContract.Events.TITLE, title)
                putExtra(CalendarContract.Events.DESCRIPTION, description)
                putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime)
                putExtra(CalendarContract.EXTRA_EVENT_END_TIME, startTime + 3600000L) // 1 час
                putExtra(CalendarContract.Events.EVENT_TIMEZONE, "Europe/Moscow")
            }

            context.startActivity(intent)

        } catch (e: Exception) {
            e.printStackTrace()
            android.widget.Toast.makeText(context, "Не удалось открыть календарь", android.widget.Toast.LENGTH_LONG).show()
        }
    }
}