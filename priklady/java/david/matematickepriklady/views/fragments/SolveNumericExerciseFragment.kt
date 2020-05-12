package com.dejvid.matematika.views.fragments

import android.content.Context
import android.media.MediaPlayer
import android.os.Bundle
import android.os.Handler
import android.os.SystemClock
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.Chronometer
import androidx.core.content.res.ResourcesCompat

import com.dejvid.matematika.R
import com.dejvid.matematika.models.Constants
import com.dejvid.matematika.models.MySQLiteHelper
import com.dejvid.matematika.presenters.NumericExercisePresenter
import kotlinx.android.synthetic.main.exercise_numeric_exercise.*
import kotlinx.android.synthetic.main.exercise_numeric_exercise.view.*
import kotlinx.android.synthetic.main.exercise_stats.*
import kotlinx.android.synthetic.main.exercise_stats.view.*
import kotlinx.android.synthetic.main.fragment_choose_difficulty.view.*
import kotlinx.android.synthetic.main.fragment_solve_numeric_exercise.view.*
import kotlinx.android.synthetic.main.fragment_solve_numeric_exercise.view.mainTitle
import java.util.ArrayList
import kotlin.random.Random


private const val DIFFICULTY = "difficulty"
private const val TYPE = "ExerciseType"
private const val RECALCULATE = "Recalculate"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragmentNumeric must implement the
 * [SolveNumericExerciseFragment.OnFragmentSolveNumericInteractionListener] interface
 * to handle interaction events.
 * Use the [SolveNumericExerciseFragment.newInstance] factory method to
 * create an instance of this fragmentNumeric.
 */
class SolveNumericExerciseFragment : Fragment(), ISolveView {

    val TAG = "SolveNumericExerciseFragment"
    val BTNS_NUM = Constants.BTNS_NUM.value

    private lateinit var execPresenterNumeric : NumericExercisePresenter
    private var db : MySQLiteHelper? = null

    private lateinit var mediaPlayerCorr : MediaPlayer
    private lateinit var mediaPlayerFail : MediaPlayer
    private lateinit var chronometer: Chronometer
    private var pauseTime : Long = 0
    private var isPaused : Boolean = false

    private var recalculate : Boolean = false
    private var difficulty: Int = 0
    private var limit : Int = 0
    private var exerciseType: Int = 0
    private var listenerSolveNumeric: OnFragmentSolveNumericInteractionListener? = null

