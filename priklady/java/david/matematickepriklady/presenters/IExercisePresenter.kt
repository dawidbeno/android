package com.dejvid.matematika.presenters

import com.dejvid.matematika.models.Task

interface IExercisePresenter {

    fun nextTask() : Task

    fun solveTask(answer : String)

    fun saveActualExerciseStatistic(all:Int, corr:Int, fail:Int, elapsedTime:String)

    fun saveWrongSolvedTask()

    fun loadWrongTasks()
}