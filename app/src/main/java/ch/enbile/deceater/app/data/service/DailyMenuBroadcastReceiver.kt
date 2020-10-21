package ch.enbile.deceater.app.data.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import ch.enbile.deceater.app.R
import ch.enbile.deceater.app.data.MenuRepository
import ch.enbile.deceater.app.ui.login.LoggedInUserView
import com.google.gson.Gson
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class DailyMenuBroadcastReceiver() : BroadcastReceiver() {
    override fun onReceive(context: Context?, intent: Intent?) {

        val param = intent?.action!!;
        val loggedInUser : LoggedInUserView = Gson().fromJson(param, LoggedInUserView::class.java);
        val menuRepository = MenuRepository(loggedInUser)

        GlobalScope.launch {
            val todaysMenu = menuRepository.getDailyMenu()
            if (todaysMenu != null) {
                notify(todaysMenu.name, context!!)
            }
        }
    }

    private fun notify(menuName:String, context: Context){
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
            .setContentText(context.getString(R.string.notification_text, menuName))
            .build()
        manager.notify(notificationId++, notification)
    }
}