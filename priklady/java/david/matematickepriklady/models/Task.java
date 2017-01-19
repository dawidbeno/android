package david.matematickepriklady.models;

import david.matematickepriklady.ui.activities.ChooseActivity;

public class Task {

    private int a, b, answer;
    private int exerciseType;
    private int roof;
    private int limit = 0;
    private String strAns;

    public Task(){

    }

    public void prepare(){

        switch (exerciseType){
            case ChooseActivity.ADDITION:
                this.answer = this.a + this.b;
                break;
            case ChooseActivity.SUBSTRACTION:
                this.answer = this.a - this.b;
                break;
            case ChooseActivity.MULTIPLICATION:
                this.answer = this.a * this.b;
                break;
            case ChooseActivity.DIVISION:
                this.answer = this.a / this.b;
                break;
            case ChooseActivity.COMPARISION:
                if(a > b) strAns = " > ";
                else if(a < b) strAns = " < ";
                else if (a == b) strAns = " = ";
                break;
        }
    }


    @Override
    public String toString(){

        switch (exerciseType){
            case ChooseActivity.ADDITION:
                return ""+ a +" + "+ b +" = ";
            case ChooseActivity.SUBSTRACTION:
                return ""+ a +" - "+ b +" = ";
            case ChooseActivity.MULTIPLICATION:
                return ""+ a +" x "+ b +" = ";
            case ChooseActivity.DIVISION:
                return ""+ a +" / "+ b +" = ";
            case ChooseActivity.COMPARISION:
                return ""+ a + "  ?  " + b+" = ";
        }

        return "";
    }

    public int getA() {
        return a;
    }

    public int getB() {
        return b;
    }

    public int getAnswer() {
        return answer;
    }

    public int getExerciseType() {
        return exerciseType;
    }

    public void setA(int a) {
        this.a = a;
    }

    public void setB(int b) {
        this.b = b;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public void setExerciseType(int exerciseType) {
        this.exerciseType = exerciseType;
    }

    public int getRoof() {
        return roof;
    }

    public void setRoof(int roof) {
        this.roof = roof;
    }

    public int getLimit() {
        return limit;
    }

    public void setLimit(int limit) {
        this.limit = limit;
    }


}
