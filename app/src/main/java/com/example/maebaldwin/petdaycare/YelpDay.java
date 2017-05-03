package com.example.maebaldwin.petdaycare;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;

/**
 * Created by PETERSE_DOUG on 4/25/2017.
 */

public class YelpDay {
    private int day, start, end;

    public YelpDay(int day){
        this.day = day;
    }

    public YelpDay(int day, int start, int end){
        this(day);
        this.start = start;
        this.end = end;

    }

    public int getDay() {
        return day;
    }

    public void setDay(int day) {
        this.day = day;
    }

    public int getStart() {
        return start;
    }

    public void setStart(int start) {
        this.start = start;
    }

    public int getEnd() {
        return end;
    }

    public void setEnd(int end) {
        this.end = end;
    }

    public String getHours() {
        DecimalFormatSymbols timeSymbols = new DecimalFormatSymbols();
        timeSymbols.setDecimalSeparator(':');
        timeSymbols.setGroupingSeparator(':');

        //set unusual time format symbols
        String example = "#0,00.##";
        DecimalFormat time = new DecimalFormat(example, timeSymbols);
        time.setGroupingSize(2);
        //DecimalFormat time = new DecimalFormat("#0:00");

        Boolean openAM;
        Boolean closeAM;

        int st = this.getStart();
        int en = this.getEnd();

        if (st > 1200) {
            openAM = false;
            st -= 1200;
        } else {
            openAM = true;
        }

        if (en > 1200) {
            closeAM = false;
            en -= 1200;
        } else {
            closeAM = true;
        }

        String open = time.format(st);
        String close = time.format(en);
        String hours;

        if (openAM == true && closeAM == true){
            hours = open + "AM to " + close + "AM";
        } else if (openAM == true && closeAM == false){
            hours = open + "AM to " + close + "PM";
        } else if (openAM == false && closeAM == true){
            hours = open + "PM to " + close + "AM";
        } else if (openAM == false && closeAM == false){
            hours = open + "PM to " + close + "PM";
        } else hours = "error";

        return hours;
    }

    public String toString(){
        String string = this.getHours();
        return string;
    }

}