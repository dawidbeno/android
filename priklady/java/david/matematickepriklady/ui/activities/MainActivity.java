package david.matematickepriklady.ui.activities;

import android.content.Intent;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import david.matematickepriklady.R;
import david.matematickepriklady.presenters.MemoryPresenter;

public class MainActivity extends AppCompatActivity {

    private Bundle data;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        data = new Bundle();

        loadView();
    }



    // onClick

    public void comparisionStart(View view){
        Intent intent = new Intent(MainActivity.this, ComparisionActivity.class);
        startActivity(intent);
        //finish();
    }

    public void startChoose(View view){
        data.putInt("exercise", view.getId());
        Intent intent = new Intent(MainActivity.this, ChooseActivity.class);
        intent.putExtras(data);
        startActivity(intent);
    }

    public void information(View view){
        Intent intent = new Intent(MainActivity.this, InformationActivity.class);
        startActivity(intent);
    }


    // private
    private void loadView(){
        Button btn = (Button)findViewById(R.id.btnAdd);
        btn.setBackgroundColor(getResources().getColor(R.color.plusGreen));

        btn = (Button)findViewById(R.id.btnSub);
        btn.setBackgroundColor(getResources().getColor(R.color.minusRed));

        btn = (Button)findViewById(R.id.btnMul);
        btn.setBackgroundColor(getResources().getColor(R.color.multiplicateOrange));

        btn = (Button)findViewById(R.id.btnDiv);
        btn.setBackgroundColor(getResources().getColor(R.color.divideBlue));

        btn = (Button)findViewById(R.id.btnComp);
        btn.setBackgroundColor(getResources().getColor(R.color.comparePurple));

        TextView txv = (TextView)findViewById(R.id.mainHeading);
        txv.setTypeface(null, Typeface.BOLD);

    }

}
