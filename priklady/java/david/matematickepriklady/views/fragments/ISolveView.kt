package com.dejvid.matematika.views.fragments

interface ISolveView {

    fun showTask(task : String)

    fun showTask(numA : String, numB : String)

    fun updateStatistic(ans : Boolean)

    fun showCorrectSolution(answer : String)

    fun showIncorrectSolution(answer : String)

    fun showAnswers(incorrect : IntArray, correct : Int)


}