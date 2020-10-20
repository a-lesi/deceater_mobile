package ch.enbile.deceater.app.data.service

import android.app.Service
import android.content.Context
import android.content.Intent

import android.os.IBinder

class MenuNotificationService() : Service() {
    var alarm: DailyMenyBroadcastReceiver = DailyMenyBroadcastReceiver()
    override fun onCreate() {
        super.onCreate()
    }

    fun onStart(context: Context?, intent: Intent?, startId: Int) {
        alarm.SetAlarm(context!!)
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}