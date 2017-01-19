package david.matematickepriklady.ui.activities;

import android.content.res.Resources;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.ButtonBarLayout;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;
import android.widget.Toast;
import java.util.Arrays;
import java.util.Random;

import david.matematickepriklady.R;
import david.matematickepriklady.models.Storage;
import david.matematickepriklady.models.Task;
import david.matematickepriklady.presenters.ExercisePresenter;
import david.matematickepriklady.presenters.MemoryPresenter;

public class ExerciseActivity extends AppCompatActivity {

    private int exercise, roof, limit = 0;
    private Bundle getData = new Bundle();
    private ExercisePresenter exerPres;
    private MemoryPresenter memPres;
    private TextView tTask, tHeading, tAns;
    private TextView tCorrTasks, tFailTasks, tAllTasks;
    private Random rand = new Random();
    private Button[] btns;
    private int result;
    private Task task;
    private Chronometer chronometer;
    private long pauseTime = 0;
    private boolean paused = false;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.exercise_solving);
        task = new Task();

        exerPres = new ExercisePresenter(this);
        memPres = new MemoryPresenter(this);
        memPres.deleteAllWrongTasks();
        memPres.deleteLastTasks();

        loadExtras(); // must be loaded before loading View
        loadView();

        task.setRoof(roof);
        task.setExerciseType(exercise);
        task.setLimit(limit);
        exerPres.newTask(task);

        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();
    }


    @Override
    protected void onStop(){
        super.onStop();
        pauseTime = chronometer.getBase() - SystemClock.elapsedRealtime();
        chronometer.stop();
        String timeElapsed = chronometer.getText().toString();  // chronometer extends TextView, so getText() method is available
        memPres.saveTime(timeElapsed);
        memPres.saveType(tHeading.getText().toString());
    }

    @Override
    protected void onPause(){
        super.onPause();
        pauseTime = SystemClock.elapsedRealtime();
        paused = true;
    }

    @Override
    protected void onResume(){
        super.onResume();
        if(paused) {
            chronometer.setBase(SystemClock.elapsedRealtime() + pauseTime);
            chronometer.start();
            paused = false;
        }

    }


    public void showTask(Task task){

        tTask.setText(task.toString());
        boolean[] arr = new boolean[task.getRoof()];
        int x;
        Arrays.fill(arr, false);

        for(int i=0; i<btns.length; i++){
            do {
                x = rand.nextInt(task.getRoof());
            }while(arr[x] == true || x == task.getAnswer()); // arr[task.getAnswer()] = true  -> make this condition simple
            arr[x] = true;
            btns[i].setText(""+x);
        }

        int btnCorrect = rand.nextInt(6);
        btns[btnCorrect].setText(""+task.getAnswer());

    }


    // onClick

    public void solveTask(View view){
        incNumTask(tAllTasks);
        setBtns(false);
        Button answerBtn = (Button)findViewById(view.getId());
        String answerStr =  answerBtn.getText().toString();
        int userAnswer = Integer.parseInt(answerStr);

        tAns.setVisibility(View.VISIBLE);
        tAns.setText(" "+userAnswer);

        if(userAnswer == task.getAnswer()){
            tAns.setTextColor(this.getResources().getColor(R.color.green));
            incNumTask(tCorrTasks);
        }else{
            tAns.setTextColor(this.getResources().getColor(R.color.red));
            incNumTask(tFailTasks);
            memPres.saveTask(task);
        }

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tAns.setVisibility(View.INVISIBLE);
                setBtns(true);
                exerPres.newTask(task);
            }
        }, 1000);

    }




    // private methods

    private void loadView(){
        tTask = (TextView)findViewById(R.id.exerTask);
        tTask.setTypeface(null, Typeface.BOLD);
        tHeading = (TextView)findViewById(R.id.exerHeading);
        tAns = (TextView)findViewById(R.id.exerAns);
        tAns.setTypeface(null, Typeface.BOLD);
        tAns.setVisibility(View.INVISIBLE);

        switch (exercise){
            case ChooseActivity.ADDITION:
                tHeading.setText(R.string.scitanie);
                break;
            case ChooseActivity.SUBSTRACTION:
                tHeading.setText(R.string.odcitanie);
                break;
            case ChooseActivity.MULTIPLICATION:
                tHeading.setText(R.string.nasobenie);
                break;
            case ChooseActivity.DIVISION:
                tHeading.setText(R.string.delenie);
                break;
        }

        // load buttons
        String url = "btnAns";
        Resources res = getResources();
        int resIDbtn;
        btns = new Button[6];
        for(int i=0; i<btns.length; i++){
            url = "btnAns"+(i+1);
            resIDbtn = res.getIdentifier(url, "id", getPackageName());
            btns[i] = (Button)findViewById(resIDbtn);
            btns[i].setTextSize(20);
            switch (exercise){
                case ChooseActivity.ADDITION:
                    btns[i].setBackgroundColor(getResources().getColor(R.color.plusGreen));
                    break;
                case ChooseActivity.SUBSTRACTION:
                    btns[i].setBackgroundColor(getResources().getColor(R.color.minusRed));
                    break;
                case ChooseActivity.MULTIPLICATION:
                    btns[i].setBackgroundColor(getResources().getColor(R.color.multiplicateOrange));
                    break;
                case ChooseActivity.DIVISION:
                    btns[i].setBackgroundColor(getResources().getColor(R.color.divideBlue));
                    break;
            }
        }

        tAllTasks = (TextView)findViewById(R.id.allTasks);
        tFailTasks = (TextView)findViewById(R.id.failTasks);
        tCorrTasks = (TextView)findViewById(R.id.corrTasks);

        chronometer = (Chronometer)findViewById(R.id.chron);

    }

    private void loadExtras(){
        getData = getIntent().getExtras();
        exercise = getData.getInt("exercise");
        if(exercise == ChooseActivity.MULTIPLICATION || exercise == ChooseActivity.DIVISION) limit = getData.getInt("limit");
        roof = getData.getInt("roof");
    }



    // z tejto funkcie vyhodit tieto inkrementy a dat to do presenteru
    // ide o to, ze preco by som mal vo view iba tak volat funkcie z presenteru?????
    // vo view len funckia ktora nastavuje hodnotu a pouzit ju v presenteri a tam sa aj vybavia veci na ukladanie hodnot
    private void incNumTask(TextView tv){
        Integer a = Integer.parseInt(tv.getText().toString());
        a++;
        tv.setText(a.toString());

        switch (tv.getId()){
            case R.id.corrTasks:
                memPres.incCorrTasks();
                break;
            case R.id.failTasks:
                memPres.incFailTasks();
                break;
        }
    }

    private void setBtns(boolean b){
        for(int i=0; i<btns.length; i++){
            btns[i].setEnabled(b);
        }
    }


}
