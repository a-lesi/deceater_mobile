package ch.enbile.deceater.app

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.app.AppCompatDelegate
import ch.enbile.deceater.app.ui.login.LoginActivity
import ch.enbile.deceater.app.R

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
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
                    reset()
                } else {
                    AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO)
                    reset()
                }
            }
        }
    }
    private fun reset() {
        val intent = Intent(this@MainActivity, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}