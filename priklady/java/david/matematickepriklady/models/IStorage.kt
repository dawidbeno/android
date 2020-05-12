package com.dejvid.matematika.models

import java.util.*
import kotlin.collections.ArrayList

interface IStorage {

    fun saveLastExercise(all:Int, corr:Int, fail:Int, elapsedTime:String, type:Int)

    fun getLastExerciseStats() : ArrayList<String>

    fun getAllStats() : ArrayList<String>

    fun saveTask(actTask: Task?)

    fun getTasks() : Stack<Task>?

    fun deleteTask(id : Int) : Boolean

}