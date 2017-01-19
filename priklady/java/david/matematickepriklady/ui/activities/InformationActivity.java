package david.matematickepriklady.ui.activities;

import android.content.res.Resources;
import android.icu.text.DateFormat;
import android.icu.text.SimpleDateFormat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.Date;

import david.matematickepriklady.R;
import david.matematickepriklady.presenters.MemoryPresenter;

public class InformationActivity extends AppCompatActivity {

    private MemoryPresenter memPres;
    private TextView tInfo, time, tType;
    private LinearLayout wrongAnswers;
    private StringBuilder strBuild;
    private TextView[] tArr = new TextView[6];

    public static final int TEXTVIEW_ARR_LENGHT = 6;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_information);

        loadView();

        memPres = new MemoryPresenter(this);
        strBuild = memPres.loadTask();

        showWrongAnswers();
        showStatistis();
    }



    // private
    private void loadView(){
        tInfo = (TextView)findViewById(R.id.inform);
        time = (TextView)findViewById(R.id.showTime);
        wrongAnswers = (LinearLayout)findViewById(R.id.wrongAnswers);
        Resources res = getResources();
        String url = "";
        int resIDtext;
        for(int i=0; i<TEXTVIEW_ARR_LENGHT; i++){
            url = "stat" + i;
            resIDtext = res.getIdentifier(url, "id", getPackageName());
            tArr[i] = (TextView)findViewById(resIDtext);
        }

    }

    private void showWrongAnswers(){
        String oneAnswer = "";
        char c;
        // podmienka musi byt takto, nie otocena, pretoze po cerstvej instalacii je strBuild == null
        // preto ak sa vyhodnoti prva cast ako tru do dalsej nejde, inak by to spadlo
        // lebo z null by nemohol ziskat length
        if(strBuild == null || strBuild.length() == 0 ){
            final TextView rowTextView = new TextView(this);
            rowTextView.setText(R.string.noWrongTasks);
            rowTextView.setTextSize(15);
            LinearLayout.LayoutParams llp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            llp.setMargins(0,20,0,0);
            rowTextView.setLayoutParams(llp);
            rowTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
            wrongAnswers.addView(rowTextView);
        }else {
            for (int i = 0; i < strBuild.length(); i++) {
                c = strBuild.charAt(i);
                oneAnswer += c;
                if (c == '=') {
                    final TextView rowTextView = new TextView(this);
                    if(oneAnswer.indexOf('?') >= 0){
                        oneAnswer = oneAnswer.substring(0, (oneAnswer.length()-1));
                    }
                    rowTextView.setText(oneAnswer);
                    rowTextView.setTextAlignment(View.TEXT_ALIGNMENT_CENTER);
                    rowTextView.setTextSize(30);
                    wrongAnswers.addView(rowTextView);
                    i++;
                    oneAnswer = "";
                }
            }
        }

    }

    private void showStatistis(){
        int[] arr = memPres.getStats();
        for(int i=0; i< TEXTVIEW_ARR_LENGHT; i++){
            tArr[i].setText(String.valueOf(arr[i]));
        }
        String timeElapsed = memPres.loadTime();
        time.setText(timeElapsed);

        tType = (TextView)findViewById(R.id.actTypeID);
        tType.setText(memPres.loadType().toUpperCase());
    }

}
