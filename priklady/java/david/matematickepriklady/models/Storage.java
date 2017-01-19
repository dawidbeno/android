package david.matematickepriklady.models;


import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.OutputStreamWriter;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;


public class Storage{

    private Activity view;
    private FileOutputStream fout;
    private FileInputStream fin;
    private String fileName = "badAnswers";

    private SharedPreferences.Editor editor;
    private SharedPreferences prefs;

    public static final String LAST_ACTIVITY_TYPE = "lastActType";

    private String time;
    public static final String LAST_TASK_TIME = "lastTime";

    // last activity
    public static final String ALL_LAST_TASKS = "lastAll";
    public static final String CORR_LAST_TASKS = "lastCorr";
    public static final String FAIL_LAST_TASKS = "lastFail";

    // all actiivty
    public static final String ALL_ALL_TASKS = "allAll";
    public static final String CORR_ALL_TASKS = "allCorr";
    public static final String FAIL_ALL_TASKS = "allFail";

    static final String PREFS_NAME = "statistic";

    public Storage(Activity activity){
        this.view = activity;
    }

    public void saveTask(Task task){
        try{
            fout = view.openFileOutput(fileName, view.MODE_APPEND);
            fout.write((task.toString()+"\n").getBytes());
            fout.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public StringBuilder loadTask(){
        try{
            fin = view.openFileInput(fileName);
            InputStreamReader isr = new InputStreamReader(fin);
            BufferedReader buffReader = new BufferedReader(isr);
            StringBuilder finalString = new StringBuilder();
            String oneLine;

            while((oneLine = buffReader.readLine()) != null){
                finalString.append(oneLine);
            }

            buffReader.close();
            fin.close();
            isr.close();
            return finalString;

        }catch (Exception e){
            e.printStackTrace();
        }
        return null;
    }

    public void deleteTasks(){
        view.deleteFile(fileName);
        try {
            fout = view.openFileOutput(fileName, view.MODE_APPEND);
            fout.write("".getBytes());
            fout.close();
        }catch (Exception e){
            e.printStackTrace();
        }
    }


    public int getNumTask(String name){
        prefs = view.getSharedPreferences(PREFS_NAME, view.MODE_PRIVATE);
        try{
            return prefs.getInt(name, 0);
        }catch (Exception e){
            return 0;
        }
    }


    public void setNumTask(String name){
        int i = getNumTask(name);
        i++;
        editor = view.getSharedPreferences(PREFS_NAME, view.MODE_PRIVATE).edit();
        editor.putInt(name, i);
        editor.commit();
    }

    public void setNumTask(String name, int value){
        editor = view.getSharedPreferences(PREFS_NAME, view.MODE_PRIVATE).edit();
        editor.putInt(name, value);
        editor.commit();
    }


    public String getTime(String name) {
        prefs = view.getSharedPreferences(PREFS_NAME, view.MODE_PRIVATE);
        return prefs.getString(name, "0:00:00");
    }

    public void setTime( String name, String time) {
        editor = view.getSharedPreferences(PREFS_NAME, view.MODE_PRIVATE).edit();
        editor.putString(name, time);
        editor.commit();
    }

    public String getActType(String actType){
        prefs = view.getSharedPreferences(PREFS_NAME, view.MODE_PRIVATE);
        return prefs.getString(actType, "");
    }

    public void setActType(String name, String actType){
        editor = view.getSharedPreferences(PREFS_NAME, view.MODE_PRIVATE).edit();
        editor.putString(name, actType);
        editor.commit();
    }

}
