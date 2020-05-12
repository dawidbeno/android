package com.dejvid.matematika.views.activities

import android.content.Context
import androidx.appcompat.app.AppCompatActivity

import android.os.Bundle
import android.view.Menu
import android.view.View
import androidx.fragment.app.Fragment
import com.dejvid.matematika.R
import com.dejvid.matematika.models.Constants
import com.dejvid.matematika.models.MySQLiteHelper
import com.dejvid.matematika.views.fragments.ChooseDifficultyFragment
import com.dejvid.matematika.views.fragments.SolveComparisonExerciseFragment
import com.dejvid.matematika.views.fragments.SolveNumericExerciseFragment
import kotlinx.android.synthetic.main.activity_exercise.*
import kotlinx.android.synthetic.main.fragment_choose_difficulty.*
import kotlinx.android.synthetic.main.fragment_toolbar.*
import kotlinx.android.synthetic.main.fragment_toolbar.view.*

/**
 * Aktivita, ktora zastresuje vyber obtiaznosti a riesenie prikladov.
 * Obsahuje 2 fragmenty, jeden pre kazdy ukon.
 */
class ExerciseActivity : AppCompatActivity(),
    ChooseDifficultyFragment.OnFragmentDifficultyInteractionListener,
    SolveNumericExerciseFragment.OnFragmentSolveNumericInteractionListener,
    SolveComparisonExerciseFragment.OnFragmentSolveComparisonInteractionListener{

    var TAG = "ExerciseActivity"
    var exerciseType = 0
    var recalculate = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_exercise)

        intent.extras?.let {
            exerciseType = it.getInt("ExerciseType")
            recalculate = it.getBoolean("Recalculate")

        } ?: run {
            println("Error: Nezadany typ prikladov")
        }

        when(exerciseType){
            Constants.ADDITION.value -> {
                frame_layout.background = resources.getDrawable(R.color.bg_plusGreen)
                toolbar_title.text = resources.getText(R.string.add_title)
                mytoolbar.setBackgroundColor(resources.getColor(R.color.plusGreen))
            }
            Constants.SUBTRACTION.value -> {
                frame_layout.background = resources.getDrawable(R.color.bg_minusRed)
                toolbar_title.text = resources.getText(R.string.subtract_title)
                mytoolbar.setBackgroundColor(resources.getColor(R.color.minusRed))
            }
            Constants.MULTIPLICATION.value -> {
                frame_layout.background = resources.getDrawable(R.color.bg_multiplicateOrange)
                toolbar_title.text = resources.getText(R.string.multiply_title)
                mytoolbar.setBackgroundColor(resources.getColor(R.color.multiplicateOrange))
            }
            Constants.DIVISION.value -> {
                frame_layout.background = resources.getDrawable(R.color.bg_divideBlue)
                toolbar_title.text = resources.getText(R.string.divide_title)
                mytoolbar.setBackgroundColor(resources.getColor(R.color.divideBlue))
            }
            Constants.COMPARISON.value -> {
                frame_layout.background = resources.getDrawable(R.color.bg_comparePurple)
                toolbar_title.text = resources.getText(R.string.compare_title)
                mytoolbar.setBackgroundColor(resources.getColor(R.color.comparePurple))
            }
        }

        if(recalculate){
            if(this.exerciseType != Constants.COMPARISON.value){
                switchToSolveNumericExerciseFragment(Constants.LEVEL_1.value, exerciseType)

            } else {
                switchToSolveComparisonExerciseFragment(Constants.LEVEL_1.value)
            }
        } else {
            switchToChooseDiffFragment()
        }

        toolbar_back.setOnClickListener {
            this.onBackPressed()
        }

    }


    private fun switchToChooseDiffFragment(){
        replaceFragment(R.id.frame_layout, ChooseDifficultyFragment.newInstance(this.exerciseType), "ChooseDifficultyFragment")
    }

    /**
     * @param lvl Urcuje uroven prikladov. Hodnota je ziskana pri vybere obtiaznosti
     */
    private fun switchToSolveNumericExerciseFragment(lvl : Int, type:Int){
        replaceFragment(R.id.frame_layout, SolveNumericExerciseFragment.newInstance(lvl, type, this.recalculate), "SolveNumericExerciseFragment")
    }

    private fun switchToSolveComparisonExerciseFragment(lvl : Int){
        replaceFragment(R.id.frame_layout, SolveComparisonExerciseFragment.newInstance(lvl, this.recalculate), "SolveComparisonExerciseFragment")
    }

    private fun replaceFragment(id: Int, fragment: Fragment, tag: String){
        supportFragmentManager
            .beginTransaction()
            .replace(id, fragment, tag)
            .commit()
    }


    /*
        Overridnute metody z interfacov z fragmentov
     */
    override fun onFragmentDifficultyInteraction(lvl : Int) {

        if(this.exerciseType != Constants.COMPARISON.value){
            switchToSolveNumericExerciseFragment(lvl, exerciseType)

        } else {
            switchToSolveComparisonExerciseFragment(lvl)
        }
    }

    override fun onFragmentSolveNumericInteraction() {
        this.onBackPressed()
    }

    override fun onFragmentSolveComparisonInteraction() {
        this.onBackPressed()
    }

}
