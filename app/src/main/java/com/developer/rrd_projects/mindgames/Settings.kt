package com.developer.rrd_projects.mindgames

import android.app.*
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import androidx.appcompat.widget.SwitchCompat
import android.view.View
import android.widget.Button
import android.widget.TextView
import com.developer.rrd_projects.mindgames.animators.animateGear
import com.developer.rrd_projects.mindgames.games.GamesSet
import com.developer.rrd_projects.mindgames.games.readGameSet
import com.developer.rrd_projects.mindgames.games.writeGameSet
import org.w3c.dom.Text
import java.util.*

class Settings : AppCompatActivity() {

    var totalMin = 0
    var gamesSet = GamesSet()

    override fun onWindowFocusChanged(hasFocus: Boolean) {
        super.onWindowFocusChanged(hasFocus)

        if (hasFocus) {
            hideSystemUi()
        }
    }

    private fun hideSystemUi() {
        window.decorView.systemUiVisibility = (
                View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
                        or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                        or View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                        or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                        or View.SYSTEM_UI_FLAG_FULLSCREEN
                )
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        window.decorView.systemUiVisibility = (View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                or View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                or View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN)


        setContentView(R.layout.activity_settings)

        gamesSet = readGameSet(applicationContext)

        val btnAnimSw : SwitchCompat = findViewById(R.id.buttons_animation_sw)
        btnAnimSw.isChecked = gamesSet.buttonsAnimation

        val alertSw : SwitchCompat = findViewById(R.id.alert_sw)
        alertSw.setOnCheckedChangeListener { buttonView, isChecked -> changeMode(isChecked) }
        alertSw.isChecked = gamesSet.alarmMode

       setTime(gamesSet.time/60, gamesSet.time%60)

        val chooseBtn : Button = findViewById(R.id.select_time_btn)
        chooseBtn.setOnClickListener{chooseTime()}

        val changeName :Button = findViewById(R.id.change_name_btn)
        changeName.setOnClickListener {changeN()}

        startGears()
    }

    private fun changeN() {
        val intent: Intent = Intent(this, NameChooser::class.java)
        startActivity(intent)
    }

    private fun chooseTime() {
        TimePickerDialog(this, t , gamesSet.time/60,gamesSet.time%60,true).show()
    }

    private val t :TimePickerDialog.OnTimeSetListener = TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute -> setTime(hourOfDay,minute) }

    private fun setTime(hourOfDay: Int, minute: Int) {
        val timeText: TextView = findViewById(R.id.time_str)
        timeText.text = getString(R.string.time, hourOfDay, minute)
        totalMin = minute + (60 * hourOfDay)
    }


    private fun changeMode(isChecked: Boolean) {
        if(isChecked){
            enableTimeChooser()
        }else diableTimeChooser()
    }


    private fun diableTimeChooser() {
        val chooseBtn: Button = findViewById(R.id.select_time_btn)
        val timeText: TextView = findViewById(R.id.time_str)

        chooseBtn.isEnabled = false
        chooseBtn.setTextColor(resources.getColor(R.color.gray))
        timeText.setTextColor(resources.getColor(R.color.gray))
    }


    private fun enableTimeChooser() {
        val chooseBtn: Button = findViewById(R.id.select_time_btn)
        val timeText: TextView = findViewById(R.id.time_str)

        chooseBtn.setTextColor(resources.getColor(R.color.white))
        chooseBtn.isEnabled = true
        timeText.setTextColor(resources.getColor(R.color.white))
    }

    fun cancel(view: View){
        val intent: Intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    fun saveSettings(view: View){
        val gamesSet = readGameSet(applicationContext)
        gamesSet.buttonsAnimation = findViewById<SwitchCompat>(R.id.buttons_animation_sw).isChecked

        if( (findViewById<SwitchCompat>(R.id.alert_sw).isChecked != gamesSet.alarmMode || totalMin != gamesSet.time)){
            createNotification(findViewById<SwitchCompat>(R.id.alert_sw).isChecked )
        }

        gamesSet.time= totalMin
        gamesSet.alarmMode = findViewById<SwitchCompat>(R.id.alert_sw).isChecked
        writeGameSet(gamesSet, applicationContext)

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
    }

    private fun createNotification(b:Boolean) {
        val calendar = Calendar.getInstance()

        calendar.set(Calendar.HOUR_OF_DAY, totalMin/60)
        calendar.set(Calendar.MINUTE, totalMin%60)

        val i = Intent(applicationContext,NotificationReceiver::class.java)

        val pI = PendingIntent.getBroadcast(applicationContext,100,i,PendingIntent.FLAG_UPDATE_CURRENT)
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        if(b) {
            alarmManager.setRepeating(AlarmManager.RTC_WAKEUP, calendar.timeInMillis, AlarmManager.INTERVAL_DAY, pI)
        }else alarmManager.cancel(pI)

    }

    private fun startGears() {
        animateGear(findViewById(R.id.left_blue_gear))
        animateGear(findViewById(R.id.left_green_gear))
        animateGear(findViewById(R.id.left_orange_gear))
        animateGear(findViewById(R.id.right_blue_gear))
        animateGear(findViewById(R.id.right_green_gear))
        animateGear(findViewById(R.id.right_orange_gear))
    }

}
