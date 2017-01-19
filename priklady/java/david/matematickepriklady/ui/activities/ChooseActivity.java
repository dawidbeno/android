package david.matematickepriklady.ui.activities;

import android.content.Intent;
import android.content.res.Resources;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import david.matematickepriklady.R;

import static android.view.View.GONE;

public class ChooseActivity extends AppCompatActivity {

    private int exercise, limit = 0;
    private Bundle getData, data;
    private TextView tHeading;
    private LinearLayout lRow1, lRow2, lRow3;
    private Button btn10, btn20, btn100;

    public static final int ADDITION = 1;
    public static final int SUBSTRACTION = 2;
    public static final int MULTIPLICATION = 3;
    public static final int DIVISION = 4;
    public static final int COMPARISION = 5;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.choose_difficulty);


        loadView();


        getData = new Bundle();
        getData = getIntent().getExtras();

        data = new Bundle();

        switch (getData.getInt("exercise")){
            case R.id.btnAdd:
                tHeading.setText(R.string.scitanie);
                exercise = ADDITION;
                break;
            case R.id.btnSub:
                tHeading.setText(R.string.odcitanie);
                exercise = SUBSTRACTION;
                break;
            case R.id.btnMul:
                tHeading.setText(R.string.nasobenie);
                mixLayoutOn();
                exercise = MULTIPLICATION;
                break;
            case R.id.btnDiv:
                tHeading.setText(R.string.delenie);
                mixLayoutOn();
                exercise = DIVISION;
                break;
            default:
                Toast.makeText(getBaseContext(), "Error: bad setHeading"+R.id.btnAdd, Toast.LENGTH_SHORT).show();
                break;
        }

        setColors();

    }

    // onClick
    public void choose(View view){

        int id = view.getId();

        switch (id){
            case R.id.btnMax10:
                startExercise(10);
                break;
            case R.id.btnMax20:
                startExercise(20);
                break;
            case R.id.btnMax100:
                startExercise(100);
                break;
            default:
                Button btn = (Button)findViewById(id);
                String btnText = btn.getText().toString();
                btnText = btnText.substring(0,1);
                limit = Integer.parseInt(btnText);
                System.out.println(""+limit);
                if(limit == 1) limit = 10;
                startExercise(limit*10, limit);
                break;
        }
    }


    // private
    private void startExercise(int roof){

        data.putInt("exercise", exercise);
        data.putInt("roof", roof);
        data.putInt("limit", 0);
        Intent intent = new Intent(ChooseActivity.this, ExerciseActivity.class);
        intent.putExtras(data);
        startActivity(intent);

    }

    private void startExercise(int roof, int limit){
        data.putInt("exercise", exercise);
        data.putInt("roof", roof);
        data.putInt("limit", limit);
        Intent intent = new Intent(ChooseActivity.this, ExerciseActivity.class);
        intent.putExtras(data);
        startActivity(intent);
    }



    private void loadView(){
        tHeading = (TextView)findViewById(R.id.tHeading);
        lRow1 = (LinearLayout)findViewById(R.id.extDiffRow1);
        lRow1.setVisibility(GONE);
        lRow2 = (LinearLayout)findViewById(R.id.extDiffRow2);
        lRow2.setVisibility(GONE);
        lRow3 = (LinearLayout)findViewById(R.id.extDiffRow3);
        lRow3.setVisibility(GONE);


        btn10 = (Button) findViewById(R.id.btnMax10);
        btn20 = (Button)findViewById(R.id.btnMax20);
        btn100 = (Button)findViewById(R.id.btnMax100);

    }


    private void mixLayoutOn(){
        lRow1.setVisibility(View.VISIBLE);
        lRow2.setVisibility(View.VISIBLE);
        lRow3.setVisibility(View.VISIBLE);

//        btn10.setText(R.string.btn_do_10_mix);
//        btn20.setText(R.string.btn_do_20_mix);
//        btn100.setText(R.string.btn_do_100_mix);
    }

    private void setColors(){

        switch (exercise){
            case ADDITION:
                btn10.setBackgroundColor(getResources().getColor(R.color.plusGreen));
                btn20.setBackgroundColor(getResources().getColor(R.color.plusGreen));
                btn100.setBackgroundColor(getResources().getColor(R.color.plusGreen));

                lRow1.setBackgroundColor(getResources().getColor(R.color.plusGreen));
                lRow2.setBackgroundColor(getResources().getColor(R.color.plusGreen));
                lRow3.setBackgroundColor(getResources().getColor(R.color.plusGreen));
                break;
            case SUBSTRACTION:
                btn10.setBackgroundColor(getResources().getColor(R.color.minusRed));
                btn20.setBackgroundColor(getResources().getColor(R.color.minusRed));
                btn100.setBackgroundColor(getResources().getColor(R.color.minusRed));

                lRow1.setBackgroundColor(getResources().getColor(R.color.minusRed));
                lRow2.setBackgroundColor(getResources().getColor(R.color.minusRed));
                lRow3.setBackgroundColor(getResources().getColor(R.color.minusRed));
                break;
            case MULTIPLICATION:
                btn10.setBackgroundColor(getResources().getColor(R.color.multiplicateOrange));
                btn20.setBackgroundColor(getResources().getColor(R.color.multiplicateOrange));
                btn100.setBackgroundColor(getResources().getColor(R.color.multiplicateOrange));

                lRow1.setBackgroundColor(getResources().getColor(R.color.multiplicateOrange));
                lRow2.setBackgroundColor(getResources().getColor(R.color.multiplicateOrange));
                lRow3.setBackgroundColor(getResources().getColor(R.color.multiplicateOrange));
                break;
            case DIVISION:
                btn10.setBackgroundColor(getResources().getColor(R.color.divideBlue));
                btn20.setBackgroundColor(getResources().getColor(R.color.divideBlue));
                btn100.setBackgroundColor(getResources().getColor(R.color.divideBlue));

                lRow1.setBackgroundColor(getResources().getColor(R.color.divideBlue));
                lRow2.setBackgroundColor(getResources().getColor(R.color.divideBlue));
                lRow3.setBackgroundColor(getResources().getColor(R.color.divideBlue));
                break;
        }

    }


}
