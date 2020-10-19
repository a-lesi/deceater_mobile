package ch.enbile.deceater.app

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Intent
import android.content.SharedPreferences
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import ch.enbile.deceater.app.data.MenuRepository
import ch.enbile.deceater.app.data.adapter.MenuAdapter
import ch.enbile.deceater.app.data.model.Menu
import ch.enbile.deceater.app.ui.login.LoggedInUserView
import ch.enbile.deceater.app.ui.login.LoginActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {
    var adapter: MenuAdapter? = null
    var list = arrayListOf<Menu>()

    private lateinit var menuRepository: MenuRepository
    private lateinit var loggedInUser: LoggedInUserView

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

        loggedInUser = intent.getParcelableExtra("loggedInUser")!!;
        menuRepository = MenuRepository(loggedInUser)

        val dailyMenu = findViewById<TextView>(R.id.menu_selected)
        GlobalScope.launch {
            val menu = menuRepository.getDailyMenu()
            if (menu != null) {
                runOnUiThread {
                    dailyMenu.text = menu.name
                }
            }
        }

        val layoutManager: RecyclerView.LayoutManager = LinearLayoutManager(this)
        adapter = MenuAdapter( this@MainActivity, menuRepository)
        val dividerItemDecoration =
            DividerItemDecoration(this, DividerItemDecoration.VERTICAL)

        val recyclerView = findViewById<RecyclerView>(R.id.menu_list)
        recyclerView.layoutManager = layoutManager
        recyclerView.adapter = adapter
        recyclerView.addItemDecoration(dividerItemDecoration)

        val logOutButton = findViewById<Button>(R.id.logout)
        logOutButton.setOnClickListener {
            val myIntent = Intent(this@MainActivity, LoginActivity::class.java)
            startActivity(myIntent)
            finish()
        }

        val addMenuButton = findViewById<Button>(R.id.add_menu)
        val inputText = findViewById<EditText>(R.id.input_new_menu)
        addMenuButton.setOnClickListener {
            GlobalScope.launch {
                val result = menuRepository.tryAddMenu(Menu(0, inputText.text.toString(), "", "", false))
                if (result) {
                    val menuList = menuRepository.getMenues()
                    runOnUiThread {
                        list = ArrayList(menuList)
                        adapter?.updateMenues(list)

                        Toast.makeText(
                            it.context,
                            "${inputText.text.toString()} was added",
                            Toast.LENGTH_LONG
                        ).show()
                    }
                }
                else{
                    Toast.makeText(
                        it.context,
                        "${inputText.text.toString()} could not added",
                        Toast.LENGTH_LONG
                    ).show()
                }

                runOnUiThread {
                    inputText.text.clear()
                }
            }
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

    override fun onResume() {
        super.onResume()

        GlobalScope.launch {
            val menuList = ArrayList(menuRepository.getMenues())
            runOnUiThread {
                list = menuList
                adapter?.updateMenues(list)
            }
        }
    }

    private fun reset() {
        val intent = Intent(this@MainActivity, MainActivity::class.java)
        intent.putExtra("loggedInUser", loggedInUser)
        startActivity(intent)
        finish()
    }
}