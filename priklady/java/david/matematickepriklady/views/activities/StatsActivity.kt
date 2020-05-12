package com.dejvid.matematika.views.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import android.widget.TextView
import com.dejvid.matematika.R
import com.dejvid.matematika.models.Constants
import com.dejvid.matematika.models.MySQLiteHelper
import kotlinx.android.synthetic.main.activity_stats.*
import kotlinx.android.synthetic.main.fragment_toolbar.*
import kotlinx.android.synthetic.main.fragment_toolbar.view.*
import kotlinx.android.synthetic.main.stast_overall.*
import kotlinx.android.synthetic.main.stats_last.*
import kotlinx.android.synthetic.main.stats_wrong_exercises.*

class StatsActivity : AppCompatActivity() {

    private lateinit var storage : MySQLiteHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_stats)

        val storage = MySQLiteHelper(this, null, null, 1)
        this.storage = storage

        val arrAll = storage.getAllStats()
        textView24.text = arrAll[0]
        textView26.text = arrAll[1]
        textView28.text = arrAll[2]

        val arrLast = storage.getLastExerciseStats()
        textView14.text = arrLast[4]
        textView18.text = arrLast[0]
        textView20.text = arrLast[1]
        textView22.text = arrLast[2]

        val b = Bundle()
        b.putBoolean("Recalculate", true)
        when(arrLast[3]){
            Constants.ADDITION.value.toString() -> {
                textView11.text = resources.getText(R.string.addition)
                btnRecalculate.background = resources.getDrawable(R.drawable.btn_round_plus)
                statstoolbar.setBackgroundColor(resources.getColor(R.color.plusGreen))
                b.putInt("ExerciseType", Constants.ADDITION.value)
            }
            Constants.SUBTRACTION.value.toString() -> {
                textView11.text = resources.getText(R.string.subtraction)
                btnRecalculate.background = resources.getDrawable(R.drawable.btn_round_minus)
                statstoolbar.setBackgroundColor(resources.getColor(R.color.minusRed))
                b.putInt("ExerciseType", Constants.SUBTRACTION.value)
            }
            Constants.MULTIPLICATION.value.toString() -> {
                textView11.text = resources.getText(R.string.multiplication)
                //btnRecalculate.setBackgroundColor(resources.getColor(R.color.multiplicateOrange))
                btnRecalculate.background = resources.getDrawable(R.drawable.btn_round_multiply)
                statstoolbar.setBackgroundColor(resources.getColor(R.color.multiplicateOrange))
                b.putInt("ExerciseType", Constants.MULTIPLICATION.value)
            }
            Constants.DIVISION.value.toString() -> {
                textView11.text = resources.getText(R.string.division)
                //btnRecalculate.setBackgroundColor(resources.getColor(R.color.divideBlue))
                btnRecalculate.background = resources.getDrawable(R.drawable.btn_round_divide)
                statstoolbar.setBackgroundColor(resources.getColor(R.color.divideBlue))
                b.putInt("ExerciseType", Constants.DIVISION.value)
            }
            Constants.COMPARISON.value.toString() -> {
                textView11.text = resources.getText(R.string.comparision)
                //btnRecalculate.setBackgroundColor(resources.getColor(R.color.comparePurple))
                btnRecalculate.background = resources.getDrawable(R.drawable.btn_round_compare)
                statstoolbar.setBackgroundColor(resources.getColor(R.color.comparePurple))
                b.putInt("ExerciseType", Constants.COMPARISON.value)
            }
            else -> textView11.text = ""
        }

        statstoolbar.toolbar_title.text = resources.getText(R.string.stats_title)

        btnRecalculate.setOnClickListener {
            val intent = Intent(this, ExerciseActivity::class.java)
            intent.putExtras(b)
            startActivity(intent)
        }



        toolbar_back.setOnClickListener {
            this.onBackPressed()
        }

    }

    override fun onResume() {
        super.onResume()
        loadWrongExercises()
    }

    override fun onPause() {
        super.onPause()
        wrongAnswers.removeAllViews()
    }

    private fun loadWrongExercises(){
        val tasks = storage.getTasks()
        if (tasks != null && tasks.isNotEmpty()) {
            tasks.forEach {
                val txtView = TextView(this)
                val lp = LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT)
                lp.setMargins(0,20,0,0)
                txtView.layoutParams = lp
                txtView.text = it.toString()
                txtView.textAlignment = View.TEXT_ALIGNMENT_CENTER
                txtView.textSize = 25F
                wrongAnswers.addView(txtView,0)
            }
            btnRecalculate.isEnabled = true
            btnRecalculate.setTextColor(resources.getColor(R.color.white))
        }
        if (tasks != null && tasks.isEmpty()) {
            btnRecalculate.isEnabled = false
            btnRecalculate.setTextColor(resources.getColor(R.color.grey))
        }

    }

}
