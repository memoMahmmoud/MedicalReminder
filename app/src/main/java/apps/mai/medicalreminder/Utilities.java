package apps.mai.medicalreminder;

import android.content.Context;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Mai_ on 04-Nov-16.
 */

public final class Utilities {
    public static long timeToMillisecond(String time) throws Exception {
        Pattern p = Pattern.compile("(\\d+):(\\d+)");
        Matcher m = p.matcher(time);
        if (m.matches()) {
            int hrs = Integer.parseInt(m.group(1));
            int min = Integer.parseInt(m.group(2));
            long ms = (long) hrs * 60 * 60 * 1000 + min * 60 * 1000;
            return ms;
        } else {
            throw new Exception("Bad time format");
        }
    }
    public static String formatDate(int year,int month,int day){
        Calendar calendar = Calendar.getInstance();
        calendar.set(year, month, day);

        SimpleDateFormat format = new SimpleDateFormat("MMM d, yyyy");
        String strDate = format.format(calendar.getTime());
        return strDate;
    }
    public static String formatTime(int hour,int minute,int isAMOrPM){
        String AMOrPM;
        if (isAMOrPM == 1){
            AMOrPM = " PM";
        }else {
            AMOrPM = " AM";
        }
        if(hour > 12) {
            hour -= 12;
        }
        String hourText = String.valueOf(hour);
        String minutesText = String.valueOf(minute);

        if (hour<10){
            hourText = "0".concat(hourText);

        }
        if (hour == 0){
            hourText = "12" ;
        }
        if (minute<10){
            minutesText = "0".concat(minutesText);
        }
        return (hourText+":"+minutesText+AMOrPM);
    }
    public static String formatTime(int hour,int minute){
        String AMOrPM;

        if (hour ==0){
            hour = 12;
            AMOrPM = " AM";
        }
        else if (hour < 12){
            AMOrPM = " AM";
        }
        else {
            hour-=12;
            AMOrPM = " PM";
        }
        String hourText = String.valueOf(hour);
        String minutesText = String.valueOf(minute);
        if (minute<10){
            minutesText = "0".concat(minutesText);
        }
        return (hourText+":"+minutesText+AMOrPM);
    }
    public static int getFrequency(String freq){
        if (freq.contains("Every 24 hours")) {
            return 24;
        }
        else if (freq.contains("Every 12 hours")) {
            return 12;
        }
        else if (freq.contains("Every 8 hours")) {
            return 8;
        }
        else if (freq.contains("Every 6 hours")) {
            return 6;
        }
        else {
            return 4;
        }

    }
    public static int setFrequency(int freq){
        switch (freq){
            case 4:
                return 4;
            case 6:
                return 3;
            case 8:
                return 2;
            case 12:
                return 1;
            default:
                return 0;

        }
    }
    public static String showCertainDaysWeekString(Integer[] daysIndices,Context context){
        StringBuilder str = new StringBuilder();
        String[] days = context.getResources().getStringArray(R.array.days_week);
        for (int i = 0; i < daysIndices.length; i++) {
            str.append(days[daysIndices[i]]);
            if (i != daysIndices.length-1)
                str.append(", ");
        }
        return str.toString();
    }
}
