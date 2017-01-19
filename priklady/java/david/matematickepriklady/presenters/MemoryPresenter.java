package david.matematickepriklady.presenters;


import android.app.Activity;

import david.matematickepriklady.models.Storage;
import david.matematickepriklady.models.Task;
import david.matematickepriklady.ui.activities.InformationActivity;

public class MemoryPresenter {

    private Storage storage;

    public MemoryPresenter(Activity ac){
        storage = new Storage(ac);
    }


    public void saveTask(Task task){
        storage.saveTask(task);
    }

    public StringBuilder loadTask(){
        StringBuilder result = storage.loadTask();
        return result;
    }

    public void deleteAllWrongTasks(){
        storage.deleteTasks();
    }


    public int[] getStats(){
        int[] arr = new int[InformationActivity.TEXTVIEW_ARR_LENGHT];

        arr[0] = storage.getNumTask(Storage.ALL_LAST_TASKS);
        arr[1] = storage.getNumTask(Storage.CORR_LAST_TASKS);
        arr[2] = storage.getNumTask(Storage.FAIL_LAST_TASKS);
        arr[3] = storage.getNumTask(Storage.ALL_ALL_TASKS);
        arr[4] = storage.getNumTask(Storage.CORR_ALL_TASKS);
        arr[5] = storage.getNumTask(Storage.FAIL_ALL_TASKS);

        return arr;
    }

    public void incCorrTasks(){
        storage.setNumTask(Storage.ALL_ALL_TASKS);
        storage.setNumTask(Storage.CORR_ALL_TASKS);

        storage.setNumTask(Storage.ALL_LAST_TASKS);
        storage.setNumTask(Storage.CORR_LAST_TASKS);
    }

    public void incFailTasks(){
        storage.setNumTask(Storage.ALL_ALL_TASKS);
        storage.setNumTask(Storage.FAIL_ALL_TASKS);

        storage.setNumTask(Storage.ALL_LAST_TASKS);
        storage.setNumTask(Storage.FAIL_LAST_TASKS);
    }

    public void deleteLastTasks(){
        storage.setNumTask(Storage.ALL_LAST_TASKS,0);
        storage.setNumTask(Storage.CORR_LAST_TASKS,0);
        storage.setNumTask(Storage.FAIL_LAST_TASKS,0);
    }

    public void saveTime(String time){
        storage.setTime(Storage.LAST_TASK_TIME, time);
    }

    public String loadTime(){
        return storage.getTime(Storage.LAST_TASK_TIME);
    }

    public void saveType(String actTYpe){
        storage.setActType(Storage.LAST_ACTIVITY_TYPE, actTYpe);
    }

    public String loadType(){
        return storage.getActType(Storage.LAST_ACTIVITY_TYPE);
    }


}
