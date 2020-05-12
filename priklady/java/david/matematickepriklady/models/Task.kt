package com.dejvid.matematika.models

import android.provider.ContactsContract

class Task : ITask{


    private var id = 0
    private var a = 0
    private var b = 0
    private var exerciseType = 0
    private var difficulty = 0
    private var limit = 0
    private var wrongAnswers : IntArray? = null
    private var result : String = ""


    fun setID(id : Int){
        this.id = id
    }

    fun getID() : Int {
        return this.id
    }

    override fun setA(a : Int){
        this.a = a
    }

    override fun getA() : Int{
        return this.a
    }

    override fun setB(b : Int){
        this.b = b
    }

    override fun getB() : Int{
        return this.b
    }

    override fun setExerciseType(type: Int) {
        this.exerciseType = type
    }

    override fun getExerciseType(): Int {
        return this.exerciseType
    }

    override fun setDifficulty(diff: Int) {
        this.difficulty = diff
    }

    override fun getDifficulty(): Int {
       return this.difficulty
    }

    override fun setLimit(limit: Int) {
        this.limit = limit
    }

    override fun getLimit(): Int {
        return this.limit
    }

    override fun setResult(res: String) {
        this.result = res
    }

    override fun getResult() : String{
        return this.result
    }

    fun getWrongAnswers(): IntArray? {
        if(this.wrongAnswers != null){
            return this.wrongAnswers
        } else {
            return null
        }
    }

    fun setWrongAnswers(wa : IntArray){
        this.wrongAnswers = wa
    }

    override fun toString(): String {
        super.toString()
        var result = this.a.toString()

        when(this.exerciseType) {
            Constants.ADDITION.value -> result += " + "
            Constants.SUBTRACTION.value -> result += " - "
            Constants.MULTIPLICATION.value -> result += " * "
            Constants.DIVISION.value -> result += " : "
            Constants.COMPARISON.value -> result += "  ?  "
        }

        result += this.b.toString()

        if(this.exerciseType != Constants.COMPARISON.value) { result += " = " }

        return result
    }

}