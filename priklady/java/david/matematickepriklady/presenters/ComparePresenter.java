package david.matematickepriklady.presenters;


import java.util.Random;

import david.matematickepriklady.models.Task;
import david.matematickepriklady.ui.activities.ChooseActivity;
import david.matematickepriklady.ui.activities.ComparisionActivity;

public class ComparePresenter {

    private ComparisionActivity compAct;
    private Random rand;
    private int result, a, b;
    private MemoryPresenter memPres;
    private Task task;

    public ComparePresenter(ComparisionActivity compAct, MemoryPresenter mempres){
        this.compAct = compAct;
        this.rand = new Random();
        this.memPres = mempres;
        this.task = new Task();
        this.task.setExerciseType(ChooseActivity.COMPARISION);
        // toto je tu len pre istotu
        this.task.setLimit(1);
        this.task.setRoof(100);
    }

    public void newTask(int roof){
        a = rand.nextInt(roof);
        b = rand.nextInt(roof);
        compAct.showTask(a, b);

        if(a > b){
            result = compAct.GREATER_THAN;
        }else if(a < b){
            result = compAct.LESS_THAN;
        }else{
            result = compAct.EQUALS;
        }

        task.setA(a);
        task.setB(b);
        task.prepare();
    }


    public boolean testTask(int solve){
        if(solve == result){
            return true;
        }else{
            memPres.saveTask(task);
            return false;
        }
    }


}