    private lateinit var btnsArray : ArrayList<Button>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            difficulty = it.getInt(DIFFICULTY)
            exerciseType = it.getInt(TYPE)
            recalculate = it.getBoolean(RECALCULATE)
        }
        db = activity?.let {
            MySQLiteHelper(it, null,null,1)
        }

        if(difficulty < 10) {
            this.limit = difficulty+1
            this.difficulty = Constants.LEVEL_3.value
        }

        execPresenterNumeric = NumericExercisePresenter(this, db!!, this.recalculate)
        mediaPlayerCorr = MediaPlayer.create(activity, R.raw.correct_sound)
        mediaPlayerFail = MediaPlayer.create(activity, R.raw.wrong_sound)
    }

    override fun onCreateView( inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        var view : View = inflater.inflate(R.layout.fragment_solve_numeric_exercise, container, false)

        btnsArray = ArrayList<Button>()
        btnsArray.add(view.btnAns1)
        btnsArray.add(view.btnAns2)
        btnsArray.add(view.btnAns3)
        btnsArray.add(view.btnAns4)
        btnsArray.add(view.btnAns5)
        btnsArray.add(view.btnAns6)

        for(x in 0 until BTNS_NUM){
            btnsArray[x].setOnClickListener {
                enableBtns(false)
                execPresenterNumeric.solveTask(btnsArray[x].text.toString())
            }
            when(exerciseType){
                Constants.ADDITION.value -> {
                    btnsArray[x].background = resources.getDrawable(R.drawable.btn_round_plus)
                }
                Constants.SUBTRACTION.value -> {
                    btnsArray[x].background = resources.getDrawable(R.drawable.btn_round_minus)
                }
                Constants.MULTIPLICATION.value -> {
                    btnsArray[x].background = resources.getDrawable(R.drawable.btn_round_multiply)
                }
                Constants.DIVISION.value -> {
                    btnsArray[x].background = resources.getDrawable(R.drawable.btn_round_divide)
                }
            }
        }

        when(exerciseType){
            Constants.ADDITION.value -> {
                view.mainTitle.text = resources.getText(R.string.add_title)
                view.mainTitle.setBackgroundColor(resources.getColor(R.color.plusGreen))
            }
            Constants.SUBTRACTION.value -> {
                view.mainTitle.text = resources.getText(R.string.subtract_title)
                view.mainTitle.setBackgroundColor(resources.getColor(R.color.minusRed))
            }
            Constants.MULTIPLICATION.value -> {
                view.mainTitle.text = resources.getText(R.string.multiply_title)
                view.mainTitle.setBackgroundColor(resources.getColor(R.color.multiplicateOrange))
            }
            Constants.DIVISION.value -> {
                view.mainTitle.text = resources.getText(R.string.divide_title)
                view.mainTitle.setBackgroundColor(resources.getColor(R.color.divideBlue))
            }
        }

        chronometer = view.chron

        chronometer.base = SystemClock.elapsedRealtime()
        chronometer.start()

        view.mainTitle.visibility = View.GONE

        return view
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)

        if (context is OnFragmentSolveNumericInteractionListener) {
            listenerSolveNumeric = context
        } else {
            throw RuntimeException("$context must implement OnFragmentSolveNumericInteractionListener")
        }
    }

    // Zastavenie chronometra
    override fun onStop() {
        super.onStop()
        pauseTime = chronometer.base - SystemClock.elapsedRealtime()
        chronometer.stop()
        execPresenterNumeric.saveActualExerciseStatistic(
            allTasks.text.toString().toInt(),
            corrTasks.text.toString().toInt(),
            failTasks.text.toString().toInt(),
            chronometer.text.toString()
        )
    }

    // Pauznutie chronometra
    override fun onPause() {
        super.onPause()
        this.pauseTime = SystemClock.elapsedRealtime()
        this.isPaused = true
    }

    // Pokracovanie chronometra
    override fun onResume() {
        super.onResume()
        if (isPaused){
            chronometer.base = SystemClock.elapsedRealtime() + pauseTime
            chronometer.start()
            isPaused = false
        } else {
            execPresenterNumeric.nextTask()
        }
    }

    override fun onDetach() {
        super.onDetach()
        listenerSolveNumeric = null
    }


    fun onButtonPressed() {
        listenerSolveNumeric?.onFragmentSolveNumericInteraction()
    }


    override fun showTask(task : String){
        println("LOG: Zobrazenie tasku")

        exerAns.text = ""
        exerTaskTxtView.text = task
    }


    override fun showTask(numA: String, numB: String) {}

    override fun updateStatistic(ans : Boolean){
        println("LOG: Zmena statistiky")

        var num : Int = allTasks.text.toString().toInt()
        num += 1
        allTasks.text = num.toString()

        if(ans){
            num = corrTasks.text.toString().toInt()
            num += 1
            corrTasks.text = num.toString()

        } else {
            num = failTasks.text.toString().toInt()
            num += 1
            failTasks.text = num.toString()

        }
    }

    override fun showCorrectSolution(answer : String){
        println("LOG: Spravna odpoved")
        enableBtns(false)

        exerAns.text = answer
        exerAns.setTextColor(ResourcesCompat.getColor(resources, R.color.green, null))

        playSound(true)
        Handler().postDelayed({
            execPresenterNumeric.nextTask()
        }, 1300)
    }

    override fun showIncorrectSolution(answer : String){
        println("LOG: Nespravna odpoved")
        enableBtns(false)

        exerAns.text = answer
        exerAns.setTextColor(ResourcesCompat.getColor(resources, R.color.red, null))

        playSound(false)
        Handler().postDelayed({
            execPresenterNumeric.nextTask()
        }, 1300)
    }

    override fun showAnswers(incorrect : IntArray, correct : Int){
        println("LOG: Zobrazenie odpovedi na tlacitkach")

        for(x in 0 until BTNS_NUM){
            btnsArray[x].text = incorrect[x].toString()
        }
        var x = Random.nextInt(6)
        btnsArray[x].text = correct.toString()

        enableBtns(true)
    }


    private fun playSound(type : Boolean){
        println("LOG: Prehranie zvuku")

        if (type) {
            mediaPlayerCorr.start()
        } else {
            mediaPlayerFail.start()
        }

    }

    private fun enableBtns(en:Boolean){
        for(x in 0 until BTNS_NUM){
            btnsArray[x].isEnabled = en
        }
    }

    fun getExerciseType() : Int {
        return this.exerciseType
    }

    fun getDifficulty() : Int {
        return this.difficulty
    }

    fun getLimit() : Int {
        return this.limit
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragmentNumeric to allow an interaction in this fragmentNumeric to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    interface OnFragmentSolveNumericInteractionListener {
        fun onFragmentSolveNumericInteraction()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragmentNumeric using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @return A new instance of fragmentNumeric SolveNumericExerciseFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: Int, type:Int, recalculate:Boolean) =
            SolveNumericExerciseFragment().apply {
                arguments = Bundle().apply {
                    putInt(DIFFICULTY, param1)
                    putInt(TYPE, type)
                    putBoolean(RECALCULATE, recalculate)
                    //putSerializable(DB, db)
                }
            }
    }
}
