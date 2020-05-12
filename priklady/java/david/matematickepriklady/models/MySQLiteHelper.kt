package com.dejvid.matematika.models

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.provider.BaseColumns
import java.io.Serializable
import java.util.*
import kotlin.collections.ArrayList

class MySQLiteHelper(context: Context, name: String?, factory: SQLiteDatabase.CursorFactory?, version: Int) : IStorage, SQLiteOpenHelper(context, DATABASE_NAME, factory, DATABASE_VERSION){


    override fun onCreate(db: SQLiteDatabase?) {
        val CREATE_TASKS_TABLE = (
                "CREATE TABLE " + TABLE_TASKS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY, "
                + COLUMN_NUMA + " INTEGER, "
                + COLUMN_NUMB + " INTEGER, "
                + COLUMN_RES + " TEXT, "
                + COLUMN_TYPE + " INTEGER, "
                + COLUMN_DIFFICULTY + " INTEGER )"
                )

        db?.execSQL(CREATE_TASKS_TABLE)

        val CREATE_STATS_TABLE = (
                "CREATE TABLE " + TABLE_STATS + "("
                + COLUMN_ID + " INTEGER PRIMARY KEY, "
                + COLUMN_EXERCOUNT + " INTEGER, "
                + COLUMN_CORRECT + " INTEGER, "
                + COLUMN_INCORRECT + " INTEGER, "
                + COLUMN_TYPE + " INTEGER, "
                + COLUMN_TIME + " TEXT )"
                )
        db?.execSQL(CREATE_STATS_TABLE)

        val values = ContentValues()
        values.put(COLUMN_EXERCOUNT, 0)
        values.put(COLUMN_CORRECT, 0)
        values.put(COLUMN_INCORRECT, 0)
        db?.insert(TABLE_STATS, null, values)

        val values1 = ContentValues()
        values1.put(COLUMN_EXERCOUNT, 0)
        values1.put(COLUMN_CORRECT, 0)
        values1.put(COLUMN_INCORRECT, 0)
        values1.put(COLUMN_TYPE, 0)
        values1.put(COLUMN_TIME, "00:00")
        db?.insert(TABLE_STATS, null, values1)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        db?.execSQL("DROP TABLE IF EXISTS " + TABLE_TASKS)
        db?.execSQL("DROP TABLE IF EXISTS " + TABLE_STATS)
        onCreate(db)
    }


    override fun saveLastExercise(all: Int, corr: Int, fail: Int, elapsedTime: String, type: Int) {
        updateLastExercise(all, corr, fail, elapsedTime, type)
        updateAllStats(all, corr, fail)
    }

    override fun getAllStats(): ArrayList<String> {
        val res : ArrayList<String> = ArrayList()
        val stats = readAllStats()
        if (stats != null) {
            for (x in stats.indices){
                res.add(stats[x].toString())
            }
        }
        return res
    }

    override fun getLastExerciseStats(): ArrayList<String> {
        return readLastExercise()
    }

    override fun saveTask(actTask: Task?) {
        val values = ContentValues()
        values.put(COLUMN_NUMA, actTask?.getA())
        values.put(COLUMN_NUMB, actTask?.getB())
        values.put(COLUMN_RES, actTask?.getResult())
        values.put(COLUMN_TYPE, actTask?.getExerciseType())
        values.put(COLUMN_DIFFICULTY, actTask?.getDifficulty())

        val db = this.writableDatabase

        val newRow = db.insert(TABLE_TASKS, null, values)
        println("DEBUG: New task saved on row $newRow")
        db.close()
    }

    override fun deleteTask(id : Int) : Boolean{
        var result : Int = 0

        val query = "SELECT * FROM $TABLE_TASKS WHERE $COLUMN_ID=$id;"

        val db = this.writableDatabase

        val cursor = db.rawQuery(query, null)

        if (cursor.moveToNext()) {
            val dbid = Integer.parseInt(cursor.getString(0))
            result = db.delete(TABLE_TASKS, "$COLUMN_ID=$dbid", null)

            // other way
            //val deleteQuery = "DELETE FROM $TABLE_TASKS WHERE $COLUMN_ID = $dbid;"
            //db.execSQL(deleteQuery)
        }

        cursor.close()
        db.close()
        return result>0
    }

    fun deleteAllTasks(){
        var tasks = this.getTasks()
        if (tasks != null) {
            for(task in tasks){
                deleteTask(task.getID())
            }
        }
    }

