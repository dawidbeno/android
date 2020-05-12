package com.dejvid.matematika.views.activities

import android.content.Intent
import android.hardware.display.DisplayManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Display
import android.view.WindowManager
import com.dejvid.matematika.R
import com.dejvid.matematika.models.Constants
import kotlinx.android.synthetic.main.activity_exercise.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.fragment_toolbar.*
import kotlinx.android.synthetic.main.main_menu_addition.*
import kotlinx.android.synthetic.main.main_menu_comparison.*
import kotlinx.android.synthetic.main.main_menu_division.*
import kotlinx.android.synthetic.main.main_menu_multiplication.*
import kotlinx.android.synthetic.main.main_menu_substraction.*
import kotlin.random.Random

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val b = Bundle()

        btn_menu_add.setOnClickListener {
            val intent = Intent(this, ExerciseActivity::class.java)
            b.putInt("ExerciseType", Constants.ADDITION.value)
            intent.putExtras(b)
            startActivity(intent)
            //this.finish()
        }

        btn_menu_sub.setOnClickListener {
            val intent = Intent(this, ExerciseActivity::class.java)
            b.putInt("ExerciseType", Constants.SUBTRACTION.value)
            intent.putExtras(b)
            startActivity(intent)
            //this.finish()
        }

        btn_menu_mul.setOnClickListener {
            val intent = Intent(this, ExerciseActivity::class.java)
            b.putInt("ExerciseType", Constants.MULTIPLICATION.value)
            intent.putExtras(b)
            startActivity(intent)
            //this.finish()
        }

        btn_menu_div.setOnClickListener {
            val intent = Intent(this, ExerciseActivity::class.java)
            b.putInt("ExerciseType", Constants.DIVISION.value)
            intent.putExtras(b)
            startActivity(intent)
            //this.finish()
        }

        btn_menu_com.setOnClickListener {
            val intent = Intent(this, ExerciseActivity::class.java)
            b.putInt("ExerciseType", Constants.COMPARISON.value)
            intent.putExtras(b)
            startActivity(intent)
            //this.finish()
        }

        btnStats.setOnClickListener {
            val intent = Intent(this, StatsActivity::class.java)
            startActivity(intent)
        }

    }

}


