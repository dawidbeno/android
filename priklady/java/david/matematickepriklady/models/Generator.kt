package com.dejvid.matematika.models

import java.util.*
import kotlin.random.Random

class Generator {

    init {
        println("Vytvoreny generator")

    }


    fun generateTasks(numTasks : Int, type : Int, difficulty : Int, limit : Int) : Stack<Task> {
        var tasks : Stack<Task> = Stack()

        for(x in 0 until numTasks) {
            var task = Task()

            task.setExerciseType(type)
            task.setDifficulty(difficulty)
            task.setLimit(limit)

            when (type) {
                Constants.ADDITION.value -> {
                    do {
                        task.setA(Random.nextInt(0, difficulty))
                        task.setB(Random.nextInt(0, difficulty))
                    } while (task.getA() + task.getB() > task.getDifficulty())

                    task.setResult((task.getA() + task.getB()).toString())
                }

                Constants.SUBTRACTION.value -> {
                    do {
                        task.setA(Random.nextInt(0, difficulty))
                        task.setB(Random.nextInt(0, difficulty))
                    } while (task.getA() - task.getB() < 0)
                    task.setResult((task.getA() - task.getB()).toString())
                }
                Constants.MULTIPLICATION.value ->{
                    if(task.getLimit() > 0){
                        task.setA(Random.nextInt(0, 10))
                        task.setB(task.getLimit())
                    } else {
                        do{
                            task.setA(Random.nextInt(0, 10))
                            task.setB(Random.nextInt(0, 10))
                        } while (task.getA() * task.getB() > task.getDifficulty())
                    }
                    task.setResult((task.getA() * task.getB()).toString())
                }
                Constants.DIVISION.value ->{
                    if(task.getLimit() > 0){
                        task.setB(task.getLimit())
                        do {
                            task.setA(Random.nextInt(0, task.getLimit()*10))
                        } while ((task.getA() % task.getB()) != 0)
                        task.setResult((task.getA() / task.getB()).toString())
                    } else {
                        do {
                            task.setA(Random.nextInt(0, difficulty))
                            task.setB(Random.nextInt(1, 10))
                        } while(task.getA() % task.getB() != 0)
                    }
                    task.setResult((task.getA() / task.getB()).toString())
                }
                Constants.COMPARISON.value -> {
                    task.setA(Random.nextInt(0, difficulty))
                    task.setB(Random.nextInt(0, difficulty))
                    if (task.getA() > task.getB()) task.setResult(">")
                    if (task.getA() == task.getB()) task.setResult("=")
                    if (task.getA() < task.getB()) task.setResult("<")
                }
            }


            if (type != Constants.COMPARISON.value) {
                generateWrongAnswers(task)?.let { task.setWrongAnswers(it) }
            }

            tasks.push(task)
        }

        return tasks
    }

    fun generateWrongAnswers(task : Task) : IntArray?{
        var x = 0
        var n = 0
        //var h = if (task.getLimit()*10 > task.getDifficulty()) task.getLimit()*10 else task.getDifficulty()
        //var h = if(task.getLimit() > 1) task.getLimit()*12 else task.getDifficulty()

        var h = task.getDifficulty()
        if(task.getLimit() > 1 && task.getExerciseType() == Constants.MULTIPLICATION.value){
            h = task.getLimit()*12
        } else if (task.getLimit() > 1 && task.getExerciseType() == Constants.DIVISION.value){
            h = task.getLimit()+12
        }

        val wrongAnswers = IntArray(Constants.BTNS_NUM.value)
        while (x != Constants.BTNS_NUM.value){
            n = Random.nextInt(0, h)
            if( n == task.getResult().toInt() || wrongAnswers.contains(n) || n > Constants.LEVEL_3.value) continue
            //if( wrongAnswers[x] == task.getResult().toInt() || wrongAnswers.contains(n)) continue
            wrongAnswers[x] = n
            x++
        }
        return wrongAnswers
    }


}