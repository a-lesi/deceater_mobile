package ch.enbile.deceater.app.data.service

import android.app.AlarmManager
import android.app.PendingIntent
import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import ch.enbile.deceater.app.ui.login.LoggedInUserView
import com.google.gson.Gson
import java.util.*

class MenuNotificationService() : Service() {
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val loggedInUser : LoggedInUserView = intent?.getParcelableExtra("loggedInUser")!!;
        setAlarm(applicationContext, loggedInUser)

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }

    private fun setAlarm(context: Context, loggedInUser: LoggedInUserView) {
        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val intent = Intent(context, DailyMenuBroadcastReceiver::class.java)
        intent.action = Gson().toJson(loggedInUser)

        val pendingIntent = PendingIntent.getBroadcast(context, 0, intent, 0)

        val calendar = Calendar.getInstance()
        calendar[Calendar.HOUR_OF_DAY] = 11
        calendar[Calendar.MINUTE] = 45
        calendar[Calendar.SECOND] = 0

        if(calendar.timeInMillis > Calendar.getInstance().timeInMillis) {
            alarmManager.setExact(
                AlarmManager.RTC_WAKEUP,
                calendar.timeInMillis,
                pendingIntent
            )
        }
    }
}