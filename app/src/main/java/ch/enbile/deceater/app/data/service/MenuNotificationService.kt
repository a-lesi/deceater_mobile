package ch.enbile.deceater.app.data.service

import android.app.Service
import android.content.Intent

import android.os.IBinder

class MenuNotificationService() : Service() {
    var alarm: DailyMenyBroadcastReceiver = DailyMenyBroadcastReceiver()
    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        alarm.SetAlarm(getApplicationContext())
        return START_STICKY
    }

    override fun onCreate() {
        super.onCreate()
    }

    override fun onBind(intent: Intent?): IBinder? {
        return null
    }
}