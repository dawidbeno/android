package com.dejvid.matematika.views.fragments

import android.content.Context
import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment

import com.dejvid.matematika.R
import com.dejvid.matematika.R.*
import com.dejvid.matematika.models.Constants
import kotlinx.android.synthetic.main.activity_exercise.*
import kotlinx.android.synthetic.main.activity_exercise.view.*
import kotlinx.android.synthetic.main.fragment_choose_difficulty.view.*
import kotlinx.android.synthetic.main.choose_multiples.view.*
import kotlinx.android.synthetic.main.fragment_toolbar.view.*

private const val TYPE = "ExerciseType"

class ChooseDifficultyFragment : Fragment() {
    val TAG = "ChooseDifficultyFragment"

    private var exerciseType: Int = 0

    private var listenerDifficulty: OnFragmentDifficultyInteractionListener? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragmentNumeric
        val view : View = inflater.inflate(layout.fragment_choose_difficulty, container, false)

        arguments?.let {
            exerciseType = it.getInt(TYPE)
        }


        when(exerciseType){
            Constants.ADDITION.value -> {
                view.btnLvl1.background = resources.getDrawable(drawable.btn_round_plus)
                view.btnLvl2.background = resources.getDrawable(drawable.btn_round_plus)
                view.btnLvl3.background = resources.getDrawable(drawable.btn_round_plus)
                view.mainTitle.text = resources.getText(string.add_title)
                view.mainTitle.setBackgroundColor(resources.getColor(color.plusGreen))

            }
            Constants.SUBTRACTION.value -> {
                view.btnLvl1.background = resources.getDrawable(drawable.btn_round_minus)
                view.btnLvl2.background = resources.getDrawable(drawable.btn_round_minus)
                view.btnLvl3.background = resources.getDrawable(drawable.btn_round_minus)
                view.mainTitle.text = resources.getText(string.subtract_title)
                view.mainTitle.setBackgroundColor(resources.getColor(color.minusRed))
            }
            Constants.MULTIPLICATION.value -> {
                view.btnLvl1.background = resources.getDrawable(drawable.btn_round_multiply)
                view.btnLvl2.background = resources.getDrawable(drawable.btn_round_multiply)
                view.btnLvl3.background = resources.getDrawable(drawable.btn_round_multiply)
                view.mainTitle.text = resources.getText(string.multiply_title)
                view.mainTitle.setBackgroundColor(resources.getColor(color.multiplicateOrange))
            }
            Constants.DIVISION.value -> {
                view.btnLvl1.background = resources.getDrawable(drawable.btn_round_divide)
                view.btnLvl2.background = resources.getDrawable(drawable.btn_round_divide)
                view.btnLvl3.background = resources.getDrawable(drawable.btn_round_divide)
                view.mainTitle.text = resources.getText(string.divide_title)
                view.mainTitle.setBackgroundColor(resources.getColor(color.divideBlue))
            }
            Constants.COMPARISON.value -> {
                view.btnLvl1.background = resources.getDrawable(drawable.btn_round_compare)
                view.btnLvl2.background = resources.getDrawable(drawable.btn_round_compare)
                view.btnLvl3.background = resources.getDrawable(drawable.btn_round_compare)
                view.mainTitle.text = resources.getText(string.compare_title)
                view.mainTitle.setBackgroundColor(resources.getColor(color.comparePurple))
            }
        }

        view.btnLvl1.text = Constants.LEVEL_1.value.toString()
        view.btnLvl1.setOnClickListener {
            onButtonPressed(Constants.LEVEL_1.value)
        }

        view.btnLvl2.text = Constants.LEVEL_2.value.toString()
        view.btnLvl2.setOnClickListener {
            onButtonPressed(Constants.LEVEL_2.value)
        }

        view.btnLvl3.text = Constants.LEVEL_3.value.toString()
        view.btnLvl3.setOnClickListener {
            onButtonPressed(Constants.LEVEL_3.value)
        }

        if(exerciseType == Constants.MULTIPLICATION.value || exerciseType == Constants.DIVISION.value){
            view.exerciseTitle.visibility = View.VISIBLE
            view.numPicker.maxValue = 10
            view.numPicker.minValue = 2


            when(exerciseType){
                Constants.MULTIPLICATION.value -> {
                    view.multiplyStartBtn.background = resources.getDrawable(drawable.btn_round_multiply)
                    view.chooseMultiplyTitle.text = resources.getText(string.multiples)
                }
                Constants.DIVISION.value -> {
                    view.multiplyStartBtn.background = resources.getDrawable(drawable.btn_round_divide)
                    view.chooseMultiplyTitle.text = resources.getText(string.divides)
                }
            }

            view.multiplyStartBtn.setOnClickListener {
                var num : Int = view.numPicker.value
                num -= 1
                onButtonPressed(num) // make it different from case LEVEL_1 - under 10
            }
        }

        view.mainTitle.visibility = View.GONE


        return view
    }

//    override fun onCreateOptionsMenu(menu: Menu?, inflater: MenuInflater?) {
//        super.onCreateOptionsMenu(menu, inflater)
//        when(exerciseType){
//            Constants.ADDITION.value -> {
//                mytoolbar.awsmtoolbar.setBackgroundColor(resources.getColor(color.plusGreen))
//            }
//            Constants.SUBTRACTION.value -> {
//                mytoolbar.awsmtoolbar.setBackgroundColor(resources.getColor(color.minusRed))
//            }
//            Constants.MULTIPLICATION.value -> {
//                mytoolbar.awsmtoolbar.setBackgroundColor(resources.getColor(color.multiplicateOrange))
//            }
//            Constants.DIVISION.value -> {
//                mytoolbar.awsmtoolbar.setBackgroundColor(resources.getColor(color.divideBlue))
//            }
//            Constants.COMPARISON.value -> {
//                mytoolbar.awsmtoolbar.setBackgroundColor(resources.getColor(color.comparePurple))
//            }
//        }
//    }


    override fun onAttach(context: Context) {
        super.onAttach(context)
        if (context is OnFragmentDifficultyInteractionListener) {
            listenerDifficulty = context
        } else {
            throw RuntimeException("$context must implement OnFragmentDifficultyInteractionListener")
        }


    }

    override fun onDetach() {
        super.onDetach()
        listenerDifficulty = null
    }


    private fun onButtonPressed(lvl : Int) {
        listenerDifficulty?.onFragmentDifficultyInteraction(lvl)
    }

    /**
     * This interface must be implemented by activities that contain this
     * fragmentNumeric to allow an interaction in this fragmentNumeric to be communicated
     * to the activity and potentially other fragments contained in that
     * activity.
     */
    interface OnFragmentDifficultyInteractionListener {
        fun onFragmentDifficultyInteraction(lvl : Int)
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragmentNumeric using the provided parameters.
         *
         * @return A new instance of fragmentNumeric ChooseDifficultyFragment.
         */
        @JvmStatic
        fun newInstance(type : Int) = ChooseDifficultyFragment().apply {
            arguments = Bundle().apply {
                putInt(TYPE, type)
            }
        }
    }
}
