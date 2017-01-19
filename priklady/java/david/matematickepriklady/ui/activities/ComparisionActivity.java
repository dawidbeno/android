package david.matematickepriklady.ui.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.SystemClock;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Chronometer;
import android.widget.TextView;

import david.matematickepriklady.R;
import david.matematickepriklady.presenters.ComparePresenter;
import david.matematickepriklady.presenters.MemoryPresenter;

public class ComparisionActivity extends AppCompatActivity {


    private int roof;
    private TextView tNumOne, tNumTwo, tMid;
    private TextView tCorrTasks, tFailTasks, tAllTasks, tHeading;
    private Button btnGt, btnLt, btnEq;
    private ComparePresenter compPres;
    private MemoryPresenter memPres;
    private ColorStateList defColor;

    public static final int GREATER_THAN = 1;
    public static final int LESS_THAN = 2;
    public static final int EQUALS = 3;

    private Chronometer chronometer;
    private long pauseTime = 0;
    private boolean paused = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.comparision_difficulty);

        memPres = new MemoryPresenter(this);
        compPres = new ComparePresenter(this, memPres);

        loadViewDiff();

        memPres.deleteAllWrongTasks();
        memPres.deleteLastTasks();


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

    public void showTask(Integer numOne, Integer numTwo){
        tNumOne.setText(numOne.toString());
        tNumTwo.setText(numTwo.toString());

    }


    // onClick

    public void compUnder10(View view){
        roof = 10;
        setContentView(R.layout.comparision_solving);
        loadViewSolv();
        compPres.newTask(roof);
    }

    public void compUnder20(View view){
        roof = 20;
        setContentView(R.layout.comparision_solving);
        loadViewSolv();
        compPres.newTask(roof);
    }

    public void compUnder100(View view){
        roof = 100;
        setContentView(R.layout.comparision_solving);
        loadViewSolv();
        compPres.newTask(roof);
    }


    // skusit zlucit tieto tri metody do jednej

    public void solveTaskGT(View view){
        incNumTask(tAllTasks);
        setBtns(false);
        tMid.setText(R.string.gt);
        if(compPres.testTask(GREATER_THAN)){
            tMid.setTextColor(this.getResources().getColor(R.color.green));
            incNumTask(tCorrTasks);
        }else{
            tMid.setTextColor(this.getResources().getColor(R.color.red));
            incNumTask(tFailTasks);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tMid.setText(R.string.questionMark);
                tMid.setTextColor(defColor);
                compPres.newTask(roof);
                setBtns(true);
            }
        }, 1000);
    }

    public void solveTaskLT(View view){
        incNumTask(tAllTasks);
        setBtns(false);
        tMid.setText(R.string.lt);
        if(compPres.testTask(LESS_THAN)){
            tMid.setTextColor(this.getResources().getColor(R.color.green));
            incNumTask(tCorrTasks);
        }else{
            tMid.setTextColor(this.getResources().getColor(R.color.red));
            incNumTask(tFailTasks);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tMid.setText(R.string.questionMark);
                tMid.setTextColor(defColor);
                compPres.newTask(roof);
                setBtns(true);
            }
        }, 1000);
    }

    public void solveTaskEQ(View view){
        incNumTask(tAllTasks);
        setBtns(false);
        tMid.setText(R.string.eq);
        if(compPres.testTask(EQUALS)){
            tMid.setTextColor(this.getResources().getColor(R.color.green));
            incNumTask(tCorrTasks);
        }else{
            tMid.setTextColor(this.getResources().getColor(R.color.red));
            incNumTask(tFailTasks);
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                tMid.setText(R.string.questionMark);
                tMid.setTextColor(defColor);
                compPres.newTask(roof);
                setBtns(true);
            }
        }, 1000);
    }



    // private

    private void loadViewSolv(){
        tNumOne = (TextView)findViewById(R.id.compTaskTextLeft);
        tNumOne.setTypeface(null, Typeface.BOLD);
        tNumTwo = (TextView)findViewById(R.id.compTaskTextRight);
        tNumTwo.setTypeface(null, Typeface.BOLD);
        tMid = (TextView)findViewById(R.id.compTaskTextMid);
        tMid.setTypeface(null, Typeface.BOLD);
        defColor = tNumOne.getTextColors();

        tHeading = (TextView)findViewById(R.id.tHeading2);

        tCorrTasks = (TextView)findViewById(R.id.compSolvCorrectTaskNum);
        tFailTasks = (TextView)findViewById(R.id.compSolvFailTaskNum);
        tAllTasks = (TextView)findViewById(R.id.compSolvAllTaskNum);

        btnGt = (Button)findViewById(R.id.btnGt);
        btnGt.setBackgroundColor(getResources().getColor(R.color.comparePurple));
        btnLt = (Button)findViewById(R.id.btnLt);
        btnLt.setBackgroundColor(getResources().getColor(R.color.comparePurple));
        btnEq = (Button)findViewById(R.id.btnEq);
        btnEq.setBackgroundColor(getResources().getColor(R.color.comparePurple));

        chronometer = (Chronometer)findViewById(R.id.chron2);
        chronometer.setBase(SystemClock.elapsedRealtime());
        chronometer.start();

    }

    private void setBtns(boolean b){
        btnGt.setEnabled(b);
        btnLt.setEnabled(b);
        btnEq.setEnabled(b);
    }

    private void incNumTask(TextView tv){
        Integer a = Integer.parseInt(tv.getText().toString());
        a++;
        tv.setText(a.toString());

        switch (tv.getId()){
            case R.id.compSolvCorrectTaskNum:
                memPres.incCorrTasks();
                break;
            case R.id.compSolvFailTaskNum:
                memPres.incFailTasks();
                break;
        }
    }


    private void loadViewDiff(){
        Button btn = (Button)findViewById(R.id.porov_10);
        btn.setBackgroundColor(getResources().getColor(R.color.comparePurple));

        btn = (Button)findViewById(R.id.porov_20);
        btn.setBackgroundColor(getResources().getColor(R.color.comparePurple));

        btn = (Button)findViewById(R.id.porov_100);
        btn.setBackgroundColor(getResources().getColor(R.color.comparePurple));


    }

}
