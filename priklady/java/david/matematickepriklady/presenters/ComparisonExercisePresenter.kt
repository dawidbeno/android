package com.dejvid.matematika.presenters

import com.dejvid.matematika.models.Constants
import com.dejvid.matematika.models.Generator
import com.dejvid.matematika.models.MySQLiteHelper
import com.dejvid.matematika.models.Task
import com.dejvid.matematika.views.fragments.SolveComparisonExerciseFragment
import java.util.*

class ComparisonExercisePresenter(view : SolveComparisonExerciseFragment, db : MySQLiteHelper, recalculate:Boolean) : IExercisePresenter {

    private var actTask : Task? = null
    private var tasks : Stack<Task> = Stack()
    private var generator = Generator()
    private var difficulty : Int = 0
    private var storage : MySQLiteHelper
    private var fragmentComparison : SolveComparisonExerciseFragment
    private var recalculate : Boolean = false

    init {
        println("Vytvoreny presenter pre view: " + view.TAG)
        this.fragmentComparison = view
        //this.type = this.fragmentNumeric.getExerciseType()
        this.difficulty = this.fragmentComparison.getDifficulty()
        this.storage = db
        this.recalculate = recalculate

        if(recalculate){
            tasks = this.storage.getTasks()!!
        }else{
            tasks = generator.generateTasks(10, Constants.COMPARISON.value, this.difficulty, 0)
            this.storage.deleteAllTasks()
        }

    }


    override fun nextTask(): Task {
        var task = Task()
        task.setID(-1)

        if (tasks.size == 0 && !this.recalculate){
            tasks = generator.generateTasks(10, Constants.COMPARISON.value, this.difficulty, 0)
            task = tasks.pop()
        } else if(tasks.size > 0 && !this.recalculate){
            task = tasks.pop()
        }
        else if(tasks.size == 0 && this.recalculate){
            this.fragmentComparison.onButtonPressed()
        }
        else if(tasks.size > 0 && this.recalculate){
            task = tasks.pop()
            this.storage.deleteTask(task.getID())
        }

        if(task.getID() != -1) {
            this.actTask = task
        } else {
            this.fragmentComparison.onButtonPressed()
        }

        this.actTask = task

        fragmentComparison.showTask(task.getA().toString(), task.getB().toString())      // zobrazenie tasku

        return task
    }

    override fun solveTask(answer: String) {
        println("LOG: Solve task")

        if(answer == this.actTask?.getResult()){
            fragmentComparison.showCorrectSolution(answer)
            fragmentComparison.updateStatistic(true)

        } else {
            fragmentComparison.showIncorrectSolution(answer)
            fragmentComparison.updateStatistic(false)
            saveWrongSolvedTask()
        }
    }

    override fun saveActualExerciseStatistic(all: Int, corr: Int, fail: Int, elapsedTime: String) {
        this.storage.saveLastExercise(all, corr, fail, elapsedTime, Constants.COMPARISON.value)
    }

    override fun saveWrongSolvedTask() {
        this.storage.saveTask(this.actTask)
    }

    override fun loadWrongTasks() {
        this.tasks = storage.getTasks()!!
    }


}