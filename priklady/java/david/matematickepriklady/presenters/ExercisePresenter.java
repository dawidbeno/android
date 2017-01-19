package david.matematickepriklady.presenters;

import java.util.Random;

import david.matematickepriklady.models.Storage;
import david.matematickepriklady.models.Task;
import david.matematickepriklady.ui.activities.ChooseActivity;
import david.matematickepriklady.ui.activities.ExerciseActivity;

public class ExercisePresenter {

    private ExerciseActivity exView;
    private Random rand;
    private int result, a, b;

    public ExercisePresenter(ExerciseActivity ex){
        this.exView = ex;
        this.rand = new Random();
    }


    public void newTask(Task task){

        switch (task.getExerciseType()){
            case ChooseActivity.ADDITION:
                do {
                    task.setA(rand.nextInt(task.getRoof()));
                    task.setB(rand.nextInt(task.getRoof()));
                }while (task.getA() + task.getB() > task.getRoof());
                task.prepare();
                break;
            case ChooseActivity.SUBSTRACTION:
                do {
                    task.setA(rand.nextInt(task.getRoof()));
                    task.setB(rand.nextInt(task.getRoof()));
                }while (task.getA()-task.getB() < 0);
                task.prepare();
                break;
            case ChooseActivity.MULTIPLICATION:
                if(task.getLimit() > 0){
                    task.setA(task.getLimit());
                    task.setB(rand.nextInt(10));
                }else{
                    do {
                        task.setA(rand.nextInt(10));
                        task.setB(rand.nextInt(10));
                    }while (task.getA() * task.getB() > task.getRoof());
                }
                task.prepare();
                break;
            case ChooseActivity.DIVISION:
                if(task.getLimit() > 0){
                    task.setB(task.getLimit());
                    do{
                        task.setA(rand.nextInt(task.getLimit()*10));
                    }while(task.getA() % task.getB() != 0);
                }else{
                    do{
                        task.setA(rand.nextInt(task.getRoof()));
                        task.setB(rand.nextInt(10)+1);
                    }while(task.getA() % task.getB() != 0);
                }
                task.prepare();
                break;

        }

        exView.showTask(task);
    }




}
