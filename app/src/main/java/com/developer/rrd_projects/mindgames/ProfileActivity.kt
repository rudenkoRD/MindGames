package com.developer.rrd_projects.mindgames

import android.content.Intent
import android.graphics.Point
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Display
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import com.developer.rrd_projects.mindgames.animators.animateButtons
import com.developer.rrd_projects.mindgames.animators.animateGear
import com.developer.rrd_projects.mindgames.games.readGameSet
import com.developer.rrd_projects.mindgames.person.getExpForLevel
import com.developer.rrd_projects.mindgames.person.getImageId
import com.developer.rrd_projects.mindgames.person.readPerson

class ProfileActivity : MyGameActivity() {

    var comeFr: String = ""

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

        setContentView(R.layout.activity_profile)

        startGears()

        val changeBtn: Button = findViewById(R.id.change_btn)
        val backBtn: Button = findViewById(R.id.back_btn)
        val premAkkBtn: Button = findViewById(R.id.prem_akk_btn)

        if(readGameSet(applicationContext).buttonsAnimation) {moveBtns(changeBtn, backBtn, premAkkBtn)}


        setUpProfileScreen()

        comeFr = intent.getStringExtra("comesFrom")

        premAkkBtn.setOnClickListener { goToPrem() }
    }

    private fun goToPrem() {
        val intent = Intent(this, PremShow::class.java)
        startActivity(intent)
        finish()
    }

    private fun setUpProfileScreen() {
        val person = readPerson(applicationContext)

        findViewById<ImageView>(R.id.profile_icon_view).setImageDrawable(getDrawable(getImageId(person.icon)))
        findViewById<TextView>(R.id.user_name_text).text = person.userName
        findViewById<TextView>(R.id.profile_level_text).text = getString(R.string.level_text, person.level)

        findViewById<TextView>(R.id.games_played_label).text = getString(R.string.games_played_str, person.gamesPlayed)
        findViewById<TextView>(R.id.fav_game_label).text = getString(R.string.favourite_game_str, person.favGame)
        findViewById<TextView>(R.id.max_exp_label).text = getString(R.string.max_exp_str, person.maxExp)
        findViewById<TextView>(R.id.time_in_game_label).text = getString(R.string.time_play_str, person.timeInGame/60, person.timeInGame%60)
        findViewById<TextView>(R.id.profile_exp_text).text = getString(R.string.profile_level_progress, person.exp , getExpForLevel(person.level))

        val levelBar : ProgressBar = findViewById(R.id.profile_level_bar)
        val percent = ((person.exp * 100) / getExpForLevel(person.level))*100
        levelBar.progress = percent
        //animateProgressBar(levelBar, percent, 0)
    }

    fun goBackToMain(view: View) {

        var inten = Intent()
        if (comeFr == "mainMenu" || comeFr == "prem") {
            inten = Intent(this, MainActivity::class.java)
        } else if (comeFr == "games") {
            inten = Intent(this, Games::class.java)
        }
        startActivity(inten)
        finish()
        return
    }


    private fun startGears() {
        animateGear(findViewById(R.id.left_blue_gear))
        animateGear(findViewById(R.id.left_green_gear))
        animateGear(findViewById(R.id.left_orange_gear))
        animateGear(findViewById(R.id.right_blue_gear))
        animateGear(findViewById(R.id.right_green_gear))
        animateGear(findViewById(R.id.right_orange_gear))
    }


    private fun moveBtns(changeBtn: Button, backBtn: Button, premBtn: Button) {
        val width = getScreenWidth()

        animateButtons(premBtn, width, 1500, 50)
        animateButtons(changeBtn, width, 1500, 100)
        animateButtons(backBtn, width, 1500, 150)

    }

    private fun getScreenWidth(): Float {
        val display: Display = windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        return size.x.toFloat()
    }

    fun goToIcons(view: View) {
        val intent = Intent(this, Icons::class.java)
        intent.putExtra("comesFrom", comeFr)
        startActivity(intent)
        finish()
    }
}