    override fun getTasks(): Stack<Task>? {
        var tasks = Stack<Task>()

        val query = "SELECT * FROM $TABLE_TASKS"

        val db = this.readableDatabase

        val cursor = db.rawQuery(query, null)

        while(cursor.moveToNext()) {
            var task = Task()
            task?.setID(Integer.parseInt(cursor.getString(0)))
            task?.setA(cursor.getInt(1))
            task?.setB(cursor.getInt(2))
            task?.setResult(cursor.getString(3))
            task?.setExerciseType(cursor.getInt(4))
            task?.setDifficulty(cursor.getInt(5))
            tasks.add(0, task)
        }
        cursor.close()

        db.close()
        return tasks
    }


    private fun updateAllStats(all: Int, corr: Int, fail: Int){
        val stats : IntArray? = readAllStats()
        val db = this.writableDatabase

        if (stats != null){
            var values = ContentValues()
            stats[0] += all
            stats[1] += corr
            stats[2] += fail

            values.put(COLUMN_EXERCOUNT, stats[0])
            values.put(COLUMN_CORRECT, stats[1])
            values.put(COLUMN_INCORRECT, stats[2])
            db.update(TABLE_STATS, values , "$COLUMN_ID=$ID_ALL_STATS", null )

        } else {
            val values = ContentValues()
            values.put(COLUMN_EXERCOUNT, all)
            values.put(COLUMN_CORRECT, corr)
            values.put(COLUMN_INCORRECT, fail)
            db.insert(TABLE_STATS, null, values)
        }

        db.close()
    }

    private fun updateLastExercise(all: Int, corr: Int, fail: Int, elapsedTime: String, type: Int){
        val stats : ArrayList<String> = readLastExercise()
        val db = this.writableDatabase

        if (stats.isNotEmpty()) {
            val values = ContentValues()
            values.put(COLUMN_EXERCOUNT, all)
            values.put(COLUMN_CORRECT, corr)
            values.put(COLUMN_INCORRECT, fail)
            values.put(COLUMN_TYPE, type)
            values.put(COLUMN_TIME, elapsedTime)
            db.update(TABLE_STATS, values, "$COLUMN_ID=$ID_LAST_EXER_STATS", null)

        } else {
            val values = ContentValues()
            values.put(COLUMN_EXERCOUNT, all)
            values.put(COLUMN_CORRECT, corr)
            values.put(COLUMN_INCORRECT, fail)
            values.put(COLUMN_TYPE, type)
            values.put(COLUMN_TIME, elapsedTime)
            db.insert(TABLE_STATS, null, values)

        }

        db.close()

    }

    private fun readAllStats() : IntArray? {
        val res = IntArray(3)
        val query = "SELECT * FROM $TABLE_STATS WHERE $COLUMN_ID=$ID_ALL_STATS"
        val db = this.readableDatabase
        val cursor = db.rawQuery(query, null)

        while(cursor.moveToNext()){
            res.set(0, cursor.getInt(1))
            res.set(1, cursor.getInt(2))
            res.set(2, cursor.getInt(3))
        }

        cursor.close()
        db.close()
        return res
    }

    private fun readLastExercise() : ArrayList<String>{
        val res : ArrayList<String> = ArrayList()
        val query = "SELECT * FROM $TABLE_STATS WHERE $COLUMN_ID=$ID_LAST_EXER_STATS"
        val db = this.readableDatabase
        val cursor = db.rawQuery(query, null)

        while(cursor.moveToNext() ){
            res.add(cursor.getInt(1).toString())
            res.add(cursor.getInt(2).toString())
            res.add(cursor.getInt(3).toString())
            res.add(cursor.getInt(4).toString())
            res.add(cursor.getString(5))
        }

        cursor.close()
        db.close()
        return res
    }

    companion object {

        private val DATABASE_VERSION = 1
        private val DATABASE_NAME = "exercise.db"
        val TABLE_TASKS = "tasks"
        val TABLE_STATS = "stats"

        val COLUMN_ID = "_id"
        val COLUMN_NUMA = "NumA"
        val COLUMN_NUMB = "NumB"
        val COLUMN_RES = "result"
        val COLUMN_TYPE = "type"
        val COLUMN_DIFFICULTY = "difficulty"
        val COLUMN_EXERCOUNT = "ExerCount"
        val COLUMN_CORRECT = "Correct"
        val COLUMN_INCORRECT = "Incorrect"
        val COLUMN_TIME = "Time"

        val ID_ALL_STATS = 1
        val ID_LAST_EXER_STATS = 2
    }


}