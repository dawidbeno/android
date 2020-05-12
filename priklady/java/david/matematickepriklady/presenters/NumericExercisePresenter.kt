package com.dejvid.matematika.presenters

import com.dejvid.matematika.models.Generator
import com.dejvid.matematika.models.MySQLiteHelper
import com.dejvid.matematika.models.Task
import com.dejvid.matematika.views.fragments.SolveNumericExerciseFragment
import java.util.*


/**
 * Presenter obsahuje stack taskov, ktory pop-uje ked si view vypyta novu ulohu
 * Ak je stack prazdny, tak sa vyziada vygenerovanie novej ulohy - resp. moze sa vygenerovat niekolko uloh naviac.
 *
 * Ak sa pocitaju zle vyriesene tasky, tak tie, co sa nedostanu zo stacku, tak sa ulozia spat do pamate
 */
class NumericExercisePresenter(view : SolveNumericExerciseFragment, db : MySQLiteHelper, recalculate:Boolean) : IExercisePresenter {

    private var actTask : Task? = null
    private var tasks : Stack<Task> = Stack()
    private var generator = Generator()
    private var storage : MySQLiteHelper
    private var fragmentNumeric : SolveNumericExerciseFragment
    private var type : Int = 0
    private var difficulty : Int = 0
    private var limit : Int = 0
    private var recalculate : Boolean = false

    init {
        println("Vytvoreny presenter pre view: " + view.TAG)
        this.fragmentNumeric = view
        this.type = this.fragmentNumeric.getExerciseType()
        this.difficulty = this.fragmentNumeric.getDifficulty()
        this.limit = this.fragmentNumeric.getLimit()
        this.storage = db
        this.recalculate = recalculate

        if(recalculate){
            tasks = this.storage.getTasks()!!
        }else {
            tasks = generator.generateTasks(10, this.type, this.difficulty, this.limit)
            this.storage.deleteAllTasks()
        }
    }


    /**
     * Nacitanie jedneho tasku z poolu taskov, ktore boli ziskane z Modelu
     */
    override fun nextTask() : Task{
        var task = Task()
        task.setID(-1)

        if (tasks.size == 0 && !this.recalculate) {
            tasks = generator.generateTasks(10, this.type, this.difficulty, this.limit)
            task = tasks.pop()
        }
        else if(tasks.size > 0 && !this.recalculate){
            task = tasks.pop()
        }
        else if(tasks.size == 0 && this.recalculate){
            this.fragmentNumeric.onButtonPressed()
        }
        else if(tasks.size > 0 && this.recalculate){
            task = tasks.pop()
            task.setWrongAnswers(this.generator.generateWrongAnswers(task)!!)
        }

        if(task.getID() != -1) {
            this.actTask = task
        } else {
            this.fragmentNumeric.onButtonPressed()
        }

        fragmentNumeric.showTask(task.toString())      // zobrazenie tasku
        task.getWrongAnswers()?.let { fragmentNumeric.showAnswers(it, task.getResult().toInt()) }

        return task
    }

    override fun solveTask(answer : String) {
        println("LOG: Solve task")

        if(recalculate) {
            this.actTask?.getID()?.let { this.storage.deleteTask(it) }
        }

        if(answer.toInt() == this.actTask?.getResult()?.toInt()){
            fragmentNumeric.showCorrectSolution(answer)
            fragmentNumeric.updateStatistic(true)

        } else {
            fragmentNumeric.showIncorrectSolution(answer)
            fragmentNumeric.updateStatistic(false)
            saveWrongSolvedTask()
        }
    }

    override fun saveActualExerciseStatistic(all:Int, corr:Int, fail:Int, elapsedTime:String){
        this.storage.saveLastExercise(all, corr, fail, elapsedTime, this.type)
        val arr = this.storage.getAllStats()
        println("celkovo"+arr[0])
    }

    override fun saveWrongSolvedTask(){
        this.storage.saveTask(this.actTask)
    }

    /**
     * Nacitanie zle vypocitanych taskov z pamate do stacku
     */
    override fun loadWrongTasks(){
        this.tasks = storage.getTasks()!!
    }

}