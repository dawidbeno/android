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
import com.dejvid.matematika.models.MySQLiteHelper
import com.dejvid.matematika.presenters.ComparisonExercisePresenter
import kotlinx.android.synthetic.main.exercise_comparison_exercise.*
import kotlinx.android.synthetic.main.exercise_comparison_exercise.view.*
import kotlinx.android.synthetic.main.exercise_stats.*
import kotlinx.android.synthetic.main.exercise_stats.view.*
import kotlinx.android.synthetic.main.exercise_title.*
import kotlinx.android.synthetic.main.fragment_choose_difficulty.view.*
import kotlinx.android.synthetic.main.fragment_solve_comparison_exercise.view.*
import kotlinx.android.synthetic.main.fragment_solve_comparison_exercise.view.mainTitle
import java.util.ArrayList

private const val DIFFICULTY = "difficulty"
private const val RECALCULATE = "Recalculate"

/**
 * A simple [Fragment] subclass.
 * Activities that contain this fragmentNumeric must implement the
 * [SolveComparisonExerciseFragment.OnFragmentSolveComparisonInteractionListener] interface
 * to handle interaction events.
 * Use the [SolveComparisonExerciseFragment.newInstance] factory method to
 * create an instance of this fragmentNumeric.
 */
class SolveComparisonExerciseFragment : Fragment(), ISolveView {

    val TAG = "SolveComparisonExerciseFragment"

    private lateinit var execPresenterCompar : ComparisonExercisePresenter
    private lateinit var mediaPlayerCorr : MediaPlayer
    private lateinit var mediaPlayerFail : MediaPlayer
    private lateinit var chronometer : Chronometer
    private var pauseTime : Long = 0
    private var isPaused : Boolean = false
    private var numBtns : Int = 3

    private var recalculate : Boolean = false
    private var difficulty: Int = 0
    private var listenerSolveComparison: OnFragmentSolveComparisonInteractionListener? = null
    private var db : MySQLiteHelper? = null

    private lateinit var btnsArray : ArrayList<Button>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            this.difficulty = it.getInt(DIFFICULTY)
            recalculate = it.getBoolean(RECALCULATE)
        }

        db = activity?.let { MySQLiteHelper(it, null,null,1) }
        execPresenterCompar = ComparisonExercisePresenter(this, db!!, this.recalculate)

        mediaPlayerCorr = MediaPlayer.create(activity, R.raw.correct_sound)
        mediaPlayerFail = MediaPlayer.create(activity, R.raw.wrong_sound)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragmentNumeric
        var view : View = inflater.inflate(R.layout.fragment_solve_comparison_exercise, container, false)

        view.btnGt.setOnClickListener {
            execPresenterCompar.solveTask(resources.getString(R.string.greaterThan))
        }

        view.btnEq.setOnClickListener {
            execPresenterCompar.solveTask(resources.getString(R.string.equal))
        }

        view.btnLt.setOnClickListener {
            execPresenterCompar.solveTask(resources.getString(R.string.lessThan))
        }

        btnsArray = ArrayList<Button>()
        btnsArray.add(view.btnGt)
        btnsArray.add(view.btnEq)
        btnsArray.add(view.btnLt)

        for(x in 0 until numBtns){
            btnsArray[x].background = resources.getDrawable(R.drawable.btn_round_compare)
        }

        view.mainTitle.text = resources.getText(R.string.compare_title)
        view.mainTitle.setBackgroundColor(resources.getColor(R.color.comparePurple))

        chronometer = view.chron

        chronometer.base = SystemClock.elapsedRealtime()
        chronometer.start()

        view.mainTitle.visibility = View.GONE
        return view
    }

    // Zastavenie chronometra
    override fun onStop() {
        super.onStop()
        pauseTime = chronometer.base - SystemClock.elapsedRealtime()
        chronometer.stop()
        execPresenterCompar.saveActualExerciseStatistic(
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
            execPresenterCompar.nextTask()
        }
    }

    fun onButtonPressed() {
        listenerSolveComparison?.onFragmentSolveComparisonInteraction()
    }

    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentSolveComparisonInteractionListener) {
            listenerSolveComparison = context
        } else {
            throw RuntimeException(context.toString() + " must implement OnFragmentSolveComparisonInteractionListener")
        }
    }

    override fun onDetach() {
        super.onDetach()
        listenerSolveComparison = null
    }

    override fun showTask(numA : String, numB : String){
        println("LOG: Zobrazenie tasku")

        compTaskTextMid.text = ""
        compTaskTextLeft.text = numA
        compTaskTextRight.text = numB
        enableBtns(true)
    }

    override fun showTask(task: String) {}

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

        compTaskTextMid.text = answer
        compTaskTextMid.setTextColor(ResourcesCompat.getColor(resources, R.color.green, null))

        playSound(true)
        Handler().postDelayed({
            execPresenterCompar.nextTask()
        }, 1300)
    }

    override fun showIncorrectSolution(answer : String){
        println("LOG: Nespravna odpoved")
        enableBtns(false)

        compTaskTextMid.text = answer
        compTaskTextMid.setTextColor(ResourcesCompat.getColor(resources, R.color.red, null))

        playSound(false)
        Handler().postDelayed({
            execPresenterCompar.nextTask()
        }, 1300)

    }

    override fun showAnswers(incorrect: IntArray, correct: Int) {
        println("LOG: Zobrazenie odpovedi na tlacitkach")
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
        for(x in 0 until numBtns){
            btnsArray[x].isEnabled = en
        }
    }

    fun getDifficulty() : Int {
        return this.difficulty
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragmentNumeric to allow an interaction in this fragmentNumeric to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     *
     *
     * See the Android Training lesson [Communicating with Other Fragments]
     * (http://developer.android.com/training/basics/fragments/communicating.html)
     * for more information.
     */
    interface OnFragmentSolveComparisonInteractionListener {
        fun onFragmentSolveComparisonInteraction()
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragmentNumeric using the provided parameters.
         *
         * @param lvl Parameter 1.
         * @return A new instance of fragmentNumeric SolveComparisonExerciseFragment.
         */
        @JvmStatic
        fun newInstance(lvl: Int, recalculate:Boolean) =
            SolveComparisonExerciseFragment().apply {
                arguments = Bundle().apply {
                    putInt(DIFFICULTY, lvl)
                    putBoolean(RECALCULATE, recalculate)
                    //putSerializable(DB, db)
                }
            }
    }
}
