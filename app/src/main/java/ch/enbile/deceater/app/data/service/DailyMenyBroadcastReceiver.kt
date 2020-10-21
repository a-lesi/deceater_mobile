package ch.enbile.deceater.app.data.service

import android.app.*
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import android.os.PowerManager
import android.widget.Toast
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import ch.enbile.deceater.app.R
import java.util.*


class DailyMenyBroadcastReceiver() : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {
        notify("nichts", context!!)
    }

    fun SetAlarm(context: Context) {
        val am = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val i = Intent(context, DailyMenyBroadcastReceiver::class.java)
        val pi = PendingIntent.getBroadcast(context, 0, i, 0)

        val calendar: Calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
        }

        am.setRepeating(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            1000 * 60,
            pi
        )
    }

    private fun notify(test:String, context: Context){
        val CID = "Deceater_Channel"
        val CNAME = "Deceater Notifications"
        val CDESC = "Ein Channel für Deceater "
        val CIMP = NotificationManager.IMPORTANCE_HIGH
        var notificationId = 1
        val manager: NotificationManagerCompat = NotificationManagerCompat.from(context)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel: NotificationChannel = NotificationChannel(CID, CNAME, CIMP)
            channel.description = CDESC
            manager.createNotificationChannel(channel)
        }

        val notification: Notification
        notification = NotificationCompat.Builder(context, CID)
            .setSmallIcon(R.drawable.ic_stat_deceater)
            .setContentTitle(context.getString(R.string.notification_title))
            .setContentText("Heute wird ${test} gegessen")
            .build()
        manager.notify(notificationId++, notification)
    }
}