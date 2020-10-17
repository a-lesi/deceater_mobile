package ch.enbile.deceater.app

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import ch.enbile.deceater.app.ui.login.LoginActivity
import ch.enbile.deceater.app.R

class MainActivity : AppCompatActivity() {
    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        val appSettingPrefs: SharedPreferences = getSharedPreferences("AppSettingPrefs", 0)
        val sharedPrefsEdit: SharedPreferences.Editor = appSettingPrefs.edit()

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            setTheme(R.style.DarkTheme)
        } else {
            setTheme(R.style.AppTheme)
        }

        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val logOutButton = findViewById<Button>(R.id.logout)
        logOutButton.setOnClickListener {
            val myIntent = Intent(this@MainActivity, LoginActivity::class.java)
            this@MainActivity.startActivity(myIntent)
        }

        val themeSwitch = findViewById<androidx.appcompat.widget.SwitchCompat>(R.id.switch1)

        if (AppCompatDelegate.getDefaultNightMode() == AppCompatDelegate.MODE_NIGHT_YES) {
            themeSwitch.isChecked = true
        }
        themeSwitch.setOnCheckedChangeListener{ compoundButton, isChecked ->
            run {
                if (isChecked) {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES)
                    sharedPrefsEdit.putBoolean("NightMode", true)
                    sharedPrefsEdit.apply()
                    reset()
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    sharedPrefsEdit.putBoolean("NightMode", false)
                    sharedPrefsEdit.apply()
                    reset()
                }
            }
        }

        val CID = "Deceater_Channel"
        val CNAME = "Deceater Notifications"
        val CDESC = "Ein Channel für Deceater "
        val CIMP = NotificationManager.IMPORTANCE_HIGH
        var notificationId = 1
        val manager: NotificationManagerCompat = NotificationManagerCompat.from(this)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel: NotificationChannel = NotificationChannel(CID, CNAME, CIMP)
            channel.description = CDESC
            manager.createNotificationChannel(channel)
        }

        val notification: Notification
        notification = NotificationCompat.Builder(this, CID)
            .setSmallIcon(R.drawable.ic_stat_deceater)
            .setContentTitle(getString(R.string.notification_title))
            .setContentText(getString(R.string.notification_text))
            .build()
        manager.notify(notificationId++, notification)
    }

    private fun reset() {
        val intent = Intent(this@MainActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}