package com.developer.rrd_projects.mindgames

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.view.View
import android.widget.EditText
import com.developer.rrd_projects.mindgames.animators.animateGear
import com.developer.rrd_projects.mindgames.person.Person
import com.developer.rrd_projects.mindgames.person.readPerson
import com.developer.rrd_projects.mindgames.person.writePerson

class NameChooser : AppCompatActivity() {

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


        setContentView(R.layout.activity_name_choser)
        startGears()
    }

    private fun startGears(){
        animateGear(findViewById(R.id.left_blue_gear))
        animateGear(findViewById(R.id.left_green_gear))
        animateGear(findViewById(R.id.left_orange_gear))
        animateGear(findViewById(R.id.right_blue_gear))
        animateGear(findViewById(R.id.right_green_gear))
        animateGear(findViewById(R.id.right_orange_gear))
    }

    fun createPerson(view: View){
        val name:EditText = findViewById(R.id.editPersonName)
        val n:String = name.text.toString()


        if(n != ""){
            val person = readPerson(applicationContext)
            person.userName = n
            writePerson(person, applicationContext)
        }

        val intent = Intent(this, MainActivity::class.java)
        startActivity(intent)
        finish()
    }
}
