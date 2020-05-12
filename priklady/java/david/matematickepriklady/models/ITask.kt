package com.dejvid.matematika.models

interface ITask {

    override fun toString() : String

    fun getResult() : String

    fun setResult(answ : String)

    fun setExerciseType(type : Int)

    fun getExerciseType() : Int

    fun setDifficulty(diff : Int)

    fun getDifficulty() : Int

    fun setLimit(limit : Int)

    fun getLimit() : Int

    fun setA(a : Int)

    fun getA() : Int

    fun setB(b : Int)

    fun getB() : Int
}